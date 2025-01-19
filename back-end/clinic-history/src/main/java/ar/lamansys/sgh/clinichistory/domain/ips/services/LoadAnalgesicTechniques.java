package ar.lamansys.sgh.clinichistory.domain.ips.services;

import ar.lamansys.sgh.clinichistory.application.notes.NoteService;
import ar.lamansys.sgh.clinichistory.domain.ips.AnalgesicTechniqueBo;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.ips.AnalgesicTechniqueRepository;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.ips.entity.AnalgesicTechnique;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import static java.util.Objects.nonNull;

@Slf4j
@RequiredArgsConstructor
@Service
public class LoadAnalgesicTechniques {

    private final LoadAnestheticSubstances loadAnestheticSubstances;
    private final NoteService noteService;
    private final AnalgesicTechniqueRepository analgesicTechniqueRepository;

    public List<AnalgesicTechniqueBo> run(Long documentId, List<AnalgesicTechniqueBo> analgesicTechniques) {
        log.debug("Input parameters -> documentId {} analgesicTechniques {}", documentId, analgesicTechniques);

        loadAnestheticSubstances.run(documentId, analgesicTechniques);
        analgesicTechniques.stream()
                .filter(this::hasToSaveEntity)
                .forEach(this::saveEntity);

        log.debug("Output -> {}", analgesicTechniques);
        return analgesicTechniques;
    }

    private void saveEntity(AnalgesicTechniqueBo analgesicTechniqueBo) {

        Integer anestheticSubstanceId = analgesicTechniqueBo.getId();

        String injectionNote = analgesicTechniqueBo.getInjectionNote();
        Long injectionNoteId = nonNull(injectionNote) && !injectionNote.isBlank()  ? noteService.createNote(injectionNote) : null;

        Boolean catheter = analgesicTechniqueBo.getCatheter();
        String catheterNote = analgesicTechniqueBo.getCatheterNote();
        Long catheterNoteId = nonNull(catheterNote) && !catheterNote.isBlank() ? noteService.createNote(catheterNote) : null;

        AnalgesicTechnique analgesicTechnique = analgesicTechniqueRepository.save(new AnalgesicTechnique(null, anestheticSubstanceId, injectionNoteId, catheter, catheterNoteId));
        analgesicTechniqueBo.setAnalgesicTechniqueId(analgesicTechnique.getId());
    }

    private boolean hasToSaveEntity(AnalgesicTechniqueBo analgesicTechniqueBo) {
        return nonNull(analgesicTechniqueBo.getInjectionNote())
                || nonNull(analgesicTechniqueBo.getCatheter())
                || nonNull(analgesicTechniqueBo.getCatheterNote());
    }
}
