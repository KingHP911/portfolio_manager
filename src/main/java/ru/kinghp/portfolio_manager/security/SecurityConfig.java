package ru.kinghp.portfolio_manager.security;

import lombok.Data;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import ru.kinghp.portfolio_manager.service.impl.DBUserServiceImpl;

@Data
@Configuration
//@EnableWebSecurity(debug = true)
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final DBUserServiceImpl userService;
    private final PasswordEncoder encoder;
    private final JWTFilter jwtFilter;

    // Бин AuthenticationManager используется в контроллере аутентификации
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userService).passwordEncoder(encoder);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http.csrf().disable()
                .authorizeRequests()
                .antMatchers("/registration").not().fullyAuthenticated()
                .antMatchers("/authenticate").not().fullyAuthenticated()
                .antMatchers("/ws").not().fullyAuthenticated()
                .and().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        http.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

    }


    //старая реализация для @Controller
//    @Override
//    protected void configure(HttpSecurity http) throws Exception {
//        http.authorizeRequests()
//                .antMatchers("/registration").not().fullyAuthenticated()
//                .antMatchers("/login").not().fullyAuthenticated()
//                .antMatchers("/**").authenticated()
//                .and().formLogin()
//                .loginPage("/login")
//                .loginProcessingUrl("/perform-login") //так же по умолчанию
//                .usernameParameter("login") //по умолчанию username
//                .passwordParameter("password") //так же по умолчанию
//                .defaultSuccessUrl("/")
//                .and()
//                    .logout()
//                    .logoutUrl("/logout")
//                    .logoutSuccessUrl("/login")
//                    .invalidateHttpSession(true)
//                    .deleteCookies("JSESSIONID")
//                    .permitAll()
//        ;
//    }

}
