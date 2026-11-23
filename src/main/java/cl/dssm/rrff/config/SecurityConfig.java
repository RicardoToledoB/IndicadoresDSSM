package cl.dssm.rrff.config;

import com.nimbusds.jose.jwk.source.ImmutableSecret;
import java.nio.charset.StandardCharsets;
import java.util.List;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.security.oauth2.jwt.JwtValidators;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@EnableMethodSecurity
public class SecurityConfig {
    @Bean SecretKey jwtKey(@Value("${app.jwt.secret}") String secret) {
        byte[] bytes = secret.getBytes(StandardCharsets.UTF_8);
        if (bytes.length < 32 || secret.startsWith("REEMPLAZAR") || secret.startsWith("CAMBIAR")) {
            throw new IllegalStateException("Configurar app.jwt.secret con clave aleatoria privada (32+ bytes) antes de iniciar");
        }
        return new SecretKeySpec(bytes,"HmacSHA256");
    }
    @Bean JwtEncoder jwtEncoder(SecretKey jwtKey) { return new NimbusJwtEncoder(new ImmutableSecret<>(jwtKey)); }
    @Bean JwtDecoder jwtDecoder(SecretKey jwtKey,@Value("${app.jwt.issuer}") String issuer) {
        NimbusJwtDecoder decoder=NimbusJwtDecoder.withSecretKey(jwtKey).macAlgorithm(MacAlgorithm.HS256).build();
        decoder.setJwtValidator(JwtValidators.createDefaultWithIssuer(issuer));
        return decoder;
    }
    @Bean PasswordEncoder passwordEncoder(){ return new BCryptPasswordEncoder(12); }
    @Bean JwtAuthenticationConverter jwtAuthenticationConverter(){
        JwtGrantedAuthoritiesConverter roles = new JwtGrantedAuthoritiesConverter();
        roles.setAuthoritiesClaimName("roles"); roles.setAuthorityPrefix("");
        JwtAuthenticationConverter converter = new JwtAuthenticationConverter();
        converter.setJwtGrantedAuthoritiesConverter(roles);
        return converter;
    }
    @Bean SecurityFilterChain filterChain(HttpSecurity http, JwtAuthenticationConverter converter) throws Exception {
        return http
          .csrf(AbstractHttpConfigurer::disable).cors(Customizer.withDefaults())
          .sessionManagement(s -> s.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
          .httpBasic(AbstractHttpConfigurer::disable).formLogin(AbstractHttpConfigurer::disable)
          .authorizeHttpRequests(auth -> auth
             .requestMatchers(HttpMethod.POST,"/api/v1/auth/login").permitAll()
             .requestMatchers(HttpMethod.GET,"/actuator/health").permitAll()
             // Swagger y la especificacion R05 solo exponen documentacion; los endpoints de negocio conservan JWT.
             .requestMatchers(HttpMethod.GET,"/v3/api-docs", "/v3/api-docs/**", "/swagger-ui.html", "/swagger-ui/**", "/docs/rrff-7-2-r05-openapi.json").permitAll()
             .anyRequest().authenticated())
          .oauth2ResourceServer(oauth -> oauth.jwt(jwt -> jwt.jwtAuthenticationConverter(converter)))
          .build();
    }
    @Bean CorsConfigurationSource corsSource(@Value("${app.cors.allowed-origin}") String allowedOrigin){
        CorsConfiguration conf = new CorsConfiguration();
        conf.setAllowedOrigins(List.of(allowedOrigin));
        conf.setAllowedMethods(List.of("GET","POST","PUT","PATCH","OPTIONS"));
        conf.setAllowedHeaders(List.of("Authorization","Content-Type","If-Match"));
        conf.setMaxAge(3600L);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", conf);
        return source;
    }
}
