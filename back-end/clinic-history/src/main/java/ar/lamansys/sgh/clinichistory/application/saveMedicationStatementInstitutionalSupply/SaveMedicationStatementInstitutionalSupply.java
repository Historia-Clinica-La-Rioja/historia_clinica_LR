package ar.lamansys.sgh.clinichistory.application.saveMedicationStatementInstitutionalSupply;

import ar.lamansys.sgh.clinichistory.application.ports.output.MedicationStatementInstitutionalSupplyPort;
import ar.lamansys.sgh.clinichistory.application.ports.output.MedicationStatementPort;
import ar.lamansys.sgh.clinichistory.application.saveMedicationStatementInstitutionalSupply.exception.SaveMedicationStatementInstitutionalSupplyException;
import ar.lamansys.sgh.clinichistory.domain.ips.SaveMedicationStatementInstitutionalSupplyBo;
import ar.lamansys.sgh.clinichistory.domain.ips.SaveMedicationStatementInstitutionalSupplyMedicationBo;
import ar.lamansys.sgh.clinichistory.domain.ips.enums.ESaveMedicationStatementInstitutionSupplyException;
import ar.lamansys.sgh.clinichistory.domain.ips.services.SnomedService;
import lombok.RequiredArgsConstructor;

import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class SaveMedicationStatementInstitutionalSupply {

	private final SnomedService snomedService;

	private final MedicationStatementPort medicationStatementPort;

	private final MedicationStatementInstitutionalSupplyPort medicationStatementInstitutionalSupplyPort;

	public Integer run(SaveMedicationStatementInstitutionalSupplyBo saveMedicationStatementInstitutionalSupply) {
		log.debug("Input parameters -> saveMedicationStatementInstitutionalSupplyList {}", saveMedicationStatementInstitutionalSupply);
		assertSaveMedicationStatementInstitutionalSupply(saveMedicationStatementInstitutionalSupply);
		setSnomedIds(saveMedicationStatementInstitutionalSupply);
		Integer result = medicationStatementInstitutionalSupplyPort.save(saveMedicationStatementInstitutionalSupply);
		log.debug("Output -> {}", result);
		return result;
	}

	private void assertSaveMedicationStatementInstitutionalSupply(SaveMedicationStatementInstitutionalSupplyBo saveMedicationStatementInstitutionalSupply) {
		assertMedicationStatementValidStateTransition(saveMedicationStatementInstitutionalSupply.getMedicationStatementId());
		assertValidMedicationQuantities(saveMedicationStatementInstitutionalSupply);
	}

	private void assertValidMedicationQuantities(SaveMedicationStatementInstitutionalSupplyBo saveMedicationStatementInstitutionalSupply) {
		Double medicationQuantity = medicationStatementPort.getMedicationStatementQuantityById(saveMedicationStatementInstitutionalSupply.getMedicationStatementId());
		Double quantityReceived = Double.valueOf(saveMedicationStatementInstitutionalSupply.getMedications().stream()
				.map(SaveMedicationStatementInstitutionalSupplyMedicationBo::getQuantity)
				.reduce((short) 0, (a, b) -> (short) (a + b)));
		if (!quantityReceived.equals(medicationQuantity))
			throw new SaveMedicationStatementInstitutionalSupplyException(ESaveMedicationStatementInstitutionSupplyException.INVALID_MEDICATION_QUANTITIES, "Las cantidades dispensadas no coinciden con las recetadas");
	}

	private void setSnomedIds(SaveMedicationStatementInstitutionalSupplyBo saveMedicationStatementInstitutionalSupplyList) {
		saveMedicationStatementInstitutionalSupplyList.getMedications().forEach(this::setMedicationSnomedId);
	}

	private void setMedicationSnomedId(SaveMedicationStatementInstitutionalSupplyMedicationBo medication) {
		int snomedId = snomedService.getSnomedIdBySctidAndDescription(medication.getSctid(), medication.getPt());
		medication.setSnomedId(snomedId);
	}

	private void assertMedicationStatementValidStateTransition(Integer medicationStatementId) {
		short ACTIVE_STATUS_ID = 1;
		Short medicationStatementLineStateId = medicationStatementPort.fetchMedicationStatementLineStateById(medicationStatementId);
		if (!medicationStatementLineStateId.equals(ACTIVE_STATUS_ID))
			throw new SaveMedicationStatementInstitutionalSupplyException(ESaveMedicationStatementInstitutionSupplyException.INVALID_MEDICATION_STATEMENT_STATE, "No puede dispensarse un renglón de la receta no activo");
	}

}
