package net.pladema.emergencycare.application.getallavailabledoctorsofficesbysector;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import net.pladema.emergencycare.application.port.output.EmergencyCareDoctorsOfficeStorage;

import net.pladema.medicalconsultation.doctorsoffice.service.domain.DoctorsOfficeBo;

import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class GetAllAvailableDoctorsOfficesBySector {

	private final EmergencyCareDoctorsOfficeStorage emergencyCareDoctorsOfficeStorage;

	public List<DoctorsOfficeBo> run(Integer sectorId){
		log.debug("Input GetAllAvailableDoctorsOfficesBySector parameters -> sectorId {}", sectorId);
		List<DoctorsOfficeBo> result = emergencyCareDoctorsOfficeStorage.getAllAvailableBySectorId(sectorId);
		log.debug("Output -> result {}", result);
		return result;
	}
}
