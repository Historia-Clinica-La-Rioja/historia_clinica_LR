package ar.lamansys.sgh.clinichistory.domain.ips.services;

import ar.lamansys.sgh.clinichistory.application.document.DocumentService;
import ar.lamansys.sgh.clinichistory.application.notes.NoteService;
import ar.lamansys.sgh.clinichistory.domain.ips.QuantityBo;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.ips.DosageRepository;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.ips.MedicationStatementRepository;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.ips.QuantityRepository;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.ips.entity.Dosage;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.ips.entity.MedicationStatement;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.ips.entity.Quantity;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.masterdata.MedicamentStatementStatusRepository;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.masterdata.entity.MedicationStatementStatus;
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
    private final NoteService noteService;

	private final QuantityRepository quantityRepository;

    public LoadMedications(MedicationStatementRepository medicationStatementRepository,
                           DosageRepository dosageRepository,
                           MedicamentStatementStatusRepository medicamentStatementStatusRepository,
                           DocumentService documentService,
                           SnomedService snomedService,
                           NoteService noteService,
						   QuantityRepository quantityRepository){
        this.medicationStatementRepository = medicationStatementRepository;
        this.dosageRepository = dosageRepository;
        this.medicamentStatementStatusRepository = medicamentStatementStatusRepository;
        this.documentService = documentService;
        this.snomedService = snomedService;
        this.noteService = noteService;
		this.quantityRepository = quantityRepository;
    }

    public List<MedicationBo> run(Integer patientId, Long documentId, List<MedicationBo> medications) {
        LOG.debug("Input parameters -> patientId {}, documentId {}, medications {}", patientId, documentId, medications);
        assertRequiredFields(documentId);
        medications.forEach(medication -> {
			if(medication.getId()==null) {
				Integer snomedId = snomedService.getSnomedId(medication.getSnomed()).orElseGet(() -> snomedService.createSnomedTerm(medication.getSnomed()));
				MedicationStatement medicationStatement = saveMedicationStatement(patientId, medication, snomedId);
				medication.setId(medicationStatement.getId());
				medication.setStatusId(medicationStatement.getStatusId());
				medication.setStatus(getStatus(medication.getStatusId()));
			} else {
				medicationStatementRepository.findById(medication.getId()).ifPresent(med -> {
					med.setStatusId(medication.getStatusId());
					medicationStatementRepository.save(med);
				});
			}
            documentService.createDocumentMedication(documentId, medication.getId());
        });
        List<MedicationBo> result = medications;
        LOG.debug(OUTPUT, result);
        return result;
    }



    private MedicationStatement saveMedicationStatement(Integer patientId, MedicationBo medicationBo, Integer snomedId) {
        LOG.debug("Input parameters -> patientId {}, medication {}, snomedId {}", patientId, medicationBo, snomedId);
		Long quantityId = createQuantity(medicationBo.getDosage());
		Dosage newDosage = createDosage(medicationBo.getDosage(), quantityId);
		MedicationStatement medicationStatement = new MedicationStatement(
                patientId,
                snomedId,
                medicationBo.getStatusId(),
                medicationBo.getNoteId() != null ? medicationBo.getNoteId()  : noteService.createNote(medicationBo.getNote()),
                medicationBo.getHealthCondition() != null ? medicationBo.getHealthCondition().getId() : null,
                newDosage != null ? newDosage.getId() : null,
				medicationBo.getPrescriptionLineNumber(),
				medicationBo.getIsDigital(),
				medicationBo.getPrescriptionDate(),
				medicationBo.getDueDate());

        medicationStatement = medicationStatementRepository.save(medicationStatement);
        LOG.debug("medicationStatement saved -> {}", medicationStatement.getId());
        LOG.debug(OUTPUT, medicationStatement);
        return medicationStatement;
    }

    private void assertRequiredFields(Long documentId) {
        LOG.debug("Input parameters -> documentId {}", documentId);
        Assert.notNull(documentId, "El identificador de la institución es obligatorio");
    }

	private Long createQuantity(DosageBo dosageBo) {
		LOG.debug("Input parameters -> dosageBo {}", dosageBo);
		if (dosageBo == null) {
			return null;
		}
		QuantityBo quantityBo = dosageBo.getQuantity();
		Quantity quantity = new Quantity();
		Long quantityId = null;
		if (quantityBo != null && quantityBo.getValue() != null) {
			quantity.setValue(quantityBo.getValue().doubleValue());
			quantity.setUnit(quantityBo.getUnit());
			quantityId = quantityRepository.save(quantity).getId();
		}
		LOG.debug(OUTPUT, quantityId);
		return quantityId;
	}

    private Dosage createDosage(DosageBo dosage, Long id) {
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
		newDosage.setDosesByDay(dosage.getDosesByDay());
		newDosage.setDosesByUnit(dosage.getDosesByUnit());
		newDosage.setDoseQuantityId(id);
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

