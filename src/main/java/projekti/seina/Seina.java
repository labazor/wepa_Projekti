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

@NoArgsConstructor
@AllArgsConstructor
@Data
public class Seina{
    
    private Kayttaja omistaja;
    
    private List<Viesti> viestit;
}
