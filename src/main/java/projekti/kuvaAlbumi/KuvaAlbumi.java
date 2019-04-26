package projekti.kuvaAlbumi;

import projekti.kayttaja.Kayttaja;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.AbstractPersistable;
import projekti.kuva.Kuva;

/**
 *
 * @author Lassi Puurunen
 */

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
public class KuvaAlbumi extends AbstractPersistable<Long> {
    
    @OneToOne
    private Kayttaja omistaja;
    
    @OneToMany
    private List<Kuva> kuvat;
    
}
