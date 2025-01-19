package ar.lamansys.sgh.publicapi.prescription.application.port.out;

import ar.lamansys.sgh.publicapi.prescription.domain.ChangePrescriptionStateMultipleBo;

public interface ChangePrescriptionStateMultipleCommercialPort {

	void save(ChangePrescriptionStateMultipleBo changePrescriptionStateMultipleBo, PrescriptionIdentifier prescriptionIdentifier);

}
