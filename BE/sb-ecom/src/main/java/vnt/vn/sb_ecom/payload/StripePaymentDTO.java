package vnt.vn.sb_ecom.payload;

import lombok.Data;
import vnt.vn.sb_ecom.model.Address;

import java.util.Map;

@Data
public class StripePaymentDTO {
    private Long amount;
    private String currency;
    private String email;
    private String name;
    private String description;
    private Address address;
    private Map<String, String> metadata;
}