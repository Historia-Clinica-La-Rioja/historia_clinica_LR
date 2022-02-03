package net.pladema.clinichistory.requests.servicerequests.controller.mapper;

import ar.lamansys.sgh.clinichistory.domain.document.PatientInfoBo;
import ar.lamansys.sgh.shared.infrastructure.input.service.BasicPatientDto;
import net.pladema.clinichistory.requests.controller.dto.PrescriptionItemDto;
import net.pladema.clinichistory.requests.servicerequests.service.domain.ServiceRequestBo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.List;


@Component
public class CreateServiceRequestMapper {
    private static final Logger LOG = LoggerFactory.getLogger(CreateServiceRequestMapper.class);
    private static final String OUTPUT = "OUTPUT -> {}";

    public ServiceRequestBo parseTo(StudyMapper studyMapper, Integer doctorId, BasicPatientDto patientDto, String categoryId, Integer medicalCoverageId, List<PrescriptionItemDto> studies){
        LOG.debug("parseTo -> doctorId {}, patientDto {}, medicalCoverageId {}, studies {} ", doctorId, patientDto, medicalCoverageId, studies);
        ServiceRequestBo result = new ServiceRequestBo();
        result.setCategoryId(categoryId);
        result.setPatientInfo(new PatientInfoBo(patientDto.getId(), patientDto.getPerson().getGender().getId(), patientDto.getPerson().getAge()));
        result.setMedicalCoverageId(medicalCoverageId);
        result.setDoctorId(doctorId);
        result.setDiagnosticReports(studyMapper.parseToList(studies));
        LOG.debug(OUTPUT, result);
        return result;
    }
}
