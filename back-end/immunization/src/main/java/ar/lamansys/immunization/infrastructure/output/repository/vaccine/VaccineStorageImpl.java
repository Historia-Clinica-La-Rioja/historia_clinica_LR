package ar.lamansys.immunization.infrastructure.output.repository.vaccine;

import ar.lamansys.immunization.domain.vaccine.VaccineBo;
import ar.lamansys.immunization.domain.vaccine.VaccineDoseBo;
import ar.lamansys.immunization.domain.vaccine.VaccineRuleBo;
import ar.lamansys.immunization.domain.vaccine.VaccineSchemeBo;
import ar.lamansys.immunization.domain.vaccine.VaccineStorage;
import ar.lamansys.immunization.domain.vaccine.VaccineConditionApplicationBo;
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
                "       vr.condition_application_id, vca.description as condition_application, " +
                "       vr.dose, vr.dose_order, " +
                "       vs.id AS scheme_id, vs.description AS scheme_description, " +
                "       vs.minimum_threshold_days AS scheme_minimum_threshold_days, vs.maximum_threshold_days AS scheme_maximum_threshold_days, " +
                "       vr.minimum_threshold_days AS rule_minimum_threshold_days, vr.maximum_threshold_days AS rule_maximum_threshold_days, " +
                "       vr.time_between_doses_days " +
                "FROM {h-schema}vaccine_nomivac_rule vr " +
                "JOIN {h-schema}vaccine v ON (v.sisa_code = vr.sisa_code) " +
                "JOIN {h-schema}vaccine_condition_application vca ON (vca.id = vr.condition_application_id) " +
                "JOIN {h-schema}vaccine_scheme vs ON (vs.id = vr.scheme_id) " +
                "JOIN {h-schema}nomivac_snomed_map nsm ON (nsm.sisa_code = v.sisa_code) " +
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
                new VaccineRuleBo((Integer) row[13], (Integer) row[14], (Integer) row[15],
                        new VaccineSchemeBo((Short) row[9], (String) row[10], (Integer) row[11], (Integer) row[12]),
                        new VaccineDoseBo((String) row[7], (Short) row[8]),
                        new VaccineConditionApplicationBo((Short) row[5], (String) row[6]))
        ).collect(Collectors.toList());
        return new VaccineBo((Short) rows.get(0)[0], (Short) rows.get(0)[1],(String) rows.get(0)[2],
                (Integer) rows.get(0)[3], (Integer) rows.get(0)[4], rules);
    }

}
