package projekti.kayttaja;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import projekti.Linkki;
import projekti.tili.Tili;
import projekti.tili.TiliRepository;

/**
 *
 * @author Lassi Puurunen
 */

@Controller
public class KayttajaController {
    
    @Autowired
    TiliRepository tiliRepository;
    
    @Autowired
    KayttajaRepository kayttajaRepository;
    
    @Autowired
    PasswordEncoder passwordEncoder;
    
    
    @GetMapping("/kayttaja")
    public String redirectKayttaja() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String kayttajatunnus = auth.getName();
        
        return "redirect:/kayttaja/" + kayttajatunnus;
    }
    
    
    @GetMapping("/kayttaja/{kayttajatunnus}")
    public String getKayttajaByKayttajatunnus(Model model, @PathVariable String kayttajatunnus) { 
        Tili tili = tiliRepository.findByKayttajatunnus(kayttajatunnus);
        
        
        model.addAttribute("etusivuKuvaus", "Profiili");
        model.addAttribute("etusivuLinkki", "/kayttaja/" + tili.getKayttajatunnus() + "/");
        
        List<Linkki> navigointi = new ArrayList<>();
        navigointi.add(new Linkki("Haku", "/kayttaja/" + tili.getKayttajatunnus() + "/haku"));
        navigointi.add(new Linkki("Kaveripyynnöt", "/kayttaja/" + tili.getKayttajatunnus() + "/kaveripyynnöt"));
        navigointi.add(new Linkki("Kuva-albumi", "/kayttaja/" + tili.getKayttajatunnus() + "/albumi"));
        navigointi.add(new Linkki("Seinä", "/kayttaja/" + tili.getKayttajatunnus() + "/seina"));
        navigointi.add(new Linkki("Kirjaudu ulos", "/kirjaudu?logout"));
        
        
        
        
        //kuvaus minusta
        model.addAttribute("kuvaus", tili.getKayttaja().getKuvaus());
        
        
        
        model.addAttribute("navigointi", navigointi);
        
        
        
        return "kayttaja";
    }
    
    @PostMapping("/kayttaja")
    public String addTili(@RequestParam String kayttajatunnus, @RequestParam String salasana, @RequestParam String nimi) {
        if (tiliRepository.findByKayttajatunnus(kayttajatunnus) != null) {
            return "redirect:/kayttaja";
        }
        
        Tili a = new Tili(kayttajatunnus, passwordEncoder.encode(salasana), new Kayttaja());
        kayttajaRepository.save(a.getKayttaja());
        tiliRepository.save(a);
        
        Kayttaja k = a.getKayttaja();
        k.setNimi(nimi);
        k.setTili(a);
        kayttajaRepository.save(k);
        

        return "redirect:/kayttaja";
    }
}
