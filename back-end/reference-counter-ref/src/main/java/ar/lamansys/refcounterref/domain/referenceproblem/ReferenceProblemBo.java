package ar.lamansys.refcounterref.domain.referenceproblem;

import ar.lamansys.sgh.clinichistory.domain.ips.SnomedBo;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.annotation.Nullable;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class ReferenceProblemBo {

    @Nullable
    private Integer id;

    @Valid
    @NotNull(message = "{value.mandatory}")
    private SnomedBo snomed;

    @NotNull
    private Integer referenceId;

    public ReferenceProblemBo(Integer id, String sctid, String pt, Integer referenceId) {
        this.id = id;
        this.snomed = new SnomedBo(sctid,pt);
        this.referenceId = referenceId;
    }

    public ReferenceProblemBo(ReferenceProblemBo referenceProblemBo) {
        this.id = referenceProblemBo.getId();
        this.snomed = referenceProblemBo.getSnomed();
        this.referenceId = referenceProblemBo.getReferenceId();
    }
}
