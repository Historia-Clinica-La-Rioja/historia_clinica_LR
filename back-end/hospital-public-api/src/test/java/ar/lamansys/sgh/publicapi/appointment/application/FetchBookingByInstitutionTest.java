package ar.lamansys.sgh.publicapi.appointment.application;

import ar.lamansys.sgh.publicapi.appointment.application.fetchbookingbyinstitution.FetchBookingByInstitution;
import ar.lamansys.sgh.publicapi.appointment.application.fetchbookingbyinstitution.exception.BookingByInstitutionAccessDeniedException;
import ar.lamansys.sgh.publicapi.appointment.infrastructure.input.service.AppointmentPublicApiPermissions;
import ar.lamansys.sgh.shared.infrastructure.input.service.appointment.SharedAppointmentPort;

import ar.lamansys.sgh.shared.infrastructure.input.service.appointment.dto.PublicAppointmentClinicalSpecialty;
import ar.lamansys.sgh.shared.infrastructure.input.service.appointment.dto.PublicAppointmentDoctorDto;
import ar.lamansys.sgh.shared.infrastructure.input.service.appointment.dto.PublicAppointmentInstitution;
import ar.lamansys.sgh.shared.infrastructure.input.service.appointment.dto.PublicAppointmentListDto;

import ar.lamansys.sgh.shared.infrastructure.input.service.appointment.dto.PublicAppointmentMedicalCoverage;
import ar.lamansys.sgh.shared.infrastructure.input.service.appointment.dto.PublicAppointmentPatientDto;

import ar.lamansys.sgh.shared.infrastructure.input.service.appointment.dto.PublicAppointmentPersonDto;

import ar.lamansys.sgh.shared.infrastructure.input.service.appointment.dto.PublicAppointmentStatus;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class FetchBookingByInstitutionTest {

	private static final Integer institutionId = 1;

	@Mock
	private SharedAppointmentPort appointmentPort;

	@Mock
	private AppointmentPublicApiPermissions appointmentPublicApiPermissions;

	private FetchBookingByInstitution fetchBookingByInstitution;

	@BeforeEach
	public void setUp() {
		fetchBookingByInstitution = new FetchBookingByInstitution(appointmentPort, appointmentPublicApiPermissions);
	}

	@Test
	void testRun(){
		PublicAppointmentPersonDto personDto = new PublicAppointmentPersonDto("firstName","lastName","idNumber", (short)1);
		PublicAppointmentPatientDto patientDto = new PublicAppointmentPatientDto(1,personDto);
		PublicAppointmentDoctorDto doctorDto = new PublicAppointmentDoctorDto("123",personDto);
		PublicAppointmentStatus status = new PublicAppointmentStatus((short)1,"description");
		PublicAppointmentMedicalCoverage appointmentMedicalCoverage = new PublicAppointmentMedicalCoverage("cuit","name","affiliateNumber");
		PublicAppointmentInstitution publicAppointmentInstitution = new PublicAppointmentInstitution(1,"cuit","sisa");
		PublicAppointmentClinicalSpecialty publicAppointmentClinicalSpecialty = new PublicAppointmentClinicalSpecialty("sctid", 1, "name");
		List<PublicAppointmentListDto> list = Collections.singletonList(
				new PublicAppointmentListDto(
						institutionId,
						"2024-01-01",
						"13:00:00",
						false,
						"123456",
						patientDto,
						doctorDto,
						status,
						appointmentMedicalCoverage,
						publicAppointmentInstitution,
						publicAppointmentClinicalSpecialty
				)
		);
		when(appointmentPublicApiPermissions.canAccessBookingByInstitution(institutionId)).thenReturn(true);
		when(appointmentPort.fetchAppointments(1,"123456",null, LocalDate.of(2024, 7, 1), LocalDate.of(2024, 7, 31))).thenReturn(list);

		Collection<PublicAppointmentListDto> result = fetchBookingByInstitution.run(1,"123456", LocalDate.of(2024, 7, 1), LocalDate.of(2024, 7, 31));

		assertEquals(list,result);
	}

	@Test
	public void fetchBookingByInstitutionAccessDenied(){
		when(appointmentPublicApiPermissions.canAccessBookingByInstitution(institutionId)).thenReturn(false);
		assertThrows(BookingByInstitutionAccessDeniedException.class, () -> {
			fetchBookingByInstitution.run(institutionId,"idNumber",LocalDate.of(2024, 7, 1), LocalDate.of(2024, 7, 31));
		});
	}

}
