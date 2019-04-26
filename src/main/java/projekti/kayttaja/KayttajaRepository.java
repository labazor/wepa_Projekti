package projekti.kayttaja;

import org.springframework.data.jpa.repository.JpaRepository;
import projekti.tili.Tili;

/**
 *
 * @author Lassi Puurunen
 */
public interface KayttajaRepository extends JpaRepository<Kayttaja, Long> {
    
    Kayttaja getByTili(Tili tili);
    
}
