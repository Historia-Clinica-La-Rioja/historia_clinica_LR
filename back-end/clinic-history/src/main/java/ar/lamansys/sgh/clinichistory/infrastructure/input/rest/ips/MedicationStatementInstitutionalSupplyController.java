package ar.lamansys.sgh.clinichistory.infrastructure.input.rest.ips;

import ar.lamansys.sgh.clinichistory.application.saveMedicationStatementInstitutionalSupply.SaveMedicationStatementInstitutionalSupply;
import ar.lamansys.sgh.clinichistory.domain.ips.SaveMedicationStatementInstitutionalSupplyBo;
import ar.lamansys.sgh.clinichistory.infrastructure.input.rest.ips.dto.SaveMedicationStatementInstitutionalSupplyDto;
import ar.lamansys.sgh.clinichistory.infrastructure.input.rest.ips.mapper.MedicationStatementInstitutionalSupplyMapper;
import lombok.RequiredArgsConstructor;

import lombok.extern.slf4j.Slf4j;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/institution/{institutionId}/medication-statement-institutional-supply")
@RestController
public class MedicationStatementInstitutionalSupplyController {

	private final MedicationStatementInstitutionalSupplyMapper medicationStatementInstitutionalSupplyMapper;

	private final SaveMedicationStatementInstitutionalSupply saveMedicationStatementInstitutionalSupply;

	@PostMapping("/save")
	@PreAuthorize("hasPermission(#institutionId, 'PERSONAL_DE_FARMACIA')")
	public Integer saveMedicationStatementInstitutionalSupply(@PathVariable("institutionId") Integer institutionId,
															  @RequestBody SaveMedicationStatementInstitutionalSupplyDto saveMedicationStatementInstitutionalSupplyDto) {
		log.debug("Input parameters -> institutionId {}, saveMedicationStatementInstitutionalSupplyDto {}", institutionId, saveMedicationStatementInstitutionalSupplyDto);
		SaveMedicationStatementInstitutionalSupplyBo supplyBo = medicationStatementInstitutionalSupplyMapper.fromSaveMedicationStatementInstitutionalSupplyDto(institutionId, saveMedicationStatementInstitutionalSupplyDto);
		Integer result = saveMedicationStatementInstitutionalSupply.run(supplyBo);
		log.debug("Output -> {}", result);
		return result;
	}

}
