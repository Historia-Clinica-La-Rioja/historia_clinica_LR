package net.pladema.reports.repository;

import net.pladema.reports.repository.entity.FormVVo;

import java.util.Optional;

public interface FormReportRepository {

    Optional<FormVVo> getFormVInfo(Integer appointmentId);

}
