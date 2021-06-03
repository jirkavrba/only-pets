package dev.vrba.onlypets.security;

import org.jetbrains.annotations.NotNull;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()
            .cors()
                .and()
            .oauth2Login()
                .loginProcessingUrl("/oauth2/callback")
                .defaultSuccessUrl("/app")
            .and()
                .logout()
                .logoutSuccessUrl("/")
                .permitAll()
            .and()
            .authorizeRequests()
                .antMatchers(
                        "/",
                        "/oauth2/authorization/discord",
                        "/oauth2/callback"
                )
                    .permitAll()
            .anyRequest()
                .authenticated();
    }

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(@NotNull CorsRegistry registry) {
                registry.addMapping("/api/**")
                        // TODO: Change this to environmental variable / configuration property
                        .allowedOrigins("http://localhost:8081")
                        .allowedMethods("*")
                        .allowCredentials(true);
            }
        };
    }
}
