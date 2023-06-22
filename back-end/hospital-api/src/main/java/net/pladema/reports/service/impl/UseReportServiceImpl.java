package net.pladema.reports.service.impl;

import ar.lamansys.sgx.shared.restclient.configuration.resttemplate.exception.RestTemplateApiException;
import net.pladema.report.application.savereport.SaveReport;
import net.pladema.reports.repository.UseReportRepository;
import net.pladema.report.service.UseReportService;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class UseReportServiceImpl implements UseReportService {

	private final UseReportRepository useReportRepository;

	private final SaveReport saveReport;

	@Value("${prescription.domain.number}")
	private Integer domain;

	public UseReportServiceImpl(UseReportRepository useReportRepository, SaveReport saveReport) {
		this.useReportRepository = useReportRepository;
		this.saveReport = saveReport;
	}

	@Override
	public void execute() {
		try {
			saveReport.run(useReportRepository.getImplementedInstitutions(), "/institutional/institution");
			saveReport.run(useReportRepository.getUserSpecialty(domain), "/domain/specialty/user_specialty");
			saveReport.run(useReportRepository.getInstitutionSpecialty(), "/institutional/institution_specialty");
			saveReport.run(useReportRepository.getInstitutionConsultation("2023-01-20"), "/institutional/institution_consultation");
			saveReport.run(useReportRepository.getGivenAppointment("2023-01-15"), "/institutional/given_appointment");
			saveReport.run(useReportRepository.getConfirmedOrAttendedAppointment("2023-01-20"), "/institutional/confirmed_or_attended_appointment");
		} catch (RestTemplateApiException e) {
			throw new RuntimeException(e);
		}
	}

}
