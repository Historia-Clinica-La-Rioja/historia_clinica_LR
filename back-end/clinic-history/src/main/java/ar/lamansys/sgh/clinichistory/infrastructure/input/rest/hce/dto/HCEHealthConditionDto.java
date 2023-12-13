package ar.lamansys.sgh.clinichistory.infrastructure.input.rest.hce.dto;

import ar.lamansys.sgx.shared.dates.configuration.JacksonDateFormatConfig;
import ar.lamansys.sgx.shared.dates.controller.dto.DateTimeDto;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.annotation.Nullable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class HCEHealthConditionDto extends HCEClinicalTermDto {

    @JsonFormat(pattern = JacksonDateFormatConfig.DATE_FORMAT)
    @Nullable
    private String startDate;

    @JsonFormat(pattern = JacksonDateFormatConfig.DATE_FORMAT)
    @Nullable
    private String inactivationDate;

    @Nullable
    private String severity;

    @Nullable
    private String institutionName;

    @Nullable
    private String professionalName;

    @Nullable
    private DateTimeDto createdOn;

    @Nullable
    private String note;

    private Boolean hasPendingReference;

    @Nullable
    private Boolean canBeMarkAsError;
}
