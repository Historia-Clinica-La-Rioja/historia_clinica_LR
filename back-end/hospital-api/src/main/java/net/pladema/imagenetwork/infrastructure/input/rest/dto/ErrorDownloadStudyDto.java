package net.pladema.imagenetwork.infrastructure.input.rest.dto;

import ar.lamansys.sgx.shared.dates.controller.dto.DateTimeDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class ErrorDownloadStudyDto {

    private Integer pacServerId;
    private Short errorCode;
    private String errorCodeDescription;
    private DateTimeDto effectiveTime;
    private String fileUuid;

}
