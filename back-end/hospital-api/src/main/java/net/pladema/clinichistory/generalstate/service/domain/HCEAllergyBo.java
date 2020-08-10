package net.pladema.clinichistory.generalstate.service.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import net.pladema.clinichistory.generalstate.repository.domain.HCEAllergyVo;
import net.pladema.clinichistory.ips.service.domain.SnomedBo;

import java.time.LocalDate;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class HCEAllergyBo extends HCEClinicalTermBo {

    private String verificationId;

    private String categoryId;

    private LocalDate startDate;

    public HCEAllergyBo(HCEAllergyVo hceAllergyVo) {
        super();
        setId(hceAllergyVo.getId());
        setStatusId(hceAllergyVo.getStatusId());
        setSnomed(new SnomedBo(hceAllergyVo.getSnomed()));
        setVerificationId(hceAllergyVo.getVerificationId());
        setCategoryId(hceAllergyVo.getCategoryId());
        setStartDate(hceAllergyVo.getStartDate());
    }
}
