package net.pladema.reports.infrastructure.output.repository.backoffice;

import static net.pladema.sgx.NewStoreBuilder.fromJpa;
import static net.pladema.sgx.backoffice.permissions.NewBackofficePermissionBuilder.permitOnly;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import net.pladema.reports.infrastructure.output.repository.ReportQueueRepository;
import net.pladema.reports.infrastructure.output.repository.entity.ReportQueue;
import net.pladema.sgx.backoffice.BOMethod;
import net.pladema.sgx.backoffice.rest.BackofficeEntityValidatorAdapter;
import net.pladema.sgx.backoffice.rest.NewAbstractBackofficeController;

@RestController
@RequestMapping("backoffice/report-queue")
public class BackofficeReportQueueController extends NewAbstractBackofficeController<ReportQueue, Integer> {

	public BackofficeReportQueueController(ReportQueueRepository reportQueueRepository) {
		super(
				fromJpa(reportQueueRepository),
				permitOnly(BOMethod.GET_ONE, BOMethod.GET_LIST),
				new BackofficeEntityValidatorAdapter<>()
		);
	}

}
