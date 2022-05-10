package net.pladema.reports.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.transaction.annotation.Transactional;

import net.pladema.reports.repository.entity.FormVAppointmentVo;
import net.pladema.reports.repository.entity.FormVOdontologyVo;
import net.pladema.reports.repository.entity.FormVOutpatientVo;

public interface FormReportRepository {

    Optional<FormVAppointmentVo> getAppointmentFormVInfo(Integer appointmentId);

    Optional<FormVOutpatientVo> getConsultationFormVInfo(Long documentId);

	@Transactional(readOnly = true)
	Optional<FormVOutpatientVo> getOdontologyConsultationFormVGeneralInfo(Long documentId);

	List<FormVOdontologyVo> getOdontologyConsultationFormVDataInfo(Long documentId);

	List<FormVOdontologyVo> getOdontologyConsultationFormVOtherDataInfo(Long documentId);
}
