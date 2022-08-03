package net.pladema.reports.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.transaction.annotation.Transactional;

import net.pladema.reports.repository.entity.FormVAppointmentVo;
import net.pladema.reports.repository.entity.FormVReportDataVo;
import net.pladema.reports.repository.entity.FormVOutpatientVo;

public interface FormReportRepository {

    Optional<FormVAppointmentVo> getAppointmentFormVInfo(Integer appointmentId);

    Optional<FormVOutpatientVo> getConsultationFormVInfo(Long documentId);

	@Transactional(readOnly = true)
	Optional<FormVOutpatientVo> getOdontologyConsultationFormVGeneralInfo(Long documentId);

	List<FormVReportDataVo> getOdontologyConsultationFormVDataInfo(Long documentId);

	List<FormVReportDataVo> getOdontologyConsultationFormVOtherDataInfo(Long documentId);

	Optional<FormVOutpatientVo> getNursingConsultationFormVGeneralInfo(Long documentId);

	Optional<FormVReportDataVo> getNursingConsultationFormVDataInfo(Long documentId);
}
