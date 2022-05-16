package ar.lamansys.sgh.publicapi.infrastructure.input.rest.appointment;


import java.time.LocalDate;
import java.util.Collection;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import ar.lamansys.sgh.shared.infrastructure.input.service.appointment.SharedAppointmentPort;
import ar.lamansys.sgh.shared.infrastructure.input.service.appointment.dto.PublicAppointmentListDto;
import ar.lamansys.sgh.shared.infrastructure.input.service.institution.SharedInstitutionPort;
import ar.lamansys.sgx.shared.dates.configuration.LocalDateMapper;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/public-api/institution/{sisaCode}/appointment")
public class AppointmentPublicController {
	private final SharedAppointmentPort appointmentPort;
	private final SharedInstitutionPort sharedInstitutionPort;
	private final LocalDateMapper localDateMapper;
	public AppointmentPublicController(SharedAppointmentPort appointmentPort,
									   SharedInstitutionPort sharedInstitutionPort,
									   LocalDateMapper localDateMapper) {
		this.appointmentPort = appointmentPort;
		this.sharedInstitutionPort = sharedInstitutionPort;
		this.localDateMapper = localDateMapper;
	}

	@GetMapping
	@ResponseStatus(HttpStatus.OK)
	public Collection<PublicAppointmentListDto> getList(
			@PathVariable(name = "sisaCode") String sisaCode,
			@RequestParam(name = "identificationNumber", required = false) String identificationNumber,
			@RequestParam(name = "startDate", required = false) String startDateStr,
			@RequestParam(name = "endDate", required = false) String endDateStr
	) {
		LocalDate startDate = localDateMapper.fromStringToLocalDate(startDateStr);
		LocalDate endDate = localDateMapper.fromStringToLocalDate(endDateStr);
		return appointmentPort.fetchAppointments(sisaCode, identificationNumber,
				List.of((short)1, (short)2, (short)3, (short)4,(short)5, (short)7, (short)8), startDate, endDate);
	}

	@PutMapping("/{appointmentId}/cancel")
	public void cancelAppointment(
			@PathVariable(name = "sisaCode") String sisaCode,
			@PathVariable(name = "appointmentId") Integer appointmentId) {
		var institution  = sharedInstitutionPort.fetchInstitutionBySisaCode(sisaCode);
		appointmentPort.cancelAppointment(institution.getId(), appointmentId);
	}
}
