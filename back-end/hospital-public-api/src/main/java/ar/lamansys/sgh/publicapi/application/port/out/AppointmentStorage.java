package ar.lamansys.sgh.publicapi.application.port.out;

import ar.lamansys.sgh.publicapi.domain.AppointmentsByUserBo;

public interface AppointmentStorage {
	AppointmentsByUserBo getAppointmentsDataByDni(String identificationNumber, Short identificationTypeId, Short genderId, String birthDate);
}
