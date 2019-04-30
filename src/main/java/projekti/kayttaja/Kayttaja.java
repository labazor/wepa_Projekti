package projekti.kayttaja;

import java.util.List;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.AbstractPersistable;
import projekti.linkki.Linkki;
import projekti.kaveripyynto.Kaveripyynto;
import projekti.kuva.Kuva;
import projekti.kuvaAlbumi.KuvaAlbumi;
import projekti.seina.Seina;
import projekti.tili.Tili;
import projekti.viesti.Viesti;

/**
 *
 * @author Lassi Puurunen
 */

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Kayttaja extends AbstractPersistable<Long> {
    
    @OneToOne
    private Tili tili;
    
    private String nimi, kuvaus;
    
    private String kaveripyyntoLinkki;
    
    @OneToOne
    private Kuva profiilikuva;
    
    @ManyToMany
    private List<Kayttaja> kaverit;
    
    @OneToMany
    private List<Kaveripyynto> kaveripyynnot;
    
    @OneToOne
    private Seina seina;
    
    @OneToOne
    private KuvaAlbumi kuvaAlbumi;
    
    @OneToMany
    private List<Viesti> viestit;
    
    @OneToMany
    private List<Kuva> kuvat;

}
