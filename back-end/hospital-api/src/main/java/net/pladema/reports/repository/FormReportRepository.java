package net.pladema.reports.repository;

import net.pladema.reports.repository.entity.FormVAppointmentVo;
import net.pladema.reports.repository.entity.FormVOutpatientVo;

import java.util.Optional;

public interface FormReportRepository {

    Optional<FormVAppointmentVo> getAppointmentFormVInfo(Integer appointmentId);

    Optional<FormVOutpatientVo> getConsultationFormVInfo(Long documentId);
}
