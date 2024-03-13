package ar.lamansys.sgh.clinichistory.domain.ips.services;

import ar.lamansys.sgh.clinichistory.application.notes.NoteService;
import ar.lamansys.sgh.clinichistory.domain.ips.PostAnesthesiaStatusBo;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.DocumentPostAnesthesiaStatusRepository;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.entity.DocumentPostAnesthesiaStatus;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import static java.util.Objects.nonNull;

@Slf4j
@RequiredArgsConstructor
@Service
public class LoadPostAnesthesiaStatus {

    private final NoteService noteService;
    private final DocumentPostAnesthesiaStatusRepository documentPostAnesthesiaStatusRepository;

    public PostAnesthesiaStatusBo run(Long documentId, Optional<PostAnesthesiaStatusBo> postAnesthesiaStatus) {
        log.debug("Input parameters -> documentId {} postAnesthesiaStatus {}", documentId, postAnesthesiaStatus);

        postAnesthesiaStatus.filter(this::hasToSaveEntity)
                .ifPresent(postAnesthesiaStatusBo -> this.saveEntity(documentId, postAnesthesiaStatusBo));

        log.debug("Output -> {}", postAnesthesiaStatus);
        return postAnesthesiaStatus.orElse(null);
    }

    private boolean hasToSaveEntity(PostAnesthesiaStatusBo postAnesthesiaStatus) {
        return nonNull(postAnesthesiaStatus.getIntentionalSensitivity())
                || nonNull(postAnesthesiaStatus.getCornealReflex())
                || nonNull(postAnesthesiaStatus.getObeyOrders())
                || nonNull(postAnesthesiaStatus.getTalk())
                || nonNull(postAnesthesiaStatus.getRespiratoryDepression())
                || nonNull(postAnesthesiaStatus.getCirculatoryDepression())
                || nonNull(postAnesthesiaStatus.getVomiting())
                || nonNull(postAnesthesiaStatus.getCurated())
                || nonNull(postAnesthesiaStatus.getTrachealCannula())
                || nonNull(postAnesthesiaStatus.getPharyngealCannula())
                || nonNull(postAnesthesiaStatus.getInternment())
                || nonNull(postAnesthesiaStatus.getInternmentPlace())
                || nonNull(postAnesthesiaStatus.getNote());
    }

    private void saveEntity(Long documentId, PostAnesthesiaStatusBo postAnesthesiaStatusBo) {

        Long noteId = noteService.createNote(postAnesthesiaStatusBo.getNote());
        Boolean intentionalSensitivity = postAnesthesiaStatusBo.getIntentionalSensitivity();
        Boolean cornealReflex = postAnesthesiaStatusBo.getCornealReflex();
        Boolean obeyOrders = postAnesthesiaStatusBo.getObeyOrders();
        Boolean talk = postAnesthesiaStatusBo.getTalk();
        Boolean respiratoryDepression = postAnesthesiaStatusBo.getRespiratoryDepression();
        Boolean circulatoryDepression = postAnesthesiaStatusBo.getCirculatoryDepression();
        Boolean vomiting = postAnesthesiaStatusBo.getVomiting();
        Boolean curated = postAnesthesiaStatusBo.getCurated();
        Boolean trachealCannula = postAnesthesiaStatusBo.getTrachealCannula();
        Boolean pharyngealCannula = postAnesthesiaStatusBo.getPharyngealCannula();
        Boolean internment = postAnesthesiaStatusBo.getInternment();
        Short internmentPlaceId = postAnesthesiaStatusBo.getIntermentPlaceId();

        DocumentPostAnesthesiaStatus saved = documentPostAnesthesiaStatusRepository.save(new DocumentPostAnesthesiaStatus(
                documentId, intentionalSensitivity, cornealReflex, obeyOrders, talk, respiratoryDepression,
                circulatoryDepression, vomiting, curated, trachealCannula, pharyngealCannula, internment, internmentPlaceId,
                noteId));
        postAnesthesiaStatusBo.setId(saved.getDocumentId());
    }
}
