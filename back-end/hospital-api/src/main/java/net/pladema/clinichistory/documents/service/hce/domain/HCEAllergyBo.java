package net.pladema.clinichistory.documents.service.hce.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import net.pladema.clinichistory.documents.repository.hce.domain.HCEAllergyVo;
import net.pladema.clinichistory.documents.service.ips.domain.SnomedBo;

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
