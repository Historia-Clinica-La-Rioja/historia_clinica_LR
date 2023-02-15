package ar.lamansys.sgh.clinichistory.application.fetchHCE;

import ar.lamansys.sgh.clinichistory.domain.ips.services.MedicationCalculateStatus;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.hce.HCEMedicationStatementRepository;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.hce.entity.HCEMedicationVo;
import ar.lamansys.sgh.clinichistory.domain.hce.HCEMedicationBo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.function.Function;

@Service
public class HCEMedicationServiceImpl implements HCEMedicationService {

    private static final Logger LOG = LoggerFactory.getLogger(HCEMedicationServiceImpl.class);

    private static final String LOGGING_OUTPUT = "Output -> {}";
    private static final String LOGGING_INPUT = "Input parameters -> patientId {} ";

    private final HCEMedicationStatementRepository hceMedicationStatementRepository;

    private final MedicationCalculateStatus medicationCalculateStatus;

    public HCEMedicationServiceImpl(HCEMedicationStatementRepository hceMedicationStatementRepository,
                                    MedicationCalculateStatus medicationCalculateStatus) {
        this.hceMedicationStatementRepository = hceMedicationStatementRepository;
        this.medicationCalculateStatus = medicationCalculateStatus;
    }

    @Override
    public List<HCEMedicationBo> getMedication(Integer patientId) {
        LOG.debug(LOGGING_INPUT, patientId);
        List<HCEMedicationVo> resultQuery = hceMedicationStatementRepository.getMedication(patientId);
        List<HCEMedicationBo> result = resultQuery.stream()
                .map(HCEMedicationBo::new)
				.filter(distinctByKey(HCEMedicationBo::getSnomedSctid))
                .collect(Collectors.toList());
        result.forEach((hceMedicationBo -> hceMedicationBo.setStatus(medicationCalculateStatus.execute(hceMedicationBo.getStatusId(), hceMedicationBo.getDosage()))));
        LOG.debug(LOGGING_OUTPUT, result);
        return result;
    }

	private static <T> Predicate<T> distinctByKey(Function<? super T, ?> keyExtractor) {
		Map<Object, Boolean> seen = new ConcurrentHashMap<>();
		return t -> seen.putIfAbsent(keyExtractor.apply(t), Boolean.TRUE) == null;
	}

}
