package ar.lamansys.sgh.publicapi.infrastructure.input.rest.dto;

import ar.lamansys.sgh.publicapi.domain.EExternalEncounterType;
import ar.lamansys.sgx.shared.dates.configuration.JacksonDateFormatConfig;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.annotation.Nullable;
import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class ExternalEncounterDto {

    @Nullable
    private Integer id;

    private String externalEncounterId;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = JacksonDateFormatConfig.DATE_TIME_FORMAT)
    private LocalDateTime externalEncounterDate;

    private EExternalEncounterType externalEncounterType;

    public ExternalEncounterDto(@Nullable Integer id, String externalEncounterId, LocalDateTime externalEncounterDate, String externalEncounterType) {
        this.id = id;
        this.externalEncounterId = externalEncounterId;
        this.externalEncounterDate = externalEncounterDate;
        this.externalEncounterType = EExternalEncounterType.map(externalEncounterType);
    }

    public void setExternalEncounterType(String externalEncounterType){
        this.externalEncounterType = EExternalEncounterType.map(externalEncounterType);
    }

}
