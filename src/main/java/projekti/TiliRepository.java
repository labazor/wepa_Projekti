package projekti;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 *
 * @author Lassi Puurunen
 */
public interface TiliRepository extends JpaRepository<Tili, Long> {
    
}
