package net.pladema.internation.service.documents.anamnesis.impl;

import net.pladema.internation.controller.mapper.ips.MedicationMapper;
import net.pladema.internation.repository.ips.MedicationStatementRepository;
import net.pladema.internation.repository.ips.entity.MedicationStatement;
import net.pladema.internation.repository.masterdata.entity.MedicationStatementStatus;
import net.pladema.internation.service.NoteService;
import net.pladema.internation.service.SnomedService;
import net.pladema.internation.service.documents.DocumentService;
import net.pladema.internation.service.documents.anamnesis.MedicationService;
import net.pladema.internation.service.domain.ips.MedicationBo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

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
        LOG.debug("Input parameters -> patientId {}, documentId {}, medicationBos {}", documentId, patientId, medications);
        medications.stream().filter(MedicationBo::mustSave).forEach(medication -> {
            String sctid = snomedService.createSnomedTerm(medication.getSnomed());
            MedicationStatement medicationStatement = saveMedicationStatement(patientId, medication, sctid);
            medication.setId(medicationStatement.getId());
            documentService.createDocumentMedication(documentId, medicationStatement.getId());
        });
        return medications.stream().filter(m -> !m.isDeleted()).collect(Collectors.toList());
    }

    @Override
    public List<MedicationBo> getMedicationsGeneralState(Integer internmentEpisodeId) {
        LOG.debug("Input parameters -> internmentEpisodeId {}", internmentEpisodeId);
        List<MedicationBo> result = medicationMapper.toListMedicationBo(
                medicationStatementRepository.findGeneralState(internmentEpisodeId));
        LOG.debug(OUTPUT, result);
        return result;
    }

    private MedicationStatement saveMedicationStatement(Integer patientId, MedicationBo medicationBo, String sctid) {
        LOG.debug("Input parameters -> patientId {}, medicationBo {}, sctid {}", patientId, medicationBo, sctid);
        MedicationStatement medicationStatement = new MedicationStatement(
                patientId,
                sctid,
                MedicationStatementStatus.ACTIVE,
                noteService.createNote(medicationBo.getNote()),
                medicationBo.isDeleted());

        medicationStatement = medicationStatementRepository.save(medicationStatement);
        LOG.debug("medicationStatement saved ->", medicationStatement.getId());
        LOG.debug(OUTPUT, medicationStatement);
        return medicationStatement;
    }

}
