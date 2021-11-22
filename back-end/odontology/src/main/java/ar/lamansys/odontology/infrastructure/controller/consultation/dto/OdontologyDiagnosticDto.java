package ar.lamansys.odontology.infrastructure.controller.consultation.dto;


import ar.lamansys.sgh.clinichistory.infrastructure.input.rest.ips.dto.SnomedDto;
import ar.lamansys.sgx.shared.dates.controller.dto.DateDto;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.annotation.Nullable;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Getter
@Setter
@ToString
public class OdontologyDiagnosticDto implements Serializable {

    @NotNull(message = "{value.mandatory}")
    @Valid
    private SnomedDto snomed;

    @Nullable
    private String severity;

    @Nullable
    private DateDto startDate;

    @Nullable
    private DateDto endDate;

    private boolean chronic;

}
