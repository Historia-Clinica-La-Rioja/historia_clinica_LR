package net.pladema.reports.repository;

import java.util.Optional;

import net.pladema.reports.repository.entity.AnnexIIAppointmentVo;
import net.pladema.reports.repository.entity.AnnexIIOdontologyVo;
import net.pladema.reports.repository.entity.AnnexIIOutpatientVo;

public interface AnnexReportRepository {

    Optional<AnnexIIAppointmentVo> getAppointmentAnnexInfo(Integer appointmentId);

    Optional<AnnexIIOutpatientVo> getConsultationAnnexInfo(Long documentId);

	Optional<AnnexIIOutpatientVo> getOdontologyConsultationAnnexGeneralInfo(Long documentId);

	Optional<AnnexIIOdontologyVo> getOdontologyConsultationAnnexDataInfo(Long documentId);
}
