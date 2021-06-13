package net.pladema.clinichistory.documents.core.ips;

import net.pladema.clinichistory.documents.core.cie10.CalculateCie10Facade;
import net.pladema.clinichistory.documents.repository.ips.DosageRepository;
import net.pladema.clinichistory.documents.repository.ips.MedicationStatementRepository;
import net.pladema.clinichistory.documents.repository.ips.entity.Dosage;
import net.pladema.clinichistory.documents.repository.ips.entity.MedicationStatement;
import net.pladema.clinichistory.documents.repository.ips.masterdata.MedicamentStatementStatusRepository;
import net.pladema.clinichistory.documents.repository.ips.masterdata.entity.MedicationStatementStatus;
import net.pladema.clinichistory.documents.service.DocumentService;
import net.pladema.clinichistory.documents.service.NoteService;
import net.pladema.clinichistory.documents.service.domain.PatientInfoBo;
import net.pladema.clinichistory.documents.service.ips.SnomedService;
import net.pladema.clinichistory.documents.service.ips.domain.DosageBo;
import net.pladema.clinichistory.documents.service.ips.domain.HealthConditionBo;
import net.pladema.clinichistory.documents.service.ips.domain.MedicationBo;
import net.pladema.clinichistory.documents.service.ips.domain.SnomedBo;
import net.pladema.clinichistory.documents.service.ips.domain.enums.EUnitsOfTimeBo;
import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@DataJpaTest(showSql = false)
public class CreateMedicationServiceImplTest {

    private CreateMedicationServiceImpl medicationServiceImpl;

    @Autowired
    private MedicationStatementRepository medicationStatementRepository;

    @Autowired
    private DosageRepository dosageRepository;

    @Autowired
    private MedicamentStatementStatusRepository medicamentStatementStatusRepository;

    @MockBean
    private DocumentService documentService;

    @MockBean
    private SnomedService snomedService;

    @MockBean
    private CalculateCie10Facade calculateCie10Facade;

    @MockBean
    private NoteService noteService;

    @Before
    public void setUp() {
        medicationServiceImpl = new CreateMedicationServiceImpl(
                medicationStatementRepository,
                dosageRepository,
                medicamentStatementStatusRepository,
                documentService,
                snomedService,
                calculateCie10Facade,
                noteService
        );
    }

    @Test
    public void createDocument_withEmptyList() {
        var result = medicationServiceImpl.execute(new PatientInfoBo(1, (short)2, (short)2), 1l, Collections.emptyList());
        Assertions.assertThat(result.isEmpty())
                    .isTrue();
    }

    @Test
    public void createDocument_complete_success() {
        PatientInfoBo patientInfo =new PatientInfoBo(1, (short)2, (short)2);

        Integer snomedId = 1;
        when(snomedService.createSnomedTerm(new SnomedBo("IBUPROFENO", "IBUPROFENO"))).thenReturn(snomedId);
        when(noteService.createNote(any())).thenReturn(null);

        when(documentService.createDocumentMedication(any(), any())).thenReturn(null);


        MedicationBo medication = createMedicationBo("IBUPROFENO", 13,
                createDosageBo(15d,8, false, EUnitsOfTimeBo.HOUR, LocalDate.of(2020,05,25)));
        var result = medicationServiceImpl.execute(patientInfo, 1l, List.of(medication));
        Assertions.assertThat(result.size())
                .isEqualTo(1);

        MedicationBo resultMedication = result.get(0);

        Assertions.assertThat(resultMedication)
                .isNotNull();

        Assertions.assertThat(resultMedication.getId())
                .isNotNull();

        MedicationStatement medicationStatement = medicationStatementRepository.getOne(medication.getId());

        Assertions.assertThat(medicationStatement)
                .isNotNull();

        Assertions.assertThat(medicationStatement.getSnomedId())
                .isEqualTo(snomedId);

        Assertions.assertThat(medicationStatement.getStatusId())
                .isEqualTo(MedicationStatementStatus.ACTIVE);

        Assertions.assertThat(medicationStatement.getHealthConditionId())
                .isEqualTo(13);
        Assertions.assertThat(medicationStatement.getDosageId())
                .isNotNull();

        Dosage dosage = dosageRepository.findById(medicationStatement.getDosageId()).get();
        Assertions.assertThat(dosage)
                .isNotNull();

        Assertions.assertThat(dosage.getDuration())
                .isEqualTo(15d);

        Assertions.assertThat(dosage.getFrequency())
                .isEqualTo(8);

        Assertions.assertThat(dosage.getEndDate())
                .isNotNull()
                .isEqualTo(LocalDate.of(2020,06, 9));


        Assertions.assertThat(dosage.getPeriodUnit())
                .isEqualTo(EUnitsOfTimeBo.HOUR.getValue());
    }

