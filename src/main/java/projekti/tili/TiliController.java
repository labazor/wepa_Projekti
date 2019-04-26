/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package projekti.tili;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import projekti.Linkki;
import projekti.kayttaja.Kayttaja;
import projekti.kayttaja.KayttajaRepository;
import projekti.tili.Tili;

/**
 *
 * @author User
 */
@Controller
public class TiliController {
    
    @Autowired
    TiliRepository tiliRepository;
    
    @Autowired
    KayttajaRepository kayttajaRepository;
    
    @Autowired
    PasswordEncoder passwordEncoder;
    
    @GetMapping("/kirjaudu")
    public String login(Model model) {
        model.addAttribute("linkki", new Linkki("Luo uusi tili", "/tili/uusi"));
        return "kirjaudu";
    }
    
    @GetMapping("/tili/uusi")
    public String uusi() {
        return "uusitili";
    }
    
    @PostMapping("/tili/uusi")
    public String add(@RequestParam String nimi, @RequestParam String kayttajatunnus, @RequestParam String salasana) {
        if (tiliRepository.findByKayttajatunnus(kayttajatunnus) != null) {
            return "redirect:/kayttaja";
        }
        
        Kayttaja kayttaja = new Kayttaja();
        kayttaja.setNimi(nimi);
        kayttajaRepository.save(kayttaja);
        
        Tili a = new Tili(kayttajatunnus, passwordEncoder.encode(salasana), kayttaja);
        
        tiliRepository.save(a);
        
        kayttaja.setTili(tiliRepository.findByKayttajatunnus(kayttajatunnus));
        kayttajaRepository.save(kayttaja);
        
        return "redirect:/kayttaja";
    }
}
