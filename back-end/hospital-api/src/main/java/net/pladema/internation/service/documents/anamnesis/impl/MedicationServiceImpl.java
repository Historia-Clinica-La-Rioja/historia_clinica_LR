package net.pladema.internation.service.documents.anamnesis.impl;

import net.pladema.internation.repository.ips.MedicationStatementRepository;
import net.pladema.internation.repository.ips.entity.MedicationStatement;
import net.pladema.internation.service.NoteService;
import net.pladema.internation.service.SnomedService;
import net.pladema.internation.service.documents.DocumentService;
import net.pladema.internation.service.documents.anamnesis.MedicationService;
import net.pladema.internation.service.domain.ips.Medication;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MedicationServiceImpl implements MedicationService {

    private static final Logger LOG = LoggerFactory.getLogger(AllergyServiceImpl.class);

    private final MedicationStatementRepository medicationStatementRepository;

    private final DocumentService documentService;

    private final SnomedService snomedService;

    private final NoteService noteService;

    public MedicationServiceImpl(MedicationStatementRepository medicationStatementRepository,
                                 DocumentService documentService,
                                 SnomedService snomedService,
                                 NoteService noteService){
        this.medicationStatementRepository = medicationStatementRepository;
        this.documentService = documentService;
        this.snomedService = snomedService;
        this.noteService = noteService;
    }

    @Override
    public void loadMedications(Integer patientId, Long documentId, List<Medication> medications) throws DataIntegrityViolationException {
        LOG.debug("Going to load medications -> {}", medications);
        LOG.debug("Input parameters -> patientId {}, documentId {}, medications {}", documentId, patientId, medications);

        medications.forEach(medication -> {

            String sctid = snomedService.createSnomedTerm(medication.getSnomed());
            if(sctid == null)
                throw new IllegalArgumentException("snomed.invalid");

            MedicationStatement medicationStatement = new MedicationStatement(
                    patientId,
                    sctid,
                    medication.getStatusId(),
                    noteService.createNote(medication.getNote()),
                    medication.isDeleted());

            medicationStatement = medicationStatementRepository.save(medicationStatement);
            documentService.createDocumentMedication(documentId, medicationStatement.getId());
        });
    }

}
