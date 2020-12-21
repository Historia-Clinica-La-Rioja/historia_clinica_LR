package net.pladema.clinichistory.requests.servicerequests.controller.mapper;

import net.pladema.clinichistory.requests.controller.dto.PrescriptionDto;
import net.pladema.clinichistory.requests.controller.dto.PrescriptionItemDto;
import net.pladema.clinichistory.requests.servicerequests.service.domain.ServiceRequestBo;
import org.mapstruct.Mapper;
import org.mapstruct.Named;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.List;


@Component
public class CreateServiceRequestMapper {
    private static final Logger LOG = LoggerFactory.getLogger(CreateServiceRequestMapper.class);
    private static final String OUTPUT = "OUTPUT -> {}";
    private final StudyMapper studyMapper;

    public CreateServiceRequestMapper(StudyMapper studyMapper){
        this.studyMapper = studyMapper;
    }

    @Named("parseTo")
    public ServiceRequestBo parseTo(Integer doctorId, Integer patientId, String categoryId, Integer medicalCoverageId, List<PrescriptionItemDto> studies){
        LOG.debug("parseTo -> doctorId {}, patientId {}, medicalCoverageId {}, studies {} ", doctorId, patientId, medicalCoverageId, studies);
        ServiceRequestBo result = new ServiceRequestBo();
        result.setCategoryId(categoryId);
        result.setPatientId(patientId);
        result.setMedicalCoverageId(medicalCoverageId);
        result.setDoctorId(doctorId);
        result.setDiagnosticReports(studyMapper.parseTo(studies));
        LOG.debug(OUTPUT, result);
        return result;
    }
}