    @Test
    public void createDocument_chronicMedication_success() {
        PatientInfoBo patientInfo = new PatientInfoBo(1, (short)2, (short)2);

        Integer snomedId = 1;
        when(snomedService.createSnomedTerm(new SnomedBo("IBUPROFENO", "IBUPROFENO"))).thenReturn(snomedId);
        when(noteService.createNote(any())).thenReturn(null);

        when(documentService.createDocumentMedication(any(), any())).thenReturn(null);


        MedicationBo medication = createMedicationBo("IBUPROFENO", 13,
                createDosageBo(15d,8, true, EUnitsOfTimeBo.HOUR, LocalDate.of(2020,05,25)));
        var result = medicationServiceImpl.execute(patientInfo, 1l, List.of(medication));
        Assertions.assertThat(result.size())
                .isEqualTo(1);

        MedicationBo resultMedication = result.get(0);

        Assertions.assertThat(resultMedication)
                .isNotNull();

        Assertions.assertThat(resultMedication.getId())
                .isNotNull();

        MedicationStatement medicationStatement = medicationStatementRepository.getOne(medication.getId());

        Assertions.assertThat(medicationStatement)
                .isNotNull();

        Assertions.assertThat(medicationStatement.getSnomedId())
                .isEqualTo(snomedId);

        Assertions.assertThat(medicationStatement.getStatusId())
                .isEqualTo(MedicationStatementStatus.ACTIVE);

        Assertions.assertThat(medicationStatement.getHealthConditionId())
                .isEqualTo(13);

        Assertions.assertThat(medicationStatement.getDosageId())
                .isNotNull();

        Dosage dosage = dosageRepository.findById(medicationStatement.getDosageId()).get();
        Assertions.assertThat(dosage)
                .isNotNull();

        Assertions.assertThat(dosage.getDuration())
                .isNull();

        Assertions.assertThat(dosage.getEndDate())
                .isNull();

        Assertions.assertThat(dosage.getFrequency())
                .isEqualTo(8);

        Assertions.assertThat(dosage.getPeriodUnit())
                .isEqualTo(EUnitsOfTimeBo.HOUR.getValue());
    }

    @Test
    public void createDocument_usual_medication_success() {
        PatientInfoBo patientInfo = new PatientInfoBo(1, (short)2, (short)2);

        Integer snomedId = 1;
        when(snomedService.createSnomedTerm(new SnomedBo("IBUPROFENO", "IBUPROFENO"))).thenReturn(snomedId);
        when(noteService.createNote(any())).thenReturn(null);

        when(documentService.createDocumentMedication(any(), any())).thenReturn(null);


        MedicationBo medication = createMedicationBo("IBUPROFENO", null,null);
        var result = medicationServiceImpl.execute(patientInfo, 1l, List.of(medication));
        Assertions.assertThat(result.size())
                .isEqualTo(1);

        MedicationBo resultMedication = result.get(0);

        Assertions.assertThat(resultMedication)
                .isNotNull();

        Assertions.assertThat(resultMedication.getId())
                .isNotNull();

        MedicationStatement medicationStatement = medicationStatementRepository.getOne(medication.getId());

        Assertions.assertThat(medicationStatement)
                .isNotNull();

        Assertions.assertThat(medicationStatement.getSnomedId())
                .isEqualTo(snomedId);

        Assertions.assertThat(medicationStatement.getStatusId())
                .isEqualTo(MedicationStatementStatus.ACTIVE);

        Assertions.assertThat(medicationStatement.getHealthConditionId())
                .isNull();

        Assertions.assertThat(medicationStatement.getDosageId())
                .isNull();
    }


    private DosageBo createDosageBo(Double duration, Integer frequency, boolean chronic, EUnitsOfTimeBo unitsOfTimeBo, LocalDate startDate) {
        DosageBo result = new DosageBo();
        result.setDuration(chronic ? null : duration);
        result.setFrequency(frequency);
        result.setPeriodUnit(unitsOfTimeBo);
        result.setStartDate(startDate);
        result.setChronic(chronic);
        return result;
    }

    private MedicationBo createMedicationBo(String sctid, Integer healthConditionId,
                                            DosageBo dosage) {
        MedicationBo result = new MedicationBo();
        result.setSnomed(new SnomedBo(sctid, sctid));

        HealthConditionBo hc = new HealthConditionBo();
        hc.setId(healthConditionId);
        result.setHealthCondition(hc);
        result.setDosage(dosage);
        result.setNote("Probando");
        return result;
    }
}