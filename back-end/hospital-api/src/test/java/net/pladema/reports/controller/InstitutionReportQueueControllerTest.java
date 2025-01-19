package net.pladema.reports.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;

import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.DocumentFileRepository;
import ar.lamansys.sgx.shared.dates.controller.dto.DateDto;
import ar.lamansys.sgx.shared.dates.utils.DateUtils;
import net.pladema.UnitRepository;
import net.pladema.reports.application.reportqueueprocess.GenerateReportFile;
import net.pladema.reports.application.reportqueueprocess.InstitutionExcelReportGeneratorNotFound;
import net.pladema.reports.application.reportqueueprocess.InstitutionReportQueuedNotFound;
import net.pladema.reports.application.reportqueueprocess.ProcessReportQueueJob;
import net.pladema.reports.controller.dto.AddInstitutionReportToQueueDto;
import net.pladema.reports.controller.dto.InstitutionReportQueuedDto;
import net.pladema.reports.controller.dto.InstitutionReportQueryDto;
import net.pladema.reports.controller.dto.ReportQueuedDto;
import net.pladema.reports.domain.InstitutionReportType;
import net.pladema.reports.infrastructure.output.impl.ReportQueueStorageImpl;
import net.pladema.reports.infrastructure.output.repository.InstitutionReportQueuedRepository;
import net.pladema.reports.infrastructure.output.repository.ReportQueueRepository;

@ExtendWith(MockitoExtension.class)
class InstitutionReportQueueControllerTest extends UnitRepository {
	private static final Integer INSTITUTION_ID_DEFAULT = 5;
	private static final Integer OTHER_INSTITUTION_ID = 13;

	private static final String START_DATE_DEFAULT = "2023-12-01";
	private static final String END_DATE_DEFAULT = "2023-12-31";

	private static final String REPORT_NOT_FOUND = "REPORT_NOT_FOUND";
	private InstitutionReportQueueController controller;

	@Autowired
	private InstitutionReportQueuedRepository repository;

	@Autowired
	private ReportQueueRepository reportQueueRepository;

	@Mock
	private GenerateReportFile processQueueReport;

	private ProcessReportQueueJob processIntitutionMonthlyReportQueue;

	@MockBean
	private DocumentFileRepository documentFileRepository;

	@BeforeEach
	void setUp() {
		controller = new InstitutionReportQueueController(
				reportQueueRepository,
				repository
		);
		processIntitutionMonthlyReportQueue = new ProcessReportQueueJob(
				processQueueReport,
				new ReportQueueStorageImpl(reportQueueRepository)
		);


	}

	@Test
	void list() throws InstitutionExcelReportGeneratorNotFound, InstitutionReportQueuedNotFound {
		mockProcessQueueReportWithGeneratorNotFound();

		AddInstitutionReportToQueueDto reportToQueueDto = defaultAddReportInstitutionMonthlyDto();

		assertQueueAndProcess(INSTITUTION_ID_DEFAULT, InstitutionReportType.Monthly, reportToQueueDto, "GENERATOR_NOT_FOUND");
	}

	@Test
	void monthlyReportPaging() {

		AddInstitutionReportToQueueDto reportToQueue = defaultAddReportInstitutionMonthlyDto();

		for (int i = 0; i < 15; i++) {
			queueReport(INSTITUTION_ID_DEFAULT, InstitutionReportType.Monthly, reportToQueue);
		}

		InstitutionReportQueryDto reportToQuery = newReportInstitutionMonthlyQueryDto(reportToQueue);
		reportToQuery.setPageSize(5);

		var page = controller.list(
				INSTITUTION_ID_DEFAULT,
				InstitutionReportType.Monthly,
				reportToQuery
		);

		assertThat(page.getTotalElementsAmount()).isEqualTo(15);
		assertThat(page.getContent()).hasSize(5);
	}

