package ar.lamansys.sgh.clinichistory.domain.ips.services;

import ar.lamansys.sgh.clinichistory.application.document.DocumentService;
import ar.lamansys.sgh.clinichistory.application.notes.NoteService;
import ar.lamansys.sgh.clinichistory.domain.ips.AnestheticSubstanceBo;
import ar.lamansys.sgh.clinichistory.domain.ips.DosageBo;
import ar.lamansys.sgh.clinichistory.domain.ips.QuantityBo;
import ar.lamansys.sgh.clinichistory.domain.ips.enums.EUnitsOfTimeBo;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.ips.DosageRepository;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.ips.AnestheticSubstanceRepository;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.ips.QuantityRepository;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.ips.entity.Dosage;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.ips.entity.AnestheticSubstance;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.ips.entity.Quantity;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import static java.util.Objects.nonNull;

@Slf4j
@RequiredArgsConstructor
@Service
public class LoadAnestheticSubstances {

    private final QuantityRepository quantityRepository;
    private final DosageRepository dosageRepository;
    private final SnomedService snomedService;
    private final AnestheticSubstanceRepository anestheticSubstanceRepository;
    private final NoteService noteService;
    private final DocumentService documentService;

    public List<? extends AnestheticSubstanceBo> run(Long documentId, List<? extends AnestheticSubstanceBo> substances) {
        log.debug("Input parameters -> documentId {} substances {}", documentId, substances);

        substances.forEach(anestheticSubstanceBo -> this.createAnestheticSubstance(documentId, anestheticSubstanceBo));

        log.debug("Output -> {}", substances);
        return substances;
    }

    private void createAnestheticSubstance(Long documentId, AnestheticSubstanceBo anestheticSubstanceBo) {
        if (anestheticSubstanceBo.getId() == null) {
            this.saveEntity(documentId, anestheticSubstanceBo);
        }
        documentService.createDocumentAnestheticSubstance(documentId, anestheticSubstanceBo.getId());
    }

    private void saveEntity(Long documentId, AnestheticSubstanceBo anestheticSubstanceBo) {
        Integer snomedId = snomedService.getSnomedId(anestheticSubstanceBo.getSnomed())
                .orElseGet(() -> snomedService.createSnomedTerm(anestheticSubstanceBo.getSnomed()));
        Long quantityId = this.createQuantity(anestheticSubstanceBo.getDosage());
        Integer dosageId = this.createDosage(anestheticSubstanceBo.getDosage(), quantityId);
        Short viaId = anestheticSubstanceBo.getViaId();
        String viaNote = anestheticSubstanceBo.getViaNote();
        Long viaNoteId = null;
        if (viaNote != null)
            viaNoteId = noteService.createNote(viaNote);
        Short typeId = anestheticSubstanceBo.getTypeId();

        AnestheticSubstance anestheticSubstance = anestheticSubstanceRepository.save(new AnestheticSubstance(null, documentId, snomedId, dosageId, viaId, viaNoteId, typeId));
        anestheticSubstanceBo.setId(anestheticSubstance.getId());
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
        newDosage.setStartDate(nonNull(dosage.getStartDate()) ? dosage.getStartDate() : LocalDateTime.now());
        newDosage.setEndDate(dosage.getEndDate());
        newDosage.setSuspendedStartDate(dosage.getSuspendedStartDate());
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
