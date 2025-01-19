package ar.lamansys.sgh.publicapi.prescription.application.changeprescriptionstatemultiplecommercial;

import ar.lamansys.sgh.publicapi.prescription.application.changeprescriptionstatemultiplecommercial.exception.PrescriptionMedicationBlankFieldException;
import ar.lamansys.sgh.publicapi.prescription.application.changeprescriptionstatemultiplecommercial.exception.PrescriptionMedicationIncorrectPaymentException;
import ar.lamansys.sgh.publicapi.prescription.application.changeprescriptionstatemultiplecommercial.exception.PrescriptionMedicationInvalidSoldUnitsException;
import ar.lamansys.sgh.publicapi.prescription.application.port.out.ChangePrescriptionStateMultipleCommercialPort;
import ar.lamansys.sgh.publicapi.prescription.application.port.out.PrescriptionIdentifier;
import ar.lamansys.sgh.publicapi.prescription.domain.ChangePrescriptionStateMultipleBo;
import ar.lamansys.sgh.publicapi.prescription.domain.ChangePrescriptionStateMultipleMedicationBo;
import ar.lamansys.sgh.publicapi.prescription.domain.DispensedMedicationBo;
import ar.lamansys.sgh.publicapi.prescription.domain.exceptions.PrescriptionIdMatchException;
import ar.lamansys.sgh.publicapi.prescription.domain.exceptions.PrescriptionNotFoundException;
import ar.lamansys.sgh.publicapi.prescription.infrastructure.PrescriptionPublicApiPermissions;
import ar.lamansys.sgh.publicapi.prescription.infrastructure.input.rest.exceptions.PrescriptionDispenseAccessDeniedException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class ChangePrescriptionStateMultipleCommercial {

	@Value("${prescription.domain.number}")
	private int domainNumber;

	private final PrescriptionPublicApiPermissions prescriptionPublicApiPermissions;

	private final ChangePrescriptionStateMultipleCommercialPort changePrescriptionStateMultipleCommercialPort;

	public void run(ChangePrescriptionStateMultipleBo changePrescriptionStateMultipleBo, String pathPrescriptionId) {
		log.debug("Input parameters -> changePrescriptionStateMultipleBo {}, pathPrescriptionId {}", changePrescriptionStateMultipleBo, pathPrescriptionId);
		PrescriptionIdentifier prescriptionIdentifier = PrescriptionIdentifier.parse(pathPrescriptionId);
		assertValidRequest(changePrescriptionStateMultipleBo, prescriptionIdentifier, pathPrescriptionId);
		changePrescriptionStateMultipleCommercialPort.save(changePrescriptionStateMultipleBo, prescriptionIdentifier);
		log.debug("Process finalized");
	}

	private void assertValidRequest(ChangePrescriptionStateMultipleBo changePrescriptionStateMultipleBo, PrescriptionIdentifier prescriptionIdentifier, String pathPrescriptionId) {
		assertPermissions();
		assertPrescriptionData(prescriptionIdentifier, changePrescriptionStateMultipleBo.getPrescriptionId(), pathPrescriptionId);
		assertValidRequestStatus(changePrescriptionStateMultipleBo);
	}

	private void assertValidRequestStatus(ChangePrescriptionStateMultipleBo changePrescriptionStateMultipleBo) {
		List<Integer> emptyFieldsLines = emptyFieldsLines(changePrescriptionStateMultipleBo);
		if (!emptyFieldsLines.isEmpty())
			throw new PrescriptionMedicationBlankFieldException("El/los renglón/es " + emptyFieldsLines.stream().map(String::valueOf).collect(Collectors.joining(",")) + " tiene/n campos vacíos");
		List<Integer> errorsSoldUnits = soldUnitsInvalid(changePrescriptionStateMultipleBo);
		if (!errorsSoldUnits.isEmpty())
			throw new PrescriptionMedicationInvalidSoldUnitsException("El/los renglón/es " + errorsSoldUnits.stream().map(String::valueOf).collect(Collectors.joining(",")) + " no tiene/n unidades vendidas");
		List<Integer> incorrectPayments = incorrectPayments(changePrescriptionStateMultipleBo);
		if (!incorrectPayments.isEmpty())
			throw new PrescriptionMedicationIncorrectPaymentException("En el/los renglón/es " + errorsSoldUnits.stream().map(String::valueOf).collect(Collectors.joining(",")) + " la suma de pagos no coincide con el precio total");
	}

	private List<Integer> incorrectPayments(ChangePrescriptionStateMultipleBo changePrescriptionStateMultipleBo) {
		return changePrescriptionStateMultipleBo.getChangePrescriptionStateLineMedicationList()
				.stream()
				.filter(this::lineHasIncorrectTotalPrice)
				.map(ChangePrescriptionStateMultipleMedicationBo::getPrescriptionLine)
				.collect(Collectors.toList());
	}

	private boolean lineHasIncorrectTotalPrice(ChangePrescriptionStateMultipleMedicationBo line) {
		return line.getDispensedMedicationBos().stream().anyMatch(this::medicationTotalIsIncorrect);
	}

	private boolean medicationTotalIsIncorrect(DispensedMedicationBo medication) {
		return medication.getAffiliatePayment() + medication.getMedicalCoveragePayment() != medication.getPrice();
	}

	private List<Integer> soldUnitsInvalid(ChangePrescriptionStateMultipleBo changePrescriptionStateMultipleBo) {
		return changePrescriptionStateMultipleBo.getChangePrescriptionStateLineMedicationList()
				.stream()
				.filter(this::lineHasMedicationsWIthNoSoldUnits)
				.map(ChangePrescriptionStateMultipleMedicationBo::getPrescriptionLine)
				.collect(Collectors.toList());
	}

	private boolean lineHasMedicationsWIthNoSoldUnits(ChangePrescriptionStateMultipleMedicationBo line) {
		return line.getDispensedMedicationBos().stream().anyMatch(medication -> medication.getSoldUnits() <= 0);
	}

	private List<Integer> emptyFieldsLines(ChangePrescriptionStateMultipleBo changePrescriptionStateMultipleBo) {
		return changePrescriptionStateMultipleBo.getChangePrescriptionStateLineMedicationList()
				.stream()
				.filter(line -> lineHasMedicationsWithEmptyFields(line.getDispensedMedicationBos()))
				.map(ChangePrescriptionStateMultipleMedicationBo::getPrescriptionLine)
				.collect(Collectors.toList());
	}

	private boolean lineHasMedicationsWithEmptyFields(List<DispensedMedicationBo> dispensedMedicationBos) {
		return dispensedMedicationBos.stream().anyMatch(this::medicationHasEmptyFields);
	}

	private boolean medicationHasEmptyFields(DispensedMedicationBo medication) {
		return medication.getBrand().isBlank() || medication.getCommercialPresentation().isBlank() || medication.getCommercialName().isBlank();
	}

	private void assertPrescriptionData(PrescriptionIdentifier prescriptionIdentifier, String prescriptionId, String pathPrescriptionId) {
		if (Integer.parseInt(prescriptionIdentifier.domain) != domainNumber)
			throw new PrescriptionNotFoundException("La receta no existe en el dominio.");
		if (!pathPrescriptionId.equals(prescriptionId))
			throw new PrescriptionIdMatchException("Los id de prescripción no coinciden.");
	}

	private void assertPermissions() {
		if (!prescriptionPublicApiPermissions.canAccess())
			throw new PrescriptionDispenseAccessDeniedException();
	}

}
