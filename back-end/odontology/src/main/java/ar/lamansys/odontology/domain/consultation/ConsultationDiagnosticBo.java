package ar.lamansys.odontology.domain.consultation;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
public class ConsultationDiagnosticBo extends ClinicalTermBo {

    private String severity;

    private LocalDate startDate;

    private LocalDate endDate;

    private boolean chronic;

}
