package net.pladema.clinichistory.requests.servicerequests.service.impl;

import net.pladema.clinichistory.documents.service.DocumentFactory;
import net.pladema.clinichistory.hospitalization.service.documents.validation.SnomedValidator;
import net.pladema.clinichistory.requests.servicerequests.repository.ServiceRequestRepository;
import net.pladema.clinichistory.requests.servicerequests.repository.entity.ServiceRequest;
import net.pladema.clinichistory.requests.servicerequests.service.CreateServiceRequestService;
import net.pladema.clinichistory.requests.servicerequests.service.domain.ServiceRequestBo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

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
        Assert.notNull(serviceRequestBo.getMedicalCoverageId(), "El identificador de la cobertura médica es obligatorio");
        Assert.notEmpty(serviceRequestBo.getDiagnosticReports(), "La orden tiene que tener asociada al menos un estudio");
        serviceRequestBo.getDiagnosticReports().forEach(dr -> {
            Assert.notNull(dr.getHealthConditionId(), "El estudio tiene que estar asociado a un problema");
        });

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
