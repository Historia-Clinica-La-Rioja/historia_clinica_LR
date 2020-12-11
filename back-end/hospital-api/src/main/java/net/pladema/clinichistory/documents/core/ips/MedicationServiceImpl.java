package net.pladema.clinichistory.documents.core.ips;

import net.pladema.clinichistory.documents.repository.ips.DosageRepository;
import net.pladema.clinichistory.documents.repository.ips.MedicationStatementRepository;
import net.pladema.clinichistory.documents.repository.ips.entity.Dosage;
import net.pladema.clinichistory.documents.repository.ips.entity.MedicationStatement;
import net.pladema.clinichistory.documents.repository.ips.masterdata.MedicamentStatementStatusRepository;
import net.pladema.clinichistory.documents.repository.ips.masterdata.entity.MedicationStatementStatus;
import net.pladema.clinichistory.documents.service.DocumentService;
import net.pladema.clinichistory.documents.service.NoteService;
import net.pladema.clinichistory.documents.service.ips.MedicationService;
import net.pladema.clinichistory.documents.service.ips.SnomedService;
import net.pladema.clinichistory.documents.service.ips.domain.DosageBo;
import net.pladema.clinichistory.documents.service.ips.domain.MedicationBo;
import net.pladema.clinichistory.documents.service.ips.domain.enums.EUnitsOfTimeBo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MedicationServiceImpl implements MedicationService {

    private static final Logger LOG = LoggerFactory.getLogger(MedicationServiceImpl.class);

    public static final String OUTPUT = "Output -> {}";

    private final MedicationStatementRepository medicationStatementRepository;

    private final DosageRepository dosageRepository;

    private final MedicamentStatementStatusRepository medicamentStatementStatusRepository;

    private final DocumentService documentService;

    private final SnomedService snomedService;

    private final NoteService noteService;

    public MedicationServiceImpl(MedicationStatementRepository medicationStatementRepository,
                                 DosageRepository dosageRepository,
                                 MedicamentStatementStatusRepository medicamentStatementStatusRepository,
                                 DocumentService documentService,
                                 SnomedService snomedService,
                                 NoteService noteService){
        this.medicationStatementRepository = medicationStatementRepository;
        this.dosageRepository = dosageRepository;
        this.medicamentStatementStatusRepository = medicamentStatementStatusRepository;
        this.documentService = documentService;
        this.snomedService = snomedService;
        this.noteService = noteService;
    }

    @Override
    public List<MedicationBo> loadMedications(Integer patientId, Long documentId, List<MedicationBo> medications) {
        LOG.debug("Input parameters -> patientId {}, documentId {}, medications {}", documentId, patientId, medications);
        medications.forEach(medication -> {
            Integer snomedId = snomedService.getSnomedId(medication.getSnomed())
                    .orElseGet(() -> snomedService.createSnomedTerm(medication.getSnomed()));
            MedicationStatement medicationStatement = saveMedicationStatement(patientId, medication, snomedId);

            medication.setId(medicationStatement.getId());
            medication.setStatusId(medicationStatement.getStatusId());
            medication.setStatus(getStatus(medication.getStatusId()));

            documentService.createDocumentMedication(documentId, medicationStatement.getId());
        });
        List<MedicationBo> result = medications;
        LOG.debug(OUTPUT, result);
        return result;
    }



    private MedicationStatement saveMedicationStatement(Integer patientId, MedicationBo medicationBo, Integer snomedId) {
        LOG.debug("Input parameters -> patientId {}, medication {}, snomedId {}", patientId, medicationBo, snomedId);

        Dosage newDosage = createDosage(medicationBo.getDosage());
        MedicationStatement medicationStatement = new MedicationStatement(
                patientId,
                snomedId,
                medicationBo.getStatusId(),
                noteService.createNote(medicationBo.getNote()),
                medicationBo.getHealthCondition().getId(),
                newDosage != null ? newDosage.getId() : null);

        medicationStatement = medicationStatementRepository.save(medicationStatement);
        LOG.debug("medicationStatement saved -> {}", medicationStatement.getId());
        LOG.debug(OUTPUT, medicationStatement);
        return medicationStatement;
    }

    private Dosage createDosage(DosageBo dosage) {
        LOG.debug("Input parameters -> dosage {}", dosage);
        if (dosage == null)
            return null;
        Dosage newDosage = new Dosage();
        newDosage.setChronic(dosage.isChronic());
        newDosage.setStartDate(dosage.getStartDate());
        newDosage.setEndDate(dosage.getEndDate());
        newDosage.setSuspendedStartDate(dosage.getStartDate());
        newDosage.setSuspendedEndDate(dosage.getSuspendedEndDate());
        newDosage.setDuration(!dosage.isChronic() ? dosage.getDuration() : null);
        newDosage.setDurationUnit(EUnitsOfTimeBo.DAY.getValue());
        newDosage.setPeriodUnit(dosage.getPeriodUnit());
        newDosage.setFrequency(EUnitsOfTimeBo.DAY.getValue().equals(dosage.getPeriodUnit()) ? 1 : dosage.getFrequency());
        newDosage = dosageRepository.save(newDosage);
        LOG.debug(OUTPUT, newDosage);
        return newDosage;

    }

    private String getStatus(String id) {
        return medicamentStatementStatusRepository.findById(id).map(MedicationStatementStatus::getDescription).orElse(null);
    }

}

