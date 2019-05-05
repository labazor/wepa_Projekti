/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package projekti.kommentti;

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
public class Kommentti extends AbstractPersistable<Long>  {
    
    private LocalDateTime aika;
    private String teksti;
    
    @OneToOne
    private Kayttaja kommentoija;
}
