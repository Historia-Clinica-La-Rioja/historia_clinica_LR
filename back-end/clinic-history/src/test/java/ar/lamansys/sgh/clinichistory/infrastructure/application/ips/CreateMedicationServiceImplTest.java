package ar.lamansys.sgh.clinichistory.infrastructure.application.ips;

import ar.lamansys.sgh.clinichistory.UnitRepository;
import ar.lamansys.sgh.clinichistory.application.document.DocumentService;
import ar.lamansys.sgh.clinichistory.application.notes.NoteService;
import ar.lamansys.sgh.clinichistory.domain.ips.DosageBo;
import ar.lamansys.sgh.clinichistory.domain.ips.EUnitsOfTimeBo;
import ar.lamansys.sgh.clinichistory.domain.ips.HealthConditionBo;
import ar.lamansys.sgh.clinichistory.domain.ips.MedicationBo;
import ar.lamansys.sgh.clinichistory.domain.ips.QuantityBo;
import ar.lamansys.sgh.clinichistory.domain.ips.SnomedBo;
import ar.lamansys.sgh.clinichistory.domain.ips.services.LoadMedications;
import ar.lamansys.sgh.clinichistory.domain.ips.services.SnomedService;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.DocumentRepository;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.DocumentRiskFactorRepository;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.ips.DosageRepository;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.ips.MedicationStatementRepository;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.ips.QuantityRepository;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.ips.entity.Dosage;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.ips.entity.MedicationStatement;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.masterdata.MedicamentStatementStatusRepository;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.masterdata.SnomedRepository;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.masterdata.entity.MedicationStatementStatus;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class CreateMedicationServiceImplTest extends UnitRepository {

    private LoadMedications medicationServiceImpl;

    @Autowired
    private MedicationStatementRepository medicationStatementRepository;

    @Autowired
    private DosageRepository dosageRepository;

    @Autowired
    private MedicamentStatementStatusRepository medicamentStatementStatusRepository;

	@Autowired
	private QuantityRepository quantityRepository;

    @MockBean
    private DocumentService documentService;

    @MockBean
    private SnomedService snomedService;

    @MockBean
    private NoteService noteService;

	@MockBean
	private SnomedRepository snomedRepository;

	@MockBean
	private DocumentRiskFactorRepository documentRiskFactorRepository;

	@MockBean
	private DocumentRepository documentRepository;

    @BeforeEach
    void setUp() {
        medicationServiceImpl = new LoadMedications(
                medicationStatementRepository,
                dosageRepository,
                medicamentStatementStatusRepository,
                documentService,
                snomedService,
                noteService,
                quantityRepository
        );
    }

    @Test
    void createDocument_withEmptyList() {
        var result = medicationServiceImpl.run(1, 1l, Collections.emptyList());
        Assertions.assertThat(result.isEmpty())
                    .isTrue();
    }

    @Test
    void createDocument_complete_success() {
        Integer patientInfo = 1;

        Integer snomedId = 1;
        when(snomedService.createSnomedTerm(new SnomedBo("IBUPROFENO", "IBUPROFENO"))).thenReturn(snomedId);
        when(noteService.createNote(any())).thenReturn(null);

        when(documentService.createDocumentMedication(any(), any())).thenReturn(null);


        MedicationBo medication = createMedicationBo("IBUPROFENO", 13,
                createDosageBo(15d,8, false, EUnitsOfTimeBo.HOUR, LocalDateTime.of(2020,05,25,0,0,0), new QuantityBo(1,"ml")));
        var result = medicationServiceImpl.run(patientInfo, 1l, List.of(medication));
        Assertions.assertThat(result.size())
                .isEqualTo(1);

        MedicationBo resultMedication = result.get(0);

        Assertions.assertThat(resultMedication)
                .isNotNull();

        Assertions.assertThat(resultMedication.getId())
                .isNotNull();

        MedicationStatement medicationStatement = medicationStatementRepository.findById(medication.getId()).get();

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
                .isEqualTo(LocalDateTime.of(2020,06, 9,0,0,0));


        Assertions.assertThat(dosage.getPeriodUnit())
                .isEqualTo(EUnitsOfTimeBo.HOUR.getValue());
    }

    @Test
    void createDocument_chronicMedication_success() {
        Integer patientInfo = 1;

        Integer snomedId = 1;
        when(snomedService.createSnomedTerm(new SnomedBo("IBUPROFENO", "IBUPROFENO"))).thenReturn(snomedId);
        when(noteService.createNote(any())).thenReturn(null);

        when(documentService.createDocumentMedication(any(), any())).thenReturn(null);


        MedicationBo medication = createMedicationBo("IBUPROFENO", 13,
                createDosageBo(15d,8, true, EUnitsOfTimeBo.HOUR, LocalDateTime.of(2020,05,25,0,0,0), new QuantityBo(1,"ml")));
        var result = medicationServiceImpl.run(patientInfo, 1l, List.of(medication));
        Assertions.assertThat(result.size())
                .isEqualTo(1);

        MedicationBo resultMedication = result.get(0);

        Assertions.assertThat(resultMedication)
                .isNotNull();

        Assertions.assertThat(resultMedication.getId())
                .isNotNull();

        MedicationStatement medicationStatement = medicationStatementRepository.findById(medication.getId()).get();

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
    void createDocument_usual_medication_success() {
        Integer patientInfo = 1;

        Integer snomedId = 1;
        when(snomedService.createSnomedTerm(new SnomedBo("IBUPROFENO", "IBUPROFENO"))).thenReturn(snomedId);
        when(noteService.createNote(any())).thenReturn(null);

        when(documentService.createDocumentMedication(any(), any())).thenReturn(null);


        MedicationBo medication = createMedicationBo("IBUPROFENO", null,createDosageBo(15d,8, true, EUnitsOfTimeBo.HOUR, LocalDateTime.of(2020,05,25,0,0,0), new QuantityBo(2,"ml")));
        var result = medicationServiceImpl.run(patientInfo, 1l, List.of(medication));
        Assertions.assertThat(result.size())
                .isEqualTo(1);

        MedicationBo resultMedication = result.get(0);

        Assertions.assertThat(resultMedication)
                .isNotNull();

        Assertions.assertThat(resultMedication.getId())
                .isNotNull();

        MedicationStatement medicationStatement = medicationStatementRepository.findById(medication.getId()).get();

        Assertions.assertThat(medicationStatement)
                .isNotNull();

        Assertions.assertThat(medicationStatement.getSnomedId())
                .isEqualTo(snomedId);

        Assertions.assertThat(medicationStatement.getStatusId())
                .isEqualTo(MedicationStatementStatus.ACTIVE);

        Assertions.assertThat(medicationStatement.getHealthConditionId())
                .isNull();

        Assertions.assertThat(medicationStatement.getDosageId())
                .isNotNull();
    }


    private DosageBo createDosageBo(Double duration, Integer frequency, boolean chronic, EUnitsOfTimeBo unitsOfTimeBo, LocalDateTime startDate, QuantityBo quantityBo) {
        DosageBo result = new DosageBo();
        result.setDuration(chronic ? null : duration);
        result.setFrequency(frequency);
        result.setPeriodUnit(unitsOfTimeBo);
        result.setStartDate(startDate);
        result.setChronic(chronic);
		result.setQuantity(quantityBo);
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