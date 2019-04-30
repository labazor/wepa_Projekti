package projekti.kayttaja;

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import projekti.HaeNavigointi;
import projekti.linkki.Linkki;
import projekti.tili.Tili;
import projekti.tili.TiliRepository;

/**
 *
 * @author Lassi Puurunen
 */
@Controller
public class HakuController {
    
    @Autowired
    HaeNavigointi haeNavigointi;
    
    @Autowired
    TiliRepository tiliRepository;
    
    @Autowired
    KayttajaRepository kayttajaRepository;
    
    @GetMapping("/kayttaja/{kayttajatunnus}/haku")
    public String getHaku(Model model, @PathVariable String kayttajatunnus) {
        Tili tili = tiliRepository.findByKayttajatunnus(kayttajatunnus);
        
        haeNavigointi.hae(model, tili);
        
        
        return "haku";
    }
    
    @PostMapping("/haku")
    public String hae(@RequestParam String hakusana) {
        
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String kayttajatunnus = auth.getName();
        
        return "redirect:/kayttaja/" + kayttajatunnus + "/haku/" + hakusana;
    }
    
    @GetMapping("/kayttaja/{kayttajatunnus}/haku/{hakusana}")
    public String getHaku(Model model, @PathVariable String kayttajatunnus, @PathVariable String hakusana) {
        Tili tili = tiliRepository.findByKayttajatunnus(kayttajatunnus);
        
        haeNavigointi.hae(model, tili);
        
        List<Kayttaja> hakutulokset = kayttajaRepository.getByNimiIgnoreCaseContaining(hakusana);
        List<Kayttaja> palautettavat = new ArrayList<>();
        
        for (Kayttaja kayttaja : hakutulokset) {
            if (tili.getKayttaja() != kayttaja) {
                palautettavat.add(kayttaja);
            }
        }
        
        model.addAttribute("palautettavat", palautettavat);
        
        return "hakutulokset";
    }

}
