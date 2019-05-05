package projekti.seina;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
import projekti.kommentti.Kommentti;
import projekti.tili.Tili;
import projekti.tili.TiliRepository;
import projekti.viesti.Viesti;
import projekti.viesti.ViestiRepository;

/**
 *
 * @author Lassi Puurunen
 */
@Controller
public class SeinaController {
    
    @Autowired
    HaeNavigointi haeNavigointi;
    
    @Autowired
    TiliRepository tiliRepository;
    
    @Autowired
    ViestiRepository viestiRepository;
    
    @GetMapping("/kayttaja/{kayttajatunnus}/seina")
    public String getSeina(Model model, @PathVariable String kayttajatunnus) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String kayttaja = auth.getName();
        Tili tili = tiliRepository.findByKayttajatunnus(kayttaja);
        haeNavigointi.hae(model, tili);
        
        
        Pageable pageable = PageRequest.of(0, 25, Sort.by("id").descending());

        List<Kayttaja> listattavatKayttajat = new ArrayList<>();
        listattavatKayttajat.add(tili.getKayttaja());
        listattavatKayttajat.addAll(tili.getKayttaja().getKaverit());
        
        List<Viesti> viestit = viestiRepository.findByLahettajaIn(pageable, listattavatKayttajat);
        
        //Rajataan viesteissä olevien kommenttien määrä 10:n
        for (Viesti viesti : viestit) {
            List<Kommentti> kommentit = new ArrayList<>();
            for (int i = viesti.getKommentit().size() - 1; i > viesti.getKommentit().size() - 11 ; i--) {
                if (i < 0) {
                    break;
                } 
                kommentit.add(viesti.getKommentit().get(i));
            }
            viesti.setKommentit(kommentit);
        }
        
        model.addAttribute("viestit", viestit);
        
        return "seina";
    }
    
    @PostMapping("/kirjoitaseinalle")
    public String kirjoitaSeinalle(@RequestParam String viesti) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String kayttaja = auth.getName();
        Tili tili = tiliRepository.findByKayttajatunnus(kayttaja);
        
        Viesti v = new Viesti();
        v.setLahettaja(tili.getKayttaja());
        v.setSisalto(viesti);
        v.setLahetysaika(LocalDateTime.now());
        
        viestiRepository.save(v);
        
        
        
        return "redirect:/kayttaja/" + kayttaja + "/seina";
    } 
    
}
