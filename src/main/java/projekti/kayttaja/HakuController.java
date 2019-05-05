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
import org.springframework.web.bind.annotation.ResponseBody;
import projekti.HaeNavigointi;
import projekti.Linkki;
import projekti.kuva.Kuva;
import projekti.kuva.KuvaRepository;
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
    KuvaRepository kuvaRepository;
    
    @Autowired
    KayttajaRepository kayttajaRepository;
    
    @GetMapping("/kayttaja/{kayttajatunnus}/haku")
    public String getHaku(Model model, @PathVariable String kayttajatunnus) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String kayttaja = auth.getName();
        
        Tili tili = tiliRepository.findByKayttajatunnus(kayttaja);
        
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
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String kayttajatunn = auth.getName();
        
        Tili tili = tiliRepository.findByKayttajatunnus(kayttajatunn);
        
        haeNavigointi.hae(model, tili);
        
        List<Kayttaja> hakutulokset = kayttajaRepository.getByNimiIgnoreCaseContaining(hakusana);
        List<Kayttaja> palautettavat = new ArrayList<>();
        
        for (Kayttaja kayttaja : hakutulokset) {
            if (tili.getKayttaja() != kayttaja && !tili.getKayttaja().getKaverit().contains(kayttaja)) {
                palautettavat.add(kayttaja);
            }
        }
        
        model.addAttribute("palautettavat", palautettavat);
        
        return "hakutulokset";
    }
    
    @GetMapping(path = "/profiilikuva/{id}", produces = "image/*")
    @ResponseBody
    public byte[] getImage(@PathVariable Long id) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String kayttajatunnus = auth.getName();
        
        
        Kuva k = kuvaRepository.getOne(id);
        
        if (k != null) {
            return k.getTiedosto();
            
        }
        return null;
        
    }

}
