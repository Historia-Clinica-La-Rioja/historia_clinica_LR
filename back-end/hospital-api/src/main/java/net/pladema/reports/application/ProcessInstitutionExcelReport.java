package net.pladema.reports.application;

import java.util.Optional;

import net.pladema.reports.application.generators.GenerateEmergencyCareNominalDetailExcelReport;

import net.pladema.reports.application.generators.GenerateImageNetworkProductivityExcelReport;

import org.springframework.stereotype.Service;

import ar.lamansys.sgx.shared.files.FileService;
import ar.lamansys.sgx.shared.files.infrastructure.output.repository.FileInfo;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.pladema.reports.application.generators.InstitutionExcelReportGenerator;
import net.pladema.reports.application.reportqueueprocess.InstitutionExcelReportGeneratorNotFound;
import net.pladema.reports.domain.InstitutionReportType;
import net.pladema.reports.infrastructure.output.repository.entity.InstitutionReportQueued;
import net.pladema.reports.application.generators.GenerateInstitutionMonthlyExcelReport;
import net.pladema.reports.application.generators.GenerateAppointmentNominalDetailExcelReport;

@Slf4j
@AllArgsConstructor
@Service
public class ProcessInstitutionExcelReport {
	private final GenerateInstitutionMonthlyExcelReport generateInstitutionMonthlyExcelReport;
	private final GenerateAppointmentNominalDetailExcelReport generateAppointmentNominalDetailExcelReport;
	private final GenerateEmergencyCareNominalDetailExcelReport generateEmergencyCareNominalDetailExcelReport;
	private final GenerateImageNetworkProductivityExcelReport generateImageNetworkProductivityExcelReport;
	private final FileService fileService;

	public FileInfo run(InstitutionReportQueued reportInstitutionMonthlyQueue) throws InstitutionExcelReportGeneratorNotFound {
		InstitutionExcelReportGenerator institutionReportGenerator = getInstitutionReportGenerator(reportInstitutionMonthlyQueue.getReportType())
				.orElseThrow(InstitutionExcelReportGeneratorNotFound::new);

		var institutionMonthlyReportParams = ReportInstitutionQueryBo.builder()
				.institutionId(reportInstitutionMonthlyQueue.getInstitutionId())
				.startDate(reportInstitutionMonthlyQueue.getStartDate())
				.endDate(reportInstitutionMonthlyQueue.getEndDate())
				.clinicalSpecialtyId(reportInstitutionMonthlyQueue.getClinicalSpecialtyId())
				.doctorId(reportInstitutionMonthlyQueue.getDoctorId())
				.hierarchicalUnitTypeId(reportInstitutionMonthlyQueue.getHierarchicalUnitTypeId())
				.hierarchicalUnitId(reportInstitutionMonthlyQueue.getHierarchicalUnitId())
				.includeHierarchicalUnitDescendants(reportInstitutionMonthlyQueue.isIncludeHierarchicalUnitDescendants())
				.appointmentStateId(reportInstitutionMonthlyQueue.getAppointmentStateId())
				.build();

		var storedFile = institutionReportGenerator.run(institutionMonthlyReportParams);

		var uuid = fileService.createUuid();
		var randomPath = String.format("reports/%s/%s-%s", reportInstitutionMonthlyQueue.getInstitutionId(), uuid, storedFile.filename);

		var path = fileService.buildCompletePath(randomPath);
		return fileService.saveStreamInPath(
				path,
				uuid,
				"REPORT-PROCESSOR",
				true,
				storedFile
		);
	}

	private Optional<InstitutionExcelReportGenerator> getInstitutionReportGenerator(InstitutionReportType reportType) {
		if (InstitutionReportType.Monthly.equals(reportType)) {
			return Optional.of(generateInstitutionMonthlyExcelReport);
		}
		if (InstitutionReportType.AppointmentNominalDetail.equals(reportType)) {
			return Optional.of(generateAppointmentNominalDetailExcelReport);
		}
		if (InstitutionReportType.EmergencyCareNominalDetail.equals(reportType)) {
			return Optional.of(generateEmergencyCareNominalDetailExcelReport);
		}
		if (InstitutionReportType.ImageNetworkProductivity.equals(reportType)) {
			return Optional.of(generateImageNetworkProductivityExcelReport);
		}
		return Optional.empty();
	}

}
