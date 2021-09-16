package net.pladema.nomivac.application.synchronizeImmunization;

import ar.lamansys.sgh.shared.infrastructure.input.service.BasicDataPersonDto;
import ar.lamansys.sgh.shared.infrastructure.input.service.BasicPatientDto;
import ar.lamansys.sgh.shared.infrastructure.input.service.SharedPatientPort;
import ar.lamansys.sgh.shared.infrastructure.input.service.institution.InstitutionInfoDto;
import ar.lamansys.sgh.shared.infrastructure.input.service.institution.SharedInstitutionPort;
import ar.lamansys.sgx.shared.dates.configuration.DateTimeProvider;
import ar.lamansys.sgx.shared.scheduling.infrastructure.output.service.SyncErrorService;
import net.pladema.nomivac.infrastructure.output.immunization.repository.NomivacImmunizationSync;
import net.pladema.nomivac.infrastructure.output.immunization.repository.NomivacImmunizationSyncRepository;
import net.pladema.nomivac.infrastructure.output.immunization.repository.VNomivacImmunizationData;
import net.pladema.nomivac.infrastructure.output.immunization.repository.VNomivacImmunizationDataRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource("classpath:nomivac-e2e-test.properties")
@Disabled
class SynchronizeImmunizationE2ETest {

    private static final String COMPLETE = "255594003";

    @Autowired
    private SynchronizeImmunization synchronizeImmunization;

    @MockBean
    private DateTimeProvider dateTimeProvider;

    @MockBean
    private SharedPatientPort sharedPatientPort;

    @MockBean
    private SharedInstitutionPort sharedInstitutionPort;

    @Autowired
    private VNomivacImmunizationDataRepository VNomivacImmunizationDataRepository;

    @Autowired
    private NomivacImmunizationSyncRepository nomivacImmunizationSyncRepository;

    @MockBean
    private SyncErrorService<NomivacImmunizationSync, Integer> syncErrorService;

    @Test
    void success(){
        when(dateTimeProvider.nowDateTime()).thenReturn(LocalDateTime.of(2020,12,10,10,10));
        when(sharedPatientPort.getBasicDataFromPatient(any())).thenReturn(new BasicPatientDto(14, mockValidPerson(), (short)1));
        when(sharedInstitutionPort.fetchInstitutionById(any())).thenReturn(new InstitutionInfoDto(55, "Hospital ejemplo", "50460142155193"));
        VNomivacImmunizationDataRepository.save(new VNomivacImmunizationData(1, 14,
                "871822003", "vacuna que contiene antígeno de virus Hepatitis B como único ingrediente (producto medicinal)",
                COMPLETE, LocalDate.of(2025, 04, 29), null, 14,
                (short)3, "Calendario Nacional", (short)109, "Regular", "2da dosis", (short)2,
                "WVX12003",null, true, LocalDateTime.of(2020,10,10,10,10), -1));
        synchronizeImmunization.run();
        var result = nomivacImmunizationSyncRepository.findById(1);
        Assertions.assertTrue(result.isPresent());
        Assertions.assertEquals(502, result.get().getStatusCode());
    }


    @Test
    void invalidVaccineConditionSchemeDoseCombination(){
        when(dateTimeProvider.nowDateTime()).thenReturn(LocalDateTime.of(2020,12,10,10,10));
        when(sharedPatientPort.getBasicDataFromPatient(any())).thenReturn(new BasicPatientDto(14, mockValidPerson(), (short)1));
        when(sharedInstitutionPort.fetchInstitutionById(any())).thenReturn(new InstitutionInfoDto(55, "Hospital ejemplo", "50460142155193"));

        VNomivacImmunizationDataRepository.save(new VNomivacImmunizationData(1, 14,
                "1052330009", "vacuna antineumocócica conjugada decavalente",
                COMPLETE, LocalDate.of(2025, 04, 29), null, 14,
                (short)3, "Calendario Nacional", (short)109, "Regular", "2da dosis", (short)2,
                "WVX12003",null, true, LocalDateTime.of(2020,10,10,10,10), -1));
        synchronizeImmunization.run();
    }

    @BeforeEach
    void cleanDatabase(){
        nomivacImmunizationSyncRepository.deleteAll();
        VNomivacImmunizationDataRepository.deleteAll();
    }

    private BasicDataPersonDto mockValidPerson(){
        var result = new BasicDataPersonDto();
        result.setFirstName("Gerónimo");
        result.setLastName("Carrasquill");
        result.setIdentificationNumber("13694305");
        return result;
    }

}