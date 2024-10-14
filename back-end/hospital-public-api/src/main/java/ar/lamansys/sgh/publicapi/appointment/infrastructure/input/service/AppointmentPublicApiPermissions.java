package ar.lamansys.sgh.publicapi.appointment.infrastructure.input.service;

public interface AppointmentPublicApiPermissions {

	boolean canAccessAppointment(Integer institutionId);

	boolean canCancelAppointment(Integer institutionId);

	boolean canAccessBookingByInstitution(Integer institutionId);

	boolean canAccessCancelBookingByInstitution(Integer institutionId);

	boolean canAccessMakeBooking(Integer institutionId);

	boolean canAccessFetchBookingProfessionals(Integer institutionId);

	boolean canAccessFetchAllBookingInstitutions();

	boolean canAccessFetchAllBookingInstitutionsExtended();

	boolean canAccessFetchMedicalCoverages();

	boolean canAccessFetchBookingSpecialties();

	boolean canAccessFetchBookingPracticesBySpecialtyAndHealthInsurance();

	boolean canAccessFetchBookingPracticesByProfessionalAndHealthInsurance();

	boolean canAccessFetchBookingSpecialtiesByProfessional();
}
