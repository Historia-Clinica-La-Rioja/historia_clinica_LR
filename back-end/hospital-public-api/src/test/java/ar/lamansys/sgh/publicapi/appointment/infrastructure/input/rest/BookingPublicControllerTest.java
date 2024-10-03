package ar.lamansys.sgh.publicapi.appointment.infrastructure.input.rest;

import ar.lamansys.sgh.publicapi.appointment.application.checkmailexists.CheckMailExists;
import ar.lamansys.sgh.publicapi.appointment.application.fetchBookingPracticesBySpecialtyAndHealthInsurance.FetchBookingPracticesBySpecialtyAndHealthInsurance;
import ar.lamansys.sgh.publicapi.appointment.application.fetchallbookinginstitutions.FetchAllBookingInstitutions;
import ar.lamansys.sgh.publicapi.appointment.application.fetchallbookinginstitutionsextended.FetchAllBookingInstitutionsExtended;
import ar.lamansys.sgh.publicapi.appointment.application.fetchbookingpracticesbyprofessionalandhealthinsurance.FetchBookingPracticesByProfessionalAndHealthInsurance;
import ar.lamansys.sgh.publicapi.appointment.application.fetchbookingspecialties.FetchBookingSpecialties;
import ar.lamansys.sgh.publicapi.appointment.application.fetchbookingspecialtiesbyprofessional.FetchBookingSpecialtiesByProfessional;
import ar.lamansys.sgh.publicapi.appointment.application.fetchmedicalcoverages.FetchMedicalCoverages;

