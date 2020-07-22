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

    public HCEAllergyBo(HCEAllergyVo hceAllergieVo) {
        super();
        setId(hceAllergieVo.getId());
        setStatusId(hceAllergieVo.getStatusId());
        setSnomed(new SnomedBo(hceAllergieVo.getSnomed()));
        setVerificationId(hceAllergieVo.getVerificationId());
        setCategoryId(hceAllergieVo.getCategoryId());
        setStartDate(hceAllergieVo.getStartDate());
    }
}
