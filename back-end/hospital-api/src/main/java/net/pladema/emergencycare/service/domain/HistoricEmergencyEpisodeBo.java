package net.pladema.emergencycare.service.domain;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HistoricEmergencyEpisodeBo {

    private Integer emergencyCareEpisodeId;

    private LocalDateTime changeStateDate;

    private Short emergencyCareStateId;

    private Integer doctorsOfficeId;

	private Integer shockroomId;

	private Integer bedId;

	private Short calls;

    public HistoricEmergencyEpisodeBo(EmergencyCareBo emergencyCareBo) {
        this.emergencyCareEpisodeId = emergencyCareBo.getId();
        this.emergencyCareStateId = emergencyCareBo.getEmergencyCareStateId();
        this.doctorsOfficeId = emergencyCareBo.getDoctorsOfficeId();
    }

    public HistoricEmergencyEpisodeBo(Integer emergencyCareEpisodeId, Short emergencyCareStateId, Integer doctorsOfficeId) {
        this.emergencyCareEpisodeId = emergencyCareEpisodeId;
        this.emergencyCareStateId = emergencyCareStateId;
        this.doctorsOfficeId = doctorsOfficeId;
    }

	public HistoricEmergencyEpisodeBo(Integer emergencyCareEpisodeId, Short emergencyCareStateId) {
		this.emergencyCareEpisodeId = emergencyCareEpisodeId;
		this.emergencyCareStateId = emergencyCareStateId;
	}

}
