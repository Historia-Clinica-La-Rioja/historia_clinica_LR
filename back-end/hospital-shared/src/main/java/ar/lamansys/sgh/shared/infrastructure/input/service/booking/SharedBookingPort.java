package ar.lamansys.sgh.shared.infrastructure.input.service.booking;


import ar.lamansys.sgh.shared.infrastructure.input.service.appointment.exceptions.BookingPersonMailNotExistsException;
import ar.lamansys.sgh.shared.infrastructure.input.service.appointment.exceptions.ProfessionalAlreadyBookedException;
import ar.lamansys.sgh.shared.infrastructure.input.service.appointment.exceptions.SaveExternalBookingException;

import java.util.List;

public interface SharedBookingPort {

	SavedBookingAppointmentDto makeBooking(BookingDto bookingDto, boolean onlineBooking) throws ProfessionalAlreadyBookedException, BookingPersonMailNotExistsException, SaveExternalBookingException;

	void cancelBooking(String uuid);

	List<BookingInstitutionDto> fetchAllBookingInstitutions();

	List<BookingInstitutionExtendedDto> fetchAllBookingInstitutionsExtended();

	List<BookingHealthInsuranceDto> fetchAllMedicalCoverages();
	List<PracticeDto> fetchPracticesBySpecialtyAndHealthInsurance(Integer clinicalSpecialtyId,
																  Integer medicalCoverageId, boolean all);

	List<PracticeDto> fetchPracticesByProfessionalAndHealthInsurance(Integer healthcareProfessionalId,
																	 Integer medicalCoverageId,
																	 Integer clinicalSpecialtyId, boolean all);
	List<BookingSpecialtyDto> fetchSpecialties();
	List<BookingSpecialtyDto> fetchSpecialtiesByProfessional(Integer healthcareProfessionalId);

	List<BookingProfessionalDto> fetchBookingProfessionals(Integer institutionId, Integer medicalCoverageId, boolean all);

	ProfessionalAvailabilityDto fetchAvailabilityByPracticeAndProfessional(
			Integer institutionId,
			Integer professionalId,
			Integer clinicalSpecialtyId,
			Integer practiceId
	);

	ProfessionalAvailabilityDto fetchAvailabilityByPracticeAndProfessional(
			Integer institutionId,
			Integer professionalId,
			Integer clinicalSpecialtyId,
			Integer practiceId,
			Integer coverageId,
			String maxDate
	);

	List<ProfessionalAvailabilityDto> fetchAvailabilityByPractice(Integer institutionId,  Integer clinicalSpecialtyId, Integer practiceId, Integer medicalCoverageId);

}
