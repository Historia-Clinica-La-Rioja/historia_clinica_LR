package net.pladema.reports.repository;

import net.pladema.reports.repository.entity.AnnexIIVo;

import java.util.Optional;

public interface AnnexReportRepository {

    Optional<AnnexIIVo> getAppointmentAnnexInfo(Integer appointmentId);

    Optional<AnnexIIVo> getOutpatientAnnexInfo(Integer outpatientId);
}
