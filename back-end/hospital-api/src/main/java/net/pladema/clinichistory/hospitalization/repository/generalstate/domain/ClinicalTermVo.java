package net.pladema.clinichistory.hospitalization.repository.generalstate.domain;

import lombok.*;
import net.pladema.clinichistory.ips.repository.masterdata.entity.Snomed;

import java.util.Objects;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public abstract class ClinicalTermVo {

    private Integer id;

    private Snomed snomed;

    private String statusId;

    private String status;

    public ClinicalTermVo(Integer id, Snomed snomed, String statusId) {
        this.id = id;
        this.snomed = snomed;
        this.statusId = statusId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ClinicalTermVo that = (ClinicalTermVo) o;
        return id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
