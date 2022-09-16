package org.hcom.config.security;

import lombok.RequiredArgsConstructor;
import org.hcom.config.security.authorize.CustomLoginSuccessHandler;
import org.hcom.models.user.enums.UserRole;
import org.hcom.services.user.UserService;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@RequiredArgsConstructor
@SpringBootConfiguration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class HCommunitySecurityConfig extends WebSecurityConfigurerAdapter {

    private final UserService userService;
    private final UserDetailsService userDetailsService;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http.csrf().disable().headers().frameOptions().disable();

        // authority
        http.authorizeRequests()
                .antMatchers("/api/v1/**", "/article/new/*gall")
                .hasAnyRole(UserRole.USER.name(), UserRole.ADMIN.name(), UserRole.MANAGER.name(), UserRole.DEVELOPER.name())
                .antMatchers("/", "/js/**", "/css/**", "/images/**", "/login/**", "/logout/**", "/signup/**", "/error/**", "/article/**", "/api", "/swagger-ui/**").permitAll()
                .anyRequest().authenticated();

        // login
        http.formLogin()
                .loginPage("/login")
                .usernameParameter("username")
                .passwordParameter("password")
                .loginProcessingUrl("/login")
                .defaultSuccessUrl("/")
                .successHandler(successHandler())
                .failureUrl("/login/error")
                .permitAll();

        // logout
        http.logout()
                .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                .logoutSuccessUrl("/login")
                .invalidateHttpSession(true);

        http.rememberMe()
                .rememberMeParameter("remember-me")
                .tokenValiditySeconds(3600)
                .userDetailsService(userDetailsService)
                .alwaysRemember(false);

        http.userDetailsService(userService);
    }

    @Override
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userService).passwordEncoder(passwordEncoder());
    }

    @Bean
    public AuthenticationSuccessHandler successHandler() {
        return new CustomLoginSuccessHandler();
    }
}
