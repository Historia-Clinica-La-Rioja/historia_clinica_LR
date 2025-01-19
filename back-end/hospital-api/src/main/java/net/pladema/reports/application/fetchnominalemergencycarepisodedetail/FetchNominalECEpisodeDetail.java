package net.pladema.reports.application.fetchnominalemergencycarepisodedetail;

import ar.lamansys.sgx.shared.reports.util.struct.IWorkbook;
import lombok.RequiredArgsConstructor;
import net.pladema.reports.application.fetchnominalemergencycarepisodedetail.exception.NominalECEDetailReportException;
import net.pladema.reports.application.fetchnominalemergencycarepisodedetail.exception.NominalECEDetailReportExceptionEnum;
import net.pladema.reports.application.ports.NominalECEpisodeDetailStorage;
import net.pladema.reports.domain.ReportSearchFilterBo;

import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

@RequiredArgsConstructor
@Service
public class FetchNominalECEpisodeDetail {

	private static final Integer ONE_WEEK = 7;

	private final NominalECEpisodeDetailStorage nominalECEpisodeDetailStorage;

	public IWorkbook run(String title, ReportSearchFilterBo filter) {
		validateDates(filter.getFromDate(), filter.getToDate());
		return nominalECEpisodeDetailStorage.buildNominalECEpisodeDetailExcelReport(title, filter);
	}

	private void validateDates(LocalDate from, LocalDate to) {
		if (ChronoUnit.DAYS.between(from, to) > ONE_WEEK)
			throw new NominalECEDetailReportException(NominalECEDetailReportExceptionEnum.INVALID_DATES, "La fecha ingresada debe contemplar como maximo un lapso de una semana");
	}

}
