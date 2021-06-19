package net.pladema.clinichistory.documents.controller.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import ar.lamansys.sgh.clinichistory.infrastructure.input.rest.ips.dto.SnomedDto;
import ar.lamansys.sgx.shared.dates.configuration.JacksonDateFormatConfig;
import org.springframework.validation.annotation.Validated;

import javax.annotation.Nullable;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Getter
@Setter
@ToString
@Validated
public class HCEImmunizationDto implements Serializable {

    private static final long serialVersionUID = 6092032949244933507L;

    @Nullable
    private Integer id;

    @Nullable
    private String statusId;

    @NotNull(message = "{value.mandatory}")
    @Valid
    private SnomedDto snomed;

    @JsonFormat(pattern = JacksonDateFormatConfig.DATE_FORMAT)
    private String administrationDate;
}
