package ar.lamansys.odontology.domain.consultation;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ConsultationBo {

    private Integer consultationId;

    private Integer patientId;

    private Integer institutionId;

    private Integer clinicalSpecialtyId;

    private List<ConsultationDentalActionBo> dentalActions = new ArrayList<>();

    private List<ConsultationReasonBo> reasons = new ArrayList<>();

    private List<ConsultationDiagnosticBo> diagnostics = new ArrayList<>();

    private List<ConsultationProcedureBo> procedures = new ArrayList<>();

    private List<ConsultationPersonalHistoryBo> personalHistories = new ArrayList<>();

    private List<ConsultationAllergyBo> allergies = new ArrayList<>();

    private List<ConsultationMedicationBo> medications = new ArrayList<>();

    private String evolutionNote;

    private Integer permanentTeethPresent;

    private Integer temporaryTeethPresent;

}
