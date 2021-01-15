package net.pladema.clinichistory.requests.medicationrequests.service.impl;

import net.pladema.clinichistory.documents.core.ips.MedicationCalculateStatus;
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
import net.pladema.clinichistory.requests.medicationrequests.service.domain.ChangeStateMedicationRequestBo;
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

    private final MedicationStatementRepository medicationStatementRepository;

    private final SnomedService snomedService;

    private final MedicationCalculateStatus medicationCalculateStatus;
    
    private final NoteService noteService;

    private final DateTimeProvider dateTimeProvider;
    
    public ChangeStateMedicationServiceImpl(MedicationStatementRepository medicationStatementRepository,
                                            CreateMedicationService createMedicationService,
                                            DosageRepository dosageRepository,
                                            DocumentService documentService,
                                            SnomedService snomedService,
                                            MedicationCalculateStatus medicationCalculateStatus,
                                            NoteService noteService,
                                            DateTimeProvider dateTimeProvider) {
        this.medicationStatementRepository = medicationStatementRepository;
        this.createMedicationService = createMedicationService;
        this.dosageRepository = dosageRepository;
        this.documentService = documentService;
        this.snomedService = snomedService;
        this.medicationCalculateStatus = medicationCalculateStatus;
        this.noteService = noteService;
        this.dateTimeProvider = dateTimeProvider;
    }


    @Override
    @Transactional
    public void execute(PatientInfoBo patient, ChangeStateMedicationRequestBo changeStateMedicationRequestBo) {
        LOG.debug("Input parameters -> patient {}, changeStateMedicationRequestBo {}", patient, changeStateMedicationRequestBo);
        String newStatusId = changeStateMedicationRequestBo.getStatusId();
        Double duration = changeStateMedicationRequestBo.getDayQuantity();
        assertRequiredFields(patient, newStatusId);

        changeStateMedicationRequestBo.getMedicationsIds().forEach(mid ->
            medicationStatementRepository.findById(mid).ifPresent(medication -> {
                var dosage = parseTo(medication.getDosageId() != null ? dosageRepository.findById(medication.getDosageId()).orElse(null) : null);
                assertChangeState(newStatusId, duration, medication, dosage);

                MedicationBo newMedication = updateMedication(medication, dosage, newStatusId, duration, changeStateMedicationRequestBo.getObservations());

                var documentMedication = documentService.getDocumentFromMedication(mid);
                createMedicationService.execute(patient, documentMedication.getDocumentId(), List.of(newMedication));
            })
        );
    }
    private void assertRequiredFields(PatientInfoBo patient, String newStatusId) {
        LOG.debug("Input parameters -> patient {}, newStatusId {}", patient, newStatusId);
        var patientInfoValidator = new PatientInfoValidator();
        patientInfoValidator.isValid(patient);

        var medicationStatusValidator = new MedicationStatementValidator();
        medicationStatusValidator.isValid(newStatusId);
    }

    private void assertChangeState(String newStatusId, Double duration, MedicationStatement medication, DosageBo dosage) {
        LOG.debug("Input parameters -> newStatusId {}, duration {}, medication {}, dosage {}", newStatusId, duration, medication, dosage);
        final String LA_MEDICACIÓN_CON_ID = "La medicación con id ";
        Integer id = medication.getId();
        String currentStatusId = medication.getStatusId();
        if (MedicationStatementStatus.SUSPENDED.equals(newStatusId)){
            Assert.notNull(duration, "La cantidad de dias de suspensión es obligatoria");
            Assert.isTrue(!MedicationStatementStatus.STOPPED.equals(medicationCalculateStatus.execute(currentStatusId, dosage)), LA_MEDICACIÓN_CON_ID + id + " no se puede suspender porque ya esta finalizada");
            Assert.isTrue(!MedicationStatementStatus.SUSPENDED.equals(medicationCalculateStatus.execute(currentStatusId, dosage)), LA_MEDICACIÓN_CON_ID+ id + " no se puede suspender porque ya esta suspendida");
        }

        if (MedicationStatementStatus.STOPPED.equals(newStatusId)){
            Assert.isTrue(!MedicationStatementStatus.STOPPED.equals(medicationCalculateStatus.execute(currentStatusId, dosage)), LA_MEDICACIÓN_CON_ID+ id + " no se puede finalizar porque ya esta finalizada");
        }

        if (MedicationStatementStatus.ACTIVE.equals(newStatusId)) {
            Assert.isTrue(!MedicationStatementStatus.ACTIVE.equals(medicationCalculateStatus.execute(currentStatusId, dosage)), LA_MEDICACIÓN_CON_ID+ id + " no se puede activar porque ya esta activa");
            Assert.isTrue(!MedicationStatementStatus.STOPPED.equals(medicationCalculateStatus.execute(currentStatusId, dosage)), LA_MEDICACIÓN_CON_ID+ id + " no se puede activar porque ya esta finalizada");
        }
    }

    private DosageBo parseTo(Dosage dosage) {
        LOG.debug("Input parameters -> dosage {}", dosage);
        DosageBo result = new DosageBo();
        if (dosage == null)
            return result;
        result.setId(dosage.getId());
        result.setDuration(dosage.getDuration());
        result.setStartDate(dosage.getStartDate());
        result.setEndDate(dosage.getEndDate());
        result.setChronic(dosage.getChronic() != null && dosage.getChronic());
        result.setFrequency(dosage.getFrequency());
        result.setPeriodUnit(EUnitsOfTimeBo.map(dosage.getPeriodUnit()));
        result.setSuspendedStartDate(dosage.getSuspendedStartDate());
        result.setSuspendedEndDate(dosage.getSuspendedEndDate());
        return result;
    }

    private MedicationBo updateMedication(MedicationStatement medication, DosageBo dosage, String newStatusId, Double duration, String observations) {
        LOG.debug("Input parameters -> medication {}, dosage {}, statusId {}, duration {}, observations {}", medication, dosage, newStatusId, duration, observations);
        MedicationBo result = new MedicationBo();


        var hc = new HealthConditionBo();
        hc.setId(medication.getHealthConditionId());
        result.setHealthCondition(hc);

        result.setStatusId(newStatusId);
        result.setNoteId(observations != null ? noteService.createNote(observations) : medication.getNoteId());

        result.setDosage(create(dosage, newStatusId, duration));

        result.setSnomed(snomedService.getSnomed(medication.getSnomedId()));
        LOG.debug("Result {}", result);
        return result;
    }

    private DosageBo create(DosageBo dosage, String newStatusId, Double duration) {
        LOG.debug("Input parameters -> dosage {}, statusId {}, duration {}", dosage, newStatusId, duration);
        DosageBo result = new DosageBo();
        if (dosage.getDuration() != null )
            result.setDuration(dosage.getDuration());
        else result.setDuration(duration);
        result.setStartDate(dosage.getStartDate() != null ? dosage.getStartDate() : dateTimeProvider.nowDate());
        result.setChronic(dosage.isChronic());
        result.setFrequency(dosage.getFrequency());
        result.setPeriodUnit(dosage.getPeriodUnit() != null ? EUnitsOfTimeBo.map(dosage.getPeriodUnit()) : EUnitsOfTimeBo.HOUR);
        if (MedicationStatementStatus.SUSPENDED.equals(newStatusId)){
            result.setSuspendedStartDate(dateTimeProvider.nowDate());
            LocalDate suspendedEndDate = result.getSuspendedStartDate().plusDays(duration.longValue());
            result.setSuspendedEndDate(suspendedEndDate);
        }

        if (MedicationStatementStatus.STOPPED.equals(newStatusId)) {
            result.setEndDate(dateTimeProvider.nowDate());
            result.setSuspendedStartDate(null);
            result.setSuspendedEndDate(null);
            result.setChronic(false);
        }

        if (MedicationStatementStatus.ACTIVE.equals(newStatusId)) {
            result.setSuspendedStartDate(null);
            result.setSuspendedEndDate(null);
        }

        return result;
    }
}