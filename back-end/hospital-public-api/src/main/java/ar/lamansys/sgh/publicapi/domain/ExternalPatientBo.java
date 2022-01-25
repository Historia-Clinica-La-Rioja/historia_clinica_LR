package ar.lamansys.sgh.publicapi.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.annotation.Nullable;

@Getter
@Setter
@ToString
public class ExternalPatientBo {

    @Nullable
    private Integer patientId;

    @Nullable
    private String externalId;

    public ExternalPatientBo(@Nullable Integer patientId, @Nullable String externalId) {
        this.patientId = patientId;
        this.externalId = externalId;
    }

}

