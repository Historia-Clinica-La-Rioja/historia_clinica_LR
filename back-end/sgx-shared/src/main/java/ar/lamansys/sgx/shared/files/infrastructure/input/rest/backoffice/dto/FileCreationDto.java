package ar.lamansys.sgx.shared.files.infrastructure.input.rest.backoffice.dto;

import java.io.Serializable;
import java.time.ZonedDateTime;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class FileCreationDto implements Serializable {

    private ZonedDateTime createdOn;

    public FileCreationDto(ZonedDateTime createdOn) {
        this.createdOn = createdOn;
    }
}
