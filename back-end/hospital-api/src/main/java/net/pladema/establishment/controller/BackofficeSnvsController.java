package net.pladema.establishment.controller;

import net.pladema.establishment.controller.constraints.validator.permissions.BackofficeSnvsValidator;
import net.pladema.sgx.backoffice.repository.BackofficeRepository;
import net.pladema.sgx.backoffice.rest.AbstractBackofficeController;
import net.pladema.sgx.backoffice.rest.BackofficeQueryAdapter;
import net.pladema.snvs.infrastructure.output.repository.report.SnvsReport;
import net.pladema.snvs.infrastructure.output.repository.report.SnvsReportRepository;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("backoffice/snvs")
public class BackofficeSnvsController extends AbstractBackofficeController<SnvsReport, Integer> {

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
}
