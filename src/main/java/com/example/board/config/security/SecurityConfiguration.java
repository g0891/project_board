package com.example.board.config.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    private final UserDetailsService userDetailsService;
    private final String apiPath;

    public SecurityConfiguration(/*@Qualifier(value = "myUserDetailsService")*/ UserDetailsService userDetailsService, @Value("${api.path}") String apiPath) {
        this.userDetailsService = userDetailsService;
        this.apiPath = apiPath;
    }

    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .csrf().disable()
                .authorizeRequests()
                    .antMatchers(apiPath + "/persons/register").not().authenticated()
                /*.antMatchers(HttpMethod.GET, "/projects/**")
                    .hasAuthority(Permission.PROJECTS_READ.getPermission())
                .antMatchers("/projects/**")
                    .hasAuthority(Permission.PROJECTS_WRITE.getPermission())*/
                /*.antMatchers("/**")
                    .denyAll()*/
                    .anyRequest()
                        .authenticated()
                .and()
                    .httpBasic();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(daoAuthenticationProvider());
    }

    /*@Bean
    @Override
    protected UserDetailsService userDetailsService() {
        return new InMemoryUserDetailsManager(
                User
                    .builder()
                    .username("author")
                    .password(passwordEncoder().encode("author"))
                    .authorities(PersonRole.AUTHOR.getAuthorities())
                    //.roles(PersonRole.AUTHOR.name())
                    .build(),
                User
                    .builder()
                    .username("customer")
                    .password(passwordEncoder().encode("customer"))
                    //.roles(PersonRole.CUSTOMER.name())
                    .authorities(PersonRole.CUSTOMER.getAuthorities())
                    .build()
                );
    }*/

    @Bean
    protected PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(12);
    }

    @Bean
    protected DaoAuthenticationProvider daoAuthenticationProvider() {
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
        daoAuthenticationProvider.setUserDetailsService(userDetailsService);
        return daoAuthenticationProvider;
    }

}
