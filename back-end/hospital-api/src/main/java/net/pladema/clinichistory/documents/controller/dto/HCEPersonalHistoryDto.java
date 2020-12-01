package net.pladema.clinichistory.documents.controller.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.pladema.sgx.dates.configuration.JacksonDateFormatConfig;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class HCEPersonalHistoryDto extends HCEClinicalTermDto {

    @JsonFormat(pattern = JacksonDateFormatConfig.DATE_FORMAT)
    private String startDate;

    @JsonFormat(pattern = JacksonDateFormatConfig.DATE_FORMAT)
    private String inactivationDate;
}
