package vnt.vn.sb_ecom.service;

import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import vnt.vn.sb_ecom.payload.AuthenticationResult;
import vnt.vn.sb_ecom.payload.UserResponse;
import vnt.vn.sb_ecom.security.request.LoginRequest;
import vnt.vn.sb_ecom.security.request.SignupRequest;
import vnt.vn.sb_ecom.security.response.MessageResponse;
import vnt.vn.sb_ecom.security.response.UserInfoResponse;


public interface AuthService {

    AuthenticationResult login(LoginRequest loginRequest);

    ResponseEntity<MessageResponse> register(@Valid SignupRequest signupRequest);

    UserInfoResponse getCurrentUserDetails(Authentication authentication);

    ResponseCookie logoutUser();

    UserResponse getAllSellers(Pageable pageable);
}
