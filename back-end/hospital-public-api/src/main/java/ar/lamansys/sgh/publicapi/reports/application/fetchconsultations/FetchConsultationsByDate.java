package ar.lamansys.sgh.publicapi.reports.application.fetchconsultations;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;

import ar.lamansys.sgh.publicapi.reports.application.fetchconsultations.exceptions.FetchConsultationsAccessDeniedException;
import ar.lamansys.sgh.publicapi.reports.application.fetchconsultations.exceptions.InstitutionNotFoundException;
import ar.lamansys.sgh.publicapi.reports.application.fetchconsultations.exceptions.WrongDateFilterException;
import ar.lamansys.sgh.publicapi.reports.application.port.out.ConsultationsByDateStorage;
import ar.lamansys.sgh.publicapi.reports.domain.fetchconsultationsbydate.ConsultationBo;
import ar.lamansys.sgh.publicapi.reports.infrastructure.input.service.ConsultationsByDatePublicApiPermissions;
import ar.lamansys.sgx.shared.dates.configuration.LocalDateMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@AllArgsConstructor
@Service
public class FetchConsultationsByDate {

	private final ConsultationsByDateStorage consultationsByDateStorage;
	private final ConsultationsByDatePublicApiPermissions consultationsByDatePublicApiPermissions;
	private final LocalDateMapper localDateMapper;

	public List<ConsultationBo> run(String dateFrom, String dateUntil, String refsetCode, Integer hierarchicalUnitId) {

		log.debug("Checking permissions");
		assertUserCanAccess();

		log.debug("Find institutionId from refsetCode {}", refsetCode);
		Integer institutionId = refsetCode != null ? findInstitutionId(refsetCode) : null;

		var localDateFrom = localDateMapper.fromStringToLocalDate(dateFrom);
		var localDateUntil = localDateMapper.fromStringToLocalDate(dateUntil);

		assertDates(localDateFrom, localDateUntil);

		log.debug("Get Consultations from dateFrom {}, dateUntil{}, refsetCode{}, hierarchicalUnitId{}", localDateFrom, localDateUntil, refsetCode, hierarchicalUnitId);
		List<ConsultationBo> result = consultationsByDateStorage.fetchConsultations(localDateFrom, localDateUntil, institutionId, hierarchicalUnitId);
		log.debug("Got consultations Info -> {}", result);
		return result;
	}

	private void assertDates(LocalDate localDateFrom, LocalDate localDateUntil) {
		if(localDateUntil.minusDays(30).isAfter(localDateFrom))
			throw new WrongDateFilterException();
	}

	private void assertUserCanAccess() {
		if(!consultationsByDatePublicApiPermissions.canFetchConsultations()) {
			throw new FetchConsultationsAccessDeniedException();
		}
	}

	private Integer findInstitutionId(String refsetCode) {
		return consultationsByDatePublicApiPermissions.getInstitutionId(refsetCode)
				.orElseThrow(InstitutionNotFoundException::new);
	}

}
