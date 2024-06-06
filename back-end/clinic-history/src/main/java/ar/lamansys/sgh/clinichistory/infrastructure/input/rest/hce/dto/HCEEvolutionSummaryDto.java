package ar.lamansys.sgh.clinichistory.infrastructure.input.rest.hce.dto;

import ar.lamansys.sgh.shared.infrastructure.input.service.ClinicalSpecialtyDto;
import ar.lamansys.sgx.shared.dates.controller.dto.DateTimeDto;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@ToString
public class HCEEvolutionSummaryDto {

    private Integer consultationId;

    private HCEDocumentDataDto document;

    private ClinicalSpecialtyDto clinicalSpecialty;

    private List<HCEProblemDto> healthConditions;

    private DateTimeDto startDate;

    private List<HCEReasonDto> reasons;

    private HCEHealthcareProfessionalDto professional;

    private List<@Valid HCEProcedureDto> procedures = new ArrayList<>();

    private String evolutionNote;

	private String institutionName;

	private ElectronicJointSignatureProfessionalsDto electronicJointSignatureProfessionals;

}
