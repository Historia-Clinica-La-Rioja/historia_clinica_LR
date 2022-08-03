package ar.lamansys.sgh.clinichistory.domain.ips.services;

import ar.lamansys.sgh.clinichistory.application.calculatecie10.CalculateCie10Facade;
import ar.lamansys.sgh.clinichistory.application.calculatecie10.Cie10FacadeRuleFeature;
import ar.lamansys.sgh.clinichistory.application.document.DocumentService;
import ar.lamansys.sgh.clinichistory.application.notes.NoteService;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.ips.DosageRepository;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.ips.MedicationStatementRepository;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.ips.entity.Dosage;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.ips.entity.MedicationStatement;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.masterdata.MedicamentStatementStatusRepository;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.masterdata.entity.MedicationStatementStatus;
import ar.lamansys.sgh.clinichistory.domain.document.PatientInfoBo;
import ar.lamansys.sgh.clinichistory.domain.ips.DosageBo;
import ar.lamansys.sgh.clinichistory.domain.ips.MedicationBo;
import ar.lamansys.sgh.clinichistory.domain.ips.EUnitsOfTimeBo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.List;

@Service
public class LoadMedications {

    private static final Logger LOG = LoggerFactory.getLogger(LoadMedications.class);

    public static final String OUTPUT = "Output -> {}";

    private final MedicationStatementRepository medicationStatementRepository;

    private final DosageRepository dosageRepository;

    private final MedicamentStatementStatusRepository medicamentStatementStatusRepository;

    private final DocumentService documentService;

    private final SnomedService snomedService;

    private final CalculateCie10Facade calculateCie10Facade;

    private final NoteService noteService;

    public LoadMedications(MedicationStatementRepository medicationStatementRepository,
                           DosageRepository dosageRepository,
                           MedicamentStatementStatusRepository medicamentStatementStatusRepository,
                           DocumentService documentService,
                           SnomedService snomedService,
                           CalculateCie10Facade calculateCie10Facade,
                           NoteService noteService){
        this.medicationStatementRepository = medicationStatementRepository;
        this.dosageRepository = dosageRepository;
        this.medicamentStatementStatusRepository = medicamentStatementStatusRepository;
        this.documentService = documentService;
        this.snomedService = snomedService;
        this.calculateCie10Facade = calculateCie10Facade;
        this.noteService = noteService;
    }

    public List<MedicationBo> run(PatientInfoBo patientInfo, Long documentId, List<MedicationBo> medications) {
        LOG.debug("Input parameters -> patientInfo {}, documentId {}, medications {}", patientInfo, documentId, medications);
        assertRequiredFields(documentId);
        medications.forEach(medication -> {
			if(medication.getId()==null) {
				Integer snomedId = snomedService.getSnomedId(medication.getSnomed()).orElseGet(() -> snomedService.createSnomedTerm(medication.getSnomed()));
				String cie10Codes = calculateCie10Facade.execute(medication.getSnomed().getSctid(), new Cie10FacadeRuleFeature(patientInfo.getGenderId(), patientInfo.getAge()));
				MedicationStatement medicationStatement = saveMedicationStatement(patientInfo.getId(), medication, snomedId, cie10Codes);

				medication.setId(medicationStatement.getId());
				medication.setStatusId(medicationStatement.getStatusId());
				medication.setStatus(getStatus(medication.getStatusId()));
			}
            documentService.createDocumentMedication(documentId, medication.getId());
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
        newDosage.setSuspendedStartDate(dosage.getStartDate().toLocalDate());
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

