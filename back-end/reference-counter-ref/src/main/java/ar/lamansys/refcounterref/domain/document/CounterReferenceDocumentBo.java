package ar.lamansys.refcounterref.domain.document;

import ar.lamansys.refcounterref.domain.allergy.CounterReferenceAllergyBo;
import ar.lamansys.refcounterref.domain.counterreference.ReferenceAdministrativeClosureBo;
import ar.lamansys.refcounterref.domain.counterreference.CounterReferenceBo;
import ar.lamansys.refcounterref.domain.medication.CounterReferenceMedicationBo;
import ar.lamansys.refcounterref.domain.procedure.CounterReferenceProcedureBo;
import ar.lamansys.sgh.clinichistory.domain.ReferableItemBo;
import lombok.Getter;

import java.time.LocalDate;
import java.util.List;

@Getter
public class CounterReferenceDocumentBo {

    private Long id;

    private Integer patientId;

    private Integer encounterId;

    private Integer institutionId;

    private Integer doctorId;

    private Integer clinicalSpecialtyId;

    private String counterReferenceNote;

    private Integer patientMedicalCoverage;

    private List<CounterReferenceProcedureBo> procedures;

    private List<CounterReferenceMedicationBo> medications;

    private ReferableItemBo<CounterReferenceAllergyBo> allergies;

    private LocalDate performedDate;

	private Integer medicalCoverageId;

    public CounterReferenceDocumentBo(Long id,
                                      CounterReferenceBo counterReferenceBo,
                                      Integer encounterId,
                                      Integer doctorId,
                                      LocalDate performedDate) {
        this.id = id;
        this.patientId = counterReferenceBo.getPatientId();
        this.encounterId = encounterId;
        this.institutionId = counterReferenceBo.getInstitutionId();
        this.doctorId = doctorId;
        this.clinicalSpecialtyId = counterReferenceBo.getClinicalSpecialtyId();
        this.counterReferenceNote = counterReferenceBo.getCounterReferenceNote();
        this.patientMedicalCoverage = counterReferenceBo.getPatientMedicalCoverageId();
        this.procedures = counterReferenceBo.getProcedures();
        this.medications = counterReferenceBo.getMedications();
        this.allergies = counterReferenceBo.getAllergies();
        this.performedDate = performedDate;
		this.medicalCoverageId = counterReferenceBo.getPatientMedicalCoverageId();
    }

}