package net.pladema.internation.service.documents.anamnesis.impl;

import net.pladema.internation.controller.mapper.ips.MedicationMapper;
import net.pladema.internation.repository.ips.MedicationStatementRepository;
import net.pladema.internation.repository.ips.entity.MedicationStatement;
import net.pladema.internation.service.NoteService;
import net.pladema.internation.service.SnomedService;
import net.pladema.internation.service.documents.DocumentService;
import net.pladema.internation.service.documents.anamnesis.MedicationService;
import net.pladema.internation.service.domain.ips.MedicationBo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MedicationServiceImpl implements MedicationService {

    private static final Logger LOG = LoggerFactory.getLogger(AllergyServiceImpl.class);

    public static final String OUTPUT = "Output -> {}";

    private final MedicationStatementRepository medicationStatementRepository;

    private final MedicationMapper medicationMapper;

    private final DocumentService documentService;

    private final SnomedService snomedService;

    private final NoteService noteService;

    public MedicationServiceImpl(MedicationStatementRepository medicationStatementRepository,
                                 MedicationMapper medicationMapper,
                                 DocumentService documentService,
                                 SnomedService snomedService,
                                 NoteService noteService){
        this.medicationStatementRepository = medicationStatementRepository;
        this.medicationMapper = medicationMapper;
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

            documentService.createDocumentMedication(documentId, medicationStatement.getId());
        });
        List<MedicationBo> result = medications;
        LOG.debug(OUTPUT, result);
        return result;
    }

    @Override
    public List<MedicationBo> getMedicationsGeneralState(Integer internmentEpisodeId) {
        LOG.debug("Input parameters -> internmentEpisodeId {}", internmentEpisodeId);
        List<MedicationBo> result = medicationMapper.toListMedicationBo(
                medicationStatementRepository.findGeneralState(internmentEpisodeId));
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
        LOG.debug("medicationStatement saved ->", medicationStatement.getId());
        LOG.debug(OUTPUT, medicationStatement);
        return medicationStatement;
    }

}
