package ar.lamansys.sgh.publicapi.domain;

import ar.lamansys.sgh.publicapi.domain.exceptions.ExternalPatientBoEnumException;
import ar.lamansys.sgh.publicapi.domain.exceptions.ExternalPatientBoException;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.annotation.Nullable;
import java.time.LocalDateTime;

@Getter
@Setter
@ToString
public class ExternalPatientBo {

    @Nullable
    private Integer id;

    @Nullable
    private Integer patientId;

    private String externalId;

    private String externalEncounterId;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
    private LocalDateTime externalEncounterDate;

    private EExternalEncounterType eExternalEncounterType;

    public ExternalPatientBo(Integer id, Integer patientId, String externalId,
                             String externalEncounterId, LocalDateTime externalEncounterDate,
                             String externalEncounterType) throws ExternalPatientBoException {
        this.id = id;
        this.patientId = patientId;
        if(externalId==null)
            throw new ExternalPatientBoException(ExternalPatientBoEnumException.NULL_EXTERNAL_ID,"El id externo es obligatorio");
        this.externalId = externalId;
        if(externalEncounterId==null)
            throw new ExternalPatientBoException(ExternalPatientBoEnumException.NULL_EXTERNAL_ENCOUNTER_ID,"El id de encuentro externo es obligatorio");
        this.externalEncounterId = externalEncounterId;
        if(externalEncounterDate==null)
            throw new ExternalPatientBoException(ExternalPatientBoEnumException.NULL_EXTERNAL_ENCOUNTER_DATE,"La fecha de encuentro externo es obligatoria");
        this.externalEncounterDate = externalEncounterDate;
        if(externalEncounterType==null)
            throw new ExternalPatientBoException(ExternalPatientBoEnumException.NULL_EXTERNAL_ENCOUNTER_TYPE,"El tipo de encuentro externo es obligatorio");
        this.eExternalEncounterType = EExternalEncounterType.map(externalEncounterType);
    }

}

