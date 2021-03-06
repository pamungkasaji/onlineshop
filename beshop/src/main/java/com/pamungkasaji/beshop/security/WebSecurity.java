package com.pamungkasaji.beshop.security;

import com.pamungkasaji.beshop.repository.UserRepository;
import com.pamungkasaji.beshop.service.UserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.cors.reactive.CorsWebFilter;

import java.util.Arrays;

//@EnableGlobalMethodSecurity(securedEnabled=true, prePostEnabled=true)
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
public class WebSecurity extends WebSecurityConfigurerAdapter {

    private final UserService userDetailsService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final UserRepository userRepository;

    public WebSecurity(UserService userDetailsService, BCryptPasswordEncoder bCryptPasswordEncoder, UserRepository userRepository) {
        this.userDetailsService = userDetailsService;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.userRepository = userRepository;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(bCryptPasswordEncoder);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .cors().and()
                .csrf().disable().authorizeRequests()
                .antMatchers(HttpMethod.POST, SecurityConstants.SIGN_UP_URL)
                .permitAll()
                .antMatchers(HttpMethod.GET, "/api/products")
                .permitAll()
                .antMatchers(HttpMethod.GET, "/api/products/**")
                .permitAll()

                .antMatchers(HttpMethod.GET, "/api/config/paypal")
                .permitAll()

                .antMatchers(HttpMethod.POST, "/api/orders/checkout")
                .permitAll()

                //midtrans
                .antMatchers(HttpMethod.PUT, "/api/orders/{\\d+}/pay")
                .permitAll()
                .antMatchers(HttpMethod.GET, "/api/orders/{\\d+}/payment")
                .permitAll()
                .antMatchers(HttpMethod.POST, "/api/payment/notification")
                .permitAll()

//                .antMatchers(HttpMethod.GET, "/api/payment/key")
//                .permitAll()

                //shipping

                .antMatchers(HttpMethod.GET, "/api/shipping/**")
                .permitAll()
//                .antMatchers(HttpMethod.GET, "/api/shipping/city")
//                .permitAll()
                .antMatchers(HttpMethod.POST, "/api/shipping/cost")
                .permitAll()

//                .antMatchers(HttpMethod.GET, "/api/shipping/**")
//                .permitAll()

                .antMatchers("/error")
                .permitAll()

                .antMatchers("/",
                        "/error",
                        "/favicon.ico",
                        "/**/*.png",
                        "/**/*.gif",
                        "/**/*.svg",
                        "/**/*.jpg",
                        "/**/*.html",
                        "/**/*.css",
                        "/**/*.js").permitAll()

                .antMatchers("/images/**",
                        "/api/products/upload",
                        "/api/login",
                        "/api/auth2/**").permitAll()

                .antMatchers(SecurityConstants.H2_CONSOLE)
                .permitAll()
//                .antMatchers(HttpMethod.DELETE, "/users/**").hasRole("ADMIN")
                .anyRequest().authenticated().and()
                .addFilter(getAuthenticationFilter())
                .addFilter(new AuthorizationFilter(authenticationManager(), userRepository))
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        http.headers().frameOptions().disable();

    }

    protected AuthenticationFilter getAuthenticationFilter() throws Exception {
        final AuthenticationFilter filter = new AuthenticationFilter(authenticationManager());
        filter.setFilterProcessesUrl("/api/users/login");
        return filter;
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        final CorsConfiguration corsConfig = new CorsConfiguration();

        corsConfig.setAllowedOrigins(Arrays.asList("http://shop.pamungkasaji.com", "http://localhost:3000"));
        corsConfig.setAllowedMethods(Arrays.asList("*"));
        corsConfig.setAllowedHeaders(Arrays.asList("*"));

        final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", corsConfig);

        return source;
    }
}
