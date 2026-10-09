package vnt.vn.sb_ecom.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import vnt.vn.sb_ecom.config.AppConstants;
import vnt.vn.sb_ecom.payload.AuthenticationResult;
import vnt.vn.sb_ecom.repository.RoleRepository;
import vnt.vn.sb_ecom.repository.UserRepository;
import vnt.vn.sb_ecom.security.jwt.JwtUtils;
import vnt.vn.sb_ecom.security.request.LoginRequest;
import vnt.vn.sb_ecom.security.request.SignupRequest;
import vnt.vn.sb_ecom.security.response.MessageResponse;
import vnt.vn.sb_ecom.service.AuthService;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/auth")
public class AuthenticationController {

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    private AuthService authService;

    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@RequestBody LoginRequest loginRequest, Authentication authentication){

//        try{
//            authentication = authenticationManager.authenticate(
//                    new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword())
//            );
//
//        }catch(AuthenticationException e){
//
//            Map<String, Object> map = new HashMap<>();
//            map.put("message", "Bad credentials");
//            map.put("status", false);
//
//            return new ResponseEntity<Object>(map, HttpStatus.UNAUTHORIZED);
//        }
//        SecurityContextHolder.getContext().setAuthentication(authentication);
//        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
//        ResponseCookie jwtCookie = jwtUtils.generateJwtCookie(userDetails);
////        String jwtToken = jwtUtils.generateTokenFromUsername(userDetails.getUsername());
//        List<String> roles = userDetails.getAuthorities().stream()
//                .map( item -> item.getAuthority())
//                .toList();

//        UserInfoResponse response = new UserInfoResponse(userDetails.getId(), userDetails.getUsername(), roles, userDetails.getEmail(), jwtCookie.toString());
//        return new ResponseEntity<>(HttpHeaders.SET_COOKIE, jwtCookie.toString().body(response));

        AuthenticationResult result = authService.login(loginRequest);
        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE,
                        result.getJwtCookie().toString())
                .body(result.getResponse());
    }

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signupRequest){

//        if(userRepository.existsByUserName(signupRequest.getUsername())){
//            return ResponseEntity.badRequest().body(new MessageResponse("Error: Username is already taken!"));
//        }
//
//        if(userRepository.existsByEmail(signupRequest.getEmail())){
//            return ResponseEntity.badRequest().body(new MessageResponse("Error: Email is already taken!"));
//        }
//
//        Users user = new Users(
//                signupRequest.getUsername(),
//                signupRequest.getEmail(),
//                passwordEncoder.encode(signupRequest.getPassword()));
//
//        Set<String> strRoles = signupRequest.getRoles();
//        Set<Roles> roles = new HashSet<>();
//
//        if(strRoles == null){
//            Roles userRole = roleRepository.findByRoleName(AppRole.ROLE_USER).orElseThrow(()
//            -> new RuntimeException("Error: Role is not found"));
//            roles.add(userRole);
//        }else{
//            strRoles.forEach(
//                    role -> {
//                        switch(role){
//                            case "admin":
//                                Roles adminRole = roleRepository.findByRoleName(AppRole.ROLE_ADMIN).orElseThrow(()
//                                        -> new RuntimeException("Error: Role is not found"));
//                                roles.add(adminRole);
//                                break;
//                            case "seller":
//                                Roles sellerRole = roleRepository.findByRoleName(AppRole.ROLE_SELLER).orElseThrow(()
//                                        -> new RuntimeException("Error: Role is not found"));
//                                roles.add(sellerRole);
//                                break;
//                            default:
//                                Roles userRole = roleRepository.findByRoleName(AppRole.ROLE_USER).orElseThrow(()
//                                        -> new RuntimeException("Error: Role is not found"));
//                                roles.add(userRole);
//                        }
//                    }
//            );
//        }
//
//        user.setRoles(roles);
//        userRepository.save(user);
//        return ResponseEntity.ok(new MessageResponse("User registered successfully"));



        return authService.register(signupRequest);
    }

    @GetMapping("/username")
    public String currentUserName(Authentication authentication){
        if(authentication != null) return authentication.getName();
        return "Null";
    }

    @GetMapping("/user")
    public ResponseEntity<?> getUserDetails(Authentication authentication){
//        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getDetails();
//
//        List<String> roles =  userDetails.getAuthorities().stream()
//                .map(auth -> auth.getAuthority())
//                .collect(Collectors.toList());
//        ResponseCookie jwtCookie = jwtUtils.generateJwtCookie(userDetails);
//        UserInfoResponse response = new UserInfoResponse(userDetails.getId(), userDetails.getUsername(), roles, userDetails.getEmail(), jwtCookie.toString());

        return ResponseEntity.ok(authService.getCurrentUserDetails(authentication));
    }

    @PostMapping("/signout")
    public ResponseEntity<?> signoutUser(){
        ResponseCookie cookie = authService.logoutUser();
        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE,
                        cookie.toString())
                .body(new MessageResponse("You've been signed out!"));
    }

    @GetMapping("/sellers")
    public ResponseEntity<?> getAllSellers(
            @RequestParam(name = "pageNumber", defaultValue = AppConstants.PAGE_NUMBER, required = false) Integer pageNumber) {

        Sort sortByAndOrder = Sort.by(AppConstants.SORT_USERS_BY).descending();
        Pageable pageDetails = PageRequest.of(pageNumber ,
                Integer.parseInt(AppConstants.PAGE_SIZE), sortByAndOrder);

        return ResponseEntity.ok(authService.getAllSellers(pageDetails));
    }









}
