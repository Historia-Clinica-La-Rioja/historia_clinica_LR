package ar.lamansys.sgh.clinichistory.infrastructure.output.repository.ips.entity;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@Table(name = "tracheal_intubation")
@Entity
public class TrachealIntubation {

    @EmbeddedId
    private TrachealIntubationPK trachealIntubationPK;

    public TrachealIntubation(Integer anestheticTechniqueId, Short trachealIntubationId) {
        trachealIntubationPK = new TrachealIntubationPK(anestheticTechniqueId, trachealIntubationId);
    }
}
