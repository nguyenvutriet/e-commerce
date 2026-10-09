package vnt.vn.sb_ecom.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import vnt.vn.sb_ecom.model.AppRole;
import vnt.vn.sb_ecom.model.Roles;
import vnt.vn.sb_ecom.model.Users;
import vnt.vn.sb_ecom.repository.RoleRepository;
import vnt.vn.sb_ecom.repository.UserRepository;
import vnt.vn.sb_ecom.security.jwt.AuthEntryPointJwt;
import vnt.vn.sb_ecom.security.jwt.AuthTokenFilter;
import vnt.vn.sb_ecom.security.services.UserDetailsServiceImpl;

import java.util.Set;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

    @Autowired
    UserDetailsServiceImpl userDetailsService;

    @Autowired
    private AuthEntryPointJwt unauthorizedHandler;

    @Bean
    public AuthTokenFilter authenticationJwtTokenFilter(){
        return new AuthTokenFilter();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider(){
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider(userDetailsService);
        authenticationProvider.setPasswordEncoder(passwordEncoder());

        return authenticationProvider;
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }


    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws  Exception{
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{
        http
                .csrf(csrf  -> csrf.disable())
                .cors(cors -> {})
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS
                ))
                .exceptionHandling(exception -> exception.authenticationEntryPoint(unauthorizedHandler))
        .authorizeHttpRequests(auth -> auth.requestMatchers("/api/auth/**").permitAll()
                .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                .requestMatchers("/v3/api-docs/**").permitAll()
                .requestMatchers("/h2-console/**").permitAll()
                .requestMatchers("/swagger-ui/**").permitAll()
                .requestMatchers("/api/public/**").permitAll()
                .requestMatchers("/api/admin/**").hasRole("ADMIN")
                .requestMatchers("/api/seller/**").hasAnyRole("SELLER", "ADMIN")
//                .requestMatchers("/api/admin/**").permitAll()
                .requestMatchers("/images/**").permitAll()
                .requestMatchers("/api/test/**").permitAll()
                .requestMatchers("/api/public/products/**").permitAll()
                .requestMatchers("/api/addresses").permitAll()
//                .requestMatchers(HttpMethod.OPTIONS,"/api/carts/**").permitAll()
                .anyRequest().authenticated())
                .authenticationProvider(authenticationProvider())
                .addFilterBefore(authenticationJwtTokenFilter(), UsernamePasswordAuthenticationFilter.class)
                .headers(headers -> headers.frameOptions(
                        frameOptionss -> frameOptionss.sameOrigin()
                ));


        return http.build();
    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer(){
        return (web -> web.ignoring().requestMatchers(
                "/v2/api-docs",
                "/configuration/ui",
                "/swagger-resources/**",
                "/configuration/security",
                "/swagger-ui.html" ,
                "/webjars/**"));
    }

    @Bean
    public CommandLineRunner initData(RoleRepository roleRepository, UserRepository userRepository, PasswordEncoder passwordEncoder) {
        return args -> {
            // Retrieve or create roles
            Roles userRole = roleRepository.findByRoleName(AppRole.ROLE_USER)
                    .orElseGet(() -> {
                        Roles newUserRole = new Roles(AppRole.ROLE_USER);
                        return roleRepository.save(newUserRole);
                    });

            Roles sellerRole = roleRepository.findByRoleName(AppRole.ROLE_SELLER)
                    .orElseGet(() -> {
                        Roles newSellerRole = new Roles(AppRole.ROLE_SELLER);
                        return roleRepository.save(newSellerRole);
                    });

            Roles adminRole = roleRepository.findByRoleName(AppRole.ROLE_ADMIN)
                    .orElseGet(() -> {
                        Roles newAdminRole = new Roles(AppRole.ROLE_ADMIN);
                        return roleRepository.save(newAdminRole);
                    });

            Set<Roles> userRoles = Set.of(userRole);
            Set<Roles> sellerRoles = Set.of(sellerRole);
            Set<Roles> adminRoles = Set.of(userRole, sellerRole, adminRole);


            // Create users if not already present
            if (!userRepository.existsByUserName("user1")) {
                Users user1 = new Users("user1", "user1@example.com", passwordEncoder.encode("password1"));
                userRepository.save(user1);
            }

            if (!userRepository.existsByUserName("seller1")) {
                Users seller1 = new Users("seller1", "seller1@example.com", passwordEncoder.encode("password2"));
                userRepository.save(seller1);
            }

            if (!userRepository.existsByUserName("admin")) {
                Users admin = new Users("admin", "admin@example.com", passwordEncoder.encode("adminPass"));
                userRepository.save(admin);
            }

            // Update roles for existing user
//            userRepository.findByUserName("user1").ifPresent(user -> {
//                user.setRoles(userRoles);
//                userRepository.save(user);
//            });
            Users user1 = userRepository.findByUserName("user1");
            if(user1 != null){
                user1.setRoles(userRoles);
                userRepository.save(user1);
            }


//            userRepository.findByUserName("seller1").ifPresent(seller -> {
//                seller.setRoles(sellerRoles);
//                userRepository.save(seller);
//            });
            Users seller1 = userRepository.findByUserName("seller1");
            if(seller1 != null){
                seller1.setRoles(userRoles);
                userRepository.save(seller1);
            }

//            userRepository.findByUserName("admin").ifPresent(admin -> {
//                admin.setRoles(adminRoles);
//                userRepository.save(admin);
//            });
            Users admin1 = userRepository.findByUserName("admin1");
            if(admin1 != null){
                admin1.setRoles(userRoles);
                userRepository.save(admin1);
            }


        };
    }





}
