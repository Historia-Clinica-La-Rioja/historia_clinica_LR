package net.pladema.imagenetwork.domain;

import ar.lamansys.sgx.shared.exceptions.SelfValidating;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@AllArgsConstructor
public class ErrorDownloadStudyBo extends SelfValidating<ErrorDownloadStudyBo> {

    @NotBlank
    private String studyInstanceUID;

    @NotNull
    private Integer pacServerId;

    @NotNull
    private Short errorCode;

    @NotBlank
    private String errorCodeDescription;

    @NotNull
    private LocalDateTime effectiveTime;

    @NotNull
    private LocalDateTime createdOn;

    @NotNull
    private Integer institutionId;

    @NotBlank
    private String fileUuid;
}
