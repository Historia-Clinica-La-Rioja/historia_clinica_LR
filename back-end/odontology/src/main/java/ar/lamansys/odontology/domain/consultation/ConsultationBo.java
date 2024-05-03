package ar.lamansys.odontology.domain.consultation;

import ar.lamansys.odontology.domain.consultation.reference.ReferenceBo;
import ar.lamansys.sgh.clinichistory.domain.ReferableItemBo;
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

    private ReferableItemBo<ConsultationPersonalHistoryBo> personalHistories = new ReferableItemBo<>();

    private ReferableItemBo<ConsultationAllergyBo> allergies = new ReferableItemBo<>();

    private List<ConsultationMedicationBo> medications = new ArrayList<>();

	private List<ReferenceBo> references = new ArrayList<>();

	private String evolutionNote;

    private Integer permanentTeethPresent;

    private Integer temporaryTeethPresent;

	private Integer patientMedicalCoverageId;

	private Integer hierarchicalUnitId;
}
