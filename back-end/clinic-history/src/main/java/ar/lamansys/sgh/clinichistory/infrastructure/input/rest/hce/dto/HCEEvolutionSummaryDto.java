package ar.lamansys.sgh.clinichistory.infrastructure.input.rest.hce.dto;

import ar.lamansys.sgh.shared.infrastructure.input.service.ClinicalSpecialtyDto;
import ar.lamansys.sgx.shared.dates.configuration.JacksonDateFormatConfig;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.Valid;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@ToString
public class HCEEvolutionSummaryDto implements Serializable {

    private Integer consultationId;

    private HCEDocumentDataDto document;

    private ClinicalSpecialtyDto clinicalSpecialty;

    private List<HCEHealthConditionDto> healthConditions;

    @JsonFormat(pattern = JacksonDateFormatConfig.DATE_FORMAT)
    private String startDate;

    private List<HCEReasonDto> reasons;

    private HCEHealthcareProfessionalDto professional;

    private List<@Valid HCEProcedureDto> procedures = new ArrayList<>();

    private String evolutionNote;
}
