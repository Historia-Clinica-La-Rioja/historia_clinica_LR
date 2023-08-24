package ar.lamansys.sgh.publicapi.prescription.application.port.out;

import java.util.List;
import java.util.Optional;

import ar.lamansys.sgh.publicapi.prescription.domain.ChangePrescriptionStateBo;
import ar.lamansys.sgh.publicapi.prescription.domain.PrescriptionBo;
import ar.lamansys.sgh.publicapi.prescription.domain.PrescriptionsDataBo;

public interface PrescriptionStorage {
	Optional<PrescriptionBo> getPrescriptionByIdAndDni(String prescriptionId, String identificationNumber);

	void changePrescriptionState(ChangePrescriptionStateBo changePrescriptionLineStateBo, String prescriptionId, String identificationNumber);

	Optional<List<PrescriptionsDataBo>> getPrescriptionsDataByDni(String identificationNumber);
}
