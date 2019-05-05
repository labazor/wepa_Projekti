package projekti.tykkays;

import java.sql.Date;
import java.time.LocalDateTime;
import javax.persistence.Entity;
import javax.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.AbstractPersistable;
import projekti.kayttaja.Kayttaja;

/**
 *
 * @author Lassi Puurunen
 */

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Tykkays extends AbstractPersistable<Long> {
    
    
    private LocalDateTime aika;
    
    @OneToOne
    private Kayttaja tykkaaja;
}
