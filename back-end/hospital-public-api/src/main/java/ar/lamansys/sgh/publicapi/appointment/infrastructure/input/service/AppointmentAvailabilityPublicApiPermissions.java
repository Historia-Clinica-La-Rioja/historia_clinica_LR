package ar.lamansys.sgh.publicapi.appointment.infrastructure.input.service;

public interface AppointmentAvailabilityPublicApiPermissions {

	boolean canCheckAvailabilityBySpecialty(Integer institutionId);
}
