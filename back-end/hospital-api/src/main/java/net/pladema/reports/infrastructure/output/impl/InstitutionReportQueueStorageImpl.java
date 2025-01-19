package net.pladema.reports.infrastructure.output.impl;

import java.time.LocalDateTime;
import java.util.List;

import ar.lamansys.sgx.shared.security.UserInfo;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import lombok.AllArgsConstructor;
import net.pladema.reports.application.ReportInstitutionQueryBo;
import net.pladema.reports.domain.InstitutionReportType;
import net.pladema.reports.domain.InstitutionReportQueueItemBo;
import net.pladema.reports.domain.ReportQueueItemBo;
import net.pladema.reports.infrastructure.output.InstitutionReportQueueStorage;
import net.pladema.reports.infrastructure.output.repository.InstitutionReportQueuedRepository;
import net.pladema.reports.infrastructure.output.repository.ReportQueueRepository;
import net.pladema.reports.infrastructure.output.repository.entity.InstitutionReportQueued;
import net.pladema.reports.infrastructure.output.repository.entity.ReportQueue;

@AllArgsConstructor
public class InstitutionReportQueueStorageImpl implements InstitutionReportQueueStorage {

	private final ReportQueueRepository reportQueueRepository;
	private final InstitutionReportQueuedRepository repository;

	@Override
	public Page<InstitutionReportQueueItemBo> findQueueReports(InstitutionReportType reportType, ReportInstitutionQueryBo reportInstitutionMonthlyQueue, Pageable pageable) {
		var entity = entityFromBo(reportType, reportInstitutionMonthlyQueue);
		Example<InstitutionReportQueued> example = Example.of(entity, ExampleMatcher.matchingAll().withIncludeNullValues().withIgnorePaths("id"));

		Page<InstitutionReportQueued> reports = repository.findAll(example, pageable);

		List<ReportQueue> queueReports = reportQueueRepository.findAllById(reports.map(InstitutionReportQueued::getId));

		return reports.map(
				reportData -> boFromEntity(queueReports.stream().filter(qr -> qr.getId().equals(reportData.getId())).findFirst().orElse(null), reportData)
		);

	}

	@Override
	public InstitutionReportQueueItemBo addReportToQueue(InstitutionReportType reportType, ReportInstitutionQueryBo reportInstitutionMonthly) {
		var entity = entityFromBo(reportType, reportInstitutionMonthly);

		var queueItem = queueInstitutionReport();
		entity.setId(queueItem.getId());
		entity.setReportType(reportType);
		return boFromEntity(queueItem, repository.save(entity));
	}

	private ReportQueue queueInstitutionReport() {
		ReportQueue newReportQueue = new ReportQueue();
		newReportQueue.setCreatedOn(LocalDateTime.now());
		newReportQueue.setCreatedBy(UserInfo.getCurrentAuditor());
		var queueItem = reportQueueRepository.save(newReportQueue);
		return queueItem;
	}

	private static InstitutionReportQueued entityFromBo(
			InstitutionReportType reportType,
			ReportInstitutionQueryBo reportInstitutionMonthlyQueue
	) {
		InstitutionReportQueued e = new InstitutionReportQueued();
		e.setReportType(reportType);
		e.setStartDate(reportInstitutionMonthlyQueue.startDate);
		e.setEndDate(reportInstitutionMonthlyQueue.endDate);
		e.setInstitutionId(reportInstitutionMonthlyQueue.institutionId);
		e.setClinicalSpecialtyId(reportInstitutionMonthlyQueue.clinicalSpecialtyId);
		e.setDoctorId(reportInstitutionMonthlyQueue.doctorId);
		e.setHierarchicalUnitTypeId(reportInstitutionMonthlyQueue.hierarchicalUnitTypeId);
		e.setHierarchicalUnitId(reportInstitutionMonthlyQueue.hierarchicalUnitId);
		e.setIncludeHierarchicalUnitDescendants(reportInstitutionMonthlyQueue.includeHierarchicalUnitDescendants);
		e.setAppointmentStateId(reportInstitutionMonthlyQueue.appointmentStateId);
		return e;
	}

	private static ReportQueueItemBo boFromEntity(
			ReportQueue reportQueue
	) {
		return ReportQueueItemBo.builder()
				.id(reportQueue.getId())
				.createdOn(reportQueue.getCreatedOn())
				.generatedOn(reportQueue.getGeneratedOn())
				.fileId(reportQueue.getFileId())
				.generatedError(reportQueue.getGeneratedError())
				.build();
	}

	private static InstitutionReportQueueItemBo boFromEntity(
			ReportQueue reportQueue,
			InstitutionReportQueued reportInstitutionMonthlyQueue
	) {
		return InstitutionReportQueueItemBo.builder()
				.queued(boFromEntity(reportQueue))
				// filtro
				.startDate(reportInstitutionMonthlyQueue.getStartDate())
				.endDate(reportInstitutionMonthlyQueue.getEndDate())
				.clinicalSpecialtyId(reportInstitutionMonthlyQueue.getClinicalSpecialtyId())
				.doctorId(reportInstitutionMonthlyQueue.getDoctorId())
				.hierarchicalUnitTypeId(reportInstitutionMonthlyQueue.getHierarchicalUnitTypeId())
				.hierarchicalUnitId(reportInstitutionMonthlyQueue.getHierarchicalUnitId())
				.includeHierarchicalUnitDescendants(reportInstitutionMonthlyQueue.isIncludeHierarchicalUnitDescendants())
				.appointmentStateId(reportInstitutionMonthlyQueue.getAppointmentStateId())
				.build();

	}

}
