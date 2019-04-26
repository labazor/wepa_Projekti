package projekti.tili;

import projekti.kayttaja.Kayttaja;
import javax.persistence.Entity;
import javax.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.AbstractPersistable;

/**
 *
 * @author Lassi Puurunen
 */

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Tili extends AbstractPersistable<Long> {
    
    private String kayttajatunnus, salasana;
    
    @OneToOne
    private Kayttaja kayttaja;
    
}
