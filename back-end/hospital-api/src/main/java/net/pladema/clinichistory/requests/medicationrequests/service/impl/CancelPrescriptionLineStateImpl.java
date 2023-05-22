package net.pladema.clinichistory.requests.medicationrequests.service.impl;

import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.ips.MedicationStatementRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.pladema.clinichistory.requests.medicationrequests.repository.entity.MedicationStatementLineState;
import net.pladema.clinichistory.requests.medicationrequests.service.CancelPrescriptionLineState;

import org.springframework.stereotype.Service;

import javax.validation.ConstraintViolationException;

import java.util.Collections;

@Service
@Slf4j
@AllArgsConstructor
public class CancelPrescriptionLineStateImpl implements CancelPrescriptionLineState {

	private final MedicationStatementRepository medicationStatementRepository;

	@Override
	public void execute(Integer medicationStatementId) {
		log.debug("Input parameters -> medicationStatementId {}", medicationStatementId);
		medicationStatementRepository.findById(medicationStatementId).ifPresent(
				medicationStatement -> {
					if (medicationStatement.getPrescriptionLineState() == MedicationStatementLineState.DISPENSADO_CERRADO
						|| medicationStatement.getPrescriptionLineState() == MedicationStatementLineState.DISPENSADO_PROVISORIO)
						throw new ConstraintViolationException("La receta ya fue dispensada", Collections.emptySet());

					if (medicationStatement.getPrescriptionLineState() == MedicationStatementLineState.CANCELADO_RECETA)
						throw new ConstraintViolationException("Esta receta está anulada", Collections.emptySet());

					medicationStatement.setPrescriptionLineState(MedicationStatementLineState.CANCELADO_RECETA);
					medicationStatementRepository.save(medicationStatement);
				}
		);
	}
}
