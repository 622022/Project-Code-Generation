package io.swagger.configuration;

import io.swagger.dao.ApiKeyRepository;
import io.swagger.filter.ApiKeyAuthFilter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;

@Configuration
@EnableWebSecurity
@Order(2)
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    private ApiKeyRepository repository;

    public SecurityConfiguration(ApiKeyRepository repository)
    {
        this.repository = repository;
    }

    @Value("bearerAuth")
    private String headerName;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        ApiKeyAuthFilter filter = new ApiKeyAuthFilter(headerName);
        filter.setAuthenticationManager(authentication ->
        {
            String principle = (String)authentication.getPrincipal();

            if (!repository.findById(principle).isPresent()){
            throw new BadCredentialsException("API key not found");
           }
            authentication.setAuthenticated(true);
            return authentication;
        }
        );

        http
                .antMatcher("/users/**")
                .csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .addFilter(filter).authorizeRequests()
                .anyRequest().authenticated();
    }
}
