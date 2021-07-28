package ar.lamansys.immunization.infrastructure.output.repository.vaccine;

import ar.lamansys.immunization.domain.vaccine.VaccineBo;
import ar.lamansys.immunization.domain.vaccine.VaccineDoseBo;
import ar.lamansys.immunization.domain.vaccine.VaccineStorage;
import ar.lamansys.immunization.domain.vaccine.VaccineRuleBo;
import ar.lamansys.immunization.domain.vaccine.VaccineSchemeBo;
import ar.lamansys.immunization.domain.vaccine.conditionapplication.VaccineConditionApplicationBo;
import ar.lamansys.sgh.shared.infrastructure.input.service.SharedSnowstormPort;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class VaccineStorageMockImpl implements VaccineStorage {

    private final Logger logger;

    private final List<VaccineBo> vaccines;

    private final SharedSnowstormPort sharedSnowstormPort;

    public VaccineStorageMockImpl(SharedSnowstormPort sharedSnowstormPort) {
        this.sharedSnowstormPort = sharedSnowstormPort;
        this.logger = LoggerFactory.getLogger(this.getClass());
        this.vaccines = load();
    }

    private List<VaccineBo> load() {
        var asplenicoGT5 = new VaccineSchemeBo((short)437, "Asplénico mayor de 5 años", 1825, 42000);
        var asplenicoLT5 = new VaccineSchemeBo((short)436, "Asplénico menor de 5 años", 42, 1824);
        var atrasadoAdolescente = new VaccineSchemeBo((short)383, "Atrasado Adolescente", 4384, 5474);
        var atrasadoLactante = new VaccineSchemeBo((short)382, "Atrasado Lactantes", 150, 1459);
        var regularAdolescente = new VaccineSchemeBo((short)305, "Regular Adolescentes", 3654, 4383);
        var regularLactante = new VaccineSchemeBo((short)304, "Regular Lactantes", 56, 720);
        var atrasado = new VaccineSchemeBo((short)184, "Atrasado", 120, 2189);
        var regular1 = new VaccineSchemeBo((short)75, "Regular", 42, 729);

        var dose1 = new VaccineDoseBo("dose1", (short)1);
        var dose2 = new VaccineDoseBo("dose2", (short)1);
        var dose3 = new VaccineDoseBo("dose3", (short)1);
        var reinforcement = new VaccineDoseBo("reinforcement", (short)1);
        var uniqueDose = new VaccineDoseBo("unique dose", (short)1);

        return List.of(
                new VaccineBo((short)162, "Meningocócica Tetravalente Conjugada", 56, 23725,
                        List.of(new VaccineRuleBo(0, 0, 0, asplenicoGT5 , dose1, VaccineConditionApplicationBo.ASPLENIC),
                                new VaccineRuleBo(0, 0, 56, asplenicoGT5, reinforcement, VaccineConditionApplicationBo.ASPLENIC),
                                new VaccineRuleBo(0, 0, 0, asplenicoLT5, dose1, VaccineConditionApplicationBo.ASPLENIC),
                                new VaccineRuleBo(0, 0, 56, asplenicoLT5, dose2, VaccineConditionApplicationBo.ASPLENIC),
                                new VaccineRuleBo(0, 0, 56, asplenicoLT5, dose3, VaccineConditionApplicationBo.ASPLENIC),
                                new VaccineRuleBo(0, 0, 56, asplenicoLT5, reinforcement, VaccineConditionApplicationBo.ASPLENIC),
                                new VaccineRuleBo(0, 0, 0, atrasadoAdolescente, uniqueDose, VaccineConditionApplicationBo.NATIONAL_CALENDAR),
                                new VaccineRuleBo(150, 1459, 0, atrasadoLactante, dose1, VaccineConditionApplicationBo.NATIONAL_CALENDAR),
                                new VaccineRuleBo(210, 1459, 56, atrasadoLactante, dose2, VaccineConditionApplicationBo.NATIONAL_CALENDAR),
                                new VaccineRuleBo(730, 1459, 56, atrasadoLactante, reinforcement, VaccineConditionApplicationBo.NATIONAL_CALENDAR),
                                new VaccineRuleBo(0, 0, 0, regularAdolescente, uniqueDose, VaccineConditionApplicationBo.NATIONAL_CALENDAR),
                                new VaccineRuleBo(56, 149, 0, regularLactante, dose1, VaccineConditionApplicationBo.NATIONAL_CALENDAR),
                                new VaccineRuleBo(150, 209, 56, regularLactante, dose2, VaccineConditionApplicationBo.NATIONAL_CALENDAR),
                                new VaccineRuleBo(360, 729, 56, regularLactante, reinforcement, VaccineConditionApplicationBo.NATIONAL_CALENDAR))),
                new VaccineBo((short)130, "Quintuple", 42, 2189,
                        List.of(new VaccineRuleBo(120, 2189, 0, atrasado, dose1, VaccineConditionApplicationBo.NATIONAL_CALENDAR),
                                new VaccineRuleBo(180, 2189, 28, atrasado, dose2, VaccineConditionApplicationBo.NATIONAL_CALENDAR),
                                new VaccineRuleBo(240, 2189, 56, atrasado, dose3, VaccineConditionApplicationBo.NATIONAL_CALENDAR),
                                new VaccineRuleBo(730, 2189, 180, atrasado, reinforcement, VaccineConditionApplicationBo.NATIONAL_CALENDAR),
                                new VaccineRuleBo(0, 0, 0, regular1, dose1, VaccineConditionApplicationBo.NATIONAL_CALENDAR),
                                new VaccineRuleBo(0, 0, 28, regular1, dose2, VaccineConditionApplicationBo.NATIONAL_CALENDAR),
                                new VaccineRuleBo(0, 0, 56, regular1, dose3, VaccineConditionApplicationBo.NATIONAL_CALENDAR),
                                new VaccineRuleBo(0, 0, 180, regular1, reinforcement, VaccineConditionApplicationBo.NATIONAL_CALENDAR))));
 }

    @Override
    public Optional<VaccineBo> findById(String sctid) {
        logger.debug("Find vaccine by id {}", sctid);
        return Optional.ofNullable(sharedSnowstormPort.mapSctidToNomivacId(sctid))
                .map(Short::parseShort)
                .map(vaccineId -> vaccines.stream().filter(vaccineBo -> vaccineBo.getId().equals(vaccineId)).findFirst().orElse(null));
    }
}
