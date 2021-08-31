package net.pladema.clinichistory.hospitalization.controller.maindiagnoses.dto;

import ar.lamansys.sgh.clinichistory.infrastructure.input.rest.ips.dto.DocumentObservationsDto;
import ar.lamansys.sgh.clinichistory.infrastructure.input.rest.ips.dto.HealthConditionDto;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Getter
@Setter
@ToString
public class MainDiagnosisDto implements Serializable {

    @NotNull
    private DocumentObservationsDto notes;

    @NotNull(message = "{diagnosis.mandatory}")
    private HealthConditionDto mainDiagnosis;

}
