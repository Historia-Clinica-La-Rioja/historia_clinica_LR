package net.pladema.clinichistory.ips.controller.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.annotation.Nullable;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Getter
@Setter
@ToString
public class ClinicalObservationDto implements Serializable {

    @Nullable
    private Integer id;

    @NotNull(message = "{value.mandatory}")
    private String value;
}
