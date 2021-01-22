package net.pladema.emergencycare.repository.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import net.pladema.emergencycare.repository.entity.EmergencyCareEpisode;
import net.pladema.emergencycare.repository.entity.PoliceIntervention;

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

	private String firstname;

	private String lastname;

	private Integer patientId;

	private Short triageCategoryId;

	private Integer institutionId;

	private Short emergencyCareTypeId;

	private Short emergencyCareStateId;

	private Short emergencyCareEntranceTypeId;

	private Integer doctorsOfficeId;

	private String doctorsOfficeDescription;

	private String ambulanceCompanyId;

	private LocalDateTime createdOn;

	private PoliceInterventionVo policeIntervention;

	public EmergencyCareVo(EmergencyCareEpisode emergencyCareEpisode, String firstname, String lastname, String doctorsOfficeDescription){
		this.id = emergencyCareEpisode.getId();
		this.patientId = emergencyCareEpisode.getPatientId();
		this.triageCategoryId = emergencyCareEpisode.getTriageCategoryId();
		this.institutionId = emergencyCareEpisode.getInstitutionId();
		this.emergencyCareTypeId = emergencyCareEpisode.getEmergencyCareTypeId();
		this.emergencyCareStateId = emergencyCareEpisode.getEmergencyCareStateId();
		this.emergencyCareEntranceTypeId = emergencyCareEpisode.getEmergencyCareEntranceTypeId();
		this.doctorsOfficeId = emergencyCareEpisode.getDoctorsOfficeId();
		this.ambulanceCompanyId = emergencyCareEpisode.getAmbulanceCompanyId();
		this.createdOn = emergencyCareEpisode.getCreatedOn();
		this.firstname = firstname;
		this.lastname = lastname;
		this.doctorsOfficeDescription = doctorsOfficeDescription;
	}

	public EmergencyCareVo(EmergencyCareEpisode emergencyCareEpisode, String firstname, String lastname, String doctorsOfficeDescription, PoliceIntervention policeIntervention){
		this(emergencyCareEpisode, firstname, lastname, doctorsOfficeDescription);
		this.policeIntervention = policeIntervention != null ? new PoliceInterventionVo(policeIntervention) : null;
	}
}
