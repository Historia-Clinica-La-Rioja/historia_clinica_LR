package net.pladema.clinichistory.requests.medicationrequests.service.impl;

import ar.lamansys.sgh.clinichistory.domain.ips.services.LoadMedications;
import ar.lamansys.sgh.clinichistory.domain.ips.services.SnomedService;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.ips.DosageRepository;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.ips.MedicationStatementRepository;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.ips.entity.Dosage;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.ips.entity.MedicationStatement;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.masterdata.entity.MedicationStatementStatus;
import ar.lamansys.sgh.clinichistory.domain.ips.services.MedicationCalculateStatus;
import ar.lamansys.sgh.clinichistory.application.document.DocumentService;
import ar.lamansys.sgh.clinichistory.application.notes.NoteService;
import ar.lamansys.sgh.clinichistory.domain.document.PatientInfoBo;
import ar.lamansys.sgh.clinichistory.domain.ips.DosageBo;
import ar.lamansys.sgh.clinichistory.domain.ips.HealthConditionBo;
import ar.lamansys.sgh.clinichistory.domain.ips.MedicationBo;
import ar.lamansys.sgh.clinichistory.domain.ips.EUnitsOfTimeBo;
import net.pladema.clinichistory.hospitalization.service.documents.validation.MedicationStatementValidator;
import net.pladema.clinichistory.hospitalization.service.documents.validation.PatientInfoValidator;
import net.pladema.clinichistory.requests.medicationrequests.service.ChangeStateMedicationService;
import net.pladema.clinichistory.requests.medicationrequests.service.domain.ChangeStateMedicationRequestBo;
import ar.lamansys.sgx.shared.dates.configuration.DateTimeProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.time.LocalDate;
import java.util.List;

@Service
public class ChangeStateMedicationServiceImpl implements ChangeStateMedicationService {

    private static final Logger LOG = LoggerFactory.getLogger(ChangeStateMedicationServiceImpl.class);

    private final LoadMedications loadMedications;

    private final DocumentService documentService;

    private final DosageRepository dosageRepository;

    private final MedicationStatementRepository medicationStatementRepository;

    private final SnomedService snomedService;

    private final MedicationCalculateStatus medicationCalculateStatus;
    
    private final NoteService noteService;

    private final DateTimeProvider dateTimeProvider;
    
    public ChangeStateMedicationServiceImpl(MedicationStatementRepository medicationStatementRepository,
                                            LoadMedications loadMedications,
                                            DosageRepository dosageRepository,
                                            DocumentService documentService,
                                            SnomedService snomedService,
                                            MedicationCalculateStatus medicationCalculateStatus,
                                            NoteService noteService,
                                            DateTimeProvider dateTimeProvider) {
        this.medicationStatementRepository = medicationStatementRepository;
        this.loadMedications = loadMedications;
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
                loadMedications.run(patient, documentMedication.getDocumentId(), List.of(newMedication));
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
        result.setStartDate(dosage.getStartDate() != null ? dosage.getStartDate() : dateTimeProvider.nowDateTime());
        result.setChronic(dosage.isChronic());
        result.setFrequency(dosage.getFrequency());
        result.setPeriodUnit(dosage.getPeriodUnit() != null ? EUnitsOfTimeBo.map(dosage.getPeriodUnit()) : EUnitsOfTimeBo.HOUR);

        if (MedicationStatementStatus.SUSPENDED.equals(newStatusId)){
            result.setSuspendedStartDate(dateTimeProvider.nowDate());
            LocalDate suspendedEndDate = result.getSuspendedStartDate().plusDays(duration.longValue());
            result.setSuspendedEndDate(suspendedEndDate);
        }

        if (MedicationStatementStatus.STOPPED.equals(newStatusId)) {
            result.setEndDate(dateTimeProvider.nowDateTime().minusDays(1));
            result.setSuspendedStartDate(null);
            result.setSuspendedEndDate(null);
        }

        if (MedicationStatementStatus.ACTIVE.equals(newStatusId)) {
            result.setSuspendedStartDate(null);
            result.setSuspendedEndDate(null);
        }

        return result;
    }
}