	private void assertQueueAndProcess(Integer institutionId, InstitutionReportType reportType, AddInstitutionReportToQueueDto reportToQueue, String generatedError) {
		getListAssertingSize(institutionId, reportType, reportToQueue, 0);

		var firstReportQueued = queueReport(institutionId, reportType, reportToQueue);

		var listReportsInstitutionMonthlyQueued = getListAssertingSize(institutionId, reportType, reportToQueue, 1);
		assertReportEquals(firstReportQueued, listReportsInstitutionMonthlyQueued.get(0));


		processIntitutionMonthlyReportQueue.run();
		assertThat(processIntitutionMonthlyReportQueue.isEmpty()).isTrue();

		InstitutionReportQueuedDto firstReportProcessed = getListAssertingSize(institutionId, reportType, reportToQueue, 1).get(0);
		assertThat(firstReportProcessed.report.generatedOn).isNotNull();
		assertThat(firstReportProcessed.report.generatedError).isEqualTo(generatedError);


		var secondReportQueued = queueReport(institutionId, reportType, reportToQueue);
		var secondListReportQueued = getListAssertingSize(institutionId, reportType, reportToQueue, 2);
		assertReportEquals(secondReportQueued, secondListReportQueued.get(0));
		assertReportEquals(firstReportProcessed, secondListReportQueued.get(1));

		processIntitutionMonthlyReportQueue.run();
		assertThat(processIntitutionMonthlyReportQueue.isEmpty()).isTrue();

		InstitutionReportQueuedDto secondReportProcessed = getListAssertingSize(institutionId, reportType, reportToQueue, 2).get(0);
		assertThat(secondReportProcessed.report.generatedOn).isNotNull();
		assertThat(secondReportProcessed.report.generatedError).isEqualTo(generatedError);

		var thirdListReportQueued = getListAssertingSize(institutionId, reportType, reportToQueue, 2);
		assertReportEquals(secondReportProcessed, thirdListReportQueued.get(0));
		assertReportEquals(firstReportProcessed, thirdListReportQueued.get(1));
	}

	private void mockProcessQueueReportWithFail() throws InstitutionExcelReportGeneratorNotFound, InstitutionReportQueuedNotFound {
		when(processQueueReport.run(any()))
				.thenThrow(new InstitutionReportQueuedNotFound());
	}

	private void mockProcessQueueReportWithGeneratorNotFound() throws InstitutionExcelReportGeneratorNotFound, InstitutionReportQueuedNotFound {
		when(processQueueReport.run(any()))
				.thenThrow(new InstitutionExcelReportGeneratorNotFound());
	}

	@Test
	void add_and_list() throws InstitutionExcelReportGeneratorNotFound, InstitutionReportQueuedNotFound {
		mockProcessQueueReportWithFail();

		assertQueueAndProcessCombinations(InstitutionReportType.AppointmentNominalDetail);
		assertQueueAndProcessCombinations(InstitutionReportType.Monthly);
	}

	private void assertQueueAndProcessCombinations(InstitutionReportType reportType) {
		assertQueueAndProcessCombinations(reportType, INSTITUTION_ID_DEFAULT, "2023-12-01", "2023-12-31");
		assertQueueAndProcessCombinations(reportType, OTHER_INSTITUTION_ID, "2023-12-01", "2023-12-31");
		assertQueueAndProcessCombinations(reportType, INSTITUTION_ID_DEFAULT, "2023-11-01", "2023-11-30");
		assertQueueAndProcessCombinations(reportType, OTHER_INSTITUTION_ID, "2023-11-01", "2023-12-30");
	}

	private void assertQueueAndProcessCombinations(
			InstitutionReportType reportType,
			Integer institutionId,
			String startDate,
			String endDate
	) {
		assertQueueAndProcessCombinations(
				reportType,
				institutionId,
				startDate,
				endDate,
				44
		);

		assertQueueAndProcessCombinations(
				reportType,
				institutionId,
				startDate,
				endDate,
				null
		);
	}

