package net.pladema.patient.service.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.pladema.patient.repository.domain.PrivateHealthInsuranceDetailsVo;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PrivateHealthInsuranceDetailsBo {

    private Integer id;

    private LocalDate startDate;

    private LocalDate endDate;

    private Integer planId;

    private String planName;

    public PrivateHealthInsuranceDetailsBo(PrivateHealthInsuranceDetailsVo privateHealthInsuranceDetailsVo){
        if (privateHealthInsuranceDetailsVo != null) {
            this.id = privateHealthInsuranceDetailsVo.getId();
            this.startDate = privateHealthInsuranceDetailsVo.getStartDate();
            this.endDate = privateHealthInsuranceDetailsVo.getEndDate();
            this.planId = privateHealthInsuranceDetailsVo.getPlanId();
        }
    }
}
