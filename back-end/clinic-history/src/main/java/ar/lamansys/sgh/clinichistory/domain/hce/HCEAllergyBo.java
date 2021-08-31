package ar.lamansys.sgh.clinichistory.domain.hce;

import ar.lamansys.sgh.clinichistory.domain.ips.SnomedBo;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.hce.entity.HCEAllergyVo;

import java.time.LocalDate;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class HCEAllergyBo extends HCEClinicalTermBo {

    private String verificationId;

    private Short categoryId;

    private Short criticalityId;

    private LocalDate startDate;

    public HCEAllergyBo(HCEAllergyVo hceAllergyVo) {
        super();
        setId(hceAllergyVo.getId());
        setStatusId(hceAllergyVo.getStatusId());
        setSnomed(new SnomedBo(hceAllergyVo.getSnomed()));
        setVerificationId(hceAllergyVo.getVerificationId());
        setCategoryId(hceAllergyVo.getCategoryId());
        setCriticalityId(hceAllergyVo.getCriticalityId());
        setStartDate(hceAllergyVo.getStartDate());
    }
}
