package net.pladema.reports.controller;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ar.lamansys.sgh.shared.infrastructure.input.service.datastructures.PageDto;
import ar.lamansys.sgx.shared.dates.configuration.LocalDateMapper;
import ar.lamansys.sgx.shared.dates.configuration.LocalDateMapperImpl;
import ar.lamansys.sgx.shared.dates.utils.DateUtils;
import lombok.extern.slf4j.Slf4j;
import net.pladema.reports.application.ReportInstitutionQueryBo;
import net.pladema.reports.application.institutionreportqueueadd.AddReportInstitutionToQueue;
import net.pladema.reports.application.institutionmonthlyreportqueuefetch.FetchReportInstitutionQueue;
import net.pladema.reports.controller.dto.AddInstitutionReportToQueueDto;
import net.pladema.reports.controller.dto.InstitutionReportQueuedDto;
import net.pladema.reports.controller.dto.InstitutionReportQueryDto;
import net.pladema.reports.controller.dto.ReportQueuedDto;
import net.pladema.reports.domain.InstitutionReportType;
import net.pladema.reports.domain.InstitutionReportQueueItemBo;
import net.pladema.reports.infrastructure.output.InstitutionReportQueueStorage;
import net.pladema.reports.infrastructure.output.impl.InstitutionReportQueueStorageImpl;
import net.pladema.reports.infrastructure.output.repository.InstitutionReportQueuedRepository;
import net.pladema.reports.infrastructure.output.repository.ReportQueueRepository;

@Slf4j
@RestController
@RequestMapping("/institutions/{institutionId}/reports/{reportType}/queue")
public class InstitutionReportQueueController {

	private static LocalDateMapper LOCAL_DATE_MAPPER = new LocalDateMapperImpl();

	private final FetchReportInstitutionQueue fetchIntitutionMonthlyReportQueue;
	private final AddReportInstitutionToQueue addIntitutionMonthlyReportToQueue;

	public InstitutionReportQueueController(
			ReportQueueRepository reportQueueRepository,
			InstitutionReportQueuedRepository repository
	) {
		InstitutionReportQueueStorage storage = new InstitutionReportQueueStorageImpl(
				reportQueueRepository,
				repository
		);
		this.fetchIntitutionMonthlyReportQueue = new FetchReportInstitutionQueue(
				storage
		);
		this.addIntitutionMonthlyReportToQueue = new AddReportInstitutionToQueue(
				storage
		);
	}

    @GetMapping
    public
	PageDto<InstitutionReportQueuedDto> list(
            @PathVariable Integer institutionId,
			@PathVariable InstitutionReportType reportType,
			InstitutionReportQueryDto query
    ) {
		log.debug("Buscando InstitutionReportQueuedDto {}", query);
		ReportInstitutionQueryBo params = ReportInstitutionQueryBo.builder()
				.institutionId(institutionId)
				.startDate(DateUtils.fromStringToLocalDate(query.getStartDate()))
				.endDate(DateUtils.fromStringToLocalDate(query.getEndDate()))
				.clinicalSpecialtyId(query.getClinicalSpecialtyId())
				.doctorId(query.getDoctorId())
				.hierarchicalUnitTypeId(query.getHierarchicalUnitTypeId())
				.hierarchicalUnitId(query.getHierarchicalUnitId())
				.includeHierarchicalUnitDescendants(query.isIncludeHierarchicalUnitDescendants())
				.appointmentStateId(query.getAppointmentStateId())
				.build();

		Pageable pageable = PageRequest.of(
				query.getPageNumber() != null ? query.getPageNumber() : 0,
				query.getPageSize() != null ? query.getPageSize() : 10,
				Sort.by(Sort.Order.desc("id"))
		);
		return PageDto.fromPage(fetchIntitutionMonthlyReportQueue.run(reportType, params, pageable)
				.map(this::toReportInstitutionMonthlyDto));

    }

	@PostMapping
	public InstitutionReportQueuedDto addToQueue(
			@PathVariable Integer institutionId,
			@PathVariable InstitutionReportType reportType,
			@RequestBody AddInstitutionReportToQueueDto reportToQueue
	) {
		log.debug("Agregando AddInstitutionReportToQueueDto {}", reportType);

		ReportInstitutionQueryBo params = newReportInstitutionMonthlyDto(institutionId, reportToQueue);
		var report = addIntitutionMonthlyReportToQueue.run(reportType, params);
		return toReportInstitutionMonthlyDto(report);
	}

	private InstitutionReportQueuedDto toReportInstitutionMonthlyDto(
			InstitutionReportQueueItemBo bo
	) {
		if (bo == null || bo.queued == null) {
			return null;
		}
		return new InstitutionReportQueuedDto(
				bo.queued.id,
				new ReportQueuedDto(
					LOCAL_DATE_MAPPER.toDateTimeDto(bo.queued.createdOn),
					LOCAL_DATE_MAPPER.toDateTimeDto(bo.queued.generatedOn),
						bo.queued.generatedError,
						bo.queued.fileId != null
				),
				LOCAL_DATE_MAPPER.toDateDto(bo.startDate),
				LOCAL_DATE_MAPPER.toDateDto(bo.endDate),
				bo.clinicalSpecialtyId,
				bo.doctorId,
				bo.hierarchicalUnitTypeId,
				bo.hierarchicalUnitId,
				bo.includeHierarchicalUnitDescendants
		);
	}

	private static ReportInstitutionQueryBo newReportInstitutionMonthlyDto(
			Integer institutionId,
			AddInstitutionReportToQueueDto reportToQueue
	) {
		return new ReportInstitutionQueryBo(
				institutionId,
				LOCAL_DATE_MAPPER.fromDateDto(reportToQueue.getStartDate()),
				LOCAL_DATE_MAPPER.fromDateDto(reportToQueue.getEndDate()),
				reportToQueue.getClinicalSpecialtyId(),
				reportToQueue.getDoctorId(),
				reportToQueue.getHierarchicalUnitTypeId(),
				reportToQueue.getHierarchicalUnitId(),
				reportToQueue.isIncludeHierarchicalUnitDescendants(),
				reportToQueue.getAppointmentStateId()
		);
	}

}
