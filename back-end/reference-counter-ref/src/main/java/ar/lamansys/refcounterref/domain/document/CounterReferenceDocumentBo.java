package ar.lamansys.refcounterref.domain.document;

import ar.lamansys.refcounterref.domain.allergy.CounterReferenceAllergyBo;
import ar.lamansys.refcounterref.domain.counterreference.CounterReferenceBo;
import ar.lamansys.refcounterref.domain.medication.CounterReferenceMedicationBo;
import ar.lamansys.refcounterref.domain.procedure.CounterReferenceProcedureBo;
import ar.lamansys.sgh.clinichistory.domain.ReferableItemBo;
import lombok.Getter;

import java.time.LocalDate;
import java.util.List;

@Getter
public class CounterReferenceDocumentBo {

    private final Long id;

    private final Integer patientId;

    private final Integer encounterId;

    private final Integer institutionId;

    private final Integer doctorId;

    private final Integer clinicalSpecialtyId;

    private final String counterReferenceNote;

    private final Integer patientMedicalCoverage;

    private final List<CounterReferenceProcedureBo> procedures;

    private final List<CounterReferenceMedicationBo> medications;

    private final ReferableItemBo<CounterReferenceAllergyBo> allergies;

    private final LocalDate performedDate;

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