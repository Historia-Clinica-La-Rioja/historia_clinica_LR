package ar.lamansys.refcounterref.domain.allergy;

import ar.lamansys.refcounterref.domain.clinicalterm.ReferenceCounterReferenceClinicalTermBo;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;

@Getter
@Setter
@ToString
public class CounterReferenceAllergyBo extends ReferenceCounterReferenceClinicalTermBo {

    private Short criticalityId;

    private String statusId;

    private String verificationId;

    private LocalDate startDate;

    private String categoryId;

}