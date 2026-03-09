package com.primebuild_online.security;

import com.primebuild_online.security.jwt.AuthEntryPointJwt;
import com.primebuild_online.security.jwt.AuthTokenFilter;
import com.primebuild_online.security.oauth2.OAuth2AuthenticationSuccessHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true,
        securedEnabled = true,
        jsr250Enabled = true)
public class SecurityConfig {


    @Autowired
    private AuthTokenFilter authTokenFilter;

    @Autowired
    private OAuth2AuthenticationSuccessHandler oAuth2AuthenticationSuccessHandler;

    @Autowired
    private AuthEntryPointJwt unauthorizedHandler;

    @Autowired
    private CustomAccessDeniedHandler customAccessDeniedHandler;

    @Bean
    public AuthTokenFilter authenticationJwtTokenFilter() {
        return new AuthTokenFilter();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration builder) throws Exception {
        return builder.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain apiSecurityFilterChain(HttpSecurity http) throws Exception {
        http
                .securityMatcher("/api/**") // Only /api/** uses this chain
                .cors(Customizer.withDefaults())
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/auth/mail").permitAll()
                        .requestMatchers("/api/auth/login").permitAll()
                        .requestMatchers("/api/auth/signup").permitAll()
                        .requestMatchers("/api/auth/forgot-password").permitAll()
                        .requestMatchers("/api/auth/reset-password").permitAll()
                        .requestMatchers("/api/auth/auth/self").permitAll()
                        .requestMatchers("/home/**").permitAll()

                        .requestMatchers(HttpMethod.GET, "/api/manufacturer/**")
                        .hasAnyAuthority("ADMIN", "USER_MANAGEMENT", "INVENTORY_MANAGEMENT",
                                "BUILD_MANAGEMENT", "INVOICE_MANAGEMENT", "CUSTOMER")
                        .requestMatchers(HttpMethod.POST, "/api/manufacturer/**")
                        .hasAnyAuthority("ADMIN", "INVENTORY_MANAGEMENT")
                        .requestMatchers(HttpMethod.PUT, "/api/manufacturer/**")
                        .hasAnyAuthority("ADMIN", "INVENTORY_MANAGEMENT")
                        .requestMatchers(HttpMethod.DELETE, "/api/manufacturer/**")
                        .hasAnyAuthority("ADMIN", "INVENTORY_MANAGEMENT")

                        .requestMatchers(HttpMethod.GET, "/api/component/**")
                        .hasAnyAuthority("ADMIN", "USER_MANAGEMENT", "INVENTORY_MANAGEMENT", "BUILD_MANAGEMENT",
                                "INVOICE_MANAGEMENT", "CUSTOMER")
                        .requestMatchers(HttpMethod.POST, "/api/component/**")
                        .hasAnyAuthority("ADMIN", "INVENTORY_MANAGEMENT")
                        .requestMatchers(HttpMethod.PUT, "/api/component/**")
                        .hasAnyAuthority("ADMIN", "INVENTORY_MANAGEMENT")
                        .requestMatchers(HttpMethod.DELETE, "/api/component/**")
                        .hasAnyAuthority("ADMIN", "INVENTORY_MANAGEMENT")

                        .requestMatchers(HttpMethod.GET, "/api/item/**")
                        .hasAnyAuthority("ADMIN", "USER_MANAGEMENT", "INVENTORY_MANAGEMENT", "BUILD_MANAGEMENT",
                                "INVOICE_MANAGEMENT", "CUSTOMER")
                        .requestMatchers(HttpMethod.POST, "/api/item/**")
                        .hasAnyAuthority("ADMIN", "INVENTORY_MANAGEMENT")
                        .requestMatchers(HttpMethod.PUT, "/api/item/**")
                        .hasAnyAuthority("ADMIN", "INVENTORY_MANAGEMENT")
                        .requestMatchers(HttpMethod.DELETE, "/api/item/**")
                        .hasAnyAuthority("ADMIN", "INVENTORY_MANAGEMENT")

                        .requestMatchers(HttpMethod.GET, "/api/feature_type/**")
                        .hasAnyAuthority("ADMIN", "USER_MANAGEMENT", "INVENTORY_MANAGEMENT", "BUILD_MANAGEMENT",
                                "INVOICE_MANAGEMENT", "CUSTOMER")
                        .requestMatchers(HttpMethod.POST, "/api/feature_type/**")
                        .hasAnyAuthority("ADMIN", "INVENTORY_MANAGEMENT")
                        .requestMatchers(HttpMethod.PUT, "/api/feature_type/**")
                        .hasAnyAuthority("ADMIN", "INVENTORY_MANAGEMENT")
                        .requestMatchers(HttpMethod.DELETE, "/api/feature_type/**")
                        .hasAnyAuthority("ADMIN", "INVENTORY_MANAGEMENT")

                        .requestMatchers(HttpMethod.GET, "/api/feature/**")
                        .hasAnyAuthority("ADMIN", "USER_MANAGEMENT", "INVENTORY_MANAGEMENT", "BUILD_MANAGEMENT",
                                "INVOICE_MANAGEMENT", "CUSTOMER")
                        .requestMatchers(HttpMethod.POST, "/api/feature/**")
                        .hasAnyAuthority("ADMIN", "INVENTORY_MANAGEMENT")
                        .requestMatchers(HttpMethod.PUT, "/api/feature/**")
                        .hasAnyAuthority("ADMIN", "INVENTORY_MANAGEMENT")
                        .requestMatchers(HttpMethod.DELETE, "/api/feature/**")
                        .hasAnyAuthority("ADMIN", "INVENTORY_MANAGEMENT")

                        .requestMatchers(HttpMethod.GET, "/api/item_feature/**")
                        .hasAnyAuthority("ADMIN", "USER_MANAGEMENT", "INVENTORY_MANAGEMENT", "BUILD_MANAGEMENT",
                                "INVOICE_MANAGEMENT", "CUSTOMER")
                        .requestMatchers(HttpMethod.POST, "/api/item_feature/**")
                        .hasAnyAuthority("ADMIN", "INVENTORY_MANAGEMENT")
                        .requestMatchers(HttpMethod.PUT, "/api/item_feature/**")
                        .hasAnyAuthority("ADMIN", "INVENTORY_MANAGEMENT")
                        .requestMatchers(HttpMethod.DELETE, "/api/item_feature/**")
                        .hasAnyAuthority("ADMIN", "INVENTORY_MANAGEMENT")

                        .requestMatchers(HttpMethod.GET, "/api/component_feature_type/**")
                        .hasAnyAuthority("ADMIN", "USER_MANAGEMENT", "INVENTORY_MANAGEMENT", "BUILD_MANAGEMENT",
                                "INVOICE_MANAGEMENT", "CUSTOMER")
                        .requestMatchers(HttpMethod.POST, "/api/component_feature_type/**")
                        .hasAnyAuthority("ADMIN", "INVENTORY_MANAGEMENT")
                        .requestMatchers(HttpMethod.PUT, "/api/component_feature_type/**")
                        .hasAnyAuthority("ADMIN", "INVENTORY_MANAGEMENT")
                        .requestMatchers(HttpMethod.DELETE, "/api/component_feature_type/**")
                        .hasAnyAuthority("ADMIN", "INVENTORY_MANAGEMENT")

                        .requestMatchers(HttpMethod.GET, "/api/build/**")
                        .hasAnyAuthority("ADMIN", "USER_MANAGEMENT", "INVENTORY_MANAGEMENT", "BUILD_MANAGEMENT",
                                "INVOICE_MANAGEMENT", "CUSTOMER")
                        .requestMatchers(HttpMethod.POST, "/api/build/**")
                        .hasAnyAuthority("ADMIN", "USER_MANAGEMENT", "INVENTORY_MANAGEMENT", "BUILD_MANAGEMENT",
                                "INVOICE_MANAGEMENT", "CUSTOMER")
                        .requestMatchers(HttpMethod.PUT, "/api/build/**")
                        .hasAnyAuthority("ADMIN", "USER_MANAGEMENT", "INVENTORY_MANAGEMENT", "BUILD_MANAGEMENT",
                                "INVOICE_MANAGEMENT", "CUSTOMER")
                        .requestMatchers(HttpMethod.DELETE, "/api/build/**")
                        .hasAnyAuthority("ADMIN", "USER_MANAGEMENT", "INVENTORY_MANAGEMENT", "BUILD_MANAGEMENT",
                                "INVOICE_MANAGEMENT", "CUSTOMER")

                        .requestMatchers(HttpMethod.GET, "/api/compatibility/**")
                        .hasAnyAuthority("ADMIN", "USER_MANAGEMENT", "INVENTORY_MANAGEMENT", "BUILD_MANAGEMENT",
                                "INVOICE_MANAGEMENT", "CUSTOMER")
                        .requestMatchers(HttpMethod.POST, "/api/compatibility/**")
                        .hasAnyAuthority("ADMIN", "USER_MANAGEMENT", "INVENTORY_MANAGEMENT", "BUILD_MANAGEMENT",
                                "INVOICE_MANAGEMENT", "CUSTOMER")
                        .requestMatchers(HttpMethod.PUT, "/api/compatibility/**")
                        .hasAnyAuthority("ADMIN", "USER_MANAGEMENT", "INVENTORY_MANAGEMENT", "BUILD_MANAGEMENT",
                                "INVOICE_MANAGEMENT", "CUSTOMER")
                        .requestMatchers(HttpMethod.DELETE, "/api/compatibility/**")
                        .hasAnyAuthority("ADMIN", "USER_MANAGEMENT", "INVENTORY_MANAGEMENT", "BUILD_MANAGEMENT",
                                "INVOICE_MANAGEMENT", "CUSTOMER")

                        .requestMatchers(HttpMethod.GET, "/api/notification/**")
                        .hasAnyAuthority("ADMIN", "USER_MANAGEMENT", "INVENTORY_MANAGEMENT", "BUILD_MANAGEMENT",
                                "INVOICE_MANAGEMENT", "CUSTOMER")
                        .requestMatchers(HttpMethod.POST, "/api/notification/**")
                        .hasAnyAuthority("ADMIN", "USER_MANAGEMENT", "INVENTORY_MANAGEMENT", "BUILD_MANAGEMENT",
                                "INVOICE_MANAGEMENT", "CUSTOMER")
                        .requestMatchers(HttpMethod.PUT, "/api/notification/**")
                        .hasAnyAuthority("ADMIN", "USER_MANAGEMENT", "INVENTORY_MANAGEMENT", "BUILD_MANAGEMENT",
                                "INVOICE_MANAGEMENT", "CUSTOMER")
                        .requestMatchers(HttpMethod.DELETE, "/api/notification/**")
                        .hasAnyAuthority("ADMIN", "USER_MANAGEMENT", "INVENTORY_MANAGEMENT", "BUILD_MANAGEMENT",
                                "INVOICE_MANAGEMENT", "CUSTOMER")

                        .requestMatchers(HttpMethod.GET, "/api/user/**")
                        .hasAnyAuthority("ADMIN", "USER_MANAGEMENT")
                        .requestMatchers(HttpMethod.POST, "/api/user/**")
                        .hasAnyAuthority("ADMIN", "USER_MANAGEMENT")
                        .requestMatchers(HttpMethod.PUT, "/api/user/**")
                        .hasAnyAuthority("ADMIN", "USER_MANAGEMENT")
                        .requestMatchers(HttpMethod.DELETE, "/api/user/**")
                        .hasAnyAuthority("ADMIN", "USER_MANAGEMENT")

                        .requestMatchers(HttpMethod.GET, "/api/role/**")
                        .hasAnyAuthority("ADMIN", "USER_MANAGEMENT")
                        .requestMatchers(HttpMethod.POST, "/api/role/**")
                        .hasAnyAuthority("ADMIN", "USER_MANAGEMENT")
                        .requestMatchers(HttpMethod.PUT, "/api/role/**")
                        .hasAnyAuthority("ADMIN", "USER_MANAGEMENT")
                        .requestMatchers(HttpMethod.DELETE, "/api/role/**")
                        .hasAnyAuthority("ADMIN", "USER_MANAGEMENT")

                        .requestMatchers(HttpMethod.GET, "/api/cart/**")
                        .hasAnyAuthority("ADMIN", "USER_MANAGEMENT", "INVENTORY_MANAGEMENT", "BUILD_MANAGEMENT",
                                "INVOICE_MANAGEMENT", "CUSTOMER")
                        .requestMatchers(HttpMethod.POST, "/api/cart/**")
                        .hasAnyAuthority("ADMIN", "USER_MANAGEMENT", "INVENTORY_MANAGEMENT", "BUILD_MANAGEMENT",
                                "INVOICE_MANAGEMENT", "CUSTOMER")
                        .requestMatchers(HttpMethod.PUT, "/api/cart/**")
                        .hasAnyAuthority("ADMIN", "USER_MANAGEMENT", "INVENTORY_MANAGEMENT", "BUILD_MANAGEMENT",
                                "INVOICE_MANAGEMENT", "CUSTOMER")
                        .requestMatchers(HttpMethod.DELETE, "/api/cart/**")
                        .hasAnyAuthority("ADMIN", "USER_MANAGEMENT", "INVENTORY_MANAGEMENT", "BUILD_MANAGEMENT",
                                "INVOICE_MANAGEMENT", "CUSTOMER")

                        .requestMatchers(HttpMethod.GET, "/api/invoice/**")
                        .hasAnyAuthority("ADMIN", "USER_MANAGEMENT", "INVENTORY_MANAGEMENT", "BUILD_MANAGEMENT",
                                "INVOICE_MANAGEMENT", "CUSTOMER")
                        .requestMatchers(HttpMethod.POST, "/api/invoice/**")
                        .hasAnyAuthority("ADMIN", "INVOICE_MANAGEMENT", "CUSTOMER")
                        .requestMatchers(HttpMethod.PUT, "/api/invoice/**")
                        .hasAnyAuthority("ADMIN", "INVOICE_MANAGEMENT")
                        .requestMatchers(HttpMethod.DELETE, "/api/invoice/**")
                        .hasAnyAuthority("ADMIN", "INVOICE_MANAGEMENT")

                        .requestMatchers(HttpMethod.GET, "/api/payment/**")
                        .hasAnyAuthority("ADMIN", "USER_MANAGEMENT", "INVENTORY_MANAGEMENT", "BUILD_MANAGEMENT",
                                "INVOICE_MANAGEMENT", "CUSTOMER")
                        .requestMatchers(HttpMethod.POST, "/api/payment/**")
                        .hasAnyAuthority("ADMIN","INVOICE_MANAGEMENT")
                        .requestMatchers(HttpMethod.PUT, "/api/payment/**")
                        .hasAnyAuthority("ADMIN","INVOICE_MANAGEMENT")
                        .requestMatchers(HttpMethod.DELETE, "/api/payment/**")
                        .hasAnyAuthority("ADMIN","INVOICE_MANAGEMENT")

                        .requestMatchers(HttpMethod.GET, "/api/exchange_rate/**")
                        .hasAnyAuthority("ADMIN", "USER_MANAGEMENT", "INVENTORY_MANAGEMENT", "BUILD_MANAGEMENT",
                                "INVOICE_MANAGEMENT")
                        .requestMatchers(HttpMethod.POST, "/api/exchange_rate/**")
                        .hasAnyAuthority("ADMIN", "INVENTORY_MANAGEMENT")
                        .requestMatchers(HttpMethod.PUT, "/api/exchange_rate/**")
                        .hasAnyAuthority("ADMIN", "INVENTORY_MANAGEMENT")
                        .requestMatchers(HttpMethod.DELETE, "/api/exchange_rate/**")
                        .hasAnyAuthority("ADMIN", "INVENTORY_MANAGEMENT")

                        .requestMatchers(HttpMethod.GET, "/api/item_data/**")
                        .hasAnyAuthority("ADMIN", "USER_MANAGEMENT", "INVENTORY_MANAGEMENT", "BUILD_MANAGEMENT",
                                "INVOICE_MANAGEMENT", "CUSTOMER")
                        .requestMatchers(HttpMethod.POST, "/api/item_data/**")
                        .hasAnyAuthority("ADMIN", "INVENTORY_MANAGEMENT")
                        .requestMatchers(HttpMethod.PUT, "/api/item_data/**")
                        .hasAnyAuthority("ADMIN", "INVENTORY_MANAGEMENT")
                        .requestMatchers(HttpMethod.DELETE, "/api/item_data/**")
                        .hasAnyAuthority("ADMIN", "INVENTORY_MANAGEMENT")

                        .requestMatchers(HttpMethod.GET, "/api/analytics/**")
                        .hasAnyAuthority("ADMIN", "USER_MANAGEMENT", "INVENTORY_MANAGEMENT", "BUILD_MANAGEMENT",
                                "INVOICE_MANAGEMENT")
                        .requestMatchers(HttpMethod.POST, "/api/analytics/**")
                        .hasAnyAuthority("ADMIN", "INVENTORY_MANAGEMENT")
                        .requestMatchers(HttpMethod.PUT, "/api/analytics/**")
                        .hasAnyAuthority("ADMIN", "INVENTORY_MANAGEMENT")
                        .requestMatchers(HttpMethod.DELETE, "/api/analytics/**")
                        .hasAnyAuthority("ADMIN", "INVENTORY_MANAGEMENT")

                        .anyRequest().authenticated()
                )
//                .formLogin(form -> form.defaultSuccessUrl("/api/user"))
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .exceptionHandling(exception -> exception.authenticationEntryPoint(unauthorizedHandler))
                .headers(headers -> headers.frameOptions(frameOptions -> frameOptions.sameOrigin()))
                .httpBasic(Customizer.withDefaults())
                .exceptionHandling(exception -> exception
                        .accessDeniedHandler(customAccessDeniedHandler)
                        .authenticationEntryPoint(unauthorizedHandler)
                )
                .addFilterBefore(authenticationJwtTokenFilter(), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public SecurityFilterChain oauth2Security(HttpSecurity http)
            throws Exception {

        http
                .securityMatcher("/oauth2/**", "/login/**")
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth ->
                        auth.anyRequest().permitAll())
                .oauth2Login(oauth ->
                        oauth.successHandler(oAuth2AuthenticationSuccessHandler));

        return http.build();
    }

}
