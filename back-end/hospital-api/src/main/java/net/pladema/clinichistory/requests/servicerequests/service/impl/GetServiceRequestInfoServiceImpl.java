package net.pladema.clinichistory.requests.servicerequests.service.impl;

import ar.lamansys.sgh.clinichistory.domain.ips.HealthConditionBo;
import ar.lamansys.sgh.clinichistory.domain.ips.SnomedBo;
import net.pladema.clinichistory.requests.servicerequests.repository.GetServiceRequestInfoRepository;
import net.pladema.clinichistory.requests.servicerequests.service.GetServiceRequestInfoService;
import ar.lamansys.sgh.clinichistory.domain.ips.DiagnosticReportBo;
import net.pladema.clinichistory.requests.servicerequests.service.domain.ServiceRequestBo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.math.BigInteger;
import java.sql.Timestamp;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class GetServiceRequestInfoServiceImpl implements GetServiceRequestInfoService {
    private static final Logger LOG = LoggerFactory.getLogger(GetServiceRequestInfoServiceImpl.class);

    private final GetServiceRequestInfoRepository getServiceRequestInfoRepository;

    public GetServiceRequestInfoServiceImpl(GetServiceRequestInfoRepository getServiceRequestInfoRepository) {
        this.getServiceRequestInfoRepository = getServiceRequestInfoRepository;
    }

    @Override
    public ServiceRequestBo run(Integer serviceRequestId) {
        LOG.debug("execute -> serviceRequestId {}", serviceRequestId);
        Assert.notNull(serviceRequestId, "El identificador de la orden es obligatorio");
        List<Object[]> resultQuery = getServiceRequestInfoRepository.run(serviceRequestId);
        ServiceRequestBo result = parseTo(resultQuery);
        return result;
    }

    private ServiceRequestBo parseTo(List<Object[]> resultQuery) {
        ServiceRequestBo result = new ServiceRequestBo();
        if (resultQuery.isEmpty())
            return result;
        result.setServiceRequestId((Integer) resultQuery.get(0)[0]);
        result.setDoctorId((Integer) resultQuery.get(0)[1]);
        result.setRequestDate((resultQuery.get(0)[2] != null) ? ((Timestamp) resultQuery.get(0)[2]).toLocalDateTime() : null);
        result.setMedicalCoverageId((Integer) resultQuery.get(0)[3]);
        result.setId(((BigInteger) resultQuery.get(0)[13]).longValue());
        result.setDiagnosticReports(
                resultQuery.stream()
                        .map(this::createDiagnosticReportBo)
                        .collect(Collectors.toList()));
        return result;
    }

    private DiagnosticReportBo createDiagnosticReportBo(Object[] row) {
        DiagnosticReportBo result = new DiagnosticReportBo();

        result.setObservations((String) row[4]);

        result.setSnomed(new SnomedBo((Integer) row[5], (String) row[6],(String) row[7]));


        HealthConditionBo healthConditionBo = new HealthConditionBo();
        healthConditionBo.setId((Integer) row[8]);
        healthConditionBo.setSnomed(new SnomedBo((Integer) row[9], (String) row[10], (String) row[11]));
        healthConditionBo.setCie10codes((String) row[12]);
        result.setHealthCondition(healthConditionBo);
        return result;
    }
}
