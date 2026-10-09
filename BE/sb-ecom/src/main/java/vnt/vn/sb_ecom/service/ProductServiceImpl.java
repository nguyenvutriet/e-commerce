package vnt.vn.sb_ecom.service;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import vnt.vn.sb_ecom.handler.APIException;
import vnt.vn.sb_ecom.handler.ResourceNotFoundException;
import vnt.vn.sb_ecom.model.Cart;
import vnt.vn.sb_ecom.model.Category;
import vnt.vn.sb_ecom.model.Product;
import vnt.vn.sb_ecom.model.Users;
import vnt.vn.sb_ecom.payload.CartDTO;
import vnt.vn.sb_ecom.payload.ProductDTO;
import vnt.vn.sb_ecom.payload.ProductResponse;
import vnt.vn.sb_ecom.repository.CartRepository;
import vnt.vn.sb_ecom.repository.CategoryRepository;
import vnt.vn.sb_ecom.repository.ProductRepository;
import vnt.vn.sb_ecom.util.AuthUtil;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductServiceImpl implements ProductService{

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private FileService fileService;

    @Value("${project.image}")
    private String path;

    @Value("${image.base.url}")
    private String imageBaseUrl;

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private CartService cartService;

    @Autowired
    private AuthUtil authUtil;

    @Override
    public ProductDTO addProduct(ProductDTO productDTO, Long categoryId) {
        // Check if product already present or not
        Category category = categoryRepository.findById(categoryId).orElseThrow(
                () -> new ResourceNotFoundException("Category", "CategoryId", categoryId)
        );

        boolean isProductNotPresent = true;
        List<Product> products = category.getProducts();
        for(Product value: products){
            if(value.getProductName().equals(productDTO.getProductName())){
                isProductNotPresent = false;
                break;
            }
        }

        if(isProductNotPresent){
            Product product = modelMapper.map(productDTO, Product.class);
            product.setImage("product.png");
            product.setCategory(category);
            product.setUser(authUtil.loggedInUser());
            double specialPrice = product.getPrice() - (product.getDiscount()*0.01) * product.getPrice();
            product.setSpecialPrice(specialPrice);
            Product savedProduct = productRepository.save(product);

            return modelMapper.map(savedProduct, ProductDTO.class);
        } else{
            throw new APIException("Product already exist!!");
        }
    }

    @Override
    public ProductResponse getAllProducts(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder, String keyword, String category) {

        Sort sortByAndOrder = sortOrder.equalsIgnoreCase("asc")
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        Pageable pageDetails = PageRequest.of(pageNumber, pageSize, sortByAndOrder);
        Specification<Product> spec = Specification.unrestricted();
        if(keyword != null && !keyword.isEmpty()){
            spec = spec.and((root, query, criteriaBuilder) -> criteriaBuilder.like(criteriaBuilder.lower(root.get("productName")), "%" + keyword.toLowerCase() + "%"));
        }

        if(category != null && !category.isEmpty()){
            spec = spec.and((root, query, criteriaBuilder) -> criteriaBuilder.like(root.get("category").get("categoryName"), category));
        }


        Page<Product> pageProducts = productRepository.findAll(spec, pageDetails);

        // product size is 0
        List<Product> products = pageProducts.getContent();
        List<ProductDTO> productDTOS = products
                .stream()
                .map(product ->
                        {
                            ProductDTO productDTO = modelMapper.map(product, ProductDTO.class);
                            productDTO.setImage(constructImageUrl(product.getImage()));
                            return productDTO;
                        }
                )
                .toList();

        ProductResponse productResponse = new ProductResponse();
        productResponse.setContent(productDTOS);
        productResponse.setPageNumber(pageProducts.getNumber());
        productResponse.setPageSize(pageProducts.getSize());
        productResponse.setTotalElements(pageProducts.getTotalElements());
        productResponse.setTotalPages(pageProducts.getTotalPages());
        productResponse.setLastPage(pageProducts.isLast());

        return productResponse;
    }

    private String constructImageUrl(String imageName){

        return imageBaseUrl.endsWith("/") ? imageBaseUrl + imageName : imageBaseUrl + "/" + imageName;
    }

    @Override
    public ProductResponse searchByCategory(Long categoryId, Integer pageNumber, Integer pageSize, String sortBy, String sortOrder) {
        // product size is 0
        Category category = categoryRepository.findById(categoryId).orElseThrow(
                () -> new ResourceNotFoundException("Category", "CategoryId", categoryId)
        );

        Sort sortByAndOrder = sortOrder.equalsIgnoreCase("asc")
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        Pageable pageDetails = PageRequest.of(pageNumber, pageSize, sortByAndOrder);
        Page<Product> pageProducts = productRepository.findByCategoryOrderByPriceAsc(category, pageDetails);

        List<Product> products = pageProducts.getContent();
        List<ProductDTO> productDTOS = products
                .stream()
                .map(product -> modelMapper.map(product, ProductDTO.class))
                .toList();

        ProductResponse productResponse = new ProductResponse();
        productResponse.setContent(productDTOS);
        productResponse.setPageNumber(pageProducts.getNumber());
        productResponse.setPageSize(pageProducts.getSize());
        productResponse.setTotalElements(pageProducts.getTotalElements());
        productResponse.setTotalPages(pageProducts.getTotalPages());
        productResponse.setLastPage(pageProducts.isLast());

        return productResponse;
    }

    @Override
    public ProductResponse searchProductByKeyword(String keyword, Integer pageNumber, Integer pageSize, String sortBy, String sortOrder) {

        //%%TRA%

        Sort sortByAndOrder = sortOrder.equalsIgnoreCase("asc")
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        Pageable pageDetails = PageRequest.of(pageNumber, pageSize, sortByAndOrder);
        Page<Product> pageProducts = productRepository.findByProductNameLikeIgnoreCase(keyword, pageDetails);

        List<Product> products = pageProducts.getContent();
        List<ProductDTO> productDTOS = products
                .stream()
                .map(product -> modelMapper.map(product, ProductDTO.class))
                .toList();

        ProductResponse productResponse = new ProductResponse();
        productResponse.setContent(productDTOS);
        productResponse.setPageNumber(pageProducts.getNumber());
        productResponse.setPageSize(pageProducts.getSize());
        productResponse.setTotalElements(pageProducts.getTotalElements());
        productResponse.setTotalPages(pageProducts.getTotalPages());
        productResponse.setLastPage(pageProducts.isLast());

        return productResponse;
    }

    @Override
    public ProductDTO updateProduct(Long productId, ProductDTO productDTO) {

        Product product = modelMapper.map(productDTO, Product.class);

        // Get the existing product from DB
        Product productFromDB = productRepository.findById(productId).orElseThrow(
                () -> new ResourceNotFoundException("Product", "productId", productId)
        );

        // Update the product info with user shared
        productFromDB.setProductName(product.getProductName());
        productFromDB.setDescription(product.getDescription());
        productFromDB.setQuantity(product.getQuantity());
        productFromDB.setPrice(product.getPrice());
        productFromDB.setDiscount(product.getDiscount());
        productFromDB.setSpecialPrice(product.getSpecialPrice());


        // Save to DB
        Product savedProduct = productRepository.save(productFromDB);
        ProductDTO savedProductDTO = modelMapper.map(savedProduct, ProductDTO.class);

        return savedProductDTO;
    }

    @Override
    public ProductDTO deleteProduct(Long productId) {

        Product productFromDB = productRepository.findById(productId).orElseThrow(
                () -> new ResourceNotFoundException("Product", "productId", productId)
        );

        List<Cart> carts = cartRepository.findCartsByProductId(productId);
        carts.forEach(cart -> cartService.deleteProductFromCart(cart.getCartId(), productId));

        productRepository.delete((productFromDB));

        return modelMapper.map(productFromDB, ProductDTO.class);
    }

    @Override
    public ProductDTO updateProductImage(Long productId, MultipartFile image) throws IOException {

        // Get the product from DB
        Product productFromDB = productRepository.findById(productId).orElseThrow(
                () -> new ResourceNotFoundException("Product", "productId", productId)
        );

        // Upload image to server
        // Get the file name of uploaded image
//        String path = "images/";
        String fileName = fileService.uploadImage(path, image);


        // Updating the new file name to the product
        productFromDB.setImage(fileName);


        // Save updated product
        Product savedProduct = productRepository.save(productFromDB);

        List<Cart> carts = cartRepository.findCartsByProductId(productId);

        List<CartDTO> cartDTOS = carts.stream().map(cart -> {
            CartDTO cartDTO = modelMapper.map(cart, CartDTO.class);
            List<ProductDTO> products = cart.getCartItems().stream().map(
                    p -> modelMapper.map(p.getProduct(), ProductDTO.class))
                    .collect(Collectors.toList());

            cartDTO.setProducts(products);
            return cartDTO;
        }).collect(Collectors.toList());

        cartDTOS.forEach(cart -> cartService.updateProductInCarts(cart.getCartId(), productId));

        // Return DTO after mapping product to DTO
        return modelMapper.map(savedProduct, ProductDTO.class);
    }

    @Override
    public ProductResponse getAllProductsForAdmin(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder) {
        Sort sortByAndOrder = sortOrder.equalsIgnoreCase("asc")
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        Pageable pageDetails = PageRequest.of(pageNumber, pageSize, sortByAndOrder);


        Page<Product> pageProducts = productRepository.findAll(pageDetails);

        // product size is 0
        List<Product> products = pageProducts.getContent();
        List<ProductDTO> productDTOS = products
                .stream()
                .map(product ->
                        {
                            ProductDTO productDTO = modelMapper.map(product, ProductDTO.class);
                            productDTO.setImage(constructImageUrl(product.getImage()));
                            return productDTO;
                        }
                )
                .toList();

        ProductResponse productResponse = new ProductResponse();
        productResponse.setContent(productDTOS);
        productResponse.setPageNumber(pageProducts.getNumber());
        productResponse.setPageSize(pageProducts.getSize());
        productResponse.setTotalElements(pageProducts.getTotalElements());
        productResponse.setTotalPages(pageProducts.getTotalPages());
        productResponse.setLastPage(pageProducts.isLast());

        return productResponse;
    }

    @Override
    public ProductResponse getAllProductsForSeller(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder) {
        Sort sortByAndOrder = sortOrder.equalsIgnoreCase("asc")
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        Pageable pageDetails = PageRequest.of(pageNumber, pageSize, sortByAndOrder);
        Users user = authUtil.loggedInUser();
        Page<Product> pageProducts = productRepository.findByUser(user, pageDetails);

        // product size is 0
        List<Product> products = pageProducts.getContent();
        List<ProductDTO> productDTOS = products
                .stream()
                .map(product ->
                        {
                            ProductDTO productDTO = modelMapper.map(product, ProductDTO.class);
                            productDTO.setImage(constructImageUrl(product.getImage()));
                            return productDTO;
                        }
                )
                .toList();

        ProductResponse productResponse = new ProductResponse();
        productResponse.setContent(productDTOS);
        productResponse.setPageNumber(pageProducts.getNumber());
        productResponse.setPageSize(pageProducts.getSize());
        productResponse.setTotalElements(pageProducts.getTotalElements());
        productResponse.setTotalPages(pageProducts.getTotalPages());
        productResponse.setLastPage(pageProducts.isLast());

        return productResponse;
    }


}
