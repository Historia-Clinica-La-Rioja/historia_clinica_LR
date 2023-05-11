package net.pladema.emergencycare.service.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class HistoricEmergencyEpisodeBo {

    private Integer emergencyCareEpisodeId;

    private LocalDateTime changeStateDate;

    private Short emergencyCareStateId;

    private Integer doctorsOfficeId;

	private Integer shockroomId;

	private Integer bedId;

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

}
