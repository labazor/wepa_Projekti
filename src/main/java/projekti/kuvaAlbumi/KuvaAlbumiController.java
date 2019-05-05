package projekti.kuvaAlbumi;

import java.io.IOException;
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
import org.springframework.web.multipart.MultipartFile;
import projekti.HaeNavigointi;
import projekti.kuva.Kuva;
import projekti.kuva.KuvaRepository;
import projekti.tili.Tili;
import projekti.tili.TiliRepository;

/**
 *
 * @author Lassi Puurunen
 */

@Controller
public class KuvaAlbumiController {
    
    @Autowired
    TiliRepository tiliRepository;
    
    @Autowired
    KuvaRepository kuvaRepository;
    
    @Autowired
    KuvaAlbumiRepository albumiRepository;
    
    @Autowired
    HaeNavigointi haeNavigointi;
    
    @GetMapping("kayttaja/{kayttaja}/albumi")
    public String haeAlbumi(Model model, @PathVariable String kayttaja) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String kayttajatunnus = auth.getName();
        
        Tili tili = tiliRepository.findByKayttajatunnus(kayttajatunnus);
        
        haeNavigointi.hae(model, tili);
        
        if (kayttaja.equals(kayttajatunnus)) {
            
            List<Kuva> albumi = tili.getKayttaja().getKuvaAlbumi().getKuvat();

            model.addAttribute("lisaaKuva", "/kayttaja/" + kayttajatunnus + "/albumi/lisaakuva");
            model.addAttribute("albumi", albumi);

            return "albumi";
        }
        
        return "albumi";
    }
    
    @GetMapping("kayttaja/{kayttaja}/albumi/lisaakuva")
    public String lisaaKuva(Model model, @PathVariable String kayttaja) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String kayttajatunnus = auth.getName();
        Tili tili = tiliRepository.findByKayttajatunnus(kayttajatunnus);
        haeNavigointi.hae(model, tili);
        
        if (tili.getKayttaja().getKuvaAlbumi().getKuvat().size() >= 10) {
            return "albumitaysi";
        }
        return "lisaakuva";
    }
    
    @GetMapping(path = "/kayttaja/{kayttaja}/kuva/{id}", produces = "image/*")
    @ResponseBody
    public byte[] getImage(@PathVariable String kayttaja, @PathVariable Long id) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String kayttajatunnus = auth.getName();
        
        if (kayttaja.equals(kayttajatunnus)) {
            return kuvaRepository.getOne(id).getTiedosto();
            
        }
        return null;
    }
    
    @PostMapping("/lisaakuva")
    public String save(@RequestParam("file") MultipartFile file) throws IOException {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String kayttajatunnus = auth.getName();
        Tili tili = tiliRepository.findByKayttajatunnus(kayttajatunnus);
        KuvaAlbumi albumi = tili.getKayttaja().getKuvaAlbumi();
        
        if (file.getContentType().contains("image")) {
            
            Kuva kuva = new Kuva();
            kuvaRepository.save(kuva);
            kuva.setTiedosto(file.getBytes());
            kuva.setAlbumi(albumi);
            
            kuvaRepository.save(kuva);
            
            albumi.getKuvat().add(kuva);
            
            albumiRepository.save(albumi);
        }
        
        return "redirect:/kayttaja/" + kayttajatunnus + "/albumi";
    }
    
}
