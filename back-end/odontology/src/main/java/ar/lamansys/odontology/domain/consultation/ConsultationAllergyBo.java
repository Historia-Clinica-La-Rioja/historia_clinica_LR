package ar.lamansys.odontology.domain.consultation;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class ConsultationAllergyBo extends ClinicalTermBo {

    private String statusId;

    private String categoryId;

    private Short criticalityId;

    private LocalDate startDate;

    private String verificationId;

}
