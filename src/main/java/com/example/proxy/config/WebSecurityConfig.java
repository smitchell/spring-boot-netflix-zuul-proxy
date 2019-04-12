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

  private final String[] permitAllMatches;
  private final String logoutSuccessUrl;

  @Autowired
  public WebSecurityConfig(
      @Value("${example.proxy.logout-url}") final String logoutSuccessUrl,
      @Value("${example.proxy.permit-all-matches}") final String[] permitAllMatches) {
    this.logoutSuccessUrl = logoutSuccessUrl;
    this.permitAllMatches = permitAllMatches;
  }

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
    final String prefix = "Permit match for ";
    Arrays.stream(permitAllMatches).map(match -> prefix.concat(match)).forEach(log::info);
    // @formatter:off
    http

        .authorizeRequests()
            .antMatchers(permitAllMatches)
            .permitAll()
        .anyRequest().authenticated()
            .and()
        .logout()
            .invalidateHttpSession(true).permitAll()
            .logoutSuccessUrl(logoutSuccessUrl)
            .and()
        .csrf()
            .disable(); //TODO turn this back on
//                    .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse());
    // @formatter:on

  }

}
