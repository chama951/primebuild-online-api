package com.primebuild_online.security;

import com.primebuild_online.jwt.AuthEntryPointJwt;
import com.primebuild_online.jwt.AuthTokenFilter;
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
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                                .requestMatchers("/api/login").permitAll()
                                .requestMatchers("/api/signup").permitAll()
//                        .requestMatchers("/api/role/**").hasAuthority("ADMIN")
                                .requestMatchers("/api/role/**").hasAnyAuthority("ADMIN","USER_MANAGEMENT")
//                                .requestMatchers("/api/role/**").permitAll() //Debug
                                .requestMatchers("/api/user/**").hasAnyAuthority("ADMIN","USER_MANAGEMENT")
//                                .requestMatchers("/api/user/**").permitAll() //Debug
                                .requestMatchers("/api/manufacturer/**").hasAnyAuthority("ADMIN","BUILD_MANAGEMENT")

                                .anyRequest().authenticated()
                )
                .formLogin(form -> form.defaultSuccessUrl("/api/user"))
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .exceptionHandling(exception -> exception.authenticationEntryPoint(unauthorizedHandler))
                .headers(headers -> headers.frameOptions(frameOptions -> frameOptions.sameOrigin()))
                .httpBasic(Customizer.withDefaults())
                .addFilterBefore(authenticationJwtTokenFilter(), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

//    @Bean
//    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//
//        http
//                .csrf(csrf -> csrf.disable())
//
//                .authorizeHttpRequests(auth -> auth
//                        .requestMatchers(
//                                "/",
//                                "/login",
//                                "/error",
//                                "/oauth2/**"
//                        ).permitAll()
//                        .anyRequest().authenticated()
//                )
//
//                // OAuth2 login
//                .oauth2Login(oauth2 ->
//                        oauth2.successHandler(oAuth2AuthenticationSuccessHandler)
//                )
//
//                // JWT filter
//                .addFilterBefore(authTokenFilter, UsernamePasswordAuthenticationFilter.class)
//
//                .sessionManagement(session ->
//                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
//                );
//
//        return http.build();
//    }
}
