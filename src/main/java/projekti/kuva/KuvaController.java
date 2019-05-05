package projekti.kuva;

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
import projekti.kayttaja.Kayttaja;
import projekti.kayttaja.KayttajaRepository;
import projekti.kommentti.Kommentti;
import projekti.kuvaAlbumi.KuvaAlbumi;
import projekti.kuvaAlbumi.KuvaAlbumiRepository;
import projekti.tili.Tili;
import projekti.tili.TiliRepository;
import projekti.viesti.Viesti;

/**
 *
 * @author Lassi Puurunen
 */
@Controller
public class KuvaController {
    
    @Autowired
    KayttajaRepository kayttajaRepository;
    
    @Autowired
    TiliRepository tiliRepository;
    
    @Autowired
    KuvaRepository kuvaRepository;
    
    @Autowired
    KuvaAlbumiRepository albumiRepository;
    
    @Autowired
    HaeNavigointi haeNavigointi;
    
    @GetMapping("/kayttaja/{kayttajatunnus}/kuva/{kuvaId}/nayta")
    public String getKuva(Model model, @PathVariable String kayttajatunnus, @PathVariable Long kuvaId) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String kayttaja = auth.getName();
        Tili tili = tiliRepository.findByKayttajatunnus(kayttaja);
        haeNavigointi.hae(model, tili);
        
        boolean lupa = false;
        boolean vieras = false;
        Kuva k = kuvaRepository.getOne(kuvaId);
        
        List<Kayttaja> kaverit = tili.getKayttaja().getKaverit();
        
        if (tili.getKayttaja().getKuvaAlbumi().getKuvat().contains(k)) {
            lupa = true;
        }
        
        if (lupa == false) {
            for (Kayttaja kayttaja1 : kaverit) {
                if (kayttaja1.getKuvaAlbumi().getKuvat().contains(k)) {
                    lupa = true;
                    vieras = true;
                }
            }
            
            
        }
        
        
        if (lupa == true) {
            model.addAttribute("kayttaja", kayttaja);
            model.addAttribute("kuva", k);
            
            model.addAttribute("poistoLinkki", "/kayttaja/" + kayttaja + "/kuva/" + kuvaId + "/poista");
            model.addAttribute("profiilikuvaksiLinkki", "/kayttaja/" + kayttaja + "/kuva/" + kuvaId + "/profiilikuvaksi");
                
            model.addAttribute("vieras", vieras);
            
            //Rajataan kuvassa olevien kommenttien määrä 10:n
            
            List<Kommentti> kommentit = new ArrayList<>();
            for (int i = k.getKommentit().size() - 1; i > k.getKommentit().size() - 11 ; i--) {
                if (i < 0) {
                    break;
                } 
                kommentit.add(k.getKommentit().get(i));
            }
            k.setKommentit(kommentit);
            
            
        }
        
        return "kuva";
    }
    
    @PostMapping("/kuva/{id}/muokkaaKuvausta")
    public String muokkaaKuvausta(@PathVariable Long id, @RequestParam String kuvaus) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String kayttajatunnus = auth.getName();
        
        Kuva kuva = kuvaRepository.getOne(id);
        kuva.setKuvaus(kuvaus);
        kuvaRepository.save(kuva);
        
        return "redirect:/kayttaja/" + kayttajatunnus + "/kuva/" + id + "/nayta";
    }
    
    @GetMapping("/kayttaja/{kayttajatunnus}/kuva/{kuvaId}/poista")
    public String poistaKuva(Model model, @PathVariable String kayttajatunnus, @PathVariable Long kuvaId) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String kayttaja = auth.getName();
        Tili tili = tiliRepository.findByKayttajatunnus(kayttaja);
        Kuva kuva = kuvaRepository.getOne(kuvaId);
        
        if (tili.getKayttaja().getProfiilikuva().equals(kuva)) {
            haeNavigointi.hae(model, tili);
            return "profiilikuvaa";
        }
        
        if (kayttaja.equals(kayttajatunnus)) {

            KuvaAlbumi albumi = kuva.getAlbumi();
            albumi.getKuvat().remove(kuva);
            albumiRepository.save(albumi);
            
            kuvaRepository.delete(kuva);
        }
        
        return "redirect:/kayttaja/" + kayttaja + "/albumi";
    }
    
    @GetMapping("/kayttaja/{kayttajatunnus}/kuva/{kuvaId}/profiilikuvaksi")
    public String profiiliKuva(@PathVariable String kayttajatunnus, @PathVariable Long kuvaId) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String kayttaja = auth.getName();
        
        if (kayttaja.equals(kayttajatunnus)) {
            Kuva kuva = kuvaRepository.getOne(kuvaId);
            Kayttaja k = kayttajaRepository.getByTili(tiliRepository.findByKayttajatunnus(kayttajatunnus));
            k.setProfiilikuva(kuva);
            kayttajaRepository.save(k);
        }
        
        return "redirect:/kayttaja/" + kayttaja;
    }
}
