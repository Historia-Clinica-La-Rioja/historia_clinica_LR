package ar.lamansys.immunization.infrastructure.output.repository.vaccine;

import ar.lamansys.immunization.domain.vaccine.VaccineRuleStorage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import java.util.List;

@Service
public class VaccineRuleStorageImpl implements VaccineRuleStorage {

    private final Logger logger;

    private final EntityManager entityManager;

    public VaccineRuleStorageImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
        this.logger = LoggerFactory.getLogger(this.getClass());
    }

    @Override
    public boolean existRule(String sctid, Short conditionApplicationId, Short schemeId, String dose, Short doseOrder) {
        logger.debug("ExistRule vaccine sctid {}, conditionApplicationId {}, schemeId {}, dose {} , doseOrder {}",
                sctid, conditionApplicationId, schemeId, dose, doseOrder);
        String sqlString = "" +
                "SELECT vr.sisa_code " +
                "FROM {h-schema}vaccine_nomivac_rule vr " +
                "JOIN {h-schema}nomivac_snomed_map nsm ON (nsm.sisa_code = vr.sisa_code) " +
                "WHERE nsm.sctid = :sctid " +
                "AND vr.condition_application_id = :conditionApplicationId " +
                "AND vr.scheme_id = :schemeId " +
                "AND vr.dose = :dose " +
                "AND vr.dose_order = :doseOrder ";

        List<Object[]> rows = entityManager.createNativeQuery(sqlString)
                .setParameter("sctid", sctid)
                .setParameter("conditionApplicationId", conditionApplicationId)
                .setParameter("schemeId", schemeId)
                .setParameter("dose", dose)
                .setParameter("doseOrder", doseOrder)
                .getResultList();
        return !rows.isEmpty();
    }
}
