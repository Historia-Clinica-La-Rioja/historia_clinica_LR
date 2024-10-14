package ar.lamansys.sgh.publicapi.appointment.infrastructure.input.rest;

import ar.lamansys.sgh.publicapi.appointment.application.fetchBookingProfessionals.FetchBookingProfessionalsByInstitution;

import ar.lamansys.sgh.shared.infrastructure.input.service.booking.BookingProfessionalDto;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class BookingProfessionalPublicControllerTest {

	private static final Integer institutionId = 1;
	private static final Integer medicalCoverageId = 1;
	private static final boolean all = true;

	@Mock
	private FetchBookingProfessionalsByInstitution fetchBookingProfessionalsByInstitution;

	private BookingProfessionalPublicController bookingProfessionalPublicController;

	@BeforeEach
	public void setUp() {
		bookingProfessionalPublicController = new BookingProfessionalPublicController(fetchBookingProfessionalsByInstitution);
	}

	@Test
	void testGetAllBookingProfessionals() {
		List<BookingProfessionalDto> list = Collections.emptyList();
		when(fetchBookingProfessionalsByInstitution.run(institutionId, medicalCoverageId, all)).thenReturn(list);

		ResponseEntity<List<BookingProfessionalDto>> result = bookingProfessionalPublicController.getAllBookingProfessionals(institutionId, medicalCoverageId, all);

		assertEquals(list, result.getBody());
		assertEquals(200, result.getStatusCodeValue());
	}

}
