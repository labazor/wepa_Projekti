package projekti.viesti;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 *
 * @author Lassi Puurunen
 */
public interface ViestiRepository extends JpaRepository<Viesti, Long> {
    
}
