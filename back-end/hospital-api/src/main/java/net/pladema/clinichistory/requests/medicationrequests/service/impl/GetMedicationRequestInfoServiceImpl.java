package net.pladema.clinichistory.requests.medicationrequests.service.impl;

import net.pladema.clinichistory.documents.service.ips.domain.HealthConditionBo;
import net.pladema.clinichistory.documents.service.ips.domain.MedicationBo;
import net.pladema.clinichistory.documents.service.ips.domain.SnomedBo;
import net.pladema.clinichistory.requests.medicationrequests.repository.GetMedicationRequestInfoRepository;
import net.pladema.clinichistory.requests.medicationrequests.service.GetMedicationRequestInfoService;
import net.pladema.clinichistory.requests.medicationrequests.service.domain.MedicationRequestBo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.sql.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class GetMedicationRequestInfoServiceImpl implements GetMedicationRequestInfoService {

    private static final Logger LOG = LoggerFactory.getLogger(GetMedicationRequestInfoServiceImpl.class);

    private final GetMedicationRequestInfoRepository getMedicationRequestInfoRepository;

    public GetMedicationRequestInfoServiceImpl(GetMedicationRequestInfoRepository getMedicationRequestInfoRepository) {
        this.getMedicationRequestInfoRepository = getMedicationRequestInfoRepository;
    }

    @Override
    public MedicationRequestBo execute(Integer medicationRequestId) {
        LOG.debug("execute -> medicationRequestId {}", medicationRequestId);
        Assert.notNull(medicationRequestId, "El identificador de la receta es obligatorio");
        List<Object[]> resultQuery = getMedicationRequestInfoRepository.execute(medicationRequestId);
        MedicationRequestBo result = parseTo(resultQuery);
        return result;
    }

    private MedicationRequestBo parseTo(List<Object[]> resultQuery) {
        MedicationRequestBo result = new MedicationRequestBo();
        if (resultQuery.isEmpty())
            return result;
        result.setMedicationRequestId((Integer) resultQuery.get(0)[0]);
        result.setDoctorId((Integer) resultQuery.get(0)[1]);
        result.setRequestDate(resultQuery.get(0)[2] != null ? ((Date) resultQuery.get(0)[2]).toLocalDate() : null);
        result.setMedicalCoverageId((Integer) resultQuery.get(0)[3]);
        result.setMedications(
                resultQuery.stream()
                        .map(this::createMedicationBo)
                        .collect(Collectors.toList()));
        return result;
    }

    private MedicationBo createMedicationBo(Object[] row) {
        MedicationBo result = new MedicationBo();

        result.setId((Integer)row[4]);

        result.setSnomed(new SnomedBo((Integer) row[5], (String) row[6],(String) row[7]));

        result.setNote((String) row[8]);
        result.setStatusId((String) row[9]);
        result.setStatus((String) row[10]);

        HealthConditionBo healthConditionBo = new HealthConditionBo();
        healthConditionBo.setId((Integer) row[11]);
        healthConditionBo.setSnomed(new SnomedBo((Integer) row[12], (String) row[13], (String) row[14]));
        healthConditionBo.setCie10codes((String) row[15]);
        result.setHealthCondition(healthConditionBo);
        return result;
    }
}
