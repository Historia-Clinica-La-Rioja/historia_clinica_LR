package ar.lamansys.refcounterref.domain.procedure;

import ar.lamansys.refcounterref.domain.clinicalterm.ReferenceCounterReferenceClinicalTermBo;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;

@Getter
@Setter
@ToString
public class CounterReferenceProcedureBo extends ReferenceCounterReferenceClinicalTermBo {

    private LocalDate performedDate;

}