package com.primebuild_online.security;

import com.primebuild_online.security.jwt.AuthEntryPointJwt;
import com.primebuild_online.security.jwt.AuthTokenFilter;
import com.primebuild_online.security.oauth2.OAuth2AuthenticationSuccessHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
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
                                .requestMatchers("/api/auth/login").permitAll()
                                .requestMatchers("/api/auth/signup").permitAll()
//                        .requestMatchers("/api/role/**").hasAuthority("ADMIN")
                                .requestMatchers("/api/role/**").hasAnyAuthority("ADMIN", "USER_MANAGEMENT")
//                                .requestMatchers("/api/item/**").hasAnyAuthority("ADMIN", "INVENTORY_MANAGEMENT")
                                .requestMatchers("/api/item/**").hasAnyAuthority("ADMIN", "INVENTORY_MANAGEMENT", "CUSTOMER")
                                .requestMatchers("/api/vendor_item_data/**").hasAnyAuthority("ADMIN")
//                                .requestMatchers("/api/role/**").permitAll() //Debug
                                .requestMatchers("/api/user/**").hasAnyAuthority("ADMIN", "USER_MANAGEMENT")
//                                .requestMatchers("/api/user/**").permitAll() //Debug
                                .requestMatchers("/api/manufacturer/**").hasAnyAuthority("ADMIN", "BUILD_MANAGEMENT")
                                .requestMatchers("/api/component/**").hasAnyAuthority("ADMIN", "BUILD_MANAGEMENT")
                                .requestMatchers("/api/build/**").hasAnyAuthority("ADMIN", "CUSTOMER")
                                .requestMatchers("/api/invoice/**").hasAnyAuthority("ADMIN", "INVOICE_MANAGEMENT")

                                .anyRequest().authenticated()
                )
                .formLogin(form -> form.defaultSuccessUrl("/api/user"))
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
