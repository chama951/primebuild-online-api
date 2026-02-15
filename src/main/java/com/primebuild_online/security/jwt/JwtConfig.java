package com.primebuild_online.security.jwt;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import io.jsonwebtoken.io.Decoders;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

@Configuration
public class JwtConfig {

    private static final String JWT_SECRET = "mySecretKey123912738aopsgjnspkmndfsopkvajoirjg94gf2opfng2moknm";

    @Bean
    public SecretKey jwtSecretKey() {
        return new SecretKeySpec(
                Decoders.BASE64.decode(JWT_SECRET),
                "HmacSHA256"
        );
    }

    @Bean
    public JwtDecoder jwtDecoder(SecretKey secretKey) {
        return NimbusJwtDecoder.withSecretKey(secretKey).build();
    }
}
