
package projekti.kommentti;

import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
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
public class KommenttiController {
    
    @Autowired
    TiliRepository tiliRepository;
    
    @Autowired
    KommenttiRepository kommenttiRepository;
    
    @Autowired
    ViestiRepository viestiRepository;
    
    @Autowired
    KuvaRepository kuvaRepository;
    
    @PostMapping("/kommentoiviesti/{id}")
    public String kommentoiviesti(@PathVariable Long id, @RequestParam String kommentti) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String kayttaja = auth.getName();
        Tili tili = tiliRepository.findByKayttajatunnus(kayttaja);
        
        Kommentti k = new Kommentti(LocalDateTime.now(), kommentti, tili.getKayttaja());
        kommenttiRepository.save(k);
        
        Viesti v = viestiRepository.getOne(id);
        
        v.getKommentit().add(k);
        
        viestiRepository.save(v);
        
        
        return "redirect:/kayttaja/" + kayttaja + "/seina/";
    }
    
    @PostMapping("/kommentoikuva/{id}")
    public String kommentoiKuva(@PathVariable Long id, @RequestParam String kommentti) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String kayttaja = auth.getName();
        Tili tili = tiliRepository.findByKayttajatunnus(kayttaja);
        
        Kommentti k = new Kommentti(LocalDateTime.now(), kommentti, tili.getKayttaja());
        kommenttiRepository.save(k);
        
        Kuva ku = kuvaRepository.getOne(id);
        
        ku.getKommentit().add(k);
        
        kuvaRepository.save(ku);
        
        
        return "redirect:/kayttaja/" + kayttaja + "/kuva/" + id + "/nayta/";
    }
}
