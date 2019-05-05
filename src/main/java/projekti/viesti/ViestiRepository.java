package projekti.viesti;

import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import projekti.kayttaja.Kayttaja;

/**
 *
 * @author Lassi Puurunen
 */
public interface ViestiRepository extends JpaRepository<Viesti, Long> {

    public List<Viesti> findByLahettajaIn(List<Kayttaja> listattavatKayttajat);

    public List<Viesti> findByLahettajaIn(Pageable pageable, List<Kayttaja> listattavatKayttajat);
    
}
