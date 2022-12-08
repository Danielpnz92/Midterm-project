package MidTermProject.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    private UserService userService;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userService).passwordEncoder(passwordEncoder());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.httpBasic();
        http.csrf().disable();
        http.authorizeRequests()
                .antMatchers(HttpMethod.PATCH, "/api/accounts/balance_modify/{id}").hasAnyAuthority("ROLE_ADMIN")
                //he añadido la comprobación del balance de cualquier cuenta a todos los roles para los test con el MockUser
                .antMatchers(HttpMethod.GET, "/api/accounts/balance/{id}").hasAnyAuthority("ROLE_ADMIN", "ROLE_ACCOUNT_HOLDER", "ROLE_THIRD_PARTY")
                .antMatchers(HttpMethod.POST, "/api/accounts/checking").hasAnyAuthority("ROLE_ADMIN")
                .antMatchers(HttpMethod.POST, "/api/accounts/credit_card").hasAnyAuthority("ROLE_ADMIN")
                .antMatchers(HttpMethod.POST, "/api/accounts/savings").hasAnyAuthority("ROLE_ADMIN")
                .antMatchers(HttpMethod.POST, "/api/accounts/student_account").hasAnyAuthority("ROLE_ADMIN")
                .antMatchers(HttpMethod.GET, "/api/own_accounts/balance/{id}").hasAnyAuthority("ROLE_ACCOUNT_HOLDER")
                .antMatchers(HttpMethod.PATCH, "/api/accounts/transfer/{senderAccountId}/{receiverAccountId}").hasAnyAuthority("ROLE_ACCOUNT_HOLDER")
                .antMatchers(HttpMethod.POST, "/api/users/third_party").hasAnyAuthority("ROLE_ADMIN")
                .antMatchers(HttpMethod.PATCH, "/api/accounts/third_party/{amount}/{accountId}/{secretKey}").hasAnyAuthority("ROLE_THIRD_PARTY");
//                .antMatchers("/account_holder/all").authenticated();




//                .antMatchers("/api/teachers").authenticated();
//                .anyRequest().permitAll();

    }
}
