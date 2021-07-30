package ar.lamansys.odontology.domain.consultation;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ConsultationBo {

    private Integer patientId;

    private Integer institutionId;

    private Integer clinicalSpecialtyId;

    private List<ConsultationDentalActionBo> dentalActions;

    private List<ConsultationReasonBo> reasons;

    private List<ConsultationDiagnosticBo> diagnostics;

    private List<ConsultationProcedureBo> procedures;

    private List<ConsultationPersonalHistoryBo> personalHistories;

    private List<ConsultationAllergyBo> allergies;

    private String evolutionNote;

}
