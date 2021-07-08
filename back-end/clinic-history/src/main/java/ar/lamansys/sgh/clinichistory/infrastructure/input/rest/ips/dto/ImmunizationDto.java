package ar.lamansys.sgh.clinichistory.infrastructure.input.rest.ips.dto;

import ar.lamansys.sgx.shared.dates.configuration.JacksonDateFormatConfig;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.validation.annotation.Validated;

import javax.annotation.Nullable;

@Getter
@Setter
@ToString
@Validated
public class ImmunizationDto extends ClinicalTermDto {

    @JsonFormat(pattern = JacksonDateFormatConfig.DATE_FORMAT)
    private String administrationDate;

    private String note;

    @Nullable
    private Integer institutionId;

    @Nullable
    private Short doseId;

    @Nullable
    private Short conditionId;

    @Nullable
    private Short schemeId;

    @Nullable
    private String batchNumber;

}
