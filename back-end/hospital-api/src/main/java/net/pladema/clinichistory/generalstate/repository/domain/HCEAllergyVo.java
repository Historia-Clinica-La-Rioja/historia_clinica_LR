package net.pladema.clinichistory.generalstate.repository.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import net.pladema.clinichistory.hospitalization.repository.generalstate.domain.ClinicalTermVo;
import net.pladema.clinichistory.ips.repository.masterdata.entity.Snomed;

import java.time.LocalDate;
import java.util.Objects;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class HCEAllergyVo extends ClinicalTermVo {

    private String verificationId;

    private String categoryId;

    private LocalDate startDate;

    public HCEAllergyVo(Integer id, Snomed snomed, String statusId, String verificationId,
                              String categoryId, LocalDate startDate) {
        super(id, snomed, statusId);
        this.verificationId = verificationId;
        this.categoryId = categoryId;
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