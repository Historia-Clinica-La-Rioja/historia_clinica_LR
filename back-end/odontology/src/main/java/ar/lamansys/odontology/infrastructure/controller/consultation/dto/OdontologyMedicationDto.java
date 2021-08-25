package ar.lamansys.odontology.infrastructure.controller.consultation.dto;

import ar.lamansys.sgh.clinichistory.infrastructure.input.rest.ips.dto.SnomedDto;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.annotation.Nullable;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@ToString
public class OdontologyMedicationDto {

    @Nullable
    private Integer id;

    @Nullable
    private String statusId;

    @NotNull(message = "{value.mandatory}")
    @Valid
    private SnomedDto snomed;

    private String note;

    private boolean suspended = false;

}
