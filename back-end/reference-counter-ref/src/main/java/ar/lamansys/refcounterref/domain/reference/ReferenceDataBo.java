package ar.lamansys.refcounterref.domain.reference;

import java.time.LocalDateTime;
import java.util.List;

import ar.lamansys.refcounterref.domain.careline.CareLineBo;
import ar.lamansys.refcounterref.domain.clinicalspecialty.ClinicalSpecialtyBo;
import ar.lamansys.refcounterref.domain.enums.EReferenceAdministrativeState;
import ar.lamansys.refcounterref.domain.enums.EReferenceClosureType;
import ar.lamansys.refcounterref.domain.enums.EReferencePriority;
import ar.lamansys.refcounterref.domain.enums.EReferenceRegulationState;
import ar.lamansys.refcounterref.domain.enums.EReferenceStatus;
import ar.lamansys.refcounterref.domain.file.ReferenceCounterReferenceFileBo;
import ar.lamansys.refcounterref.domain.snomed.SnomedBo;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ReferenceDataBo {

    private Integer id;

	private Integer patientId;

	private Integer patientMedicalCoverageId;

	private Integer encounterId;

	private Boolean consultation;

    private LocalDateTime date;

	private ReferenceInstitutionBo institutionOrigin;

	private ReferenceInstitutionBo institutionDestination;

    private String note;

    private CareLineBo careLine;

    private ClinicalSpecialtyBo clinicalSpecialtyOrigin;

	private List<ClinicalSpecialtyBo> destinationClinicalSpecialties;

	private Integer serviceRequestId;

	private SnomedBo procedure;

	private String procedureCategory;

	private Integer professionalPersonId;

	private String professionalFullName;

	private EReferencePriority priority;

	private List<String> problems;

    private List<ReferenceCounterReferenceFileBo> files;

	private EReferenceClosureType closureType;

	private String phonePrefix;

	private String phoneNumber;

	private Integer createdBy;

	private EReferenceStatus status;

	private EReferenceRegulationState regulationState;

	private EReferenceAdministrativeState administrativeState;

	public ReferenceDataBo(Integer id, LocalDateTime date, String note,
						   Integer careLineId, String careLineDescription,
						   Integer clinicalSpecialtyOriginId, String clinicalSpecialtyOriginName,
						   Integer institutionOriginId, String institutionOriginName,
						   Integer institutionDestinationId, String institutionDestinationName,
						   Integer professionalPersonId, Integer priorityId, Integer serviceRequestId) {
		this.id = id;
		this.date = date;
		this.institutionOrigin = new ReferenceInstitutionBo(institutionOriginId, institutionOriginName);
		this.institutionDestination = new ReferenceInstitutionBo(institutionDestinationId, institutionDestinationName);
		this.note = note;
		this.careLine = new CareLineBo(careLineId, careLineDescription);
		this.clinicalSpecialtyOrigin = new ClinicalSpecialtyBo(clinicalSpecialtyOriginId, clinicalSpecialtyOriginName);
		this.professionalPersonId = professionalPersonId;
		this.priority = EReferencePriority.map(priorityId.shortValue());
		this.serviceRequestId = serviceRequestId;
	}

	public ReferenceDataBo(Integer id, Integer patientId, Integer patientMedicalCoverageId, Integer encounterId,
						   Boolean consultation, LocalDateTime date, String note,
						   Integer careLineId, String careLineDescription,
						   Integer clinicalSpecialtyOriginId, String clinicalSpecialtyOriginName,
						   Integer institutionOriginId, String institutionOriginName, Short departmentOriginId, String departmentOriginName, String provinceOriginName,
						   Integer institutionDestinationId, String institutionDestinationName, Short departmentDestinationId, String departmentDestinationName,
						   Integer professionalPersonId, Integer priorityId, Short closureType,
						   String phonePrefix, String phoneNumber, Integer serviceRequestId,
						   Integer createdBy, Short statusId, Short regulationStateId, Short administrativeStateId) {
		this.id = id;
		this.patientId = patientId;
		this.patientMedicalCoverageId = patientMedicalCoverageId;
		this.encounterId = encounterId;
		this.consultation = consultation;
		this.date = date;
		this.institutionOrigin = new ReferenceInstitutionBo(institutionOriginId, institutionOriginName, departmentOriginId, departmentOriginName, provinceOriginName);
		this.institutionDestination = new ReferenceInstitutionBo(institutionDestinationId, institutionDestinationName, departmentDestinationId, departmentDestinationName);
		this.note = note;
		this.careLine = new CareLineBo(careLineId, careLineDescription);
		this.clinicalSpecialtyOrigin = new ClinicalSpecialtyBo(clinicalSpecialtyOriginId, clinicalSpecialtyOriginName);
		this.professionalPersonId = professionalPersonId;
		this.priority = EReferencePriority.map(priorityId.shortValue());
		this.closureType = EReferenceClosureType.getById(closureType);
		this.phonePrefix = phonePrefix;
		this.phoneNumber = phoneNumber;
		this.serviceRequestId = serviceRequestId;
		this.createdBy = createdBy;
		this.status = EReferenceStatus.map(statusId);
		this.regulationState = EReferenceRegulationState.getById(regulationStateId);
		this.administrativeState = EReferenceAdministrativeState.map(administrativeStateId);
	}

	public ReferenceDataBo(String phonePrefix, String phoneNumber) {
		this.phonePrefix = phonePrefix;
		this.phoneNumber = phoneNumber;
	}

}
