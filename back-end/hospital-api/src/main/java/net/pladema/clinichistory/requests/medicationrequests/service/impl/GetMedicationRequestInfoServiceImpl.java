package net.pladema.clinichistory.requests.medicationrequests.service.impl;

import ar.lamansys.sgh.clinichistory.domain.ips.HealthConditionBo;
import ar.lamansys.sgh.clinichistory.domain.ips.MedicationBo;
import ar.lamansys.sgh.clinichistory.domain.ips.SnomedBo;
import net.pladema.clinichistory.requests.medicationrequests.repository.GetMedicationRequestInfoRepository;
import net.pladema.clinichistory.requests.medicationrequests.service.GetMedicationRequestInfoService;
import net.pladema.clinichistory.requests.medicationrequests.service.domain.MedicationRequestBo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.math.BigInteger;
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
		result.setId(((BigInteger) resultQuery.get(0)[13]).longValue());
        return result;
    }

    private MedicationBo createMedicationBo(Object[] row) {
        MedicationBo result = new MedicationBo();

        result.setNote((String) row[4]);

        result.setSnomed(new SnomedBo((Integer) row[5], (String) row[6],(String) row[7]));


        HealthConditionBo healthConditionBo = new HealthConditionBo();
        healthConditionBo.setId((Integer) row[8]);
        healthConditionBo.setSnomed(new SnomedBo((Integer) row[9], (String) row[10], (String) row[11]));
        healthConditionBo.setCie10codes((String) row[12]);
        result.setHealthCondition(healthConditionBo);
        return result;
    }
}
