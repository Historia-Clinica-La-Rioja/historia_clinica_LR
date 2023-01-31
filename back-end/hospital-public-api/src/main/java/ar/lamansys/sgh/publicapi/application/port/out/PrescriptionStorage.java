package ar.lamansys.sgh.publicapi.application.port.out;

import java.util.Optional;

import ar.lamansys.sgh.publicapi.domain.prescription.ChangePrescriptionStateBo;
import ar.lamansys.sgh.publicapi.domain.prescription.PrescriptionBo;

public interface PrescriptionStorage {
	Optional<PrescriptionBo> getPrescriptionByIdAndDni(String prescriptionId, String identificationNumber);

	void changePrescriptionState(ChangePrescriptionStateBo changePrescriptionLineStateBo, String prescriptionId, String identificationNumber);

}
