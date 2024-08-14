package net.pladema.emergencycare.service.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.pladema.emergencycare.repository.entity.EmergencyCareDischarge;
import net.pladema.medicalconsultation.diary.service.domain.ProfessionalPersonBo;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EpisodeDischargeSummaryBo {

	private Integer episodeId;

	private LocalDateTime medicalDischargeOn;

	private LocalDateTime administrativeDischargeOn;

	private String medicalDischargeProfessionalName;

	private String medicalDischargeProfessionalLastName;

	private String medicalDischargeProfessionalNameSelfDetermination;

	private String observation;

	public EpisodeDischargeSummaryBo(EmergencyCareDischarge discharge,
									 String medicalDischargeProfessionalName,
									 String medicalDischargeProfessionalLastName,
									 String medicalDischargeProfessionalNameSelfDetermination)
	{
		this.episodeId = discharge.getEmergencyCareEpisodeId();
		this.medicalDischargeOn = discharge.getMedicalDischargeOn();
		this.administrativeDischargeOn = discharge.getAdministrativeDischargeOn();
		this.observation = discharge.getObservation();
		this.medicalDischargeProfessionalName = medicalDischargeProfessionalName;
		this.medicalDischargeProfessionalLastName = medicalDischargeProfessionalLastName;
		this.medicalDischargeProfessionalNameSelfDetermination = medicalDischargeProfessionalNameSelfDetermination;
	}

}
