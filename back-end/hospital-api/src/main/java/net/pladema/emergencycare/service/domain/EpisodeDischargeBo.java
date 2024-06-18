package net.pladema.emergencycare.service.domain;

import lombok.Getter;
import ar.lamansys.sgh.clinichistory.domain.ips.SnomedBo;
import net.pladema.clinichistory.hospitalization.repository.domain.DischargeType;
import net.pladema.emergencycare.repository.entity.EmergencyCareDischarge;

import java.time.LocalDateTime;
import java.util.List;

@Getter
public class EpisodeDischargeBo {

    private Integer episodeId;

    private List<SnomedBo> problems;

    private LocalDateTime medicalDischargeOn;

    private Boolean autopsy;

    private DischargeTypeBo dischargeType;

    private LocalDateTime administrativeDischargeOn;

    private Short hospitalTransportId;

    private String ambulanceCompanyId;

	private String medicalDischargeProfessionalName;
	private String medicalDischargeProfessionalLastName;
	private String otherDischargeDescription;

	private String observation;

    public EpisodeDischargeBo(EmergencyCareDischarge emergencyCareDischarge, DischargeType dischargeType,
							  String medicalDischargeProfessionalName, String medicalDischargeProfessionalLastName,
							  String otherDischargeDescription, String observation) {
        this.episodeId = emergencyCareDischarge.getEmergencyCareEpisodeId();
        this.medicalDischargeOn = emergencyCareDischarge.getMedicalDischargeOn().minusHours(3);
        this.autopsy = emergencyCareDischarge.getAutopsy();
        this.dischargeType = new DischargeTypeBo(dischargeType);
        this.administrativeDischargeOn = emergencyCareDischarge.getAdministrativeDischargeOn();
        this.hospitalTransportId = emergencyCareDischarge.getHospitalTransportId();
        this.ambulanceCompanyId = emergencyCareDischarge.getAmbulanceCompanyId();
		this.medicalDischargeProfessionalName = medicalDischargeProfessionalName;
		this.medicalDischargeProfessionalLastName = medicalDischargeProfessionalLastName;
		this.otherDischargeDescription = otherDischargeDescription;
		this.observation = observation;
    }

    public void setProblems(List<SnomedBo> problems) {
        this.problems = problems;
    }
}
