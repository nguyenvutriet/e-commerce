package vnt.vn.sb_ecom.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import vnt.vn.sb_ecom.model.Roles;

import java.util.HashSet;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {

    private Long userId;
    private String username;
    private String email;
    private String password;
    private Set<Roles> roles = new HashSet<>();
    private AddressDTO address;
    private CartDTO cart;
}
