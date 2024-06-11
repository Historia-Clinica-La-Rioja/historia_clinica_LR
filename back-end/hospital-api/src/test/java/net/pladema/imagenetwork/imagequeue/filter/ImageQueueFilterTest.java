package net.pladema.imagenetwork.imagequeue.filter;

import ar.lamansys.sgh.shared.infrastructure.input.service.GenderDto;
import ar.lamansys.sgh.shared.infrastructure.input.service.PersonAgeDto;
import net.pladema.imagenetwork.imagequeue.application.filter.ImageQueueFilter;
import net.pladema.imagenetwork.imagequeue.domain.EImageMoveStatus;
import net.pladema.imagenetwork.imagequeue.domain.ImageQueuePatientBo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import net.pladema.imagenetwork.imagequeue.domain.ImageQueueBo;
import net.pladema.imagenetwork.imagequeue.domain.ImageQueueFilteringCriteriaBo;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

public class ImageQueueFilterTest {

    private static final ImageQueuePatientBo PATIENT1 = ImageQueuePatientBo.builder()
            .patientId(1)
            .patientTypeId((short) 1)
            .personId(101)
            .identificationType("DNI")
            .identificationNumber("11111111")
            .firstName("Juan")
            .middleNames("Carlos")
            .nameSelfDetermination("Juanito")
            .lastName("García")
            .otherLastNames("Fernández")
            .gender(new GenderDto())
            .selfPerceivedGender("Male")
            .personAgeDto( new PersonAgeDto())
            .birthDate(LocalDate.of(1982, 3, 12))
            .build();

    private static final ImageQueuePatientBo PATIENT2 = ImageQueuePatientBo.builder()
            .patientId(2)
            .patientTypeId((short) 2)
            .personId(102)
            .identificationType("DNI")
            .identificationNumber("12222222")
            .firstName("María")
            .middleNames("Isabel")
            .nameSelfDetermination("Maribel")
            .lastName("Fernández")
            .otherLastNames("Gómez")
            .gender(new GenderDto())
            .selfPerceivedGender("Female")
            .personAgeDto(new PersonAgeDto())
            .birthDate(LocalDate.of(1989, 8, 25))
            .build();

    private static final ImageQueuePatientBo PATIENT3 = ImageQueuePatientBo.builder()
            .patientId(3)
            .patientTypeId((short) 2)
            .personId(102)
            .identificationType("DNI")
            .identificationNumber("123456789")
            .firstName("Jane")
            .middleNames("Doe")
            .nameSelfDetermination("Janie")
            .lastName("Johnson")
            .otherLastNames("Smith")
            .gender(new GenderDto())
            .selfPerceivedGender("Female")
            .personAgeDto(new PersonAgeDto())
            .birthDate(LocalDate.of(1996, 5, 20))
            .build();


    private ImageQueueFilteringCriteriaBo filteringCriteria;
    private List<ImageQueueBo> initialQueue;


    @BeforeEach
    public void setUp() {
        filteringCriteria = new ImageQueueFilteringCriteriaBo();
        filteringCriteria.setStatusList(Collections.emptyList());
        initialQueue = buildInitialQueue();
    }

    @Test
    public void filterWithNoCriteria() {
        ImageQueueFilter filter = new ImageQueueFilter(filteringCriteria,false);

        List<ImageQueueBo> filteredList = filter.byImageMoveAttributes(initialQueue);
        filteredList = filter.byStudyName(filteredList);
        filteredList = filter.byPatientData(filteredList);

        assertEquals(4, filteredList.size());
    }

    @Test
    public void filterByImageMoveAttributes() {
        Integer equipmentId  = 10;
        Integer modalityId = 1;
        List<EImageMoveStatus> statusList = List.of(EImageMoveStatus.ERROR,EImageMoveStatus.PENDING);

        filteringCriteria.setEquipmentId(equipmentId);
        filteringCriteria.setModalityId(modalityId);
        filteringCriteria.setStatusList(statusList);
        ImageQueueFilter filter = new ImageQueueFilter(filteringCriteria,false);

        List<ImageQueueBo> filteredList = filter.byImageMoveAttributes(initialQueue);

        assertEquals(2, filteredList.size());
        assertTrue(filteredList.stream().allMatch(iq -> iq.getEquipmentId().equals(equipmentId)));
        assertTrue(filteredList.stream().allMatch(iq -> iq.getModalityId().equals(modalityId)));
        assertTrue(filteredList.stream().allMatch(iq -> statusList.contains(iq.getImageMoveStatus())));

    }

    @Test
    public void filterByStudyNames() {
        String expectedStudy = "radiografía de costillas";
        initialQueue.get(0).setStudies(Collections.emptyList());
        initialQueue.get(1).setStudies(List.of(expectedStudy));
        initialQueue.get(2).setStudies(List.of(expectedStudy,"tomografía - cabeza/cuello"));
        initialQueue.get(3).setStudies(List.of("tomografía - cabeza/cuello"));


        filteringCriteria.setStudy("radio");
        ImageQueueFilter filter = new ImageQueueFilter(filteringCriteria,false);

        List<ImageQueueBo> filteredList1 = filter.byStudyName(initialQueue);

        assertEquals(2, filteredList1.size());
        assertTrue(filteredList1.stream().allMatch(iq -> iq.getStudies().stream().anyMatch(study -> study.equals(expectedStudy))));


        filteringCriteria.setStudy("rádioGRafia");
        filter = new ImageQueueFilter(filteringCriteria,false);
        List<ImageQueueBo> filteredList2 = filter.byStudyName(initialQueue);

        assertEquals(2, filteredList2.size());
        assertTrue(filteredList2.stream().allMatch(iq -> iq.getStudies().stream().anyMatch(study -> study.equals(expectedStudy))));

    }

