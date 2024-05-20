package net.pladema.emergencycare.repository.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import net.pladema.emergencycare.repository.entity.EmergencyCareEpisode;
import net.pladema.emergencycare.repository.entity.PoliceInterventionDetails;
import net.pladema.emergencycare.triage.repository.entity.TriageCategory;
import net.pladema.establishment.repository.domain.BedVo;
import net.pladema.establishment.repository.domain.RoomVo;
import net.pladema.establishment.repository.entity.Bed;
import net.pladema.establishment.repository.entity.Room;
import net.pladema.medicalconsultation.doctorsoffice.repository.domain.DoctorsOfficeVo;
import net.pladema.medicalconsultation.shockroom.domain.ShockroomVo;
import net.pladema.person.repository.entity.Person;

import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class EmergencyCareVo implements Serializable {

	private static final long serialVersionUID = -8118445529514102823L;

	private Integer id;

	private PatientECEVo patient;

	private Short triageCategoryId;

	private String triageName;

	private String triageColorCode;

	private Integer institutionId;

	private Short emergencyCareTypeId;

	private Short emergencyCareStateId;

	private Short emergencyCareEntranceTypeId;

	private DoctorsOfficeVo doctorsOffice;

	private String ambulanceCompanyId;

	private LocalDateTime createdOn;

	private PoliceInterventionDetailsVo policeInterventionDetails;

	private Boolean hasPoliceIntervention;

	private ShockroomVo shockroom;

	private BedVo bed;

	private LocalDateTime endDate;

	private RoomVo room;

	private String institutionName;

	private String reason;

	public EmergencyCareVo(EmergencyCareEpisode emergencyCareEpisode, Person person, Short patientTypeId,
						   String nameSelfDetermination, String doctorsOfficeDescription, TriageCategory triage,
						   String shockroomDescription, Bed bed){
		this.id = emergencyCareEpisode.getId();
		this.patient = emergencyCareEpisode.getPatientId() != null ? new PatientECEVo(emergencyCareEpisode.getPatientId(), emergencyCareEpisode.getPatientMedicalCoverageId(), patientTypeId, person, nameSelfDetermination) : null;
		this.triageCategoryId = triage.getId();
		this.triageName = triage.getName();
		this.triageColorCode = triage.getColorCode();
		this.institutionId = emergencyCareEpisode.getInstitutionId();
		this.emergencyCareTypeId = emergencyCareEpisode.getEmergencyCareTypeId();
		this.emergencyCareStateId = emergencyCareEpisode.getEmergencyCareStateId();
		this.emergencyCareEntranceTypeId = emergencyCareEpisode.getEmergencyCareEntranceTypeId();
		this.ambulanceCompanyId = emergencyCareEpisode.getAmbulanceCompanyId();
		this.createdOn = emergencyCareEpisode.getCreatedOn();
		this.doctorsOffice = emergencyCareEpisode.getDoctorsOfficeId() != null ? new DoctorsOfficeVo(emergencyCareEpisode.getDoctorsOfficeId(), doctorsOfficeDescription) : null;
		this.hasPoliceIntervention = emergencyCareEpisode.getHasPoliceIntervention();
		this.shockroom = emergencyCareEpisode.getShockroomId() != null ? new ShockroomVo(emergencyCareEpisode.getShockroomId(), shockroomDescription) : null;
		this.bed = emergencyCareEpisode.getBedId() != null ? new BedVo(bed) : null;
		this.reason = emergencyCareEpisode.getReason();
	}

	public EmergencyCareVo(EmergencyCareEpisode emergencyCareEpisode, Person person, Short patientTypeId, String nameSalfeDetermination,
						   String doctorsOfficeDescription, TriageCategory triage, PoliceInterventionDetails policeInterventionDetails,
						   String shockroomDescription, Bed bed, LocalDateTime endDate, Room room){
		this(emergencyCareEpisode, person, patientTypeId, nameSalfeDetermination, doctorsOfficeDescription, triage, shockroomDescription, bed);
		this.policeInterventionDetails = policeInterventionDetails != null ? new PoliceInterventionDetailsVo(policeInterventionDetails) : null;
		this.endDate = endDate;
		this.room = room != null ? new RoomVo(room): null;
	}

	public EmergencyCareVo(Integer id, Integer institutionId){
		this.id = id;
		this.institutionId = institutionId;
	}

	public EmergencyCareVo(EmergencyCareEpisode emergencyCareEpisode, Person person, Short patientTypeId, String nameSalfeDetermination,
						   String doctorsOfficeDescription, TriageCategory triage, PoliceInterventionDetails policeInterventionDetails,
						   String shockroomDescription, Bed bed, LocalDateTime endDate, Room room, String institutionName){
		this(emergencyCareEpisode, person, patientTypeId, nameSalfeDetermination, doctorsOfficeDescription, triage, policeInterventionDetails, shockroomDescription, bed, endDate, room);
		this.institutionName = institutionName ;
	}
	
}
