package net.pladema.establishment.controller;

import ar.lamansys.sgx.shared.dates.controller.dto.DateDto;
import net.pladema.establishment.controller.constraints.validator.permissions.BackofficeSnvsValidator;
import net.pladema.sgx.backoffice.repository.BackofficeRepository;
import net.pladema.sgx.backoffice.rest.AbstractBackofficeController;
import net.pladema.sgx.backoffice.rest.BackofficeQueryAdapter;
import net.pladema.snvs.application.ports.event.exceptions.SnvsStorageException;
import net.pladema.snvs.application.ports.report.exceptions.ReportPortException;
import net.pladema.snvs.application.reportproblems.RetryReport;
import net.pladema.snvs.application.reportproblems.exceptions.ReportProblemException;
import net.pladema.snvs.domain.event.exceptions.SnvsEventInfoBoException;
import net.pladema.snvs.domain.problem.SnvsProblemBo;
import net.pladema.snvs.domain.problem.exceptions.SnvsProblemBoException;
import net.pladema.snvs.domain.report.SnvsReportBo;
import net.pladema.snvs.infrastructure.input.rest.dto.SnvsReportDto;
import net.pladema.snvs.infrastructure.input.rest.dto.SnvsSnomedDto;
import net.pladema.snvs.infrastructure.output.repository.report.SnvsReport;
import net.pladema.snvs.infrastructure.output.repository.report.SnvsReportRepository;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@RestController
@RequestMapping("backoffice/snvs")
public class BackofficeSnvsController extends AbstractBackofficeController<SnvsReport, Integer> {

	public static final String OUTPUT = "Output -> {}";

	RetryReport retryReport;

	public BackofficeSnvsController(SnvsReportRepository repository, BackofficeSnvsValidator backofficeSnvsValidator) {
		super(
				new BackofficeRepository<>(
						repository,
						new BackofficeQueryAdapter<SnvsReport>() {
							@Override
							public Example<SnvsReport> buildExample(SnvsReport entity) {
								ExampleMatcher matcher = ExampleMatcher
										.matching()
										.withMatcher("id", x -> x.ignoreCase().contains())
										.withMatcher("groupEventId", x -> x.ignoreCase().contains())
										.withMatcher("eventId", x -> x.ignoreCase().contains())
										.withMatcher("patientId", x -> x.ignoreCase().contains())
										.withMatcher("snomedSctid", x -> x.ignoreCase().contains())
										.withMatcher("status", x -> x.ignoreCase().contains())
										.withMatcher("responseCode", x -> x.ignoreCase().contains())
										.withMatcher("professionalId", x -> x.ignoreCase().contains())
										.withMatcher("institutionId", x -> x.ignoreCase().contains())
										.withMatcher("sisaRegisteredId", x -> x.ignoreCase().contains());
								return Example.of(entity, matcher);
							}
						}), backofficeSnvsValidator);
	}


	@PutMapping("/{snvsReportId}/retry-report")
	@ResponseStatus(code = HttpStatus.OK)
	public SnvsReportDto retryReportSnvs(
			@PathVariable(name = "snvsReportId") Integer snvsReportId) throws ReportProblemException,
			SnvsProblemBoException, SnvsEventInfoBoException, SnvsStorageException, ReportPortException {
		SnvsReportBo resultService = retryReport.run(snvsReportId);
		logger.debug(OUTPUT, resultService);
		return buildSnvsReportDto(resultService);
	}

	private SnvsReportDto buildSnvsReportDto(SnvsReportBo snvsReportBo) {
		var result = new SnvsReportDto();
		result.setSisaRegisteredId(snvsReportBo.getSisaRegisteredId());
		result.setEventId(snvsReportBo.getEventId());
		result.setGroupEventId(snvsReportBo.getGroupEventId());
		result.setManualClassificationId(snvsReportBo.getManualClassificationId());
		result.setProblem(buildSnomedDto(snvsReportBo.getProblemBo()));
		result.setLastUpdate(buildDate(snvsReportBo.getLastUpdate()));
		result.setResponseCode(snvsReportBo.getResponseCode());
		result.setStatus(snvsReportBo.getStatus());
		return result;
	}

	private SnvsSnomedDto buildSnomedDto(SnvsProblemBo problemBo) {
		if (problemBo == null)
			return null;
		return new SnvsSnomedDto(problemBo.getSctid(), problemBo.getPt());
	}

	private DateDto buildDate(LocalDate lastUpdate) {
		if (lastUpdate == null)
			return null;
		return new DateDto(lastUpdate.getYear(), lastUpdate.getMonthValue(), lastUpdate.getDayOfMonth());

	}
}
