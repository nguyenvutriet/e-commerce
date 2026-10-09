package vnt.vn.sb_ecom.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.ResponseCookie;
import vnt.vn.sb_ecom.security.response.UserInfoResponse;

@Data
@AllArgsConstructor
public class AuthenticationResult {
    private final UserInfoResponse response;
    private final ResponseCookie jwtCookie;
}
