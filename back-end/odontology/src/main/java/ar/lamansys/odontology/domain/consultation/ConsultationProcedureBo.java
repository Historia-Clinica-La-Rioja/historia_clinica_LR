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
public class ConsultationProcedureBo extends ClinicalTermBo {

    private LocalDate performedDate;

}