    @Test
    public void filterByPatientName() {
        
        initialQueue.get(0).setPatient(PATIENT1);
        initialQueue.get(1).setPatient(PATIENT2);
        initialQueue.get(2).setPatient(PATIENT3);

        filteringCriteria.setName("María");
        ImageQueueFilter filter = new ImageQueueFilter(filteringCriteria,false);

        List<ImageQueueBo> filteredList1 = filter.byPatientData(initialQueue);

        assertEquals(1, filteredList1.size());
        assertEquals(filteredList1.get(0).getPatient(), PATIENT2);


        filteringCriteria.setName("Fernández");
        filter = new ImageQueueFilter(filteringCriteria,false);
        List<ImageQueueBo> filteredList2 = filter.byPatientData(initialQueue);

        assertEquals(2, filteredList2.size());
        assertEquals(filteredList2.get(0).getPatient(), PATIENT1);
        assertEquals(filteredList2.get(1).getPatient(), PATIENT2);


        filteringCriteria.setName("rnAnde");
        filter = new ImageQueueFilter(filteringCriteria,false);
        List<ImageQueueBo> filteredList3 = filter.byPatientData(initialQueue);

        assertEquals(2, filteredList3.size());
        assertEquals(filteredList3.get(0).getPatient(), PATIENT1);
        assertEquals(filteredList3.get(1).getPatient(), PATIENT2);


        filteringCriteria.setName("Maríbel");
        filter = new ImageQueueFilter(filteringCriteria,false);
        List<ImageQueueBo> filteredList4 = filter.byPatientData(initialQueue);

        assertTrue(filteredList4.isEmpty());

    }

    @Test
    public void filterByPatientSelfDeterminedName() {
        initialQueue.get(1).setPatient(PATIENT2);

        filteringCriteria.setName("maribel");
        ImageQueueFilter filter = new ImageQueueFilter(filteringCriteria,true);

        List<ImageQueueBo> filteredList1 = filter.byPatientData(initialQueue);

        assertEquals(1, filteredList1.size());
        assertEquals(filteredList1.get(0).getPatient(), PATIENT2);


        filteringCriteria.setName("maria");
        filter = new ImageQueueFilter(filteringCriteria,true);
        List<ImageQueueBo> filteredList2 = filter.byPatientData(initialQueue);

        assertTrue(filteredList2.isEmpty());

    }

    @Test
    public void filterByPatientIdentificationNumber() {
        initialQueue.get(0).setPatient(PATIENT1);
        initialQueue.get(1).setPatient(PATIENT2);
        initialQueue.get(2).setPatient(PATIENT3);


        filteringCriteria.setIdentificationNumber("1");
        ImageQueueFilter filter = new ImageQueueFilter(filteringCriteria,false);

        List<ImageQueueBo> filteredList1 = filter.byPatientData(initialQueue);

        assertEquals(3, filteredList1.size());
        assertEquals(filteredList1.get(0).getPatient(), PATIENT1);
        assertEquals(filteredList1.get(1).getPatient(), PATIENT2);
        assertEquals(filteredList1.get(2).getPatient(), PATIENT3);


        filteringCriteria.setIdentificationNumber("2");
        filter = new ImageQueueFilter(filteringCriteria,false);
        List<ImageQueueBo> filteredList2 = filter.byPatientData(initialQueue);

        assertEquals(2, filteredList2.size());
        assertEquals(filteredList2.get(0).getPatient(), PATIENT2);
        assertEquals(filteredList2.get(1).getPatient(), PATIENT3);

        filteringCriteria.setIdentificationNumber("ABCD1234");
        filter = new ImageQueueFilter(filteringCriteria,false);
        List<ImageQueueBo> filteredList3 = filter.byPatientData(initialQueue);

        assertTrue(filteredList3.isEmpty());

    }


    private List<ImageQueueBo> buildInitialQueue() {
        return List.of(
                new ImageQueueBo(1,1,1,1,10,1,
                        1,null,"ERROR", Date.from(Instant.now()),null,
                        LocalDate.now(), LocalTime.now(), "1234"  ),
                new ImageQueueBo(2,1,2,1,10,1,
                        1,null,"PENDING", Date.from(Instant.now()),null,
                        LocalDate.now(), LocalTime.now(), "1234"),
                new ImageQueueBo(3,1,3,1,11,1,
                        1,null,"ERROR",Date.from(Instant.now()),null,
                        LocalDate.now(), LocalTime.now(), "1234"),
                new ImageQueueBo(4,1,null,2,10,1,
                        1,null,"ERROR",Date.from(Instant.now()),null,
                        LocalDate.now(), LocalTime.now(), "1234")
        );
    }

}
