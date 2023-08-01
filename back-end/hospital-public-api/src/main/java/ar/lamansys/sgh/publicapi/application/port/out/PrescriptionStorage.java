package ar.lamansys.sgh.publicapi.application.port.out;

import java.util.List;
import java.util.Optional;

import ar.lamansys.sgh.publicapi.domain.prescription.ChangePrescriptionStateBo;
import ar.lamansys.sgh.publicapi.domain.prescription.PrescriptionBo;
import ar.lamansys.sgh.publicapi.domain.prescription.PrescriptionsDataBo;

public interface PrescriptionStorage {
	Optional<PrescriptionBo> getPrescriptionByIdAndDni(String prescriptionId, String identificationNumber);

	void changePrescriptionState(ChangePrescriptionStateBo changePrescriptionLineStateBo, String prescriptionId, String identificationNumber);

	Optional<List<PrescriptionsDataBo>> getPrescriptionsDataByDni(String identificationNumber);
}
