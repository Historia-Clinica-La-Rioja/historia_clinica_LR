package ar.lamansys.sgh.clinichistory.domain.ips.services;

import ar.lamansys.sgh.clinichistory.application.notes.NoteService;
import ar.lamansys.sgh.clinichistory.domain.ips.ProcedureDescriptionBo;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.DocumentProcedureDescriptionRepository;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.entity.DocumentProcedureDescription;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import static java.util.Objects.nonNull;

@Slf4j
@RequiredArgsConstructor
@Service
public class LoadProcedureDescription {

    private final DocumentProcedureDescriptionRepository documentProcedureDescriptionRepository;
    private final NoteService noteService;

    public ProcedureDescriptionBo run(Long documentId, Optional<ProcedureDescriptionBo> procedureDescription) {
        log.debug("Input parameters -> documentId {} procedureDescription {}", documentId, procedureDescription);

        procedureDescription.filter(this::hasToSaveEntity)
                .ifPresent(procedureDescriptionBo -> this.saveEntity(documentId, procedureDescriptionBo));

        log.debug("Output -> {}", procedureDescription);
        return procedureDescription.orElse(null);
    }

    private boolean hasToSaveEntity(ProcedureDescriptionBo procedureDescription) {
        return nonNull(procedureDescription.getFoodIntake())
                || nonNull(procedureDescription.getFoodIntakeDate())
                || nonNull(procedureDescription.getNote())
                || nonNull(procedureDescription.getAsa())
                || nonNull(procedureDescription.getVenousAccess())
                || nonNull(procedureDescription.getNasogastricTube())
                || nonNull(procedureDescription.getUrinaryCatheter())
                || nonNull(procedureDescription.getAnesthesiaStartDate())
                || nonNull(procedureDescription.getAnesthesiaStartTime())
                || nonNull(procedureDescription.getAnesthesiaEndDate())
                || nonNull(procedureDescription.getAnesthesiaEndTime())
                || nonNull(procedureDescription.getSurgeryStartDate())
                || nonNull(procedureDescription.getSurgeryStartTime())
                || nonNull(procedureDescription.getSurgeryEndDate())
                || nonNull(procedureDescription.getSurgeryEndTime());
    }

    private void saveEntity(Long documentId, ProcedureDescriptionBo procedureDescriptionBo) {

        Long noteId = noteService.createNote(procedureDescriptionBo.getNote());
        Short asa = procedureDescriptionBo.getAsa();
        Boolean venousAccess = procedureDescriptionBo.getVenousAccess();
        Boolean nasogastricTube = procedureDescriptionBo.getNasogastricTube();
        Boolean urinaryCatheter = procedureDescriptionBo.getUrinaryCatheter();
        LocalTime foodIntake = procedureDescriptionBo.getFoodIntake();
        LocalDate foodIntakeDate = procedureDescriptionBo.getFoodIntakeDate();
        LocalDate anesthesiaStartDate = procedureDescriptionBo.getAnesthesiaStartDate();
        LocalTime anesthesiaStartTime = procedureDescriptionBo.getAnesthesiaStartTime();
        LocalDate anesthesiaEndDate = procedureDescriptionBo.getAnesthesiaEndDate();
        LocalTime anesthesiaEndTime = procedureDescriptionBo.getAnesthesiaEndTime();
        LocalDate surgeryStartDate = procedureDescriptionBo.getSurgeryStartDate();
        LocalTime surgeryStartTime = procedureDescriptionBo.getSurgeryStartTime();
        LocalDate surgeryEndDate = procedureDescriptionBo.getSurgeryEndDate();
        LocalTime surgeryEndTime = procedureDescriptionBo.getSurgeryEndTime();

        DocumentProcedureDescription saved = documentProcedureDescriptionRepository.save(new DocumentProcedureDescription(
                documentId, noteId, asa, venousAccess, nasogastricTube, urinaryCatheter, foodIntake,
                foodIntakeDate, anesthesiaStartDate, anesthesiaStartTime, anesthesiaEndDate, anesthesiaEndTime,
                surgeryStartDate, surgeryStartTime, surgeryEndDate, surgeryEndTime));
        procedureDescriptionBo.setId(saved.getDocumentId());
    }

}
