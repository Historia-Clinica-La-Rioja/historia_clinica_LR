package net.pladema.patient.application.sanitaryresponsibilityareacompletepatientglobalcoordinates;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import net.javacrumbs.shedlock.core.SchedulerLock;

import net.pladema.patient.application.port.output.SanitaryResponsibilityAreaPatientAddressPort;
import net.pladema.patient.domain.FetchGlobalCoordinatesSanitaryResponsibilityAreaPatientAddressBo;

import net.pladema.patient.domain.PatientGlobalCoordinatesBo;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
public class SanitaryResponsibilityAreaCompletePatientGlobalCoordinates {

	private final SanitaryResponsibilityAreaPatientAddressPort sanitaryResponsibilityAreaPatientAddressPort;

	@Scheduled(cron = "*/10 * * ? * *")
	@SchedulerLock(name = "SanitaryResponsibilityAreaCompletePatientGlobalCoordinates")
	public void run() {
		log.debug("Complete patient global coordinates");
		Optional<FetchGlobalCoordinatesSanitaryResponsibilityAreaPatientAddressBo> patientAddress = sanitaryResponsibilityAreaPatientAddressPort.getNonCompletePatientAddress();
		patientAddress.ifPresent(this::fetchAndSavePatientGlobalCoordinates);
	}

	private void fetchAndSavePatientGlobalCoordinates(FetchGlobalCoordinatesSanitaryResponsibilityAreaPatientAddressBo patientAddress) {
		log.debug("PatientId -> {}", patientAddress.getPatientId());
		PatientGlobalCoordinatesBo patientGlobalCoordinates = sanitaryResponsibilityAreaPatientAddressPort.fetchPatientGlobalCoordinatesByAddress(patientAddress);
		sanitaryResponsibilityAreaPatientAddressPort.handlePatientGlobalCoordinates(patientGlobalCoordinates);
	}

}
