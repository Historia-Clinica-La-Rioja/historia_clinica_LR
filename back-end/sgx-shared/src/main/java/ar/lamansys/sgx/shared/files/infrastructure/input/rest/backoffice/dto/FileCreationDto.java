package ar.lamansys.sgx.shared.files.infrastructure.input.rest.backoffice.dto;

import java.io.Serializable;
import java.time.ZonedDateTime;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FileCreationDto implements Serializable {

    private ZonedDateTime createdOn;

    public FileCreationDto(ZonedDateTime createdOn) {
        this.createdOn = createdOn;
    }
}
