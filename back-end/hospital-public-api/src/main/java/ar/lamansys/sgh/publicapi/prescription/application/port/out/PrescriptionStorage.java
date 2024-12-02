package ar.lamansys.sgh.publicapi.prescription.application.port.out;

import java.util.List;
import java.util.Optional;

import ar.lamansys.sgh.publicapi.prescription.domain.ChangePrescriptionStateBo;
import ar.lamansys.sgh.publicapi.prescription.domain.PrescriptionV2Bo;
import ar.lamansys.sgh.publicapi.prescription.domain.PrescriptionBo;
import ar.lamansys.sgh.publicapi.prescription.domain.PrescriptionsDataBo;
import ar.lamansys.sgh.publicapi.prescription.domain.exceptions.PrescriptionNotFoundException;

public interface PrescriptionStorage {
	Optional<PrescriptionBo> getPrescriptionByIdAndDni(PrescriptionIdentifier prescriptionIdentifier, String identificationNumber);

	void changePrescriptionState(ChangePrescriptionStateBo changePrescriptionLineStateBo, PrescriptionIdentifier prescriptionIdentifier, String identificationNumber) throws PrescriptionNotFoundException;

	Optional<List<PrescriptionsDataBo>> getPrescriptionsDataByDni(String identificationNumber);

	Optional<PrescriptionV2Bo> getPrescriptionByIdAndDniV2(PrescriptionIdentifier prescriptionIdentifier, String identificationNumber);

}
