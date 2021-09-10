package net.pladema.reports.repository;

import net.pladema.reports.repository.entity.FormVVo;

import java.util.Optional;

public interface FormReportRepository {

    Optional<FormVVo> getAppointmentFormVInfo(Integer appointmentId);

    Optional<FormVVo> getConsultationFormVInfo(Long documentId);
}