	private void assertQueueAndProcessCombinations(
			InstitutionReportType reportType,
			Integer institutionId,
			String startDate,
			String endDate,
			Integer clinicalSpecialtyId
	) {
		assertQueueAndProcessCombinations(
				reportType,
				institutionId,
				startDate,
				endDate,
				clinicalSpecialtyId,
				55
		);
		assertQueueAndProcessCombinations(
				reportType,
				institutionId,
				startDate,
				endDate,
				clinicalSpecialtyId,
				null
		);
	}

	private void assertQueueAndProcessCombinations(
			InstitutionReportType reportType,
			Integer institutionId,
			String startDate,
			String endDate,
			Integer clinicalSpecialtyId,
			Integer doctorId
	) {
		assertQueueAndProcessCombinations(
				reportType,
				institutionId,
				startDate,
				endDate,
				clinicalSpecialtyId,
				doctorId,
				66
		);
		assertQueueAndProcessCombinations(
				reportType,
				institutionId,
				startDate,
				endDate,
				clinicalSpecialtyId,
				doctorId,
				null
		);
	}

	private void assertQueueAndProcessCombinations(
			InstitutionReportType reportType,
			Integer institutionId,
			String startDate,
			String endDate,
			Integer clinicalSpecialtyId,
			Integer doctorId,
			Integer hierarchicalUnitTypeId
	) {
		assertQueueAndProcessCombinations(
				reportType,
				institutionId,
				startDate,
				endDate,
				clinicalSpecialtyId,
				doctorId,
				hierarchicalUnitTypeId,
				77
		);
		assertQueueAndProcessCombinations(
				reportType,
				institutionId,
				startDate,
				endDate,
				clinicalSpecialtyId,
				doctorId,
				hierarchicalUnitTypeId,
				null
		);
	}

	private void assertQueueAndProcessCombinations(
			InstitutionReportType reportType,
			Integer institutionId,
			String startDate,
			String endDate,
			Integer clinicalSpecialtyId,
			Integer doctorId,
			Integer hierarchicalUnitTypeId,
			Integer hierarchicalUnitId
	) {
		AddInstitutionReportToQueueDto reportTrue = addInstitutionReportToQueueDto(
				startDate,
				endDate,
				clinicalSpecialtyId,
				doctorId,
				hierarchicalUnitTypeId,
				hierarchicalUnitId,
				true,
				null
		);

		assertQueueAndProcess(institutionId, reportType, reportTrue, REPORT_NOT_FOUND);

		AddInstitutionReportToQueueDto reportFalse = addInstitutionReportToQueueDto(
				startDate,
				endDate,
				clinicalSpecialtyId,
				doctorId,
				hierarchicalUnitTypeId,
				hierarchicalUnitId,
				false,
				null
		);

		assertQueueAndProcess(institutionId, reportType, reportFalse, REPORT_NOT_FOUND);
	}


	private InstitutionReportQueuedDto queueReport(
			Integer institutionId,
			InstitutionReportType reportType,
			AddInstitutionReportToQueueDto reportToQueue
	) {

		var added = controller.addToQueue(
				institutionId,
				reportType,
				reportToQueue
		);
		assertThat(added.startDate).isEqualTo(reportToQueue.getStartDate());
		assertThat(added.endDate).isEqualTo(reportToQueue.getEndDate());
		assertReportWasQueued(added.report);
		assertThat(added.includeHierarchicalUnitDescendants).isEqualTo(reportToQueue.isIncludeHierarchicalUnitDescendants());

		return added;
	}

	private void assertReportWasQueued(ReportQueuedDto report) {
		assertThat(report.createdOn).isNotNull();
		assertThat(report.generatedOn).isNull();
		assertThat(report.generatedError).isNull();
		assertThat(report.existsFile).isFalse();
	}

