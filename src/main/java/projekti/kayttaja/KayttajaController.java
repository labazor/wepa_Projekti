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
import projekti.HaeNavigointi;
import projekti.linkki.Linkki;
import projekti.tili.Tili;
import projekti.tili.TiliRepository;

/**
 *
 * @author Lassi Puurunen
 */

@Controller
public class KayttajaController {
    
    @Autowired
    HaeNavigointi haeNavigointi;
    
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
        haeNavigointi.hae(model, tili);
        
        
        
        //kuvaus minusta
        model.addAttribute("kuvaus", tili.getKayttaja().getKuvaus());
        
        model.addAttribute("nimi", tili.getKayttaja().getNimi());
        
        
        
        model.addAttribute("vaihdaNimi", new Linkki("Vaihda nimi", "/kayttaja/" + tili.getKayttajatunnus() + "/nimi"));
        
        model.addAttribute("vaihdaKuvaus", new Linkki("Vaihda kuvaus", "/kayttaja/" + tili.getKayttajatunnus() + "/kuvaus"));
        
        
        
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
    
    @GetMapping("/kayttaja/{kayttajatunnus}/kaverit")
    public String getKayttajaByKayttajatunnusKaverit(Model model, @PathVariable String kayttajatunnus) {
        Tili tili = tiliRepository.findByKayttajatunnus(kayttajatunnus);
        haeNavigointi.hae(model, tili);
        
        model.addAttribute("kaverit", tili.getKayttaja().getKaverit());
        return "kaverit";
    }
}
