package ar.lamansys.sgh.clinichistory.domain.ips.services;

import ar.lamansys.sgh.clinichistory.application.document.DocumentService;
import ar.lamansys.sgh.clinichistory.domain.ips.DosageBo;
import ar.lamansys.sgh.clinichistory.domain.ips.PreMedicationBo;
import ar.lamansys.sgh.clinichistory.domain.ips.QuantityBo;
import ar.lamansys.sgh.clinichistory.domain.ips.enums.EUnitsOfTimeBo;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.ips.DosageRepository;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.ips.PreMedicationRepository;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.ips.QuantityRepository;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.ips.entity.Dosage;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.ips.entity.PreMedication;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.ips.entity.Quantity;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class LoadPreMedications {

    private final QuantityRepository quantityRepository;
    private final DosageRepository dosageRepository;
    private final SnomedService snomedService;
    private final PreMedicationRepository preMedicationRepository;
    private final DocumentService documentService;

    public List<PreMedicationBo> run(Long documentId, List<PreMedicationBo> preMedications) {
        log.debug("Input parameters -> documentId {} preMedications {}", documentId, preMedications);

        preMedications.forEach((medication) -> {
            if (medication.getId() == null) {
                Integer snomedId = snomedService.getSnomedId(medication.getSnomed())
                        .orElseGet(() -> snomedService.createSnomedTerm(medication.getSnomed()));
                Long quantityId = createQuantity(medication.getDosage());
                Integer dosageId = createDosage(medication.getDosage(), quantityId);
                Short viaId = medication.getViaId();

                PreMedication preMedication = preMedicationRepository.save(new PreMedication(null, documentId, snomedId, dosageId, viaId));
                medication.setId(preMedication.getId());
            }
            documentService.createDocumentPreMedication(documentId, medication.getId());
        });

        log.debug("Output -> {}", preMedications);
        return preMedications;
    }

    private Long createQuantity(DosageBo dosageBo) {
        log.trace("Input parameters -> dosageBo {}", dosageBo);
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
        log.trace("Output -> {}", quantityId);
        return quantityId;
    }

    private Integer createDosage(DosageBo dosage, Long quantityId) {
        log.trace("Input parameters -> dosage {} quantityId {}", dosage, quantityId);
        if (dosage == null)
            return null;
        Dosage newDosage = new Dosage();
        newDosage.setChronic(dosage.isChronic());
        newDosage.setStartDate(dosage.getStartDate());
        newDosage.setEndDate(dosage.getEndDate());
        newDosage.setSuspendedStartDate(dosage.getStartDate().toLocalDate());
        newDosage.setSuspendedEndDate(dosage.getSuspendedEndDate());
        newDosage.setDuration(!dosage.isChronic() ? dosage.getDuration() : null);
        newDosage.setDurationUnit(EUnitsOfTimeBo.EVENT.getValue());
        newDosage.setPeriodUnit(dosage.getPeriodUnit());
        newDosage.setDosesByDay(dosage.getDosesByDay());
        newDosage.setDosesByUnit(dosage.getDosesByUnit());
        newDosage.setDoseQuantityId(quantityId);
        newDosage.setFrequency(dosage.getFrequency());
        newDosage = dosageRepository.save(newDosage);
        Integer result = newDosage.getId();
        log.trace("Output -> {}", result);
        return result;

    }
}
