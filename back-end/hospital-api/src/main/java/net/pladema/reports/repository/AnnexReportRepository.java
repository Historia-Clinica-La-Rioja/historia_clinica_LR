package net.pladema.reports.repository;

import net.pladema.reports.repository.entity.AnnexIIAppointmentVo;
import net.pladema.reports.repository.entity.AnnexIIOutpatientVo;

import java.util.Optional;

public interface AnnexReportRepository {

    Optional<AnnexIIAppointmentVo> getAppointmentAnnexInfo(Integer appointmentId);

    Optional<AnnexIIOutpatientVo> getConsultationAnnexInfo(Long documentId);
}
