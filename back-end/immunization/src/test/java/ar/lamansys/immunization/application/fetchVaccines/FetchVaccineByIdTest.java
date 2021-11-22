package ar.lamansys.immunization.application.fetchVaccines;

import ar.lamansys.immunization.UnitRepository;
import ar.lamansys.immunization.domain.vaccine.Thresholds;
import ar.lamansys.immunization.domain.vaccine.VaccineDescription;
import ar.lamansys.immunization.domain.vaccine.VaccineDoseBo;
import ar.lamansys.immunization.infrastructure.output.repository.vaccine.NomivacSnomedMap;
import ar.lamansys.immunization.infrastructure.output.repository.vaccine.Vaccine;
import ar.lamansys.immunization.infrastructure.output.repository.vaccine.VaccineConditionApplication;
import ar.lamansys.immunization.infrastructure.output.repository.vaccine.VaccineNomivacRule;
import ar.lamansys.immunization.infrastructure.output.repository.vaccine.VaccineScheme;
import ar.lamansys.immunization.infrastructure.output.repository.vaccine.VaccineStorageImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.EntityManager;

import static org.junit.jupiter.api.Assertions.assertNull;

class FetchVaccineByIdTest extends UnitRepository {

    private FetchVaccineById fetchVaccineById;

    @Autowired
    private EntityManager entityManager;

    @BeforeEach
    public void setUp() {
        fetchVaccineById = new FetchVaccineById(new VaccineStorageImpl(entityManager));
    }

    @Test
    void success() {
        var asplenicoGT5 = save(new VaccineScheme((short)437, "Asplénico mayor de 5 años", 1825, 42000));
        var asplenicoLT5 = save(new VaccineScheme((short)436, "Asplénico menor de 5 años", 42, 1824));
        var atrasado = save(new VaccineScheme((short)184, "Atrasado", 120, 2189));
        var meningococo = save(new Vaccine((short)162, "Meningocócica Tetravalente Conjugada", 56, 23725));
        var quintuple = save(new Vaccine((short)130, "Quintuple", 42, 2189));

        var dose1 = new VaccineDoseBo("Dose1", (short)1);
        var reinforcement = new VaccineDoseBo("Refuerzo", (short)12);
        var dose2 = new VaccineDoseBo("Dose2", (short)2);
        var dose3 = new VaccineDoseBo("Dose3", (short)3);

        var asplenicCondition = save(new VaccineConditionApplication((short)1, "Asplenic"));
        var nationalCalendarCondition = save(new VaccineConditionApplication((short)3, "National Calendar"));
        save(new VaccineNomivacRule(meningococo.getSisaCode(), asplenicCondition.getId(), asplenicoGT5.getId(), dose1.getDescription(), dose1.getOrder(), 0, 0, 0));
        save(new VaccineNomivacRule(meningococo.getSisaCode(), asplenicCondition.getId(), asplenicoGT5.getId(), reinforcement.getDescription(), reinforcement.getOrder(),  0, 0, 56));
        save(new VaccineNomivacRule(meningococo.getSisaCode(), asplenicCondition.getId(), asplenicoLT5.getId(), dose1.getDescription(), dose1.getOrder(),  0, 0, 0));
        save(new VaccineNomivacRule(meningococo.getSisaCode(), asplenicCondition.getId(), asplenicoLT5.getId(), dose2.getDescription(), dose2.getOrder(),  0, 0, 56));
        save(new VaccineNomivacRule(meningococo.getSisaCode(), asplenicCondition.getId(), asplenicoLT5.getId(), dose3.getDescription(), dose3.getOrder(),  0, 0, 56));
        save(new VaccineNomivacRule(meningococo.getSisaCode(), asplenicCondition.getId(), asplenicoLT5.getId(), reinforcement.getDescription(), reinforcement.getOrder(),  0, 0, 56));

        save(new VaccineNomivacRule(quintuple.getSisaCode(), nationalCalendarCondition.getId(), atrasado.getId(), dose1.getDescription(), dose1.getOrder(), 120, 2189, 0));
        save(new VaccineNomivacRule(quintuple.getSisaCode(), nationalCalendarCondition.getId(), atrasado.getId(), dose2.getDescription(), dose2.getOrder(), 180, 2189, 28));
        save(new VaccineNomivacRule(quintuple.getSisaCode(), nationalCalendarCondition.getId(), atrasado.getId(), dose3.getDescription(), dose3.getOrder(),  240, 2189, 56));
        save(new VaccineNomivacRule(quintuple.getSisaCode(), nationalCalendarCondition.getId(), atrasado.getId(), reinforcement.getDescription(), reinforcement.getOrder(), 730, 2189, 180));

        save(new NomivacSnomedMap((short)162, "sctid"));
        var result = fetchVaccineById.run("sctid");

        Assertions.assertNotNull(result);
        Assertions.assertEquals((short)162, result.getSisaCode());
        Assertions.assertEquals(new VaccineDescription("Meningocócica Tetravalente Conjugada"), result.getDescription());
        Assertions.assertEquals(new Thresholds(56, 23725), result.getThreshold());
        Assertions.assertEquals(6, result.getRules().size());
    }

    @Test
    void unknownVaccine() {
        var result = fetchVaccineById.run("-5");
        assertNull(result);
    }
}
