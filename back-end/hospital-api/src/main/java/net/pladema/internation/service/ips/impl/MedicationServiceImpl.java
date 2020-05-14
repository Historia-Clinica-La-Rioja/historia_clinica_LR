package net.pladema.internation.service.ips.impl;

import net.pladema.internation.repository.ips.MedicationStatementRepository;
import net.pladema.internation.repository.ips.entity.MedicationStatement;
import net.pladema.internation.repository.ips.generalstate.MedicationVo;
import net.pladema.internation.service.documents.DocumentService;
import net.pladema.internation.service.general.NoteService;
import net.pladema.internation.service.ips.MedicationService;
import net.pladema.internation.service.ips.SnomedService;
import net.pladema.internation.service.ips.domain.MedicationBo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MedicationServiceImpl implements MedicationService {

    private static final Logger LOG = LoggerFactory.getLogger(MedicationServiceImpl.class);

    public static final String OUTPUT = "Output -> {}";

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
        List<MedicationVo> resultQuery = medicationStatementRepository.findGeneralState(internmentEpisodeId);
        List<MedicationBo> result = resultQuery.stream().map(MedicationBo::new).collect(Collectors.toList());
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

}
