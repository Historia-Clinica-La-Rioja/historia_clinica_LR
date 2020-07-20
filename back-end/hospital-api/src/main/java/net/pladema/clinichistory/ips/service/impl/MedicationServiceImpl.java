package net.pladema.clinichistory.ips.service.impl;

import net.pladema.clinichistory.documents.service.DocumentService;
import net.pladema.clinichistory.documents.service.NoteService;
import net.pladema.clinichistory.ips.repository.MedicationStatementRepository;
import net.pladema.clinichistory.ips.repository.entity.MedicationStatement;
import net.pladema.clinichistory.ips.repository.masterdata.MedicamentStatementStatusRepository;
import net.pladema.clinichistory.ips.repository.masterdata.entity.MedicationStatementStatus;
import net.pladema.clinichistory.ips.service.MedicationService;
import net.pladema.clinichistory.ips.service.SnomedService;
import net.pladema.clinichistory.ips.service.domain.MedicationBo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MedicationServiceImpl implements MedicationService {

    private static final Logger LOG = LoggerFactory.getLogger(MedicationServiceImpl.class);

    public static final String OUTPUT = "Output -> {}";

    private final MedicationStatementRepository medicationStatementRepository;

    private final MedicamentStatementStatusRepository medicamentStatementStatusRepository;

    private final DocumentService documentService;

    private final SnomedService snomedService;

    private final NoteService noteService;

    public MedicationServiceImpl(MedicationStatementRepository medicationStatementRepository,
                                 MedicamentStatementStatusRepository medicamentStatementStatusRepository, DocumentService documentService,
                                 SnomedService snomedService,
                                 NoteService noteService){
        this.medicationStatementRepository = medicationStatementRepository;
        this.medicamentStatementStatusRepository = medicamentStatementStatusRepository;
        this.documentService = documentService;
        this.snomedService = snomedService;
        this.noteService = noteService;
    }

    @Override
    public List<MedicationBo> loadMedications(Integer patientId, Long documentId, List<MedicationBo> medications) {
        LOG.debug("Input parameters -> patientId {}, documentId {}, medications {}", documentId, patientId, medications);
        medications.forEach(medication -> {
            String sctId = snomedService.createSnomedTerm(medication.getSnomed());
            MedicationStatement medicationStatement = saveMedicationStatement(patientId, medication, sctId);

            medication.setId(medicationStatement.getId());
            medication.setStatusId(medicationStatement.getStatusId());
            medication.setStatus(getStatus(medication.getStatusId()));

            documentService.createDocumentMedication(documentId, medicationStatement.getId());
        });
        List<MedicationBo> result = medications;
        LOG.debug(OUTPUT, result);
        return result;
    }

    private MedicationStatement saveMedicationStatement(Integer patientId, MedicationBo medicationBo, String sctId) {
        LOG.debug("Input parameters -> patientId {}, medication {}, sctId {}", patientId, medicationBo, sctId);
        MedicationStatement medicationStatement = new MedicationStatement(
                patientId,
                sctId,
                medicationBo.getStatusId(),
                noteService.createNote(medicationBo.getNote()));

        medicationStatement = medicationStatementRepository.save(medicationStatement);
        LOG.debug("medicationStatement saved -> {}", medicationStatement.getId());
        LOG.debug(OUTPUT, medicationStatement);
        return medicationStatement;
    }

    private String getStatus(String id) {
        return medicamentStatementStatusRepository.findById(id).map(MedicationStatementStatus::getDescription).orElse(null);
    }

}
