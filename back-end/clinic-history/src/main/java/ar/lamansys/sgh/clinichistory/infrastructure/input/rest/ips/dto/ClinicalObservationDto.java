package ar.lamansys.sgh.clinichistory.infrastructure.input.rest.ips.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.annotation.Nullable;
import java.io.Serializable;

@Getter
@Setter
@ToString
public class ClinicalObservationDto implements Serializable {

    @Nullable
    private Integer id;

    @Nullable
    private String value;
}
