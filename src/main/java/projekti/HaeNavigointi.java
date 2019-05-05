/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package projekti;

import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import projekti.tili.Tili;

/**
 *
 * @author Lassi
 */
@Service
public class HaeNavigointi {

    public void hae(Model model, Tili tili) {
        model.addAttribute("etusivuKuvaus", "Käyttäjä");
        model.addAttribute("etusivuLinkki", "/kayttaja/" + tili.getKayttajatunnus() + "/");
        
        List<Linkki> navigointi = new ArrayList<>();
        navigointi.add(new Linkki("Haku", "/kayttaja/" + tili.getKayttajatunnus() + "/haku"));
        navigointi.add(new Linkki("Kaveripyynnöt", "/kayttaja/" + tili.getKayttajatunnus() + "/kaveripyynnot"));
        navigointi.add(new Linkki("Kaverit", "/kayttaja/" + tili.getKayttajatunnus() + "/kaverit"));
        navigointi.add(new Linkki("Kuva-albumi", "/kayttaja/" + tili.getKayttajatunnus() + "/albumi"));
        navigointi.add(new Linkki("Seinä", "/kayttaja/" + tili.getKayttajatunnus() + "/seina"));
        navigointi.add(new Linkki("Kirjaudu ulos", "/logout"));
        model.addAttribute("navigointi", navigointi);
    }
    
}
