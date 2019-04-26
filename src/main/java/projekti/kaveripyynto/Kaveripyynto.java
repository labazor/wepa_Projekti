package projekti.kaveripyynto;

import projekti.kayttaja.Kayttaja;
import java.sql.Date;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
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
public class Kaveripyynto extends AbstractPersistable<Long>{
    
    @ManyToOne
    private Kayttaja pyytaja;
    
    @ManyToOne
    private Kayttaja kohde;
    
    private Date aika;
}
