package projekti.kayttaja;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import projekti.tili.Tili;

/**
 *
 * @author Lassi Puurunen
 */
public interface KayttajaRepository extends JpaRepository<Kayttaja, Long> {
    
    Kayttaja getByTili(Tili tili);
    
    List<Kayttaja> getByNimiIgnoreCaseContaining(String nimi);
    
}
