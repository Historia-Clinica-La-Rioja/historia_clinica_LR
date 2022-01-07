package ar.lamansys.refcounterref.domain.medication;

import ar.lamansys.refcounterref.domain.clinicalterm.ReferenceCounterReferenceClinicalTermBo;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class CounterReferenceMedicationBo extends ReferenceCounterReferenceClinicalTermBo {

    private Integer id;

    private String statusId;

    private String note;

    private boolean suspended = false;

}