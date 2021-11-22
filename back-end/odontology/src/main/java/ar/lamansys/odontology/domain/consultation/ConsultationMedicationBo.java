package ar.lamansys.odontology.domain.consultation;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ConsultationMedicationBo extends ClinicalTermBo {

    private Integer id;

    private String statusId;

    private String note;

    private boolean suspended = false;

}
