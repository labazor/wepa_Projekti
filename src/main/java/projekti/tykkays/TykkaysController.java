package projekti.tykkays;

import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import projekti.HaeNavigointi;
import projekti.kuva.Kuva;
import projekti.kuva.KuvaRepository;
import projekti.tili.Tili;
import projekti.tili.TiliRepository;
import projekti.viesti.Viesti;
import projekti.viesti.ViestiRepository;

/**
 *
 * @author Lassi Puurunen
 */
@Controller
public class TykkaysController {
    
    @Autowired
    HaeNavigointi haeNavigointi;
    
    @Autowired
    TiliRepository tiliRepository;
    
    @Autowired
    TykkaysRepository tykkaysRepository;
    
    @Autowired
    KuvaRepository kuvaRepository;
    @Autowired
    ViestiRepository viestiRepository;
    
    @GetMapping("/tykkaa/kuva/{id}")
    public String tykkaaKuvasta(@PathVariable Long id) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String kayttaja = auth.getName();
        Tili tili = tiliRepository.findByKayttajatunnus(kayttaja);
        
        Kuva k = kuvaRepository.getOne(id);
        
        for (Tykkays tykkays : k.getTykkaykset()) {
            if (tykkays.getTykkaaja().equals(tili.getKayttaja())) {
                return "redirect:/kayttaja/" + kayttaja + "/kuva/" + id + "/nayta";
            }
        }
                
        Tykkays t = new Tykkays(LocalDateTime.now(), tili.getKayttaja());
        tykkaysRepository.save(t);
        k.getTykkaykset().add(t);
        kuvaRepository.save(k);
        
        return "redirect:/kayttaja/" + kayttaja + "/kuva/" + id + "/nayta";
    }
    
    @GetMapping("/tykkaa/viesti/{id}")
    public String tykkaaViestista(@PathVariable Long id) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String kayttaja = auth.getName();
        Tili tili = tiliRepository.findByKayttajatunnus(kayttaja);
        
        Viesti v = viestiRepository.getOne(id);
        
        for (Tykkays tykkays : v.getTykkaykset()) {
            if (tykkays.getTykkaaja().equals(tili.getKayttaja())) {
                return "redirect:/kayttaja/" + kayttaja + "/seina/";
            }
        }
                
        Tykkays t = new Tykkays(LocalDateTime.now(), tili.getKayttaja());
        tykkaysRepository.save(t);
        v.getTykkaykset().add(t);
        viestiRepository.save(v);
        
        return "redirect:/kayttaja/" + kayttaja + "/seina/";
    }
}
