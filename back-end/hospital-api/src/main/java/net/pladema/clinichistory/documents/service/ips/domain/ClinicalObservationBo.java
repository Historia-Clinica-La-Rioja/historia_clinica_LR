package net.pladema.clinichistory.documents.service.ips.domain;

import lombok.*;
import net.pladema.sgx.dates.configuration.JacksonDateFormatConfig;
import net.pladema.clinichistory.documents.repository.generalstate.domain.ClinicalObservationVo;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ClinicalObservationBo implements Serializable {

    private Integer id;

    @NotNull
    private String value;

    private LocalDateTime effectiveTime;

    public ClinicalObservationBo(ClinicalObservationVo clinicalObservationVo) {
        super();
        this.id = clinicalObservationVo.getId();
        this.value = clinicalObservationVo.getValue();
        this.effectiveTime = clinicalObservationVo.getEffectiveTime();
    }

    public LocalDateTime getEffectiveTime(){
        if (effectiveTime == null)
            return LocalDateTime.now();
        return effectiveTime;
    }

    public void setEffectiveTime(LocalDateTime effectiveTime){
        this.effectiveTime = effectiveTime;
    }

    public void setEffectiveTime(String effectiveTime){
        if (effectiveTime == null)
            this.effectiveTime = null;
        this.effectiveTime = LocalDateTime.parse(effectiveTime, DateTimeFormatter.ofPattern( JacksonDateFormatConfig.DATE_TIME_FORMAT ));
    }

}
