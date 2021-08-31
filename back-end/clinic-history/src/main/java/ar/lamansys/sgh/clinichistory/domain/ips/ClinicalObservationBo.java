package ar.lamansys.sgh.clinichistory.domain.ips;

import ar.lamansys.sgx.shared.exceptions.SelfValidating;
import lombok.*;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.hospitalizationState.entity.ClinicalObservationVo;
import ar.lamansys.sgx.shared.dates.configuration.JacksonDateFormatConfig;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ClinicalObservationBo extends SelfValidating<ClinicalObservationBo> {

    private Integer id;

    @NotNull(message = "{value.mandatory}")
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
