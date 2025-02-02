package net.pladema.emergencycare.repository.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import net.pladema.emergencycare.repository.entity.EmergencyCareEpisode;
import net.pladema.emergencycare.repository.entity.PoliceInterventionDetails;
import net.pladema.emergencycare.triage.infrastructure.output.entity.Triage;
import net.pladema.emergencycare.triage.repository.domain.TriageVo;
import net.pladema.emergencycare.triage.repository.entity.TriageCategory;
import net.pladema.establishment.repository.domain.BedVo;
import net.pladema.establishment.repository.domain.RoomVo;
import net.pladema.establishment.repository.domain.SectorVo;
import net.pladema.establishment.repository.entity.Bed;
import net.pladema.establishment.repository.entity.Room;
import net.pladema.establishment.repository.entity.Sector;
import net.pladema.medicalconsultation.doctorsoffice.repository.domain.DoctorsOfficeVo;
import net.pladema.medicalconsultation.doctorsoffice.repository.entity.DoctorsOffice;
import net.pladema.medicalconsultation.shockroom.domain.ShockroomVo;
import net.pladema.medicalconsultation.shockroom.infrastructure.repository.entity.Shockroom;
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

	private LocalDateTime stateUpdatedOn;

	private SectorVo sector;

	private TriageVo triage;

	public EmergencyCareVo(EmergencyCareEpisode emergencyCareEpisode, Person person, String personIdentificationType,
						   Short patientTypeId, String nameSelfDetermination, DoctorsOffice doctorsOffice, TriageCategory triage,
						   Shockroom shockroom, Bed bed, Room room, Sector sector){
		this.id = emergencyCareEpisode.getId();
		this.patient = emergencyCareEpisode.getPatientId() != null ? new PatientECEVo(emergencyCareEpisode.getPatientId(), emergencyCareEpisode.getPatientMedicalCoverageId(), patientTypeId, person, personIdentificationType, nameSelfDetermination, emergencyCareEpisode.getPatientDescription()) : null;
		this.triageCategoryId = triage.getId();
		this.triageName = triage.getName();
		this.triageColorCode = triage.getColorCode();
		this.institutionId = emergencyCareEpisode.getInstitutionId();
		this.emergencyCareTypeId = emergencyCareEpisode.getEmergencyCareTypeId();
		this.emergencyCareStateId = emergencyCareEpisode.getEmergencyCareStateId();
		this.emergencyCareEntranceTypeId = emergencyCareEpisode.getEmergencyCareEntranceTypeId();
		this.ambulanceCompanyId = emergencyCareEpisode.getAmbulanceCompanyId();
		this.createdOn = emergencyCareEpisode.getCreatedOn();
		this.doctorsOffice = emergencyCareEpisode.getDoctorsOfficeId() != null ? new DoctorsOfficeVo(doctorsOffice.getId(), doctorsOffice.getDescription()) : null;
		this.hasPoliceIntervention = emergencyCareEpisode.getHasPoliceIntervention();
		this.shockroom = emergencyCareEpisode.getShockroomId() != null ? new ShockroomVo(shockroom.getId(), shockroom.getDescription()) : null;
		this.bed = emergencyCareEpisode.getBedId() != null ? new BedVo(bed) : null;
		this.reason = emergencyCareEpisode.getReason();
		this.room = room != null ? new RoomVo(room): null;
		this.sector = sector != null ? new SectorVo(sector) : null;
	}

	public EmergencyCareVo(EmergencyCareEpisode emergencyCareEpisode, Person person, Short patientTypeId, String nameSelfDetermination,
						   DoctorsOffice doctorsOffice, TriageCategory triage, PoliceInterventionDetails policeInterventionDetails,
						   Shockroom shockroom, Bed bed, LocalDateTime endDate, Room room){
		this(emergencyCareEpisode, person, null, patientTypeId, nameSelfDetermination, doctorsOffice, triage, shockroom, bed, room, null);
		this.policeInterventionDetails = policeInterventionDetails != null ? new PoliceInterventionDetailsVo(policeInterventionDetails) : null;
		this.endDate = endDate;
	}

	public EmergencyCareVo(Integer id, Integer institutionId){
		this.id = id;
		this.institutionId = institutionId;
	}

	public EmergencyCareVo(EmergencyCareEpisode emergencyCareEpisode, Person person, Short patientTypeId, String nameSalfeDetermination,
						   DoctorsOffice doctorsOffice, TriageCategory triage, PoliceInterventionDetails policeInterventionDetails,
						   Shockroom shockroom, Bed bed, LocalDateTime endDate, Room room, String institutionName){
		this(emergencyCareEpisode, person, patientTypeId, nameSalfeDetermination, doctorsOffice, triage, policeInterventionDetails, shockroom, bed, endDate, room);
		this.institutionName = institutionName ;
	}

	public EmergencyCareVo (EmergencyCareEpisode emergencyCareEpisode, Person person, String personIdentificationType,
							Short patientTypeId, String nameSelfDetermination, DoctorsOffice doctorsOffice, TriageCategory triageCategory,
							Shockroom shockroom, Bed bed, Room room, Sector sector, Triage triage, String specialtySectorDescription, ProfessionalPersonVo triageCreator) {
		this(emergencyCareEpisode, person, personIdentificationType, patientTypeId, nameSelfDetermination, doctorsOffice, triageCategory, shockroom, bed, room, sector);
		this.triage = triage != null ? new TriageVo(triage, emergencyCareEpisode.getEmergencyCareTypeId(), specialtySectorDescription, triageCreator) : null;
	}
}
