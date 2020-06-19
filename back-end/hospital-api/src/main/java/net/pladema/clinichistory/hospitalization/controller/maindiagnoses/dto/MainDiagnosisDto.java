package net.pladema.clinichistory.hospitalization.controller.maindiagnoses.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import net.pladema.clinichistory.hospitalization.controller.dto.DocumentObservationsDto;
import net.pladema.clinichistory.ips.controller.dto.HealthConditionDto;

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
