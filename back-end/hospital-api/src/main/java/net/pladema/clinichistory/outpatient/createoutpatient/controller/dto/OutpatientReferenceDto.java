package net.pladema.clinichistory.outpatient.createoutpatient.controller.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.annotation.Nullable;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

@Getter
@Setter
@ToString
public class OutpatientReferenceDto {

    @Nullable
    private Boolean procedure;

    @Nullable
    private Boolean consultation;

    @Nullable
    private String note;

    @Valid
    @NotNull(message = "{value.mandatory}")
    private Integer clinicalSpecialtyId;

    @Valid
    @NotNull(message = "{value.mandatory}")
    private Integer careLineId;

    @Valid
    @NotNull(message = "{value.mandatory}")
    private List<@Valid OutpatientReferenceProblemDto> problems;

}
