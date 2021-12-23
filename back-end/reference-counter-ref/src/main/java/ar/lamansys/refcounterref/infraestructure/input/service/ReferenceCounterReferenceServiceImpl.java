package ar.lamansys.refcounterref.infraestructure.input.service;

import ar.lamansys.refcounterref.application.port.CounterReferenceStorage;
import ar.lamansys.refcounterref.application.port.ReferenceCounterReferenceFileStorage;
import ar.lamansys.refcounterref.domain.counterreference.CounterReferenceSummaryBo;
import ar.lamansys.refcounterref.domain.enums.EReferenceCounterReferenceType;
import ar.lamansys.refcounterref.domain.file.ReferenceCounterReferenceFileBo;
import ar.lamansys.refcounterref.domain.procedure.CounterReferenceProcedureBo;
import ar.lamansys.refcounterref.infraestructure.input.service.mapper.CounterReferenceSummaryMapper;
import ar.lamansys.sgh.shared.infrastructure.input.service.CounterReferenceSummaryDto;
import ar.lamansys.sgh.shared.infrastructure.input.service.CounterReferenceSummaryProcedureDto;
import ar.lamansys.sgh.shared.infrastructure.input.service.ReferenceCounterReferenceFileDto;
import ar.lamansys.sgh.shared.infrastructure.input.service.SharedSnomedDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Slf4j
@Service
public class ReferenceCounterReferenceServiceImpl implements ReferenceCounterReferenceService {

    private final ReferenceCounterReferenceFileStorage referenceCounterReferenceFileStorage;
    private final CounterReferenceStorage counterReferenceStorage;
    private final CounterReferenceSummaryMapper counterReferenceSummaryMapper;

    @Override
    public List<ReferenceCounterReferenceFileDto> getReferenceFiles(Integer referenceId) {
        log.debug("Input parameter -> referenceId {}", referenceId);
        List<ReferenceCounterReferenceFileBo> referenceCounterReferenceFileBos = referenceCounterReferenceFileStorage.getFilesByReferenceCounterReferenceIdAndType(referenceId, EReferenceCounterReferenceType.REFERENCIA.getId().intValue());
        return mapToReferenceCounterReferenceFileDto(referenceCounterReferenceFileBos);
    }

    @Override
    public CounterReferenceSummaryDto getCounterReference(Integer referenceId) {
        log.debug("Input parameter -> referenceId {}", referenceId);
        CounterReferenceSummaryBo counterReferenceSummaryBo = counterReferenceStorage.getCounterReference(referenceId).orElse(new CounterReferenceSummaryBo());

        if(counterReferenceSummaryBo.getId() != null) {
            counterReferenceSummaryBo.setProcedures(counterReferenceStorage.getProceduresByCounterReference(counterReferenceSummaryBo.getId()));
            counterReferenceSummaryBo.setFiles(referenceCounterReferenceFileStorage.getFilesByReferenceCounterReferenceIdAndType(counterReferenceSummaryBo.getId(),
                    EReferenceCounterReferenceType.CONTRARREFERENCIA.getId().intValue()));
        }

        CounterReferenceSummaryDto result = counterReferenceSummaryMapper.fromCounterReferenceSummaryBo(counterReferenceSummaryBo);
        if(result.getId() != null)
            result.setProcedures(mapToCounterReferenceSummaryProcedureDto(counterReferenceSummaryBo.getProcedures()));

        return result;
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
