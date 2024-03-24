package dev.projectfitness.ipfitness.security;

import dev.projectfitness.ipfitness.security.services.JwtUserDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.AuthorizeHttpRequestsConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.intercept.AuthorizationFilter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@EnableWebSecurity
@Configuration
@RequiredArgsConstructor
public class WebSecurityConfig {
    private final JwtUserDetailsService jwtUserDetailsService;
    private final JwtAuthorizationFilter jwtAuthorizationFilter;
    private final EndpointCaptureFilter endpointCaptureFilter;

    @Value("${ipfitness.front-end.url}")
    private String frontendBaseUrl;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .cors(Customizer.withDefaults())
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(customizer -> customizer.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests((requests) -> {
                    try {
                        addAuthorizationRules(requests);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                        requests.anyRequest().denyAll();
                    }
                })
                .authenticationProvider(daoAuthenticationProvider())
                .addFilterBefore(jwtAuthorizationFilter, UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(endpointCaptureFilter, jwtAuthorizationFilter.getClass());

        return httpSecurity.build();
    }

    private void addAuthorizationRules(AuthorizeHttpRequestsConfigurer<HttpSecurity>.AuthorizationManagerRequestMatcherRegistry requests) {
        requests.requestMatchers("/api/*/auth/**").permitAll();
        requests.requestMatchers("/api/*/categories/**").permitAll();
        requests.requestMatchers("/api/*/users/*/categories").authenticated();
        requests.requestMatchers(HttpMethod.POST, "/api/*/programs/*/comments").authenticated();
        requests.requestMatchers(HttpMethod.GET, "/api/*/programs/*/comments").permitAll();
        requests.requestMatchers(HttpMethod.GET, "/api/*/exercises").authenticated();
        requests.requestMatchers(HttpMethod.POST, "/api/*/email").permitAll();
        requests.requestMatchers(HttpMethod.POST, "/utility/**").permitAll();
        requests.requestMatchers(HttpMethod.GET, "/api/*/programs/**").permitAll();
        requests.requestMatchers(HttpMethod.POST, "/api/*/programs/**").authenticated();
        requests.requestMatchers(HttpMethod.PUT, "/api/*/programs/**").authenticated();
        requests.requestMatchers(HttpMethod.DELETE, "/api/*/programs/**").authenticated();
        requests.requestMatchers(HttpMethod.POST, "/api/*/messages").authenticated();
        requests.requestMatchers("/api/*/users/*/chats/**").authenticated();
        requests.requestMatchers("/api/*/users/*/purchases").authenticated();
        requests.requestMatchers(HttpMethod.POST, "/api/*/questions").authenticated();
        requests.requestMatchers(HttpMethod.GET, "/api/*/news").permitAll();
        requests.requestMatchers(HttpMethod.GET, "/api/*/storage").permitAll();
        requests.requestMatchers(HttpMethod.POST, "/api/*/storage").authenticated();
        requests.requestMatchers("/api/*/activities/**").authenticated();
        requests.requestMatchers("/api/*/users/*/activities/**").authenticated();
        requests.requestMatchers(HttpMethod.GET, "/api/*/users/*/own-programs").authenticated();
        requests.requestMatchers(HttpMethod.GET, "/api/*/users/*/purchased-programs").authenticated();
        requests.requestMatchers("/api/*/users/*/info/**").authenticated();
        requests.anyRequest().denyAll();
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
        config.addAllowedOrigin(frontendBaseUrl);
        config.addAllowedHeader("*");
        config.addAllowedMethod("*");
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }

    @Bean
    public AuthenticationProvider daoAuthenticationProvider() {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(jwtUserDetailsService);
        authenticationProvider.setPasswordEncoder(passwordEncoder());
        return authenticationProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
