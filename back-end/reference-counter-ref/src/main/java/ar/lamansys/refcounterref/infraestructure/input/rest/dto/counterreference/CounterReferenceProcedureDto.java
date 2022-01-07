package ar.lamansys.refcounterref.infraestructure.input.rest.dto.counterreference;

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
public class CounterReferenceProcedureDto implements Serializable {

    private static final long serialVersionUID = 7053684633717527849L;

    @Valid
    @NotNull(message = "{value.mandatory}")
    private SnomedDto snomed;

    @Nullable
    private DateDto performedDate;

}