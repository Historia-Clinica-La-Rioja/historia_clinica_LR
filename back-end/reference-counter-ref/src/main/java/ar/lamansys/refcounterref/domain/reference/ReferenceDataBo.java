package ar.lamansys.refcounterref.domain.reference;

import java.time.LocalDateTime;
import java.util.List;

import ar.lamansys.refcounterref.domain.careline.CareLineBo;
import ar.lamansys.refcounterref.domain.clinicalspecialty.ClinicalSpecialtyBo;
import ar.lamansys.refcounterref.domain.enums.EReferenceClosureType;
import ar.lamansys.refcounterref.domain.enums.EReferencePriority;
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

    private LocalDateTime date;

	private ReferenceInstitutionBo institutionOrigin;

	private ReferenceInstitutionBo institutionDestination;

    private String note;

    private CareLineBo careLine;

    private ClinicalSpecialtyBo clinicalSpecialtyOrigin;

	private List<ClinicalSpecialtyBo> destinationClinicalSpecialties;

	private Integer serviceRequestId;

	private SnomedBo procedure;

	private Integer professionalPersonId;

	private String professionalFullName;

	private EReferencePriority priority;

	private List<String> problems;

    private List<ReferenceCounterReferenceFileBo> files;

	private EReferenceClosureType closureType;

	private String phonePrefix;

	private String phoneNumber;

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

	public ReferenceDataBo(Integer id, Integer patientId, LocalDateTime date, String note,
						   Integer careLineId, String careLineDescription,
						   Integer clinicalSpecialtyOriginId, String clinicalSpecialtyOriginName,
						   Integer institutionOriginId, String institutionOriginName,
						   Integer institutionDestinationId, String institutionDestinationName,
						   Integer professionalPersonId, Integer priorityId, Short closureType,
						   String phonePrefix, String phoneNumber, Integer serviceRequestId) {
		this.id = id;
		this.patientId = patientId;
		this.date = date;
		this.institutionOrigin = new ReferenceInstitutionBo(institutionOriginId, institutionOriginName);
		this.institutionDestination = new ReferenceInstitutionBo(institutionDestinationId, institutionDestinationName);
		this.note = note;
		this.careLine = new CareLineBo(careLineId, careLineDescription);
		this.clinicalSpecialtyOrigin = new ClinicalSpecialtyBo(clinicalSpecialtyOriginId, clinicalSpecialtyOriginName);
		this.professionalPersonId = professionalPersonId;
		this.priority = EReferencePriority.map(priorityId.shortValue());
		this.closureType = EReferenceClosureType.getById(closureType);
		this.phonePrefix = phonePrefix;
		this.phoneNumber = phoneNumber;
		this.serviceRequestId = serviceRequestId;
	}

	public ReferenceDataBo(String phonePrefix, String phoneNumber) {
		this.phonePrefix = phonePrefix;
		this.phoneNumber = phoneNumber;
	}

}
