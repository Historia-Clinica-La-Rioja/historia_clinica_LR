package net.pladema.clinichistory.requests.servicerequests.service.impl;

import net.pladema.clinichistory.documents.service.DocumentFactory;
import net.pladema.clinichistory.hospitalization.service.documents.validation.SnomedValidator;
import net.pladema.clinichistory.requests.servicerequests.repository.ServiceRequestRepository;
import net.pladema.clinichistory.requests.servicerequests.repository.entity.ServiceRequest;
import net.pladema.clinichistory.requests.servicerequests.service.CreateServiceRequestService;
import net.pladema.clinichistory.requests.servicerequests.service.domain.DiagnosticReportBo;
import net.pladema.clinichistory.requests.servicerequests.service.domain.ServiceRequestBo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class CreateServiceRequestServiceImpl implements CreateServiceRequestService {

    private static final Logger LOG = LoggerFactory.getLogger(CreateServiceRequestServiceImpl.class);
    public static final String OUTPUT = "Output -> {}";

    private final ServiceRequestRepository serviceRequestRepository;
    private final DocumentFactory documentFactory;

    public CreateServiceRequestServiceImpl(ServiceRequestRepository serviceRequestRepository,
                                           DocumentFactory documentFactory){
        this.serviceRequestRepository = serviceRequestRepository;
        this.documentFactory = documentFactory;
    }

    @Override
    public Integer execute(Integer institutionId, ServiceRequestBo serviceRequestBo) {
        LOG.debug("Input parameters -> serviceRequestBo {}", serviceRequestBo);

        assertRequiredFields(institutionId, serviceRequestBo);
        assertNoDuplicatedStudies(serviceRequestBo);
        ServiceRequest newServiceRequest = createServiceRequest(institutionId, serviceRequestBo);
        serviceRequestBo.setEncounterId(newServiceRequest.getId());
        documentFactory.run(serviceRequestBo);

        LOG.debug(OUTPUT, serviceRequestBo);
        return newServiceRequest.getId();
    }

    private void assertRequiredFields(Integer institutionId, ServiceRequestBo serviceRequestBo) {
        Assert.notNull(institutionId, "El identificador de la institución es obligatorio");
        Assert.notNull(serviceRequestBo.getPatientId(), "El paciente es obligatorio");
        Assert.notNull(serviceRequestBo.getDoctorId(), "El identificador del médico es obligatorio");
        Assert.notEmpty(serviceRequestBo.getDiagnosticReports(), "La orden tiene que tener asociada al menos un estudio");
        SnomedValidator snomedValidator =  new SnomedValidator();
        serviceRequestBo.getDiagnosticReports().forEach(dr -> {
            snomedValidator.isValid(dr.getSnomed());
            Assert.notNull(dr.getHealthConditionId(), "El estudio tiene que estar asociado a un problema");
        });
    }

    private void assertNoDuplicatedStudies(ServiceRequestBo serviceRequestBo) {
        Map<Pair<Integer, String>, List<DiagnosticReportBo>> result = serviceRequestBo.getDiagnosticReports()
                .stream()
                .collect(Collectors.groupingBy(p -> Pair.of(p.getHealthConditionId(), p.getSnomed().getSctid())));
        result.forEach((k,v) -> Assert.isTrue(v.size() == 1, "La orden no puede contener más de un estudio con el mismo problema y el mismo concepto snomed"));
    }

    private ServiceRequest createServiceRequest(Integer institutionId, ServiceRequestBo serviceRequestBo) {
        ServiceRequest newServiceRequest = new ServiceRequest(
                institutionId,
                serviceRequestBo.getPatientId(),
                serviceRequestBo.getDoctorId(),
                serviceRequestBo.getMedicalCoverageId(),
                serviceRequestBo.getCategoryId()
        );
        return this.serviceRequestRepository.save(newServiceRequest);
    }
}
