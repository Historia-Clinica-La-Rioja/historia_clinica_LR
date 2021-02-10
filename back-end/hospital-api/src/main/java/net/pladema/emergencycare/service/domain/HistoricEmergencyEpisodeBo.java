package net.pladema.emergencycare.service.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
public class HistoricEmergencyEpisodeBo {

    private Integer emergencyCareEpisodeId;

    private LocalDateTime changeStateDate;

    private Short emergencyCareStateId;

    private Integer doctorsOfficeId;

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
