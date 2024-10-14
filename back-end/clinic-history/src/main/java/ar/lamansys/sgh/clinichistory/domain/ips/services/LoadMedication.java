package ar.lamansys.sgh.clinichistory.domain.ips.services;

import ar.lamansys.sgh.clinichistory.application.document.DocumentService;
import ar.lamansys.sgh.clinichistory.application.notes.NoteService;
import ar.lamansys.sgh.clinichistory.domain.ips.CommercialMedicationPrescriptionBo;
import ar.lamansys.sgh.clinichistory.domain.ips.DosageBo;
import ar.lamansys.sgh.clinichistory.domain.ips.MedicationBo;
import ar.lamansys.sgh.clinichistory.domain.ips.QuantityBo;
import ar.lamansys.sgh.clinichistory.domain.ips.SnomedBo;
import ar.lamansys.sgh.clinichistory.domain.ips.enums.EUnitsOfTimeBo;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.ips.DosageRepository;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.ips.MedicationStatementCommercialPrescriptionRepository;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.ips.MedicationStatementRepository;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.ips.QuantityRepository;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.ips.entity.Dosage;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.ips.entity.MedicationStatement;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.ips.entity.MedicationStatementCommercialPrescription;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.ips.entity.Quantity;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.masterdata.MedicamentStatementStatusRepository;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.masterdata.entity.MedicationStatementStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@Service
public class LoadMedication {

    public static final String OUTPUT = "Output -> {}";

    private final MedicationStatementRepository medicationStatementRepository;
    private final DosageRepository dosageRepository;
    private final MedicamentStatementStatusRepository medicamentStatementStatusRepository;
    private final DocumentService documentService;
    private final SnomedService snomedService;
    private final NoteService noteService;
    private final QuantityRepository quantityRepository;
	private final MedicationStatementCommercialPrescriptionRepository medicationStatementCommercialPrescriptionRepository;

    public MedicationBo run(Integer patientId, Long documentId, MedicationBo medicationBo) {
        log.debug("Input parameters -> patientId {}, documentId {}, medicationBo {}", patientId, medicationBo, medicationBo);
        this.assertRequiredFields(documentId, medicationBo);
        if (medicationBo.getId() == null) {
            Integer snomedId = getSnomedId(medicationBo.getSnomed());
            MedicationStatement medicationStatement = this.saveMedicationStatement(patientId, medicationBo, snomedId);
            medicationBo.setId(medicationStatement.getId());
            medicationBo.setStatusId(medicationStatement.getStatusId());
            medicationBo.setStatus(getStatus(medicationBo.getStatusId()));
        } else {
            medicationStatementRepository.findById(medicationBo.getId())
                    .ifPresent(med -> {
                        med.setStatusId(medicationBo.getStatusId());
                        medicationStatementRepository.save(med);
                    });
        }
        documentService.createDocumentMedication(documentId, medicationBo.getId());
        log.debug(OUTPUT, medicationBo);
        return medicationBo;
    }

	private Integer getSnomedId(SnomedBo medication) {
		return snomedService.getSnomedId(medication).orElseGet(() -> snomedService.createSnomedTerm(medication));
	}


	private MedicationStatement saveMedicationStatement(Integer patientId, MedicationBo medicationBo, Integer snomedId) {
        log.debug("Input parameters -> patientId {}, medication {}, snomedId {}", patientId, medicationBo, snomedId);
        Long quantityId = createQuantity(medicationBo.getDosage());
        Dosage newDosage = createDosage(medicationBo.getDosage(), quantityId);
        MedicationStatement medicationStatement = new MedicationStatement(
                patientId,
                snomedId,
                medicationBo.getStatusId(),
                medicationBo.getNoteId() != null ? medicationBo.getNoteId() : noteService.createNote(medicationBo.getNote()),
                medicationBo.getHealthCondition() != null ? medicationBo.getHealthCondition().getId() : null,
                newDosage != null ? newDosage.getId() : null,
                medicationBo.getPrescriptionLineNumber(),
                medicationBo.getIsDigital(),
                medicationBo.getPrescriptionDate(),
                medicationBo.getDueDate());
        medicationStatement.setUuid(UUID.randomUUID());
        medicationStatement = medicationStatementRepository.save(medicationStatement);
		if (medicationBo.getCommercialMedicationPrescription() != null)
			saveCommercialMedicationPrescription(medicationStatement.getId(), medicationBo.getCommercialMedicationPrescription());
		if (medicationBo.getSuggestedCommercialMedication() != null)
			medicationStatement.setSuggestedCommercialMedicationSnomedId(getSnomedId(medicationBo.getSuggestedCommercialMedication()));
        log.debug("medicationStatement saved -> {}", medicationStatement.getId());
        log.debug(OUTPUT, medicationStatement);
        return medicationStatement;
    }

	private void saveCommercialMedicationPrescription(Integer medicationStatementId,
													  CommercialMedicationPrescriptionBo commercialMedicationPrescription) {
		MedicationStatementCommercialPrescription entity = new MedicationStatementCommercialPrescription();
		entity.setMedicationStatementId(medicationStatementId);
		entity.setPresentationUnitQuantity(commercialMedicationPrescription.getPresentationUnitQuantity());
		entity.setMedicationPackQuantity(commercialMedicationPrescription.getMedicationPackQuantity());
		medicationStatementCommercialPrescriptionRepository.save(entity);
	}

	private void assertRequiredFields(Long documentId, MedicationBo medicationBo) {
        Assert.notNull(documentId, "El identificador del documento es obligatorio");
        Assert.notNull(medicationBo, "Parámetro de medicamento no puede ser vacío");
    }

    private Long createQuantity(DosageBo dosageBo) {
        log.debug("Input parameters -> dosageBo {}", dosageBo);
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
        log.debug(OUTPUT, quantityId);
        return quantityId;
    }

    private Dosage createDosage(DosageBo dosage, Long id) {
        log.debug("Input parameters -> dosage {}", dosage);
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
        log.debug(OUTPUT, newDosage);
        return newDosage;

    }

    private String getStatus(String id) {
        return medicamentStatementStatusRepository.findById(id).map(MedicationStatementStatus::getDescription).orElse(null);
    }

}
