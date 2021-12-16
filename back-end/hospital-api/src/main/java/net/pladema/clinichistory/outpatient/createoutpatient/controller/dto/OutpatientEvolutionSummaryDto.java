package net.pladema.clinichistory.outpatient.createoutpatient.controller.dto;

import ar.lamansys.sgh.shared.infrastructure.input.service.ClinicalSpecialtyDto;
import ar.lamansys.sgx.shared.dates.configuration.JacksonDateFormatConfig;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import net.pladema.staff.controller.dto.HealthcareProfessionalDto;

import javax.validation.Valid;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@ToString
public class OutpatientEvolutionSummaryDto implements Serializable {

    private Integer consultationID;

    private ClinicalSpecialtyDto clinicalSpecialty;

    private List<OutpatientSummaryHealthConditionDto> healthConditions;

    @JsonFormat(pattern = JacksonDateFormatConfig.DATE_FORMAT)
    private String startDate;

    private List<OutpatientReasonDto> reasons;

    private HealthcareProfessionalDto professional;

    private List<@Valid OutpatientProcedureDto> procedures = new ArrayList<>();

    private String evolutionNote;
}
