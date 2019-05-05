package projekti.kuva;

import java.util.List;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.domain.AbstractPersistable;
import projekti.kommentti.Kommentti;
import projekti.kuvaAlbumi.KuvaAlbumi;
import projekti.tykkays.Tykkays;

/**
 *
 * @author Lassi Puurunen
 */

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Kuva extends AbstractPersistable<Long> {
    
    
    @Type(type = "org.hibernate.type.BinaryType")
    private byte[] tiedosto;
    private String kuvaus;
    
    @Id
    private Long id;
    
    @ManyToOne
    private KuvaAlbumi albumi;
    
    @OneToMany
    private List<Kommentti> kommentit;
    
    @OneToMany
    private List<Tykkays> tykkaykset;
}
