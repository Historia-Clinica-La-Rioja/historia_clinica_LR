package ar.lamansys.sgh.publicapi.application.fetchappointmentsdatabydni;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import ar.lamansys.sgh.publicapi.application.port.out.AppointmentStorage;
import ar.lamansys.sgh.publicapi.domain.AppointmentsByUserBo;

import java.time.LocalDate;

@Service
public class FetchAppointmentsDataByDni {

	private final Logger logger;
	private final AppointmentStorage appointmentStorage;

	public FetchAppointmentsDataByDni(AppointmentStorage appointmentStorage) {
		this.appointmentStorage = appointmentStorage;
		this.logger = LoggerFactory.getLogger(FetchAppointmentsDataByDni.class);
	}

	public AppointmentsByUserBo run(String identificationNumber, Short identificationTypeId, Short genderId, String birthDate) {
		logger.debug("Input parameters ->  identificationNumber {}", identificationNumber);
		AppointmentsByUserBo result = getFromStorage(identificationNumber, identificationTypeId, genderId, birthDate);
		logger.debug("Output -> {}", result);
		return result;
	}

	private AppointmentsByUserBo getFromStorage(String identificationNumber, Short identificationTypeId, Short genderId, String birthDate) {
		return appointmentStorage.getAppointmentsDataByDni(identificationNumber, identificationTypeId, genderId, birthDate);
	}
}