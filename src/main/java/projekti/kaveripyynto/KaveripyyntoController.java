/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package projekti.kaveripyynto;

import java.sql.Date;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import org.h2.util.DateTimeFunctions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.datetime.DateFormatter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import projekti.HaeNavigointi;
import projekti.kayttaja.Kayttaja;
import projekti.kayttaja.KayttajaRepository;
import projekti.tili.Tili;
import projekti.tili.TiliRepository;

/**
 *
 * @author Lassi
 */
@Controller
public class KaveripyyntoController {
    
    @Autowired
    HaeNavigointi haeNavigointi;
    
    @Autowired
    KaveripyyntoRepository kaveripyyntoRepository;
    
    @Autowired
    TiliRepository tiliRepository;
    
    @Autowired
    KayttajaRepository kayttajaRepository;
    
    @GetMapping("/kaveripyynto/{kaverinKayttajatunnus}")
    public String lahetaKaveripyynto(Model model, @PathVariable String kaverinKayttajatunnus) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String kayttajatunnus = auth.getName();
        Tili tili = tiliRepository.findByKayttajatunnus(kayttajatunnus);
        haeNavigointi.hae(model, tili);
        
        Tili oma = tiliRepository.findByKayttajatunnus(kayttajatunnus);
        Tili kaveri = tiliRepository.findByKayttajatunnus(kaverinKayttajatunnus);
        
        Kaveripyynto kaveripyynto = new Kaveripyynto(kayttajaRepository.getByTili(oma), kayttajaRepository.getByTili(kaveri), LocalDateTime.now(), false);
        
        for (Kaveripyynto kaveripyynto1 : kaveripyyntoRepository.findAll()) {
            if (kaveripyynto1.getPyytaja().equals(kaveripyynto.getPyytaja()) && kaveripyynto1.getKohde().equals(kaveripyynto.getKohde())) {
                return "kaveripyyntoolemassa";
            }
        }
        
        kaveripyyntoRepository.save(kaveripyynto);
        
        Kayttaja kaveriKayttaja = kayttajaRepository.getByTili(kaveri);
        
        kaveriKayttaja.getKaveripyynnot().add(kaveripyynto);
        
        kayttajaRepository.save(kaveriKayttaja);
        
        model.addAttribute("kayttaja", kaveri.getKayttaja().getNimi());
        
        return "kaveripyynto";
    }
    
    @GetMapping("/kayttaja/{kayttajatunnus}/kaveripyynnot")
    public String getKaveripyynnot(Model model, @PathVariable String kayttajatunnus) { 
        Tili tili = tiliRepository.findByKayttajatunnus(kayttajatunnus);
        haeNavigointi.hae(model, tili);
        
        List<Kaveripyynto> kaveripyynnot = tili.getKayttaja().getKaveripyynnot();
        List <Kaveripyynto> vastaamattomat = new ArrayList<>();
        for (Kaveripyynto kaveripyynto : kaveripyynnot) {
            if (kaveripyynto.isVastattu() == false) {
                vastaamattomat.add(kaveripyynto);
            }
        }
        
        model.addAttribute("vastaamattomat", vastaamattomat);
        
        return "kaveripyynnot";
    }
    
    @GetMapping("/kaveripyynto/hyvaksy/{id}")
    public String hyvaksyKaveripyynnot(Model model, @PathVariable Long id) { 
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String kayttajatunnus = auth.getName();
        Tili tili = tiliRepository.findByKayttajatunnus(kayttajatunnus);
        haeNavigointi.hae(model, tili);
        
        Kaveripyynto k = kaveripyyntoRepository.getOne(id);
        
        
        Kayttaja kohde = k.getKohde();
        Kayttaja pyytaja = k.getPyytaja();
        
        kohde.getKaverit().add(pyytaja);
        pyytaja.getKaverit().add(kohde);
        
        kayttajaRepository.save(kohde);
        kayttajaRepository.save(pyytaja);
        
        k.setVastattu(true);
        
        kaveripyyntoRepository.save(k);
        
        return "redirect:/kayttaja/" + kayttajatunnus + "/kaveripyynnot";
    }
    
    @GetMapping("/kaveripyynto/hylkaa/{id}")
    public String hylkaaKaveripyynnot(Model model, @PathVariable Long id) { 
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String kayttajatunnus = auth.getName();
        Tili tili = tiliRepository.findByKayttajatunnus(kayttajatunnus);
        haeNavigointi.hae(model, tili);
        
        Kaveripyynto k = kaveripyyntoRepository.getOne(id);
        
        k.setVastattu(true);
        
        kaveripyyntoRepository.save(k);
        
        return "redirect:/kayttaja/" + kayttajatunnus + "/kaveripyynnot";
    }
    
}
