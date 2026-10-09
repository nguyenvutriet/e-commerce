package vnt.vn.sb_ecom.service;

import jakarta.transaction.Transactional;
import vnt.vn.sb_ecom.payload.CartDTO;
import vnt.vn.sb_ecom.payload.CartItemDTO;

import java.util.List;

public interface CartService {

    CartDTO addProductToCart(Long productId, Integer quantity);

    List<CartDTO> getAllCarts();

    CartDTO getCart(String emailId, Long cartId);

    @Transactional
    CartDTO updateProductQuantity(Long productId, int delete);

    String deleteProductFromCart(Long cartId, Long productId);

    void updateProductInCarts(Long cartId, Long productId);

    String createOrUpdateCartWithItems(List<CartItemDTO> cartItems);
}
