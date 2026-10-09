package vnt.vn.sb_ecom.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import vnt.vn.sb_ecom.model.Users;
import vnt.vn.sb_ecom.repository.UserRepository;

@Component
public class AuthUtil {

    @Autowired
    private UserRepository userRepository;


    public String loggedInEmail() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Users user = userRepository.findByUserName(authentication.getName());

        return user.getEmail();
    }

    public Users loggedInUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        Users user = userRepository.findByUserName(authentication.getName());

        return user;

    }
}
