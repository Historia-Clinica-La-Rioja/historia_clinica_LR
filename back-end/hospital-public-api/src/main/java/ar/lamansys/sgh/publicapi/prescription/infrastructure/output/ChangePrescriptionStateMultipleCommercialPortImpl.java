package ar.lamansys.sgh.publicapi.prescription.infrastructure.output;

import ar.lamansys.sgh.publicapi.prescription.application.changeprescriptionstatemultiplecommercial.exception.PrescriptionLineCancellationWrongPharmacyNameException;
import ar.lamansys.sgh.publicapi.prescription.application.changeprescriptionstatemultiplecommercial.exception.PrescriptionLineDoesNotExistsException;
import ar.lamansys.sgh.publicapi.prescription.application.changeprescriptionstatemultiplecommercial.exception.PrescriptionLineInvalidStateChangeException;
import ar.lamansys.sgh.publicapi.prescription.application.port.out.ChangePrescriptionStateMultipleCommercialPort;
import ar.lamansys.sgh.publicapi.prescription.application.port.out.PrescriptionIdentifier;
import ar.lamansys.sgh.publicapi.prescription.domain.ChangePrescriptionStateMultipleBo;
import ar.lamansys.sgh.publicapi.prescription.domain.ChangePrescriptionStateMultipleMedicationBo;
import ar.lamansys.sgh.publicapi.prescription.domain.DispensedMedicationBo;
import ar.lamansys.sgh.publicapi.prescription.domain.LineStatusBo;
import ar.lamansys.sgh.publicapi.prescription.domain.PrescriptionValidStatesEnum;
import ar.lamansys.sgh.publicapi.prescription.domain.exceptions.PrescriptionNotFoundException;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class ChangePrescriptionStateMultipleCommercialPortImpl implements ChangePrescriptionStateMultipleCommercialPort {

	private final static Short RECETA = 5;

	private final static Short RECETA_DIGITAL = 14;

	private final EntityManager entityManager;

	private final MedicationStatementCommercialRepository medicationStatementCommercialRepository;

	@Transactional
	@Override
	public void save(ChangePrescriptionStateMultipleBo changePrescriptionStateMultipleBo, PrescriptionIdentifier prescriptionIdentifier) {
		List<Integer> prescriptionLineNumbers = getPrescriptionLineNumbers(changePrescriptionStateMultipleBo);

		List<LineStatusBo> newStatus = getLineStatusBos(changePrescriptionStateMultipleBo);

		List<LineStatusBo> linesStatus = getLineStatus(prescriptionIdentifier.prescriptionId, changePrescriptionStateMultipleBo.getIdentificationNumber());

		assertAllLinesExists(linesStatus, newStatus);

		List<LineStatusBo> actualLinesStatus = linesStatus.stream()
				.filter(ls -> isInNewStatus(ls.getPrescriptionLineNumber(), newStatus))
				.collect(Collectors.toList());

		assertValidStatusChanges(actualLinesStatus, newStatus);

		List<MedicationStatementCommercial> entities = new ArrayList<>();

		changePrescriptionStateMultipleBo.getChangePrescriptionStateLineMedicationList()
				.forEach(line -> entities.addAll(parseLineToMedicationStatementCommercial(line, actualLinesStatus)));

		changeMedicationsStatement(prescriptionLineNumbers, newStatus, prescriptionIdentifier.prescriptionId);

		Set<Integer> medicationStatementIds = entities.stream().map(MedicationStatementCommercial::getMedicationStatementId).collect(Collectors.toSet());
		medicationStatementCommercialRepository.logicalDeleteAllByMedicationStatementIds(medicationStatementIds);
		medicationStatementCommercialRepository.saveAll(entities);
		medicationStatementCommercialRepository.flush();
	}

	private List<LineStatusBo> getLineStatusBos(ChangePrescriptionStateMultipleBo changePrescriptionStateMultipleBo) {
		return changePrescriptionStateMultipleBo.getChangePrescriptionStateLineMedicationList().stream()
				.map(this::getLineStatusBo)
				.collect(Collectors.toList());
	}

	private List<Integer> getPrescriptionLineNumbers(ChangePrescriptionStateMultipleBo changePrescriptionStateMultipleBo) {
		return changePrescriptionStateMultipleBo.getChangePrescriptionStateLineMedicationList().stream()
				.map(ChangePrescriptionStateMultipleMedicationBo::getPrescriptionLine)
				.collect(Collectors.toList());
	}

	private List<MedicationStatementCommercial> parseLineToMedicationStatementCommercial(ChangePrescriptionStateMultipleMedicationBo line, List<LineStatusBo> actualLinesStatus) {
		List<MedicationStatementCommercial> result = new ArrayList<>();
		line.getDispensedMedicationBos().forEach(medication -> result.add(mapTo(medication, line, actualLinesStatus)));
		return result;
	}

	private void changeMedicationsStatement(List<Integer> prescriptionLineNumbers, List<LineStatusBo> newStatus, Integer prescriptionId) {
		String updateQuery = "UPDATE medication_statement " +
				"SET prescription_line_state = :status " +
				"WHERE id = ( " +
				"SELECT ms2.id " +
				"FROM medication_statement ms2 " +
				"JOIN document_medicamention_statement dms ON ms2.id = dms.medication_statement_id " +
				"JOIN document d on d.id = dms.document_id " +
				"JOIN medication_request mr ON mr.id = d.source_id " +
				"WHERE mr.id = :prescriptionId and (d.type_id = " + RECETA + " or d.type_id = " + RECETA_DIGITAL + ") AND ms2.prescription_line_number = :lineNumber " +
				")";
		Query query = entityManager.createNativeQuery(updateQuery);
		for (int i = 0; i < prescriptionLineNumbers.size(); i++) {
			Short stateId = newStatus.get(i).getPrescriptionLineState();
			PrescriptionValidStatesEnum state = PrescriptionValidStatesEnum.map(stateId);
			Short stateToSave = state.equals(PrescriptionValidStatesEnum.CANCELADO_DISPENSA) ? 1 : stateId;
			query.setParameter("status", stateToSave)
					.setParameter("prescriptionId", prescriptionId)
					.setParameter("lineNumber", prescriptionLineNumbers.get(i))
					.executeUpdate();
			entityManager.flush();
			entityManager.clear();
		}
	}

	private MedicationStatementCommercial mapTo(DispensedMedicationBo medication, ChangePrescriptionStateMultipleMedicationBo currentLine, List<LineStatusBo> lineStatusBoList) {
		Integer lineNumberId = lineStatusBoList.stream()
				.filter(line -> line.getPrescriptionLineNumber().equals(currentLine.getPrescriptionLine()))
				.map(LineStatusBo::getMedicationStatementId)
				.findFirst().orElse(-1);
		return new MedicationStatementCommercial(
				lineNumberId,
				medication.getSnomedId(),
				medication.getCommercialName(),
				medication.getCommercialPresentation(),
				medication.getSoldUnits(),
				medication.getBrand(),
				medication.getPrice(),
				medication.getAffiliatePayment(),
				medication.getMedicalCoveragePayment(),
				medication.getPharmacyName(),
				medication.getPharmacistName(),
				medication.getObservations()
		);
	}

	private void assertValidStatusChanges(List<LineStatusBo> linesStatus, List<LineStatusBo> newStatus) {
		boolean valid;
		boolean validPharmacyName = true;
		int i = 0;
		String lastStatusStr = "";
		String lastNewStatusStr = "";
		while (i < linesStatus.size()) {
			LineStatusBo oldS = linesStatus.get(i);
			LineStatusBo newS = findCorrespondingStatus(newStatus, oldS.getPrescriptionLineNumber());
			valid = PrescriptionValidStatesEnum.isValidTransition(oldS.getPrescriptionLineState(), newS.getPrescriptionLineState());
			lastStatusStr = PrescriptionValidStatesEnum.map(oldS.getPrescriptionLineState()).toString();
			lastNewStatusStr = PrescriptionValidStatesEnum.map(newS.getPrescriptionLineState()).toString();
			if (PrescriptionValidStatesEnum.map(newS.getPrescriptionLineState()).equals(PrescriptionValidStatesEnum.CANCELADO_DISPENSA)) {
				Integer id = getMedicationStatementId(linesStatus, newS.getPrescriptionLineNumber());
				if (id != null) {
					List<MedicationStatementCommercial> medications = medicationStatementCommercialRepository.findAllByMedicationStatementId(id);
					if (!medications.isEmpty() && medications.get(0).getPharmacyName() != null && !medications.get(0).getPharmacyName().equals(newS.getPharmacyName()))
						validPharmacyName = false;
				}
			}
			if (!validPharmacyName)
				throw new PrescriptionLineCancellationWrongPharmacyNameException(String.format("Para cancelar el renglón %s se debe mantener el mismo nombre de farmacia", linesStatus.get(i).getPrescriptionLineNumber()), Collections.emptySet());
			if (!valid)
				throw new PrescriptionLineInvalidStateChangeException(String.format("El renglón %s no puede cambiar del estado %s al estado %s", linesStatus.get(i).getPrescriptionLineNumber(), lastStatusStr, lastNewStatusStr), Collections.emptySet());
			i++;
		}
	}

	private Integer getMedicationStatementId(List<LineStatusBo> linesStatus, Integer prescriptionLineNumber) {
		return linesStatus.stream()
				.filter(ls -> ls.getPrescriptionLineNumber().equals(prescriptionLineNumber))
				.map(LineStatusBo::getMedicationStatementId)
				.findFirst().orElse(-1);
	}

	private LineStatusBo findCorrespondingStatus(List<LineStatusBo> newStatus, Integer prescriptionLineNumber) {
		return newStatus.stream()
				.filter(ls -> ls.getPrescriptionLineNumber().equals(prescriptionLineNumber))
				.findFirst().orElse(null);
	}

	private boolean isInNewStatus(Integer prescriptionLineNumber, List<LineStatusBo> newStatus) {
		return newStatus.stream().anyMatch(ns -> ns.getPrescriptionLineNumber().equals(prescriptionLineNumber));
	}

	private void assertAllLinesExists(List<LineStatusBo> linesStatus, List<LineStatusBo> newStatus) {
		Set<Integer> foundLines = linesStatus.stream()
				.map(LineStatusBo::getPrescriptionLineNumber)
				.collect(Collectors.toSet());

		List<Integer> nonExistentLines = newStatus.stream()
				.map(LineStatusBo::getPrescriptionLineNumber)
				.filter(ns -> !foundLines.contains(ns))
				.collect(Collectors.toList());

		if (nonExistentLines.isEmpty())
			return;

		if (nonExistentLines.size() == 1)
			throw new PrescriptionLineDoesNotExistsException("El renglón " + nonExistentLines.get(0) + " no existe en la receta indicada", Collections.emptySet());
		else {
			StringBuilder message = new StringBuilder().append("Los renglones ");
			for (int i = 0; i < nonExistentLines.size(); i++) {
				message.append(nonExistentLines.get(i));
				if(i == nonExistentLines.size() - 2)
					message.append(" y ");
				else if(i < nonExistentLines.size() - 1)
					message.append(", ");
			}
			message.append(" no existen en la receta indicada.");
			throw new PrescriptionLineDoesNotExistsException(message.toString(), Collections.emptySet());
		}
	}

	private List<LineStatusBo> getLineStatus(Integer prescriptionId, String idNumber) throws PrescriptionNotFoundException {
		String getQuery = "select ms.id, ms.prescription_line_number, ms.prescription_line_state, pp.identification_number, msc.pharmacy_name " +
				"from medication_statement ms " +
				"join document_medicamention_statement dms on ms.id = dms.medication_statement_id " +
				"join document d on d.id = dms.document_id " +
				"join medication_request mr on mr.id = d.source_id " +
				"join patient p on p.id = d.patient_id " +
				"join person pp on pp.id = p.person_id " +
				"left join medication_statement_commercial msc on msc.id = ms.id " +
				"where mr.id = :prescriptionId and (d.type_id = " + RECETA + " or d.type_id = " + RECETA_DIGITAL +")";

		List<Object[]> queryResult = entityManager.createNativeQuery(getQuery)
				.setParameter("prescriptionId", prescriptionId)
				.getResultList();

		assertExistsPrescriptionAndDni(queryResult, idNumber);

		return queryResult.stream()
				.map(this::mapTo)
				.collect(Collectors.toList());
	}

	private LineStatusBo mapTo(Object[] line) {
		return LineStatusBo.builder()
				.medicationStatementId((Integer)line[0])
				.prescriptionLineNumber((Integer)line[1])
				.prescriptionLineState((Short)line[2])
				.pharmacyName((String)line[4])
				.build();
	}

	private void assertExistsPrescriptionAndDni(List<Object[]> queryResult, String idNumber) throws PrescriptionNotFoundException {
		if (queryResult.isEmpty() || !queryResult.get(0)[3].toString().equals(idNumber))
			throw new PrescriptionNotFoundException("No se encontró la receta en el dominio");
	}

	private LineStatusBo getLineStatusBo(ChangePrescriptionStateMultipleMedicationBo line) {
		return new LineStatusBo(null, line.getPrescriptionLine(), line.getPrescriptionStateId(), line.getDispensedMedicationBos().get(0).getPharmacyName());
	}

}
