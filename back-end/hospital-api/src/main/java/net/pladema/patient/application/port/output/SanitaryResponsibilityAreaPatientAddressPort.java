package net.pladema.patient.application.port.output;

import net.pladema.patient.domain.PatientGlobalCoordinatesBo;
import net.pladema.patient.domain.FetchGlobalCoordinatesSanitaryResponsibilityAreaPatientAddressBo;

import java.util.Optional;

public interface SanitaryResponsibilityAreaPatientAddressPort {

	Optional<FetchGlobalCoordinatesSanitaryResponsibilityAreaPatientAddressBo> getNonCompletePatientAddress();

	PatientGlobalCoordinatesBo fetchPatientGlobalCoordinatesByAddress(FetchGlobalCoordinatesSanitaryResponsibilityAreaPatientAddressBo patientAddress);

	void handlePatientGlobalCoordinates(PatientGlobalCoordinatesBo patientGlobalCoordinates);

}