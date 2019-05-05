package projekti;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class DevelopmentSecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Autowired
    private CustomUserDetailsService userDetailsService;
    


    // @Override
    // public void configure(WebSecurity sec) throws Exception {
        // Pyyntöjä ei tarkasteta
        // sec.ignoring().antMatchers("/**");
        
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        
// poistetaan csrf-tarkistus käytöstä h2-konsolin vuoksi
        http.csrf().disable();
        // sallitaan framejen käyttö
        http.headers().frameOptions().sameOrigin();
        
        // Ei päästetä käyttäjää mihinkään sovelluksen resurssiin ilman
        // kirjautumista. Tarjotaan kuitenkin lomake kirjautumiseen, mihin
        // pääsee vapaasti. Tämän lisäksi uloskirjautumiseen tarjotaan
        // mahdollisuus kaikille.
        http
        .authorizeRequests()
            .antMatchers("/uusitili").permitAll()
            .anyRequest().authenticated()
            .and()
        .formLogin()
            .loginPage("/kirjaudu")
                .permitAll()
            .and()
        .logout()
                .invalidateHttpSession(true)
                      .clearAuthentication(true)
                      .deleteCookies("JSESSIONID","USER")
                      .permitAll();
        
    }
    
    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    
}
