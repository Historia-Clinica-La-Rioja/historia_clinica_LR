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
public class ExternalPatientDto {

    @Nullable
    private Integer id;

    @Nullable
    private Integer patientId;

    private String externalId;

    private String externalEncounterId;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = JacksonDateFormatConfig.DATE_TIME_FORMAT)
    private LocalDateTime externalEncounterDate;

    private EExternalEncounterType eExternalEncounterType;

    public ExternalPatientDto(Integer id, Integer patientId, String externalId,
                              String externalEncounterId, LocalDateTime externalEncounterDate,
                              String externalEncounterType) {
        this.id = id;
        this.patientId = patientId;
        this.externalId = externalId;
        this.externalEncounterId = externalEncounterId;
        this.externalEncounterDate = externalEncounterDate;
        this.eExternalEncounterType = EExternalEncounterType.map(externalEncounterType);
    }
}