package org.sda.com.pizzashop.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                .authorizeHttpRequests()
                .requestMatchers(
                        "/javax",
                        "/css/**",
                        "/img/**",
                        "/js/**",
                        "/images/**",
                        "/vendor/**",
                        "/webjars/**",
                        "/*/webjars/**",
                        "/",
                        "/index",
                        "/home",
                        "/about",
                        "/contact",
                        "/search",
                        "/send-message",
                        "/shopping-cart-add/**",
                        "/shopping-cart-remove/**",
                        "/shopping-cart/**",
                        "/client-register/**",
                        "/login"
                ).permitAll()
                .requestMatchers(
                        "/admin-register/**",
                        "/add-product/**",
                        "/update-product/**",
                        "/all-orders/**",
                        "/delete-product/**",
                        "/admin/messages/**"
                ).hasAuthority("ADMIN")
                .anyRequest().authenticated()
                .and()
                .httpBasic()
                .and()
                .formLogin()
                .loginPage("/login")
                .permitAll()
                .and()
                .logout()
                .invalidateHttpSession(true)
                .clearAuthentication(true)
                .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                .logoutSuccessUrl("/login?logout")
                .permitAll()
                .and()
                .build();
    }
}
