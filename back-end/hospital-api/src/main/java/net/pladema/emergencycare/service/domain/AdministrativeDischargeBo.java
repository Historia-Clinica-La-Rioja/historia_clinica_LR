package net.pladema.emergencycare.service.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@Setter
public class AdministrativeDischargeBo {

    private Integer episodeId;

    private LocalDateTime administrativeDischargeOn;

    private Integer userId;

    private Short hospitalTransportId;

    private String ambulanceCompanyId;

}
