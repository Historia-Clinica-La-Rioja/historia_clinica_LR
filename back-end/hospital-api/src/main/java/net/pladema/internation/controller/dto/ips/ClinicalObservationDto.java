package net.pladema.internation.controller.dto.ips;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Getter
@Setter
@ToString
public class ClinicalObservationDto implements Serializable {

    private Integer id;

    @NotNull(message = "{value.mandatory}")
    private String value;

}
