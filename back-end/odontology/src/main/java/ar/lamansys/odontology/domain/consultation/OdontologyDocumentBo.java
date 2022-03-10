package ar.lamansys.odontology.domain.consultation;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class OdontologyDocumentBo {

    private Long id;

    private Integer patientId;

    private Integer encounterId;

    private Integer institutionId;

    private Integer doctorId;

    private Integer clinicalSpecialtyId;

    private List<ConsultationDentalActionBo> dentalActions;

    private List<ConsultationReasonBo> reasons;

    private List<ConsultationDiagnosticBo> diagnostics;

    private List<ConsultationProcedureBo> procedures;

    private List<ConsultationPersonalHistoryBo> personalHistories;

    private List<ConsultationAllergyBo> allergies;

    private List<ConsultationMedicationBo> medications;

    private String evolutionNote;

    private LocalDate performedDate;

    public OdontologyDocumentBo(Long id,
                                ConsultationBo consultation,
                                Integer encounterId,
                                Integer doctorId,
                                LocalDate performedDate) {
        this.id = id;
        this.patientId = consultation.getPatientId();
        this.encounterId = encounterId;
        this.institutionId = consultation.getInstitutionId();
        this.doctorId = doctorId;
        this.clinicalSpecialtyId = consultation.getClinicalSpecialtyId();
        this.dentalActions = consultation.getDentalActions();
        this.reasons = consultation.getReasons();
        this.diagnostics = consultation.getDiagnostics();
        this.procedures = consultation.getProcedures();
        this.personalHistories = consultation.getPersonalHistories();
        this.allergies = consultation.getAllergies();
        this.medications = consultation.getMedications();
        this.evolutionNote = consultation.getEvolutionNote();
        this.performedDate = performedDate;
    }

}
