package projekti.seina;

import projekti.kuva.Kuva;
import projekti.kayttaja.Kayttaja;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.MappedSuperclass;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.AbstractPersistable;
import projekti.viesti.Viesti;

/**
 *
 * @author Lassi Puurunen
 */

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Seina extends AbstractPersistable<Long>{
    
    @OneToOne(mappedBy = "seina")
    private Kayttaja omistaja;
    
    @OneToMany(mappedBy = "id")
    private List<Viesti> viestit;
    
    @OneToMany(mappedBy = "id")
    private List<Kuva> kuvat;
}