import ar.lamansys.sgh.shared.infrastructure.input.service.booking.BookingHealthInsuranceDto;
import ar.lamansys.sgh.shared.infrastructure.input.service.booking.BookingInstitutionDto;
import ar.lamansys.sgh.shared.infrastructure.input.service.booking.BookingInstitutionExtendedDto;
import ar.lamansys.sgh.shared.infrastructure.input.service.booking.BookingSpecialtyDto;
import ar.lamansys.sgh.shared.infrastructure.input.service.booking.PracticeDto;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class BookingPublicControllerTest {

	@Mock
	private FetchAllBookingInstitutions fetchAllBookingInstitutions;

	@Mock
	private FetchAllBookingInstitutionsExtended fetchAllBookingInstitutionsExtended;

	@Mock
	private FetchMedicalCoverages fetchMedicalCoverages;

	@Mock
	private FetchBookingSpecialties fetchBookingSpecialties;

	@Mock
	private FetchBookingPracticesBySpecialtyAndHealthInsurance fetchBookingPracticesBySpecialtyAndHealthInsurance;

	@Mock
	private FetchBookingPracticesByProfessionalAndHealthInsurance fetchBookingPracticesByProfessionalAndHealthInsurance;

	@Mock
	private FetchBookingSpecialtiesByProfessional fetchBookingSpecialtiesByProfessional;

	@Mock
	private CheckMailExists checkMailExists;

	private BookingPublicController bookingPublicController;

	@BeforeEach
	public void setUp() {
		bookingPublicController = new BookingPublicController(fetchAllBookingInstitutions, fetchAllBookingInstitutionsExtended, fetchMedicalCoverages, fetchBookingSpecialties, fetchBookingPracticesBySpecialtyAndHealthInsurance, fetchBookingPracticesByProfessionalAndHealthInsurance, fetchBookingSpecialtiesByProfessional, checkMailExists);
	}

	@Test
	void testGetAllBookingInstitutions() {
		List<BookingInstitutionDto> list = Arrays.asList(
				new BookingInstitutionDto(1, "Institution 1"),
				new BookingInstitutionDto(2, "Institution 2")
		);
		when(fetchAllBookingInstitutions.run()).thenReturn(list);

		List<BookingInstitutionDto> result = bookingPublicController.getAllBookingInstitutions();

		assertEquals(list, result);
	}

	@Test
	void testGetAllBookingInstitutionsExtended() {
		List<BookingInstitutionExtendedDto> list = Arrays.asList(
				new BookingInstitutionExtendedDto(1, "Institution 1", "123", "Dependency 1", "Address 1", "City 1", "Department 1", Arrays.asList("Specialty 1"), Arrays.asList("Alias 1")),
				new BookingInstitutionExtendedDto(2, "Institution 2", "456", "Dependency 2", "Address 2", "City 2", "Department 2", Arrays.asList("Specialty 2"), Arrays.asList("Alias 2"))
		);
		when(fetchAllBookingInstitutionsExtended.run()).thenReturn(list);

		List<BookingInstitutionExtendedDto> result = bookingPublicController.getAllBookingInstitutionsExtended();
		assertEquals(list, result);
	}

	@Test
	void testFetchMedicalCoverages() {
		List<BookingHealthInsuranceDto> list = Arrays.asList(
				new BookingHealthInsuranceDto(1, "Health Insurance 1"),
				new BookingHealthInsuranceDto(2, "Health Insurance 2")
		);
		when(fetchMedicalCoverages.run()).thenReturn(list);

		ResponseEntity<List<BookingHealthInsuranceDto>> result = bookingPublicController.fetchMedicalCoverages();

		assertEquals(list, result.getBody());
		assertEquals(200, result.getStatusCodeValue());
	}

	@Test
	void testGetAllSpecialties() {
		List<BookingSpecialtyDto> list = Arrays.asList(
				new BookingSpecialtyDto(1, "Specialty 1"),
				new BookingSpecialtyDto(2, "Specialty 2")
		);
		when(fetchBookingSpecialties.run()).thenReturn(list);

		ResponseEntity<List<BookingSpecialtyDto>> result = bookingPublicController.getAllSpecialties();

		assertEquals(list, result.getBody());
		assertEquals(200, result.getStatusCodeValue());
	}

	@Test
	void testGetAllPracticesBySpecialtyAndMedicalCoverage() {
		List<PracticeDto> list = Arrays.asList(
				new PracticeDto(1, "Practice 1", true, "Full Coverage", 1001),
				new PracticeDto(2, "Practice 2", false, "No Coverage", 1002)
		);
		when(fetchBookingPracticesBySpecialtyAndHealthInsurance.run(1, 1, true)).thenReturn(list);

		ResponseEntity<List<PracticeDto>> result = bookingPublicController.getAllPracticesBySpecialtyAndMedicalCoverage(1, 1, true);

		assertEquals(list, result.getBody());
		assertEquals(200, result.getStatusCodeValue());
	}

	@Test
	void testGetAllPracticesByProfessionalAndMedicalCoverage() {
		List<PracticeDto> list = Arrays.asList(
				new PracticeDto(1, "Practice 1", true, "Full Coverage", 1001),
				new PracticeDto(2, "Practice 2", false, "No Coverage", 1002)
		);
		when(fetchBookingPracticesByProfessionalAndHealthInsurance.run(1, 1, 1, true)).thenReturn(list);

		ResponseEntity<List<PracticeDto>> result = bookingPublicController.getAllPracticesByProfessionalAndMedicalCoverage(1, 1, 1, true);

		assertEquals(list, result.getBody());
		assertEquals(200, result.getStatusCodeValue());
	}

	@Test
	void testGetSpecialtiesByProfessional() {
		List<BookingSpecialtyDto> list = Arrays.asList(
				new BookingSpecialtyDto(1, "Specialty 1"),
				new BookingSpecialtyDto(2, "Specialty 2")
		);
		when(fetchBookingSpecialtiesByProfessional.run(1)).thenReturn(list);

		ResponseEntity<List<BookingSpecialtyDto>> result = bookingPublicController.getSpecialtiesByProfessional(1);

		assertEquals(list, result.getBody());
		assertEquals(200, result.getStatusCodeValue());
	}
}