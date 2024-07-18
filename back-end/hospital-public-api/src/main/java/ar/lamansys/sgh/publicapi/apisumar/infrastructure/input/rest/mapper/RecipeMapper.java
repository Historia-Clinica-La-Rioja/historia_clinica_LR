package ar.lamansys.sgh.publicapi.apisumar.infrastructure.input.rest.mapper;

import ar.lamansys.sgh.publicapi.apisumar.domain.RecipeDetailBo;
import ar.lamansys.sgh.publicapi.apisumar.infrastructure.input.rest.dto.RecipeDetailDto;
import ar.lamansys.sgx.shared.dates.configuration.LocalDateMapper;

import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class RecipeMapper {

	private final LocalDateMapper localDateMapper;

	public RecipeMapper(LocalDateMapper localDateMapper) {
		this.localDateMapper = localDateMapper;
	}

	public List<RecipeDetailDto> mapToRecipes(List<RecipeDetailBo> recipeDetailBoList) {
		return recipeDetailBoList.stream()
				.map(this::mapToRecipes)
				.collect(Collectors.toList());
	}

	private RecipeDetailDto mapToRecipes(RecipeDetailBo recipeDetailBo) {
		return RecipeDetailDto.builder()
				.institution(recipeDetailBo.getInstitution())
				.operativeUnit(recipeDetailBo.getOperativeUnit())
				.lender(recipeDetailBo.getLender())
				.lenderIdentificationNumber(recipeDetailBo.getLenderIdentificationNumber())
				.attentionDate(recipeDetailBo.getAttentionDate())
				.patientIdentificationNumber(recipeDetailBo.getPatientIdentificationNumber())
				.patientName(recipeDetailBo.getPatientName())
				.patientSex(recipeDetailBo.getPatientSex())
				.patientGender(recipeDetailBo.getPatientGender())
				.patientSelfPerceivedName(recipeDetailBo.getPatientSelfPerceivedName())
				.patientBirthDate(recipeDetailBo.getPatientBirthDate())
				.patientAgeTurn(recipeDetailBo.getPatientAgeTurn())
				.patientAge(recipeDetailBo.getPatientAge())
				.ethnicity(recipeDetailBo.getEthnicity())
				.medicalCoverage(recipeDetailBo.getMedicalCoverage())
				.address(recipeDetailBo.getAddress())
				.location(recipeDetailBo.getLocation())
				.instructionLevel(recipeDetailBo.getInstructionLevel())
				.workSituation(recipeDetailBo.getWorkSituation())
				.medication(recipeDetailBo.getMedication())
				.relatedProblem(recipeDetailBo.getRelatedProblem())
				.evolution(recipeDetailBo.getEvolution())
				.build();
	}
}
