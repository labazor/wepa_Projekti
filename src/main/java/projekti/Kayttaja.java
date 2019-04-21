package projekti;

import java.util.List;
import javax.persistence.Entity;
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
public class Kayttaja extends AbstractPersistable<Long> {
    
    private Tili tili;
    private String nimi;
    private Kuva profiilikuva;
    private List<Kayttaja> kaverit;
    private List<Kaveripyynto> kaveripyynnot;

}
