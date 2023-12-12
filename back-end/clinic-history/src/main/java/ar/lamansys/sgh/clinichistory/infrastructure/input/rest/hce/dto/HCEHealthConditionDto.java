package ar.lamansys.sgh.clinichistory.infrastructure.input.rest.hce.dto;

import ar.lamansys.sgx.shared.dates.configuration.JacksonDateFormatConfig;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.annotation.Nullable;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class HCEHealthConditionDto extends HCEClinicalTermDto {

    @JsonFormat(pattern = JacksonDateFormatConfig.DATE_FORMAT)
    private String startDate;

    @JsonFormat(pattern = JacksonDateFormatConfig.DATE_FORMAT)
    private String inactivationDate;

    @NotNull(message = "{value.mandatory}")
    private String severity;

    private Boolean hasPendingReference;

    @Nullable
    private Boolean canBeMarkAsError;
}
