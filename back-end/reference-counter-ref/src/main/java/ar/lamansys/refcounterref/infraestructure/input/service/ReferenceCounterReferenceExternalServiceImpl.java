package ar.lamansys.refcounterref.infraestructure.input.service;

import ar.lamansys.refcounterref.application.createreference.CreateReference;
import ar.lamansys.refcounterref.application.getcounterreference.GetCounterReference;
import ar.lamansys.refcounterref.application.getreferencefile.GetReferenceFile;
import ar.lamansys.refcounterref.application.getreferenceproblem.GetReferenceProblem;
import ar.lamansys.refcounterref.domain.counterreference.CounterReferenceSummaryBo;
import ar.lamansys.refcounterref.domain.file.ReferenceCounterReferenceFileBo;
import ar.lamansys.refcounterref.domain.procedure.CounterReferenceProcedureBo;
import ar.lamansys.refcounterref.infraestructure.input.service.mapper.CounterReferenceSummaryMapper;
import ar.lamansys.refcounterref.infraestructure.input.service.mapper.ReferenceMapper;
import ar.lamansys.refcounterref.infraestructure.input.service.mapper.ReferenceProblemMapper;
import ar.lamansys.sgh.shared.infrastructure.input.service.referencecounterreference.CounterReferenceSummaryDto;
import ar.lamansys.sgh.shared.infrastructure.input.service.referencecounterreference.CounterReferenceSummaryProcedureDto;
import ar.lamansys.sgh.shared.infrastructure.input.service.referencecounterreference.ReferenceCounterReferenceFileDto;
import ar.lamansys.sgh.shared.infrastructure.input.service.SharedReferenceCounterReference;
import ar.lamansys.sgh.shared.infrastructure.input.service.SharedSnomedDto;
import ar.lamansys.sgh.shared.infrastructure.input.service.referencecounterreference.ReferenceDto;
import ar.lamansys.sgh.shared.infrastructure.input.service.referencecounterreference.ReferenceProblemDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Slf4j
@Service
public class ReferenceCounterReferenceExternalServiceImpl implements SharedReferenceCounterReference {

    private final CreateReference createReference;
    private final GetReferenceFile getReferenceFile;
    private final GetCounterReference getCounterReference;
    private final GetReferenceProblem getReferenceProblem;
    private final CounterReferenceSummaryMapper counterReferenceSummaryMapper;
    private final ReferenceMapper referenceMapper;
    private final ReferenceProblemMapper referenceProblemMapper;

    @Override
    public List<ReferenceCounterReferenceFileDto> getReferenceFilesData(Integer referenceId) {
        log.debug("Input parameter -> referenceId {}", referenceId);
        List<ReferenceCounterReferenceFileBo> referenceCounterReferenceFileBos = getReferenceFile.getReferencesFileData(referenceId);
        return mapToReferenceCounterReferenceFileDto(referenceCounterReferenceFileBos);
    }

    @Override
    public CounterReferenceSummaryDto getCounterReference(Integer referenceId) {
        log.debug("Input parameter -> referenceId {}", referenceId);
        CounterReferenceSummaryBo counterReferenceSummaryBo = getCounterReference.run(referenceId);
        CounterReferenceSummaryDto result = counterReferenceSummaryMapper.fromCounterReferenceSummaryBo(counterReferenceSummaryBo);
        if(result.getId() != null)
            result.setProcedures(mapToCounterReferenceSummaryProcedureDto(counterReferenceSummaryBo.getProcedures()));
        return result;
    }

    @Override
	@Transactional
    public void saveReferences(Integer encounterId, Integer sourceTypeId, List<ReferenceDto> refrenceDtoList) {
        log.debug("Input parameters -> encounterId {}, sourceTypeId {}, referenceDtoList {}", encounterId, sourceTypeId, refrenceDtoList);
        createReference.run(encounterId, sourceTypeId, referenceMapper.fromReferenceDtoList(refrenceDtoList));
    }

    @Override
    public List<ReferenceProblemDto> getReferencesProblemsByPatient(Integer patientId) {
        log.debug("Input parameters -> patientId {} ", patientId);
        return referenceProblemMapper.fromReferenceProblemBoList(getReferenceProblem.run(patientId));
    }

    private List<ReferenceCounterReferenceFileDto> mapToReferenceCounterReferenceFileDto(List<ReferenceCounterReferenceFileBo> referenceCounterReferenceFileBos) {
        List<ReferenceCounterReferenceFileDto> referenceCounterReferenceFileDtos = new ArrayList<>();
        referenceCounterReferenceFileBos.stream().forEach(referenceCounterReferenceFileBo ->
                referenceCounterReferenceFileDtos.add(new ReferenceCounterReferenceFileDto(referenceCounterReferenceFileBo.getFileId(), referenceCounterReferenceFileBo.getFileName()))
        );
        return referenceCounterReferenceFileDtos;
    }

    private List<CounterReferenceSummaryProcedureDto> mapToCounterReferenceSummaryProcedureDto(List<CounterReferenceProcedureBo> counterReferenceProcedureBoList) {
        List<CounterReferenceSummaryProcedureDto> counterReferenceSummaryProcedureDtoList = new ArrayList<>();
        counterReferenceProcedureBoList.stream().forEach(counterReferenceProcedureBo ->
                counterReferenceSummaryProcedureDtoList.add(new CounterReferenceSummaryProcedureDto(new SharedSnomedDto(counterReferenceProcedureBo.getSctid(), counterReferenceProcedureBo.getPt()))));
        return counterReferenceSummaryProcedureDtoList;
    }

}
