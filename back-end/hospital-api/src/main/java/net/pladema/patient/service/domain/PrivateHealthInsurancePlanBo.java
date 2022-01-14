package net.pladema.patient.service.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PrivateHealthInsurancePlanBo {

    private Integer id;

    private Integer privateHealthInsuranceId;

    private String plan;

}
