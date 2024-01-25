package ar.lamansys.sgh.clinichistory.application.fetchHospitalizationState;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.DocumentType;
import org.springframework.stereotype.Service;

import ar.lamansys.sgh.clinichistory.domain.ips.ProcedureBo;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.hospitalizationState.entity.HCHProcedureRepository;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.hospitalizationState.entity.ProcedureVo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Slf4j
@Service
public class FetchHospitalizationProceduresState {

    public static final String OUTPUT = "Output -> {}";

    private final HCHProcedureRepository hchProcedureRepository;

	public List<ProcedureBo> run(Integer internmentEpisodeId) {
        log.debug("Input parameters -> internmentEpisodeId {}", internmentEpisodeId);
        List<Short> invalidDocumentTypes = List.of(DocumentType.ANESTHETIC_REPORT);
        List<ProcedureVo> queryResult = hchProcedureRepository.findGeneralState(internmentEpisodeId, invalidDocumentTypes);
        List<ProcedureBo> result = queryResult.stream().map(ProcedureBo::new).collect(Collectors.toList());
        log.debug(OUTPUT, result);
        return result;
    }
}
