package net.pladema.clinichistory.documents.core.ips;

import net.pladema.clinichistory.documents.repository.ips.DosageRepository;
import net.pladema.clinichistory.documents.repository.ips.MedicationStatementRepository;
import net.pladema.clinichistory.documents.repository.ips.entity.Dosage;
import net.pladema.clinichistory.documents.repository.ips.entity.MedicationStatement;
import net.pladema.clinichistory.documents.repository.ips.masterdata.MedicamentStatementStatusRepository;
import net.pladema.clinichistory.documents.repository.ips.masterdata.entity.MedicationStatementStatus;
import net.pladema.clinichistory.documents.service.DocumentService;
import net.pladema.clinichistory.documents.service.NoteService;
import net.pladema.clinichistory.documents.service.domain.PatientInfoBo;
import net.pladema.clinichistory.documents.service.ips.CreateMedicationService;
import net.pladema.clinichistory.documents.service.ips.SnomedService;
import net.pladema.clinichistory.documents.service.ips.domain.DosageBo;
import net.pladema.clinichistory.documents.service.ips.domain.MedicationBo;
import net.pladema.clinichistory.documents.service.ips.domain.enums.EUnitsOfTimeBo;
import net.pladema.snowstorm.services.CalculateCie10CodesService;
import net.pladema.snowstorm.services.domain.Cie10RuleFeature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.List;

@Service
public class CreateMedicationServiceImpl implements CreateMedicationService {

    private static final Logger LOG = LoggerFactory.getLogger(CreateMedicationServiceImpl.class);

    public static final String OUTPUT = "Output -> {}";

    private final MedicationStatementRepository medicationStatementRepository;

    private final DosageRepository dosageRepository;

    private final MedicamentStatementStatusRepository medicamentStatementStatusRepository;

    private final DocumentService documentService;

    private final SnomedService snomedService;

    private final CalculateCie10CodesService calculateCie10CodesService;

    private final NoteService noteService;

    public CreateMedicationServiceImpl(MedicationStatementRepository medicationStatementRepository,
                                 DosageRepository dosageRepository,
                                 MedicamentStatementStatusRepository medicamentStatementStatusRepository,
                                 DocumentService documentService,
                                 SnomedService snomedService,
                                 CalculateCie10CodesService calculateCie10CodesService,
                                 NoteService noteService){
        this.medicationStatementRepository = medicationStatementRepository;
        this.dosageRepository = dosageRepository;
        this.medicamentStatementStatusRepository = medicamentStatementStatusRepository;
        this.documentService = documentService;
        this.snomedService = snomedService;
        this.calculateCie10CodesService = calculateCie10CodesService;
        this.noteService = noteService;
    }

    @Override
    public List<MedicationBo> execute(PatientInfoBo patientInfo, Long documentId, List<MedicationBo> medications) {
        LOG.debug("Input parameters -> patientInfo {}, documentId {}, medications {}", patientInfo, documentId, medications);
        assertRequiredFields(documentId);
        medications.forEach(medication -> {
            Integer snomedId = snomedService.getSnomedId(medication.getSnomed())
                    .orElseGet(() -> snomedService.createSnomedTerm(medication.getSnomed()));
            String cie10Codes = calculateCie10CodesService.execute(medication.getSnomed().getSctid(),
                    new Cie10RuleFeature(patientInfo.getGenderId(), patientInfo.getAge()));
            MedicationStatement medicationStatement = saveMedicationStatement(patientInfo.getId(), medication, snomedId, cie10Codes);

            medication.setId(medicationStatement.getId());
            medication.setStatusId(medicationStatement.getStatusId());
            medication.setStatus(getStatus(medication.getStatusId()));

            documentService.createDocumentMedication(documentId, medicationStatement.getId());
        });
        List<MedicationBo> result = medications;
        LOG.debug(OUTPUT, result);
        return result;
    }



    private MedicationStatement saveMedicationStatement(Integer patientId, MedicationBo medicationBo, Integer snomedId, String cie10Codes) {
        LOG.debug("Input parameters -> patientId {}, medication {}, snomedId {}, cie10Codes {}", patientId, medicationBo, snomedId, cie10Codes);
        Dosage newDosage = createDosage(medicationBo.getDosage());
        MedicationStatement medicationStatement = new MedicationStatement(
                patientId,
                snomedId,
                cie10Codes,
                medicationBo.getStatusId(),
                medicationBo.getNoteId() != null ? medicationBo.getNoteId()  : noteService.createNote(medicationBo.getNote()),
                medicationBo.getHealthCondition() != null ? medicationBo.getHealthCondition().getId() : null,
                newDosage != null ? newDosage.getId() : null);

        medicationStatement = medicationStatementRepository.save(medicationStatement);
        LOG.debug("medicationStatement saved -> {}", medicationStatement.getId());
        LOG.debug(OUTPUT, medicationStatement);
        return medicationStatement;
    }

    private void assertRequiredFields(Long documentId) {
        LOG.debug("Input parameters -> documentId {}", documentId);
        Assert.notNull(documentId, "El identificador de la institución es obligatorio");
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
        if (EUnitsOfTimeBo.DAY.getValue().equals(dosage.getPeriodUnit()))
            newDosage.setFrequency(1);
        else newDosage.setFrequency(dosage.getFrequency());
        newDosage = dosageRepository.save(newDosage);
        LOG.debug(OUTPUT, newDosage);
        return newDosage;

    }

    private String getStatus(String id) {
        return medicamentStatementStatusRepository.findById(id).map(MedicationStatementStatus::getDescription).orElse(null);
    }

}

