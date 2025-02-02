package net.pladema.clinichistory.requests.servicerequests.controller.mapper;

import ar.lamansys.sgh.clinichistory.domain.ips.StudyOrderReportInfoBo;
import ar.lamansys.sgh.clinichistory.infrastructure.input.rest.hce.dto.HCEDocumentDataDto;
import ar.lamansys.sgx.shared.dates.configuration.LocalDateMapper;
import lombok.extern.slf4j.Slf4j;
import net.pladema.clinichistory.requests.medicationrequests.controller.dto.DoctorInfoDto;
import net.pladema.clinichistory.requests.servicerequests.controller.dto.StudyOrderReportInfoDto;
import net.pladema.staff.controller.dto.ProfessionalDto;
import org.mapstruct.Named;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class StudyOrderReportInfoMapper {

    @Autowired
    private LocalDateMapper localDateMapper;

    @Named("parseToDto")
    public StudyOrderReportInfoDto parseToDto(StudyOrderReportInfoBo studyOrderReportInfoBo, ProfessionalDto professionalDto) {
        log.trace("Input -> StudyOrderReportInfoBo {}", studyOrderReportInfoBo);
        StudyOrderReportInfoDto result = new StudyOrderReportInfoDto();

        result.setStatus(studyOrderReportInfoBo.getStatus());
        result.setDoctor(DoctorInfoDto.from(professionalDto));
        result.setCreationDate(studyOrderReportInfoBo.getCreationDate());
        result.setImageId(studyOrderReportInfoBo.getImageId());
        result.setSnomed(studyOrderReportInfoBo.getSnomed());
        result.setHealthCondition(studyOrderReportInfoBo.getHealthCondition());
        result.setSource(studyOrderReportInfoBo.getSource());

        if (studyOrderReportInfoBo.getDocumentId() != null) {
            result.setHceDocumentDataDto(new HCEDocumentDataDto(studyOrderReportInfoBo.getDocumentId().longValue(), studyOrderReportInfoBo.getFileName()));
            result.setViewReport(true);
        }

        result.setIsAvailableInPACS(studyOrderReportInfoBo.getIsAvailableInPACS());
        result.setServiceRequestId(studyOrderReportInfoBo.getServiceRequestId());
        result.setDiagnosticReportId(studyOrderReportInfoBo.getDiagnosticReportId());
		result.setHasActiveAppointment(studyOrderReportInfoBo.getHasActiveAppointment());
        result.setObservationsFromServiceRequest(studyOrderReportInfoBo.getObservationsFromServiceRequest());
        result.setAppointmentDate(localDateMapper.toDateDto(studyOrderReportInfoBo.getAppointmentDate()));
        result.setAppointmentHour(localDateMapper.toTimeDto(studyOrderReportInfoBo.getAppointmentHour()));
        result.setLocalViewerUrl(studyOrderReportInfoBo.getLocalViewerUrl());
		result.setReportStatus(studyOrderReportInfoBo.getReportStatusId());
		result.setDeriveTo(studyOrderReportInfoBo.getDeriveToInstitution());
        log.trace("Output: {}", result);
        return result;
    }
}
