package projekti.viesti;

import projekti.seina.Seina;
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
public class Viesti extends AbstractPersistable<Long> {
    
    @ManyToOne
    private Seina seina;
    
    @ManyToOne
    private Kayttaja lahettaja;
    private Date lahetysaika;
    private String sisalto;
}
