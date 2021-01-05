package net.pladema.clinichistory.requests.medicationrequests.service.impl;

import net.pladema.clinichistory.documents.repository.ips.DosageRepository;
import net.pladema.clinichistory.documents.repository.ips.MedicationStatementRepository;
import net.pladema.clinichistory.documents.repository.ips.entity.Dosage;
import net.pladema.clinichistory.documents.repository.ips.entity.MedicationStatement;
import net.pladema.clinichistory.documents.repository.ips.masterdata.entity.MedicationStatementStatus;
import net.pladema.clinichistory.documents.service.DocumentService;
import net.pladema.clinichistory.documents.service.NoteService;
import net.pladema.clinichistory.documents.service.domain.PatientInfoBo;
import net.pladema.clinichistory.documents.service.ips.CreateMedicationService;
import net.pladema.clinichistory.documents.service.ips.SnomedService;
import net.pladema.clinichistory.documents.service.ips.domain.DosageBo;
import net.pladema.clinichistory.documents.service.ips.domain.HealthConditionBo;
import net.pladema.clinichistory.documents.service.ips.domain.MedicationBo;
import net.pladema.clinichistory.documents.service.ips.domain.enums.EUnitsOfTimeBo;
import net.pladema.clinichistory.hospitalization.service.documents.validation.MedicationStatementValidator;
import net.pladema.clinichistory.hospitalization.service.documents.validation.PatientInfoValidator;
import net.pladema.clinichistory.requests.medicationrequests.service.ChangeStateMedicationService;
import net.pladema.sgx.dates.configuration.DateTimeProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.List;

@Service
public class ChangeStateMedicationServiceImpl implements ChangeStateMedicationService {

    private static final Logger LOG = LoggerFactory.getLogger(ChangeStateMedicationServiceImpl.class);

    private final CreateMedicationService createMedicationService;

    private final DocumentService documentService;

    private final DosageRepository dosageRepository;

    private final NoteService noteService;

    private final MedicationStatementRepository medicationStatementRepository;

    private final SnomedService snomedService;

    private final DateTimeProvider dateTimeProvider;

    public ChangeStateMedicationServiceImpl(MedicationStatementRepository medicationStatementRepository,
                                            CreateMedicationService createMedicationService,
                                            DosageRepository dosageRepository,
                                            DocumentService documentService,
                                            NoteService noteService,
                                            SnomedService snomedService, DateTimeProvider dateTimeProvider) {
        this.medicationStatementRepository = medicationStatementRepository;
        this.createMedicationService = createMedicationService;
        this.dosageRepository = dosageRepository;
        this.documentService = documentService;
        this.noteService = noteService;
        this.snomedService = snomedService;
        this.dateTimeProvider = dateTimeProvider;
    }


    @Override
    @Transactional
    public void execute(PatientInfoBo patient, List<Integer> medicationsIds, String newStatusId, Short duration) {
        LOG.debug("Input parameters -> patient {}, medicationsIds {}, newStatusId {}, duration {}", patient, medicationsIds, newStatusId, duration);
        assertRequiredFields(patient, newStatusId);
        
        medicationsIds.forEach(mid -> {
            medicationStatementRepository.findById(mid).ifPresent(medication -> {
                var dosage = medication.getDosageId() != null ? dosageRepository.findById(medication.getDosageId()).get() : null;
                assertChangeState(newStatusId, duration, medication, dosage);

                var note = noteService.getDescriptionById(medication.getNoteId());
                MedicationBo newMedication = updateMedication(medication, dosage, note, newStatusId, duration);

                var documentMedication = documentService.getDocumentFromMedication(mid);
                createMedicationService.execute(patient, documentMedication.getDocumentId(), List.of(newMedication));
            });
        });
    }
    private void assertRequiredFields(PatientInfoBo patient, String newStatusId) {
        LOG.debug("Input parameters -> patient {}, newStatusId {}", patient, newStatusId);
        var patientInfoValidator = new PatientInfoValidator();
        patientInfoValidator.isValid(patient);

        var medicationStatusValidator = new MedicationStatementValidator();
        medicationStatusValidator.isValid(newStatusId);
    }

