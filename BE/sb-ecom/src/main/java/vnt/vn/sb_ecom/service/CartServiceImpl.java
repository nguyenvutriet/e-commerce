package vnt.vn.sb_ecom.service;

import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vnt.vn.sb_ecom.handler.APIException;
import vnt.vn.sb_ecom.handler.ResourceNotFoundException;
import vnt.vn.sb_ecom.model.Cart;
import vnt.vn.sb_ecom.model.CartItem;
import vnt.vn.sb_ecom.model.Product;
import vnt.vn.sb_ecom.payload.CartDTO;
import vnt.vn.sb_ecom.payload.CartItemDTO;
import vnt.vn.sb_ecom.payload.ProductDTO;
import vnt.vn.sb_ecom.repository.CartItemRepository;
import vnt.vn.sb_ecom.repository.CartRepository;
import vnt.vn.sb_ecom.repository.ProductRepository;
import vnt.vn.sb_ecom.util.AuthUtil;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class CartServiceImpl implements CartService{

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private AuthUtil authUtil;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CartItemRepository cartItemRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public CartDTO addProductToCart(Long productId, Integer quantity) {
        // Find existing cart or create one
        Cart cart = createCart();

        // Retrieve Product Details
        Product product =  productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "productId", productId));

        // Perform Validations
        CartItem cartItem = cartItemRepository.findCartItemByProductIdAndCartId(cart.getCartId(), productId);

        if(cartItem != null){
            throw new APIException("Product already exists in the cart. Please update the quantity instead.");
        }

        if(product.getQuantity() == 0){
            throw  new APIException(product.getProductName() + " is not available");
        }

        if(product.getQuantity() < quantity){
            throw  new APIException("Please, make an order of the " + product.getProductName() + " available quantity: " + product.getQuantity());
        }

        // Create Cart Item
        CartItem newCartItem = new CartItem();
        newCartItem.setProduct(product);
        newCartItem.setQuantity(quantity);
        newCartItem.setCart(cart);
        newCartItem.setDiscount(product.getDiscount());
        newCartItem.setProductPrice(product.getPrice());

        // Save Cart Item
        cartItemRepository.save(newCartItem);
        product.setQuantity(product.getQuantity() - quantity);
        cart.setTotalPrice(cart.getTotalPrice() + (product.getPrice() * quantity));

        cartRepository.save(cart);
        productRepository.save(product);

        CartDTO cartDTO = modelMapper.map(cart, CartDTO.class);

        List<CartItem> cartItems = cart.getCartItems();
        Stream<ProductDTO> productDTOStream = cartItems.stream()
                .map(item -> {
                    ProductDTO map = modelMapper.map(item.getProduct(), ProductDTO.class);
                    map.setQuantity(item.getQuantity());
                    return map;
                });

        cartDTO.setProducts(productDTOStream.toList());

        // Return updated cart
        return cartDTO;
    }

    @Override
    public List<CartDTO> getAllCarts() {
        List<Cart> carts = cartRepository.findAll();

        if(carts.size() == 0) throw  new APIException("No cart exists");

        List<CartDTO> cartDTOS = carts.stream()
                .map(cart -> {
                    CartDTO cartDTO = modelMapper.map(cart, CartDTO.class);

                    List<ProductDTO> products = cart.getCartItems().stream()
                            .map(cartItem ->{
                                ProductDTO productDTO = modelMapper.map(cartItem, ProductDTO.class);
                                productDTO.setQuantity(cartItem.getQuantity());
                                return productDTO;
                            }).collect(Collectors.toList());


                    cartDTO.setProducts(products);
                    return cartDTO;
                }).collect(Collectors.toList());

        return cartDTOS;
    }

    @Override
    public CartDTO getCart(String emailId, Long cartId) {
        Cart cart = cartRepository.findCartByEmailAndCartId(emailId, cartId);
        if(cart == null){
            throw  new ResourceNotFoundException("Carrt", "cartId", cartId);
        }

        CartDTO cartDTO = modelMapper.map(cart, CartDTO.class);
        cart.getCartItems().forEach(c -> c.setQuantity(c.getQuantity()));
        List<ProductDTO> products = cart.getCartItems().stream()
                .map(p -> modelMapper.map(p.getProduct(), ProductDTO.class))
                .collect(Collectors.toList());
        cartDTO.setProducts(products);
        return cartDTO;
    }

    @Override
    @Transactional
    public CartDTO updateProductQuantity(Long productId, int delete) {

        String emailId = authUtil.loggedInEmail();
        Cart userCart = cartRepository.findCartByEmail(emailId);
        Long cartId = userCart.getCartId();

        Cart cart = cartRepository.findById(cartId).orElseThrow(() -> new ResourceNotFoundException("Cart", "cartId", cartId));
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "productId", productId));

        if(product.getQuantity() == 0){
            throw  new APIException(product.getProductName() + " is not available");
        }

        if(product.getQuantity() < delete){
            throw new APIException("Please, make an order of the " + product.getProductName() + " available quantity: " + product.getQuantity());
        }

        CartItem cartItem = cartItemRepository.findCartItemByProductIdAndCartId(cartId, productId);
        if(cartItem == null) throw  new APIException("Product " + product.getProductName() + " is not in the cart");

        int newQuantity = cartItem.getQuantity() + delete;
        if(newQuantity < 0){
            throw new APIException("Cannot reduce quantity below zero for product " + product.getProductName());
        }

        if(newQuantity == 0){
            deleteProductFromCart(cartId, productId);

        }else{
            cartItem.setProductPrice(product.getSpecialPrice());
            cartItem.setQuantity(cartItem.getQuantity() + delete);
            cartItem.setDiscount(product.getDiscount());
            cart.setTotalPrice(cart.getTotalPrice() + (cartItem.getProductPrice())*delete);
            cartRepository.save(cart);
        }

        CartItem updatedItem = cartItemRepository.save(cartItem);

        if(updatedItem.getQuantity() == 0){
            cartItemRepository.deleteById(updatedItem.getCartItemId());
        }

        CartDTO cartDTO = modelMapper.map(cart, CartDTO.class);
        List<CartItem> cartItems = cart.getCartItems();
        Stream<ProductDTO> productDTOStream = cartItems.stream().map(
                item -> {
                    ProductDTO prd = modelMapper.map(item.getProduct(), ProductDTO.class);
                    prd.setQuantity(item.getQuantity());
                    return prd;

                }
        );

        cartDTO.setProducts(productDTOStream.toList());

        return cartDTO;
    }

    @Transactional
    @Override
    public String deleteProductFromCart(Long cartId, Long productId) {

        Cart cart = cartRepository.findById(cartId).orElseThrow(() -> new ResourceNotFoundException("Cart", "cartId", cartId));
        CartItem cartItem = cartItemRepository.findCartItemByProductIdAndCartId(cartId, productId);
        if(cartItem == null){
            throw  new ResourceNotFoundException("Product", "productId", productId);
        }
        cart.setTotalPrice(cart.getTotalPrice() - (cartItem.getProductPrice()*cartItem.getQuantity()));

        cartRepository.deleteCartItemByProductIdAndCartId(cartId, productId);

        return "Product " + cartItem.getProduct().getProductName() + " has been removed from the cart";
    }

    @Override
    public void updateProductInCarts(Long cartId, Long productId) {

        Cart cart = cartRepository.findById(cartId).orElseThrow(() -> new ResourceNotFoundException("Cart", "cartId", cartId));
        Product product = productRepository.findById(productId).orElseThrow(() -> new ResourceNotFoundException("Product", "productId", productId));

        CartItem cartItem = cartItemRepository.findCartItemByProductIdAndCartId(cartId, productId);

        if(cartItem == null){
            throw new APIException("Product " + product.getProductName() + " is not in the cart");
        }

        double cartPrice = cart.getTotalPrice() - (cartItem.getProductPrice() * cartItem.getQuantity());

        cartItem.setProductPrice(product.getSpecialPrice());
        cart.setTotalPrice(cartPrice + (cartItem.getProductPrice() * cartItem.getQuantity()));

        cartItemRepository.save(cartItem);

    }

    @Transactional
    @Override
    public String createOrUpdateCartWithItems(List<CartItemDTO> cartItems) {
        // Get user's email
        String emailId = authUtil.loggedInEmail();

        // check if an existing cart is available or create a new one
        Cart existingCart = cartRepository.findCartByEmail(emailId);
        if(existingCart == null){
            existingCart = new Cart();
            existingCart.setUser(authUtil.loggedInUser());
            existingCart.setTotalPrice(0.00);
            existingCart = cartRepository.save(existingCart);

        }else{
            cartItemRepository.deleteAllByCartId(existingCart.getCartId());
        }

        double totolPrice = 0.00;
        // clear all current items in the existing cart
        for(CartItemDTO cartItemDTO: cartItems){
            Long productId = cartItemDTO.getProductId();
            Integer quantity = cartItemDTO.getQuantity();

            Product product = productRepository.findById(productId)
                    .orElseThrow(() -> new ResourceNotFoundException("Product", "productId", productId));
            product.setQuantity(product.getQuantity() - quantity);
            totolPrice += product.getSpecialPrice() * quantity;

            CartItem cartItem = new CartItem();
            cartItem.setProduct(product);
            cartItem.setCart(existingCart);
            cartItem.setQuantity(quantity);
            cartItem.setProductPrice(product.getSpecialPrice());
            cartItem.setDiscount(product.getDiscount());
            cartItemRepository.save(cartItem);
        }
        // Process each item in the request to add to the cart
        // Find the product by ID
        // Directly update product stock and total price
        // Create and save cart item

        existingCart.setTotalPrice(totolPrice);
        cartRepository.save(existingCart);

        return "Cart created/updated successfully with " + cartItems.size() + " items.";
    }

    private Cart createCart(){
        Cart userCart = cartRepository.findCartByEmail(authUtil.loggedInEmail());
        if(userCart != null){
            return userCart;
        }

        Cart cart = new Cart();
        cart.setTotalPrice(0.00);
        cart.setUser(authUtil.loggedInUser());
        Cart newCart = cartRepository.save(cart);
        return newCart;
    }
}
