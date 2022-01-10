package ar.lamansys.sgh.publicapi.domain;

import ar.lamansys.sgh.publicapi.domain.exceptions.ExternalEncounterBoEnumException;
import ar.lamansys.sgh.publicapi.domain.exceptions.ExternalEncounterBoException;
import ar.lamansys.sgx.shared.dates.configuration.JacksonDateFormatConfig;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.annotation.Nullable;
import java.time.LocalDateTime;

@Getter
@ToString
@NoArgsConstructor
public class ExternalEncounterBo {

    @Nullable
    private Integer id;

    private String externalId;

    private String externalEncounterId;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = JacksonDateFormatConfig.DATE_TIME_FORMAT)
    private LocalDateTime externalEncounterDate;

    private EExternalEncounterType eExternalEncounterType;

    private Integer institutionId;

    public ExternalEncounterBo(@Nullable Integer id, String externalId, String externalEncounterId, LocalDateTime externalEncounterDate, EExternalEncounterType externalEncounterType, Integer institutionId) throws ExternalEncounterBoException {
        this.id = id;
        if (externalId == null)
            throw new ExternalEncounterBoException(ExternalEncounterBoEnumException.NULL_EXTERNAL_ID, "El id externo es obligatorio");
        this.externalId = externalId;
        if (externalEncounterId == null)
            throw new ExternalEncounterBoException(ExternalEncounterBoEnumException.NULL_EXTERNAL_ENCOUNTER_ID, "El id de encuentro externo es obligatorio");
        this.externalEncounterId = externalEncounterId;
        if (externalEncounterDate == null)
            throw new ExternalEncounterBoException(ExternalEncounterBoEnumException.NULL_EXTERNAL_ENCOUNTER_DATE, "La fecha del encuentro externo es obligatoria");
        this.externalEncounterDate = externalEncounterDate;
        if (externalEncounterType == null)
            throw new ExternalEncounterBoException(ExternalEncounterBoEnumException.NULL_EXTERNAL_ENCOUNTER_TYPE, "El tipo de encuentro externo es obligatorio");
        this.eExternalEncounterType = externalEncounterType;
        this.institutionId = institutionId;
    }

}
