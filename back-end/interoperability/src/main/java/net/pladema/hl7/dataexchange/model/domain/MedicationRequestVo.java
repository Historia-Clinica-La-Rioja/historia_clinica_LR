package net.pladema.hl7.dataexchange.model.domain;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.UUID;

@Getter
@Setter
public class MedicationRequestVo {

	private Integer mrId;
	private String status;
	private String intent;
	private String categoryCode;
	private String medicationStatementId;
	private String medicationCode;
	private String medicationDisplay;
	private String patientId;
	private String institutionId;
	private Date authoredOn;
	private Integer doctorId;
	private String problemId;
	private String problemPt;
	private Integer patientMedicalCoverageId;
	private String note;
	private Boolean hasRecipe;
	private DosageVo dosage;
	private Date prescriptionDate;
	private Date dueDate;
	private Float quantityValue;
	private String quantityUnit;
	private Short prescriptionLineState;
	private Integer prescriptionLineNumber;
	private UUID requestUuid;
	private UUID lineUuid;

	//for validation
	private String patientIdentificationNumber;
	private Integer medicalCoverageId;
	private String coverageAffiliationNumber;

	public MedicationRequestVo(Integer id, String statusId, String intentId, String categoryId,
							   Integer medicationStatementId, Integer patientId, Integer institutionId, Date requestDate,
							   Integer doctorId, String problemId, String problemPt, Integer patientMedicalCoverageId, String description, Boolean hasRecipe,
							   Integer frequency, String periodUnit, Float quantityValue, String quantityUnit,
							   Double duration, String durationUnit, Date prescriptionDate, Date dueDate,
							   Integer sequence, Integer timingRepeat, Short prescriptionLineState, Integer prescriptionLineNumber,
							   UUID requestUuid, UUID lineUuid) {
		this.mrId = id;
		this.status = statusId;
		this.intent = intentId;
		this.categoryCode = categoryId;
		this.medicationStatementId = medicationStatementId.toString();
		this.patientId = patientId.toString();
		this.institutionId = institutionId.toString();
		this.authoredOn = requestDate;
		this.doctorId = doctorId;
		this.problemId = problemId;
		this.problemPt = problemPt;
		this.patientMedicalCoverageId = patientMedicalCoverageId;
		this.note = description;
		this.hasRecipe = hasRecipe;
		this.quantityValue = quantityValue;
		this.quantityUnit = quantityUnit;
		this.prescriptionDate = prescriptionDate;
		this.dueDate = dueDate;
		this.prescriptionLineState = prescriptionLineState;
		this.prescriptionLineNumber = prescriptionLineNumber;
		this.dosage = new DosageVo(sequence,timingRepeat,duration,durationUnit,frequency,periodUnit);
		this.requestUuid = requestUuid;
		this.lineUuid = lineUuid;
	}

	public MedicationRequestVo(Integer id, String statusId, String intentId, String categoryId,
							   Integer medicationStatementId, Integer patientId, Integer institutionId, Date requestDate,
							   Integer doctorId, String medicationCode, Integer patientMedicalCoverageId, String description, Boolean hasRecipe,
							   Integer frequency, String periodUnit, Float quantityValue, String quantityUnit,
							   Double duration, String durationUnit, Date prescriptionDate, Date dueDate,
							   Integer sequence, Integer timingRepeat, Short prescriptionLineState, String patientIdentificationNumber,
							   Integer medicalCoverageId, String coverageAffiliateNumber, Integer prescriptionLineNumber) {
		this.mrId = id;
		this.status = statusId;
		this.intent = intentId;
		this.categoryCode = categoryId;
		this.medicationStatementId = medicationStatementId.toString();
		this.patientId = patientId.toString();
		this.institutionId = institutionId.toString();
		this.authoredOn = requestDate;
		this.doctorId = doctorId;
		this.medicationCode = medicationCode;
		this.patientMedicalCoverageId = patientMedicalCoverageId;
		this.note = description;
		this.hasRecipe = hasRecipe;
		this.quantityValue = quantityValue;
		this.quantityUnit = quantityUnit;
		this.prescriptionDate = prescriptionDate;
		this.dueDate = dueDate;
		this.prescriptionLineState = prescriptionLineState;
		this.dosage = new DosageVo(sequence,timingRepeat,duration,durationUnit,frequency,periodUnit);
		this.patientIdentificationNumber = patientIdentificationNumber;
		this.medicalCoverageId = medicalCoverageId;
		this.coverageAffiliationNumber = coverageAffiliateNumber;
		this.prescriptionLineNumber = prescriptionLineNumber;
	}

	public MedicationRequestVo(){

	}

}