    private void assertChangeState(String newStatusId, Short duration, MedicationStatement medication, Dosage dosage) {
        LOG.debug("Input parameters -> newStatusId {}, duration {}, medication {}, dosage {}", newStatusId, duration, medication, dosage);
        Integer id = medication.getId();
        String currentStatusId = medication.getStatusId();
        if (MedicationStatementStatus.SUSPENDED.equals(newStatusId)){
            Assert.notNull(duration, "La cantidad de dias de suspensión es obligatoria");
            Assert.isTrue(!isStopped(dosage, currentStatusId), "La medicación con id "+ id + " no se puede suspender porque ya esta finalizada");
            Assert.isTrue(!isSuspended(dosage, currentStatusId), "La medicación con id "+ id + " no se puede suspender porque ya esta suspendida");
        }

        if (MedicationStatementStatus.STOPPED.equals(newStatusId)){
            Assert.isTrue(!isStopped(dosage, currentStatusId), "La medicación con id "+ id + " no se puede finalizar porque ya esta finalizada");
        }

        if (MedicationStatementStatus.ACTIVE.equals(newStatusId)) {
            Assert.isTrue(!isActive(dosage, currentStatusId), "La medicación con id "+ id + " no se puede activar porque ya esta activa");
            Assert.isTrue(!isStopped(dosage, currentStatusId), "La medicación con id "+ id + " no se puede activar porque ya esta finalizada");
        }
    }

    private boolean isActive(Dosage dosage, String statusId) {
        if (MedicationStatementStatus.SUSPENDED.equals(statusId) && !isSuspended(dosage, statusId))
            return true;
        if (MedicationStatementStatus.STOPPED.equals(statusId) && !isStopped(dosage, statusId))
            return true;
        return MedicationStatementStatus.ACTIVE.equals(statusId);
    }

    private boolean isSuspended(Dosage dosage, String statusId) {
        if (dosage == null && !MedicationStatementStatus.SUSPENDED.equals(statusId))
            return false;
        if (dosage == null)
            return true;
        return dosage.getSuspendedEndDate() != null &&  !dateTimeProvider.nowDate().isAfter(dosage.getSuspendedEndDate());

    }

    private boolean isStopped(Dosage dosage, String statusId) {
        if (dosage == null && !MedicationStatementStatus.STOPPED.equals(statusId))
            return false;
        if (dosage == null)
            return true;
        return dosage.getEndDate() != null && !dateTimeProvider.nowDate().isBefore(dosage.getEndDate());
    }

    private MedicationBo updateMedication(MedicationStatement medication, Dosage dosage, String note, String newStatusId, Short duration) {
        LOG.debug("Input parameters -> medication {}, dosage {}, note {}, statusId {}, duration {}", medication, dosage, note, newStatusId, duration);
        MedicationBo result = new MedicationBo();


        var hc = new HealthConditionBo();
        hc.setId(medication.getHealthConditionId());
        result.setHealthCondition(hc);

        result.setStatusId(newStatusId);
        result.setNote(note);

        result.setDosage(createDosage(dosage, newStatusId, duration));

        result.setSnomed(snomedService.getSnomed(medication.getSnomedId()));
        LOG.debug("Result {}", result);
        return result;
    }

    private DosageBo createDosage(Dosage dosage, String newStatusId, Short duration) {
        LOG.debug("Input parameters -> dosage {}, statusId {}, duration {}", dosage, newStatusId, duration);
        DosageBo result = new DosageBo();
        result.setDuration(dosage != null ? dosage.getDuration() : null);
        result.setStartDate(dosage != null ? dosage.getStartDate() : dateTimeProvider.nowDate());
        result.setChronic(dosage != null && dosage.getChronic());
        result.setFrequency(dosage != null ? dosage.getFrequency() : null);
        result.setPeriodUnit(dosage != null ? EUnitsOfTimeBo.map(dosage.getPeriodUnit()) : EUnitsOfTimeBo.HOUR);
        if (MedicationStatementStatus.SUSPENDED.equals(newStatusId)){
            result.setSuspendedStartDate(dateTimeProvider.nowDate());
            LocalDate suspendedEndDate = result.getSuspendedStartDate().plusDays(duration.longValue());
            result.setSuspendedEndDate(suspendedEndDate);
        }

        if (MedicationStatementStatus.STOPPED.equals(newStatusId))
            result.setEndDate(dateTimeProvider.nowDate());

        if (MedicationStatementStatus.ACTIVE.equals(newStatusId)) {
            result.setSuspendedStartDate(null);
            result.setSuspendedEndDate(null);
            result.setEndDate(null);
        }

        return result;
    }
}


