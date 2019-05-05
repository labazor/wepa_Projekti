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
import projekti.Linkki;
import projekti.kuva.Kuva;
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
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String kayttaja = auth.getName();
        
        Tili tili = tiliRepository.findByKayttajatunnus(kayttaja);
        haeNavigointi.hae(model, tili);
        
        
        
        //kuvaus minusta
        model.addAttribute("kuvaus", tili.getKayttaja().getKuvaus());
        
        model.addAttribute("nimi", tili.getKayttaja().getNimi());
        
        Kuva kuva = tili.getKayttaja().getProfiilikuva();
        
        if (kuva == null) {
            
            model.addAttribute("kuva", new Kuva());
        } else {
            model.addAttribute("kuva", tili.getKayttaja().getProfiilikuva());
        }
        
        model.addAttribute("kayttaja", tili.getKayttajatunnus());
        
        model.addAttribute("vaihdaNimi", new Linkki("Vaihda nimi", "/kayttaja/" + tili.getKayttajatunnus() + "/nimi"));
        
        model.addAttribute("vaihdaKuvaus", new Linkki("Vaihda kuvaus", "/kayttaja/" + tili.getKayttajatunnus() + "/kuvaus"));
        
        
        
        return "kayttaja";
    }
    
    @GetMapping("/kayttaja/{kayttajatunnus}/nimi")
    public String vaihdaNimi(Model model, @PathVariable String kayttajatunnus) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String kayttaja = auth.getName();
        Tili tili = tiliRepository.findByKayttajatunnus(kayttaja);
        haeNavigointi.hae(model, tili);
        
        model.addAttribute("nimi", tili.getKayttaja().getNimi());
        
        return "nimi";
    }
    
    @GetMapping("/kayttaja/{kayttajatunnus}/kuvaus")
    public String vaihdaKuvaus(Model model, @PathVariable String kayttajatunnus) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String kayttaja = auth.getName();
        Tili tili = tiliRepository.findByKayttajatunnus(kayttaja);
        haeNavigointi.hae(model, tili);
        
        model.addAttribute("kuvaus", tili.getKayttaja().getKuvaus());
        
        
        return "kuvaus";
    }
    
    @PostMapping("/vaihdanimi") 
    public String vaihdanimi(@RequestParam String nimi) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String kayttaja = auth.getName();
        
        Tili tili = tiliRepository.findByKayttajatunnus(kayttaja);
        Kayttaja k = tili.getKayttaja();
        k.setNimi(nimi);
        kayttajaRepository.save(k);
        
        return "redirect:/kayttaja/" + kayttaja;
    }
    
    @PostMapping("/vaihdakuvaus") 
    public String vaihdakuvaus(@RequestParam String kuvaus) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String kayttaja = auth.getName();
        
        Tili tili = tiliRepository.findByKayttajatunnus(kayttaja);
        Kayttaja k = tili.getKayttaja();
        k.setKuvaus(kuvaus);
        kayttajaRepository.save(k);
        
        return "redirect:/kayttaja/" + kayttaja;
    }
    
    @GetMapping("/kayttaja/{kayttajatunnus}/kaverit")
    public String getKayttajaKaverit(Model model, @PathVariable String kayttajatunnus) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String kayttaja = auth.getName();
        Tili tili = tiliRepository.findByKayttajatunnus(kayttaja);
        haeNavigointi.hae(model, tili);
        
        List<Kayttaja> haettu = tili.getKayttaja().getKaverit();
        model.addAttribute("kayttaja", kayttaja);
        model.addAttribute("kaverit", haettu);
        return "kaverit";
    }
    
    @GetMapping("/kayttaja/{kayttajatunnus}/kaveri/{kaveri}")
    public String getKaveri(Model model, @PathVariable String kayttajatunnus, @PathVariable String kaveri) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String kayttaja = auth.getName();
        Tili tili = tiliRepository.findByKayttajatunnus(kayttaja);
        haeNavigointi.hae(model, tili);
        
        Tili kaverinTili = new Tili();
        if (kayttajatunnus.equals(kayttaja)) {
            kaverinTili = tiliRepository.findByKayttajatunnus(kaveri);
        }
        
        model.addAttribute("kaveri", kaverinTili.getKayttaja());
        model.addAttribute("albumi", kaverinTili.getKayttaja().getKuvaAlbumi().getKuvat());
        
        return "kaveri";
        
    }
}
