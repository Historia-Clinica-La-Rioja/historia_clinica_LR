package ar.lamansys.immunization.infrastructure.output.repository.vaccine;

import ar.lamansys.immunization.domain.vaccine.VaccineBo;
import ar.lamansys.immunization.domain.vaccine.VaccineStorage;
import ar.lamansys.immunization.domain.vaccine.VaccineRuleBo;
import ar.lamansys.immunization.domain.vaccine.VaccineSchemeBo;
import ar.lamansys.immunization.domain.vaccine.conditionapplication.VaccineConditionApplicationBo;
import ar.lamansys.immunization.domain.vaccine.doses.VaccineDoseBo;
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
        var deficitComplementoGT5 = new VaccineSchemeBo((short)439, "Déficit de complemento mayor de 5 años", 1825, 42000);
        var deficitComplementoLT5 = new VaccineSchemeBo((short)438, "Déficit de complemento menor de 5 años", 42, 1824);
        var regular2 = new VaccineSchemeBo((short)200, "Regular", 360, 36500);

        return List.of(
                new VaccineBo((short)162, "Meningocócica Tetravalente Conjugada", 56, 23725,
                        List.of(new VaccineRuleBo(0, 0, 0, asplenicoGT5 , VaccineDoseBo.DOSE_1, VaccineConditionApplicationBo.ASPLENIC),
                                new VaccineRuleBo(0, 0, 56, asplenicoGT5, VaccineDoseBo.REINFORCEMENT, VaccineConditionApplicationBo.ASPLENIC),
                                new VaccineRuleBo(0, 0, 0, asplenicoLT5, VaccineDoseBo.DOSE_1, VaccineConditionApplicationBo.ASPLENIC),
                                new VaccineRuleBo(0, 0, 56, asplenicoLT5, VaccineDoseBo.DOSE_2, VaccineConditionApplicationBo.ASPLENIC),
                                new VaccineRuleBo(0, 0, 56, asplenicoLT5, VaccineDoseBo.DOSE_3, VaccineConditionApplicationBo.ASPLENIC),
                                new VaccineRuleBo(0, 0, 56, asplenicoLT5, VaccineDoseBo.REINFORCEMENT, VaccineConditionApplicationBo.ASPLENIC),
                                new VaccineRuleBo(0, 0, 0, atrasadoAdolescente, VaccineDoseBo.UNIQUE_DOSE, VaccineConditionApplicationBo.NATIONAL_CALENDAR),
                                new VaccineRuleBo(150, 1459, 0, atrasadoLactante, VaccineDoseBo.DOSE_1, VaccineConditionApplicationBo.NATIONAL_CALENDAR),
                                new VaccineRuleBo(210, 1459, 56, atrasadoLactante, VaccineDoseBo.DOSE_2, VaccineConditionApplicationBo.NATIONAL_CALENDAR),
                                new VaccineRuleBo(730, 1459, 56, atrasadoLactante, VaccineDoseBo.REINFORCEMENT, VaccineConditionApplicationBo.NATIONAL_CALENDAR),
                                new VaccineRuleBo(0, 0, 0, regularAdolescente, VaccineDoseBo.UNIQUE_DOSE, VaccineConditionApplicationBo.NATIONAL_CALENDAR),
                                new VaccineRuleBo(56, 149, 0, regularLactante, VaccineDoseBo.DOSE_1, VaccineConditionApplicationBo.NATIONAL_CALENDAR),
                                new VaccineRuleBo(150, 209, 56, regularLactante, VaccineDoseBo.DOSE_2, VaccineConditionApplicationBo.NATIONAL_CALENDAR),
                                new VaccineRuleBo(360, 729, 56, regularLactante, VaccineDoseBo.REINFORCEMENT, VaccineConditionApplicationBo.NATIONAL_CALENDAR))),
                new VaccineBo((short)130, "Quintuple", 42, 2189,
                        List.of(new VaccineRuleBo(120, 2189, 0, atrasado, VaccineDoseBo.DOSE_1, VaccineConditionApplicationBo.NATIONAL_CALENDAR),
                                new VaccineRuleBo(180, 2189, 28, atrasado, VaccineDoseBo.DOSE_2, VaccineConditionApplicationBo.NATIONAL_CALENDAR),
                                new VaccineRuleBo(240, 2189, 56, atrasado, VaccineDoseBo.DOSE_3, VaccineConditionApplicationBo.NATIONAL_CALENDAR),
                                new VaccineRuleBo(730, 2189, 180, atrasado, VaccineDoseBo.REINFORCEMENT, VaccineConditionApplicationBo.NATIONAL_CALENDAR),
                                new VaccineRuleBo(0, 0, 0, regular1, VaccineDoseBo.DOSE_1, VaccineConditionApplicationBo.NATIONAL_CALENDAR),
                                new VaccineRuleBo(0, 0, 28, regular1, VaccineDoseBo.DOSE_2, VaccineConditionApplicationBo.NATIONAL_CALENDAR),
                                new VaccineRuleBo(0, 0, 56, regular1, VaccineDoseBo.DOSE_3, VaccineConditionApplicationBo.NATIONAL_CALENDAR),
                                new VaccineRuleBo(0, 0, 180, regular1, VaccineDoseBo.REINFORCEMENT, VaccineConditionApplicationBo.NATIONAL_CALENDAR))),
                new VaccineBo((short)162, "Meningocócica Tetravalente Conjugada", 56, 23725,
                        List.of(new VaccineRuleBo(0, 0, 0, deficitComplementoGT5, VaccineDoseBo.DOSE_1, VaccineConditionApplicationBo.COMPLEMENT_DEFICIT),
                                new VaccineRuleBo(0, 0, 0, deficitComplementoLT5, VaccineDoseBo.REINFORCEMENT, VaccineConditionApplicationBo.COMPLEMENT_DEFICIT),
                                new VaccineRuleBo(0, 0, 56, deficitComplementoLT5, VaccineDoseBo.DOSE_3, VaccineConditionApplicationBo.COMPLEMENT_DEFICIT),
                                new VaccineRuleBo(0, 0, 56, deficitComplementoLT5, VaccineDoseBo.DOSE_2, VaccineConditionApplicationBo.COMPLEMENT_DEFICIT),
                                new VaccineRuleBo(0, 0, 0, deficitComplementoLT5, VaccineDoseBo.DOSE_1, VaccineConditionApplicationBo.COMPLEMENT_DEFICIT))),
                new VaccineBo((short)177, "Hepatitis A y B", 360, 40515,
                        List.of(new VaccineRuleBo(0, 0, 150, regular2, VaccineDoseBo.DOSE_3, VaccineConditionApplicationBo.RISK_FACTOR),
                                new VaccineRuleBo(0, 0, 0, regular2, VaccineDoseBo.DOSE_1, VaccineConditionApplicationBo.RISK_FACTOR),
                                new VaccineRuleBo(0, 0, 28, regular2, VaccineDoseBo.DOSE_2, VaccineConditionApplicationBo.RISK_FACTOR))));
    }

    @Override
    public Optional<VaccineBo> findById(String sctid) {
        logger.debug("Find vaccine by id {}", sctid);
        return Optional.ofNullable(sharedSnowstormPort.mapSctidToNomivacId(sctid))
                .map(Short::parseShort)
                .map(vaccineId -> vaccines.stream().filter(vaccineBo -> vaccineBo.getId().equals(vaccineId)).findFirst().orElse(null));
    }
}
