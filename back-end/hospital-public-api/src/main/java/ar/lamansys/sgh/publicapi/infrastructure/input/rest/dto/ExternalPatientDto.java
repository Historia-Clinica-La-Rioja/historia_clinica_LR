package ar.lamansys.sgh.publicapi.infrastructure.input.rest.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.annotation.Nullable;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class ExternalPatientDto {

    @Nullable
    private Integer patientId;

    @Nullable
    private String externalId;

    public ExternalPatientDto(Integer patientId, String externalId) {
        this.patientId = patientId;
        this.externalId = externalId;
    }
}