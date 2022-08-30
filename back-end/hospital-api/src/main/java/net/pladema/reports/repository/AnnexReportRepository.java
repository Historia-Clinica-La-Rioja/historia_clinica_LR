package net.pladema.reports.repository;

import java.util.List;
import java.util.Optional;

import net.pladema.reports.repository.entity.AnnexIIAppointmentVo;
import net.pladema.reports.repository.entity.AnnexIIOdontologyDataVo;
import net.pladema.reports.repository.entity.AnnexIIOdontologyVo;
import net.pladema.reports.repository.entity.AnnexIIOutpatientVo;
import net.pladema.reports.repository.entity.AnnexIIReportDataVo;

public interface AnnexReportRepository {

    Optional<AnnexIIAppointmentVo> getAppointmentAnnexInfo(Integer appointmentId);

    Optional<AnnexIIOutpatientVo> getConsultationAnnexInfo(Long documentId);

	Optional<AnnexIIOutpatientVo> getOdontologyConsultationAnnexGeneralInfo(Long documentId);

	Optional<AnnexIIOdontologyVo> getOdontologyConsultationAnnexSpecialityAndHasProcedures(Long documentId);

	List<AnnexIIOdontologyDataVo> getOdontologyConsultationAnnexDataInfo(Long documentId);

	List<AnnexIIOdontologyDataVo> getOdontologyConsultationAnnexOtherDataInfo(Long documentId);

	Optional<AnnexIIOutpatientVo> getNursingConsultationAnnexGeneralInfo(Long documentId);

	Optional<AnnexIIReportDataVo> getNursingConsultationAnnexDataInfo(Long documentId);
}
