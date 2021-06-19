package ar.lamansys.sgh.clinichistory.application.fetchHospitalizationState;

import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.hospitalizationState.HCHMedicationStatementRepository;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.hospitalizationState.entity.MedicationVo;
import ar.lamansys.sgh.clinichistory.domain.ips.MedicationBo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class FetchHospitalizationMedicationState {

    private static final Logger LOG = LoggerFactory.getLogger(FetchHospitalizationMedicationState.class);

    public static final String OUTPUT = "Output -> {}";

    private final HCHMedicationStatementRepository hchMedicationStatementRepository;

    public FetchHospitalizationMedicationState(HCHMedicationStatementRepository hchMedicationStatementRepository){
        this.hchMedicationStatementRepository = hchMedicationStatementRepository;
    }

    public List<MedicationBo> run(Integer internmentEpisodeId) {
        LOG.debug("Input parameters -> internmentEpisodeId {}", internmentEpisodeId);
        List<MedicationVo> resultQuery = hchMedicationStatementRepository.findGeneralState(internmentEpisodeId);
        List<MedicationBo> result = resultQuery.stream().map(MedicationBo::new).collect(Collectors.toList());
        LOG.debug(OUTPUT, result);
        return result;
    }

}
