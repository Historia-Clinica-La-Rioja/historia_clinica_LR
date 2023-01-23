package ar.lamansys.sgh.publicapi.application.port.out;

import ar.lamansys.sgh.publicapi.domain.exceptions.PrescriptionBoException;
import ar.lamansys.sgh.publicapi.domain.prescription.PrescriptionBo;

import java.util.Optional;

public interface PrescriptionStorage {
	Optional<PrescriptionBo> getPrescriptionByIdAndDni(String prescriptionId, String identificationNumber);

}
