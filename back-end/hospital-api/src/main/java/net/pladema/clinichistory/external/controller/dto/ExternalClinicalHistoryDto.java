package net.pladema.clinichistory.external.controller.dto;

import ar.lamansys.sgx.shared.dates.configuration.JacksonDateFormatConfig;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.annotation.Nullable;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Getter
@Setter
@ToString
public class ExternalClinicalHistoryDto implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = -2406143282745407163L;

    private Integer id;
    @Nullable
    private String professionalSpecialty;

    @NotNull
    @JsonFormat(pattern = JacksonDateFormatConfig.DATE_FORMAT)
    private String consultationDate;

    @Nullable
    private String professionalName;

    @NotNull
    private String notes;

    @Nullable
    private String institution;
}
