package ar.lamansys.sgh.clinichistory.domain.ips;

import ar.lamansys.sgx.shared.exceptions.SelfValidating;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.hospitalizationState.entity.ClinicalObservationVo;
import ar.lamansys.sgx.shared.dates.configuration.JacksonDateFormatConfig;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

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

    private String statusId;

    public ClinicalObservationBo(ClinicalObservationVo clinicalObservationVo) {
        super();
        this.id = clinicalObservationVo.getId();
        this.value = clinicalObservationVo.getValue();
        this.effectiveTime = clinicalObservationVo.getEffectiveTime();
        this.statusId = clinicalObservationVo.getStatusId();
    }

    public LocalDateTime getEffectiveTime() {
        if (effectiveTime == null)
            return LocalDateTime.now();
        return effectiveTime;
    }

    public void setEffectiveTime(String effectiveTime) {
        if (effectiveTime == null) {
            this.effectiveTime = null;
            return;
        }
        this.effectiveTime = LocalDateTime.parse(effectiveTime, DateTimeFormatter.ofPattern(JacksonDateFormatConfig.DATE_TIME_FORMAT));
    }

    public LocalDateTime getLocalDateEffectiveTime() {
        return this.getEffectiveTime();
    }

}
