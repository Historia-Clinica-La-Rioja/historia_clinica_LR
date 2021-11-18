package ar.lamansys.refcounterref.domain;

import ar.lamansys.sgh.clinichistory.domain.ips.SnomedBo;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.annotation.Nullable;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@ToString
public class ReferenceProblemBo {

    @Nullable
    private Integer id;

    @Valid
    @NotNull(message = "{value.mandatory}")
    private SnomedBo snomed;

}
