package ar.lamansys.immunization.infrastructure.output.repository.vaccine;

import ar.lamansys.immunization.domain.vaccine.VaccineBo;
import ar.lamansys.immunization.domain.vaccine.VaccineDoseBo;
import ar.lamansys.immunization.domain.vaccine.VaccineRuleBo;
import ar.lamansys.immunization.domain.vaccine.VaccineSchemeBo;
import ar.lamansys.immunization.domain.vaccine.VaccineStorage;
import ar.lamansys.immunization.domain.vaccine.conditionapplication.VaccineConditionApplicationBo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Primary
public class VaccineStorageImpl implements VaccineStorage {

    private final Logger logger;

    private final EntityManager entityManager;

    public VaccineStorageImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
        this.logger = LoggerFactory.getLogger(this.getClass());
    }

    @Override
    public Optional<VaccineBo> findById(String sctid) {
        logger.debug("Find vaccine by id {}", sctid);
        String sqlString = "" +
                "SELECT v.id AS vaccine_id, v.sisa_code, v.description AS vaccine_description, " +
                "       v.minimum_threshold_days AS vaccine_minimum_threshold_days, v.maximum_threshold_days AS vaccine_maximum_threshold_days, " +
                "       vr.condition_application_id, " +
                "       vr.dose, vr.dose_order, " +
                "       vs.id AS scheme_id, vs.description AS scheme_description, " +
                "       vs.minimum_threshold_days AS scheme_minimum_threshold_days, vs.maximum_threshold_days AS scheme_maximum_threshold_days, " +
                "       vr.minimum_threshold_days AS rule_minimum_threshold_days, vr.maximum_threshold_days AS rule_maximum_threshold_days, " +
                "       vr.time_between_doses_days " +
                "FROM vaccine_nomivac_rule vr " +
                "JOIN vaccine v ON (v.sisa_code = vr.sisa_code) " +
                "JOIN vaccine_scheme vs ON (vs.id = vr.scheme_id) " +
                "JOIN nomivac_snomed_map nsm ON (nsm.sisa_code = v.sisa_code) " +
                "WHERE nsm.sctid = :sctid ";

        List<Object[]> rows = entityManager.createNativeQuery(sqlString)
                .setParameter("sctid", sctid)
                .getResultList();
        VaccineBo result = createVaccineBo(rows);
        logger.debug("Find vaccine by id result -> {}", result);
        return Optional.ofNullable(result);
    }

    private VaccineBo createVaccineBo(List<Object[]> rows) {
        if (rows.isEmpty())
            return null;
        List<VaccineRuleBo> rules = rows.stream().map(row ->
                new VaccineRuleBo((Integer) row[12], (Integer) row[13], (Integer) row[14],
                        new VaccineSchemeBo((Short) row[8], (String) row[9], (Integer) row[10], (Integer) row[11]),
                        new VaccineDoseBo((String) row[6], (Short) row[7]),
                        VaccineConditionApplicationBo.map((Short) row[5]))
        ).collect(Collectors.toList());
        return new VaccineBo((Short) rows.get(0)[0], (Short) rows.get(0)[1],(String) rows.get(0)[2],
                (Integer) rows.get(0)[3], (Integer) rows.get(0)[4], rules);
    }

}
