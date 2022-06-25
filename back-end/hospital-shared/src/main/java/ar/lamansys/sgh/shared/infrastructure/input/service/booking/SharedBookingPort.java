package ar.lamansys.sgh.shared.infrastructure.input.service.booking;


import java.util.List;

public interface SharedBookingPort {

	String makeBooking(BookingDto bookingDto);

	void cancelBooking(String uuid);

	List<BookingInstitutionDto> fetchAllBookingInstitutions();

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

	List<ProfessionalAvailabilityDto> fetchAvailabilityByPractice(Integer institutionId,  Integer clinicalSpecialtyId, Integer practiceId, Integer medicalCoverageId);

}
