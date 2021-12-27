package ar.lamansys.sgh.clinichistory.infrastructure.output.repository.backoffice.dto;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.ZonedDateTime;

@Getter
@Setter
public class CreationableDto implements Serializable {

    private ZonedDateTime createdOn;

    public CreationableDto(ZonedDateTime createdOn) {
        this.createdOn = createdOn;
    }
}
