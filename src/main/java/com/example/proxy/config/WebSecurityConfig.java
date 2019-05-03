package com.example.proxy.config;

import java.util.Arrays;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.security.oauth2.client.EnableOAuth2Sso;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Configuration
@EnableOAuth2Sso
//  @Order(SecurityProperties.ACCESS_OVERRIDE_ORDER)
@Slf4j
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

  @Bean
  public CorsFilter corsFilter() {
    final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    final CorsConfiguration config = new CorsConfiguration();
    config.setAllowCredentials(true);
    config.addAllowedOrigin("*");
    config.addAllowedHeader("*");
    config.addAllowedMethod("*");
    source.registerCorsConfiguration("/**", config);
    return new CorsFilter(source);
  }

  @Override
  public void configure(WebSecurity web) throws Exception {
    web.ignoring().antMatchers(
        "/*.css",
        "/*.js",
        "/*/webjars/**",
        "/*/css/**",
        "/*/images/**",
        "/favicon.ico");
  }

  @Override
  protected void configure(HttpSecurity http) throws Exception {
    // @formatter:off
    http

        .authorizeRequests()
            .antMatchers(new String[]{"/login","/*.css","/*.js","/favicon.ico","/*.map","/robots.txt"})
            .permitAll()
        .anyRequest().authenticated()
            .and()
        .logout()
            .invalidateHttpSession(true).permitAll()
            .logoutSuccessUrl("/angular-example")
            .and()
        .csrf()
            .disable(); //TODO turn this back on
//                    .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse());
    // @formatter:on

  }

}
