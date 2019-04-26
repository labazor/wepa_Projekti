package projekti;

import projekti.tili.TiliRepository;
import projekti.tili.Tili;
import java.util.Arrays;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 *
 * @author Lassi Puurunen
 */

@Service
public class CustomUserDetailsService implements UserDetailsService{

    @Autowired
    TiliRepository tiliRepository;
    
    @Override
    public UserDetails loadUserByUsername(String kayttajatunnus) throws UsernameNotFoundException {
        Tili tili = tiliRepository.findByKayttajatunnus(kayttajatunnus);
        
        if (tili == null) {
            throw new UsernameNotFoundException("Käyttäjätunnusta ei löytynyt: " + kayttajatunnus);
        }
        
        return new org.springframework.security.core.userdetails.User(
                tili.getKayttajatunnus(),
                tili.getSalasana(),
                true,
                true,
                true,
                true,
                Arrays.asList(new SimpleGrantedAuthority("USER")));
    }
    
}
