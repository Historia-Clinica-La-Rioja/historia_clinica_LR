package ar.lamansys.sgh.clinichistory.infrastructure.output.repository.hce.entity;

import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.ips.Snomed;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.hospitalizationState.entity.ClinicalTermVo;

import java.time.LocalDate;
import java.util.Objects;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class HCEAllergyVo extends ClinicalTermVo {

    private String verificationId;

    private Short categoryId;

    private Short criticalityId;

    private LocalDate startDate;

    public HCEAllergyVo(Integer id, Snomed snomed, String statusId, String verificationId,
                        Short categoryId, Short criticalityId, LocalDate startDate) {
        super(id, snomed, statusId);
        this.verificationId = verificationId;
        this.categoryId = categoryId;
        this.criticalityId = criticalityId;
        this.startDate = startDate;
    }

    @Override
    public int hashCode() {
        return Objects.hash(verificationId, categoryId);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        HCEAllergyVo other = (HCEAllergyVo) obj;
        return Objects.equals(verificationId, other.getVerificationId()) &&
                Objects.equals(categoryId, other.getCategoryId());
    }
}