	private void assertReportEquals(
			InstitutionReportQueuedDto reportQueued,
			InstitutionReportQueuedDto reportFound
	) {
		assertThat(reportFound.id).isEqualTo(reportQueued.id);
		assertThat(reportFound.startDate).isEqualTo(reportQueued.startDate);
		assertThat(reportFound.endDate).isEqualTo(reportQueued.endDate);
		assertThat(reportFound.includeHierarchicalUnitDescendants).isEqualTo(reportQueued.includeHierarchicalUnitDescendants);
		assertReportQueuedEquals(reportQueued.report, reportFound.report);
	}

	private void assertReportQueuedEquals(
			ReportQueuedDto reportQueued,
			ReportQueuedDto reportFound
	) {
		assertThat(reportFound.createdOn).isEqualTo(reportQueued.createdOn);
		assertThat(reportFound.generatedOn).isEqualTo(reportQueued.generatedOn);
		assertThat(reportFound.existsFile).isEqualTo(reportQueued.existsFile);
		assertThat(reportFound.generatedError).isEqualTo(reportQueued.generatedError);
	}

	private List<InstitutionReportQueuedDto> getListAssertingSize(
			Integer institutionId,
			InstitutionReportType reportType,
			AddInstitutionReportToQueueDto reportToQueue,
			Integer expectedSize
		) {
		var list = getList(
				institutionId,
				reportType,
				newReportInstitutionMonthlyQueryDto(reportToQueue)
		);
		assertThat(list).hasSize(expectedSize);
		return list;
	}

	private List<InstitutionReportQueuedDto> getList(
			Integer institutionId,
			InstitutionReportType reportType,
			InstitutionReportQueryDto reportInstitutionMonthlyQuery
	) {
		return controller.list(
				institutionId,
				reportType,
				reportInstitutionMonthlyQuery
		).getContent();
	}

	private static AddInstitutionReportToQueueDto defaultAddReportInstitutionMonthlyDto() {
		return addInstitutionReportToQueueDto(
			START_DATE_DEFAULT,
			END_DATE_DEFAULT,
			null,
			null,
			null,
			null,
			true,
				null
		);
	}

	private static AddInstitutionReportToQueueDto addInstitutionReportToQueueDto(
			String startDate,
			String endDate,
			Integer clinicalSpecialtyId,
			Integer doctorId,
			Integer hierarchicalUnitTypeId,
			Integer hierarchicalUnitId,
			boolean includeHierarchicalUnitDescendants,
			Short appointmentStateId
	) {
		return new AddInstitutionReportToQueueDto(
				fromStringToDateDto(startDate),
				fromStringToDateDto(endDate),
				clinicalSpecialtyId,
				doctorId,
				hierarchicalUnitTypeId,
				hierarchicalUnitId,
				includeHierarchicalUnitDescendants,
				appointmentStateId
		);
	}

	public static DateDto fromStringToDateDto(String dateString) {
		var date = DateUtils.fromStringToLocalDate(dateString);
		return new DateDto(date.getYear(), date.getMonthValue(), date.getDayOfMonth());
	}

	private static InstitutionReportQueryDto newReportInstitutionMonthlyQueryDto(
			AddInstitutionReportToQueueDto reportToQueue
	) {
		var result = new InstitutionReportQueryDto();
		result.setStartDate(reportToQueue.getStartDate().toString());
		result.setEndDate(reportToQueue.getEndDate().toString());
		result.setClinicalSpecialtyId(reportToQueue.getClinicalSpecialtyId());
		result.setDoctorId(reportToQueue.getDoctorId());
		result.setHierarchicalUnitTypeId(reportToQueue.getHierarchicalUnitTypeId());
		result.setHierarchicalUnitId(reportToQueue.getHierarchicalUnitId());
		result.setIncludeHierarchicalUnitDescendants(reportToQueue.isIncludeHierarchicalUnitDescendants());
		return result;
	}
}