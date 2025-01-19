package ar.lamansys.sgh.clinichistory.infrastructure.input.rest.ips.dto;

import ar.lamansys.sgh.shared.infrastructure.input.service.ProcedureTypeEnum;
import ar.lamansys.sgx.shared.dates.configuration.JacksonDateFormatConfig;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.annotation.Nullable;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class SpecificProcedureDto {

    @Nullable
    private Integer id;

    @NotNull(message = "{value.mandatory}")
    @Valid
    @EqualsAndHashCode.Include
    private SnomedDto snomed;

    @Nullable
    @JsonFormat(pattern = JacksonDateFormatConfig.DATE_FORMAT)
    @EqualsAndHashCode.Include
    private String performedDate;

    @Nullable
    private ProcedureTypeEnum type = ProcedureTypeEnum.PROCEDURE;

    @Nullable
    private Boolean isPrimary = Boolean.TRUE;

    @Nullable
    private String note;
}
