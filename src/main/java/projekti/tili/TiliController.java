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
import projekti.kuvaAlbumi.KuvaAlbumi;
import projekti.kuvaAlbumi.KuvaAlbumiRepository;
import projekti.seina.Seina;
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
    KuvaAlbumiRepository albumiRepository;
    
    @Autowired
    PasswordEncoder passwordEncoder;
    
    @GetMapping("/kirjaudu")
    public String login(Model model) {
        model.addAttribute("linkki", new Linkki("Luo uusi tili", "/uusitili"));
        return "kirjaudu";
    }
    
    @GetMapping("/uusitili")
    public String uusi() {
        return "uusitili";
    }
    
    @PostMapping("/uusitili")
    public String add(@RequestParam String nimi, @RequestParam String kayttajatunnus, @RequestParam String salasana) {
        if (tiliRepository.findByKayttajatunnus(kayttajatunnus) != null) {
            return "redirect:/kayttaja";
        }
        
        Tili tili = new Tili(kayttajatunnus, passwordEncoder.encode(salasana), new Kayttaja());
        Kayttaja kayttaja = tili.getKayttaja();
        
        kayttajaRepository.save(kayttaja);
        tiliRepository.save(tili);
        
        kayttaja.setTili(tili);
        
        kayttaja.setNimi(nimi);
        
        kayttaja.setKaveripyyntoLinkki("/kaveripyynto/" + kayttajatunnus);
        
        kayttaja.setKuvaAlbumi(new KuvaAlbumi());
        
        KuvaAlbumi albumi = kayttaja.getKuvaAlbumi();
                
        albumiRepository.save(albumi);
        kayttajaRepository.save(kayttaja);
        
        
        return "redirect:/kayttaja";
    }
}
