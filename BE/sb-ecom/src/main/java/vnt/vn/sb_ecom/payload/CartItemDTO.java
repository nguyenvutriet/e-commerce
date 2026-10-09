package vnt.vn.sb_ecom.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CartItemDTO {
    private Long productId;
    private Integer quantity;
//    private CartDTO cart;
//    private ProductDTO product;
//    private Integer quantity;
//    private Double discount;
//    private Double productPrice;

}
