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
                .antMatchers(HttpMethod.PATCH, "/accounts/balance_modify/{id}").hasAnyAuthority("ADMIN")
                .antMatchers(HttpMethod.GET, "/accounts/balance/{id}").hasAnyAuthority("ADMIN")
                .antMatchers(HttpMethod.POST, "/accounts/checking").hasAnyAuthority("ADMIN")
                .antMatchers(HttpMethod.POST, "/accounts/credit_card").hasAnyAuthority("ADMIN")
                .antMatchers(HttpMethod.POST, "/accounts/savings").hasAnyAuthority("ADMIN")
                .antMatchers(HttpMethod.POST, "/accounts/student_account").hasAnyAuthority("ADMIN")
                .antMatchers(HttpMethod.GET, "/own_accounts/balance/{id}").hasAnyAuthority("ACCOUNT_HOLDER")
                .antMatchers(HttpMethod.GET, "/accounts/transfer/{senderAccountId}/{receiverAccountId}").hasAnyAuthority("ACCOUNT_HOLDER")
                .antMatchers(HttpMethod.POST, "/users/third_party").hasAnyAuthority("ADMIN")
                .antMatchers(HttpMethod.PATCH, "/accounts/third_party/{amount}/{accountId}/{secretKey}").hasAnyAuthority("THIRD_PARTY");
//                .antMatchers("/account_holder/all").authenticated();




//                .antMatchers("/api/teachers").authenticated();
//                .anyRequest().permitAll();

    }
}
