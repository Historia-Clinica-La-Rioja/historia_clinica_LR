package ar.lamansys.refcounterref.infraestructure.input.service;

import ar.lamansys.refcounterref.application.port.ReferenceCounterReferenceFileStorage;
import ar.lamansys.refcounterref.domain.enums.EReferenceCounterReferenceType;
import ar.lamansys.refcounterref.domain.file.ReferenceCounterReferenceFileBo;
import ar.lamansys.sgh.shared.infrastructure.input.service.ReferenceCounterReferenceFileDto;
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

    @Override
    public List<ReferenceCounterReferenceFileDto> getReferenceFiles(Integer referenceId) {
        log.debug("Input parameter -> referenceId {}", referenceId);
        List<ReferenceCounterReferenceFileBo> referenceCounterReferenceFileBos = referenceCounterReferenceFileStorage.getFilesByReferenceCounterReferenceIdAndType(referenceId, EReferenceCounterReferenceType.REFERENCIA.getId().intValue());
        return mapToReferenceCounterReferenceFileDto(referenceCounterReferenceFileBos);
    }

    private List<ReferenceCounterReferenceFileDto> mapToReferenceCounterReferenceFileDto(List<ReferenceCounterReferenceFileBo> referenceCounterReferenceFileBos) {
        List<ReferenceCounterReferenceFileDto> referenceCounterReferenceFileDtos = new ArrayList<>();
        referenceCounterReferenceFileBos.stream().forEach(referenceCounterReferenceFileBo ->
                referenceCounterReferenceFileDtos.add(new ReferenceCounterReferenceFileDto(referenceCounterReferenceFileBo.getFileId(), referenceCounterReferenceFileBo.getFileName()))
        );
        return referenceCounterReferenceFileDtos;
    }
}
