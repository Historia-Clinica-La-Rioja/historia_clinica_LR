package net.pladema.internation.controller.internment.maindiagnoses.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import net.pladema.internation.controller.internment.dto.DocumentObservationsDto;
import net.pladema.internation.controller.ips.dto.HealthConditionDto;

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
