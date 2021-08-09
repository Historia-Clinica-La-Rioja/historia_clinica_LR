package ar.lamansys.odontology.infrastructure.controller.consultation.dto;

import ar.lamansys.sgh.clinichistory.infrastructure.input.rest.ips.dto.SnomedDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.annotation.Nullable;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class OdontologyDentalActionDto implements Serializable {

    @NotNull(message = "{value.mandatory}")
    @Valid
    private SnomedDto snomed;

    @NotNull(message = "{value.mandatory}")
    @Valid
    private SnomedDto tooth;

    @Nullable
    private ESurfacePositionDto surfacePosition;

    @NotNull(message = "{value.mandatory}")
    private boolean diagnostic;

}
