package ar.lamansys.sgh.clinichistory.infrastructure.output.repository.hce.entity;

import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.ips.Snomed;
import lombok.*;

import java.util.Objects;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public abstract class HCEClinicalTermVo {

    private Integer id;

    private Snomed snomed;

    private String statusId;

    private String status;

    public HCEClinicalTermVo(Integer id, Snomed snomed, String statusId) {
        this.id = id;
        this.snomed = snomed;
        this.statusId = statusId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        HCEClinicalTermVo that = (HCEClinicalTermVo) o;
        return id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
