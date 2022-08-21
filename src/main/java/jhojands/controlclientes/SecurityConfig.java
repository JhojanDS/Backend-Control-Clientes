package jhojands.controlclientes;

import jhojands.controlclientes.auth.filter.JwtAuthorizationFilter;
import jhojands.controlclientes.auth.handler.CustomAuthenticationEntryPoint;
import jhojands.controlclientes.auth.service.IJwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import jhojands.controlclientes.auth.filter.JwtAuthenticationFilter;
import jhojands.controlclientes.models.service.JpaUserDetailsService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true)
@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private JpaUserDetailsService userDetailsService;

    @Autowired
    private IJwtService jwtService;

    @Autowired
    private CustomAuthenticationEntryPoint authenticationEntryPoint;

    @Bean
    public static BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Autowired
    public void configurerGlobal(AuthenticationManagerBuilder build) throws Exception {
        build.userDetailsService(userDetailsService)
                .passwordEncoder(passwordEncoder());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http.authorizeRequests().antMatchers("/api/usuario/crear").permitAll()
                .anyRequest().authenticated()
                .and()//manejo en el error de autorizacion
                .exceptionHandling().authenticationEntryPoint(authenticationEntryPoint)
                .and()
                .addFilter(new JwtAuthenticationFilter(authenticationManager(),jwtService))
                .addFilter(new JwtAuthorizationFilter(authenticationManager(),jwtService))
                .csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
    }




}
 