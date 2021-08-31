package ar.lamansys.immunization.infrastructure.output.repository.vaccine;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

@Getter
@Entity
@Table(name = "nomivac_snomed_map")
@NoArgsConstructor
public class NomivacSnomedMap {

    @EmbeddedId
    private NomivacSnomedMapPK pk;

    public NomivacSnomedMap(Short vaccineSisaCode, String sctid) {
        this.pk = new NomivacSnomedMapPK(vaccineSisaCode, sctid);
    }


}
