package projekti.viesti;

import projekti.seina.Seina;
import projekti.kayttaja.Kayttaja;
import java.sql.Date;
import java.time.LocalDateTime;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.AbstractPersistable;
import org.springframework.format.annotation.DateTimeFormat;
import projekti.kommentti.Kommentti;
import projekti.tykkays.Tykkays;


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
    private Kayttaja lahettaja;
    private LocalDateTime lahetysaika;
    private String sisalto;
    
    @OneToMany
    private List<Kommentti> kommentit;
    
    @OneToMany
    private List<Tykkays> tykkaykset;
}
