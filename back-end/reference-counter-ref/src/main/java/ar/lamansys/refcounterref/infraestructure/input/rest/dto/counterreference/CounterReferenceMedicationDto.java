package ar.lamansys.refcounterref.infraestructure.input.rest.dto.counterreference;

import ar.lamansys.sgh.clinichistory.infrastructure.input.rest.ips.dto.SnomedDto;
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
public class CounterReferenceMedicationDto implements Serializable {

    private static final long serialVersionUID = 1074744658888666584L;

    @Valid
    @NotNull(message = "{value.mandatory}")
    private SnomedDto snomed;

    @Nullable
    private Integer id;

    @Nullable
    private String statusId;

    private String note;

    private boolean suspended = false;
}