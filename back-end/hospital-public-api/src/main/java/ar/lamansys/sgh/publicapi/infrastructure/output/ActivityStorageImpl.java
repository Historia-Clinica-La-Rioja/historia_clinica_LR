package ar.lamansys.sgh.publicapi.infrastructure.output;

import ar.lamansys.sgh.publicapi.application.port.out.ActivityStorage;
import ar.lamansys.sgh.publicapi.domain.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ActivityStorageImpl implements ActivityStorage {

    private static final Logger LOG = LoggerFactory.getLogger(ActivityStorageImpl.class);

    private final EntityManager entityManager;

    public ActivityStorageImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    private static String sqlString =
            "SELECT va.id as attention_id, va.performed_date as attention_date, " +
                    "snm.sctid , snm.pt, " +
                    "p.first_name as person_name, p.last_name as person_last_name, p.identification_number as person_identification_number, p.gender_id as person_gender_id, p.birth_date as person_birth_date, " +
                    "pmc.affiliate_number, " +
                    "va.scope_id, " +
                    "pd.administrative_discharge_date, " +
                    "d.doctor_id, d.first_name as doctor_name, d.last_name as doctor_last_name, d.birth_date as doctor_birth_date, d.gender_id as doctor_gender_id, d.identification_number as doctor_identification_number " +
                    "FROM v_attention va " +
                    "JOIN attention_reads ar ON (%s ar.attention_id = va.id) " +
                    "JOIN institution i ON (i.sisa_code = :refsetCode AND i.province_code = :provinceCode AND va.institution_id = i.id) " +
                    "JOIN (SELECT pat.id as patient_id, pp.first_name, pp.last_name, pp.identification_number , pp.gender_id, pp.birth_date " +
                            "FROM patient pat " +
                            "JOIN person pp on pp.id = pat.person_id) AS p ON (p.patient_id = va.patient_id) " +
                    "JOIN (SELECT hp.id as doctor_id, dp.first_name, dp.last_name, dp.birth_date, dp.gender_id, dp.identification_number " +
                            "FROM healthcare_professional hp " +
                            "JOIN person dp on hp.person_id = dp.id) AS d ON (d.doctor_id = va.doctor_id) " +
                    "LEFT JOIN (SELECT cs.clinical_specialty_type_id, s.sctid, s.pt " +
                                "FROM clinical_specialty cs " +
                                "JOIN snomed s on s.sctid = cs.sctid_code) snm ON (va.clinical_speciality_id = snm.clinical_specialty_type_id) " +
                    "LEFT JOIN patient_discharge pd ON (va.scope_id = 0 AND pd.internment_episode_id = va.encounter_id) " +
                    "LEFT JOIN patient_medical_coverage pmc ON (pmc.patient_id = va.patient_id)" +
                    "WHERE %s ";

    @Override
    public Optional<AttentionInfoBo> getActivityById(String refsetCode, String provinceCode, Long activityId) {
        LOG.debug("getActivityById ActivityStorage -> refsetCode {}, provinceCode {}, activityId {}", refsetCode, provinceCode, activityId);

        Query query = entityManager.createNativeQuery(String.format(sqlString, "", "va.id = :activityId "))
                .setParameter("refsetCode", refsetCode)
                .setParameter("provinceCode", provinceCode)
                .setParameter("activityId", activityId);

        List<Object[]> queryResult = query.getResultList();

        Object[] resultSearch = queryResult.size() == 1 ? queryResult.get(0) : null;
        Optional<AttentionInfoBo> result = Optional.ofNullable(resultSearch).map(this::parseToAttentionInfoBo);
        LOG.trace("Output -> {}", result);
        return result;
    }

    @Override
    public List<AttentionInfoBo> getActivitiesByInstitution(String refsetCode, String provinceCode, LocalDate fromDate, LocalDate toDate, Boolean reprocessing) {
        LOG.debug("getActivitiesByInstitution ActivityStorage -> refsetCode {}, provinceCode {}, fromDate {}, toDate {}, reprocessing{}",
                refsetCode, provinceCode, fromDate, toDate, reprocessing);

        String proccessed = "ar.processed = :reprocessing AND";
        String whereClause = "va.performed_date BETWEEN :fromDate AND :toDate ";

        Query query = entityManager.createNativeQuery(String.format(sqlString, proccessed, whereClause))
                .setParameter("refsetCode", refsetCode)
                .setParameter("provinceCode", provinceCode)
                .setParameter("fromDate", fromDate)
                .setParameter("toDate", toDate)
                .setParameter("reprocessing", reprocessing);

        List<Object[]> queryResult = query.getResultList();

        List<AttentionInfoBo> result = queryResult
                .stream()
                .map(this::parseToAttentionInfoBo)
                .collect(Collectors.toList());

        LOG.debug("Output size -> {}", result.size());
        LOG.trace("Output -> {}", result);
        return result;
    }

    @Override
    public List<AttentionInfoBo> getActivitiesByInstitutionAndPatient(
            String refsetCode, String provinceCode, String identificationNumber, LocalDate fromDate, LocalDate toDate, Boolean reprocessing) {
        LOG.debug("getActivitiesByInstitutionAndPatient ActivityStorage -> refsetCode {}, provinceCode {}, identificationNumber {}, fromDate {}, toDate {}, reprocessing{}",
                refsetCode, provinceCode, identificationNumber, fromDate, toDate, reprocessing);

        String proccessed = "ar.processed = :reprocessing AND";
        String whereClause = "va.performed_date BETWEEN :fromDate AND :toDate AND p.identification_number = :identificationNumber";

        Query query = entityManager.createNativeQuery(String.format(sqlString, proccessed, whereClause))
                .setParameter("refsetCode", refsetCode)
                .setParameter("provinceCode", provinceCode)
                .setParameter("identificationNumber", identificationNumber)
                .setParameter("fromDate", fromDate)
                .setParameter("toDate", toDate)
                .setParameter("reprocessing", reprocessing);
        List<Object[]> queryResult = query.getResultList();

        List<AttentionInfoBo> result = queryResult
                .stream()
                .map(this::parseToAttentionInfoBo)
                .collect(Collectors.toList());

        LOG.debug("Output size -> {}", result.size());
        LOG.trace("Output -> {}", result);
        return result;
    }

    @Override
    public List<AttentionInfoBo> getActivitiesByInstitutionAndCoverage(
            String refsetCode, String provinceCode, String coverageCuit, LocalDate fromDate, LocalDate toDate, Boolean reprocessing) {

        LOG.debug("getActivitiesByInstitutionAndCoverage ActivityStorage -> refsetCode {}, provinceCode {}, coverageCuit {}, fromDate {}, toDate {}, reprocessing{}",
                refsetCode, provinceCode, coverageCuit, fromDate, toDate, reprocessing);

        String proccessed = "ar.processed = :reprocessing AND";
        String whereClause = "va.performed_date BETWEEN :fromDate AND :toDate ";

        Query query = entityManager.createNativeQuery(String.format(sqlString, proccessed, whereClause))
                .setParameter("refsetCode", refsetCode)
                .setParameter("provinceCode", provinceCode)
                .setParameter("fromDate", fromDate)
                .setParameter("toDate", toDate)
                .setParameter("reprocessing", reprocessing);
        List<Object[]> queryResult = query.getResultList();

        List<AttentionInfoBo> result = queryResult
                .stream()
                .map(this::parseToAttentionInfoBo)
                .collect(Collectors.toList());

        LOG.debug("Output size -> {}", result.size());
        LOG.trace("Output -> {}", result);
        return result;
    }

    private AttentionInfoBo parseToAttentionInfoBo(Object[] rawAttention) {
        return new AttentionInfoBo(
                (Integer) rawAttention[0],
                (LocalDate) rawAttention[1],
                new SnomedBo((String) rawAttention[2], (String) rawAttention[3]),
                new PersonInfoBo((String) rawAttention[4], (String) rawAttention[5], (String) rawAttention[6], (LocalDate) rawAttention[7], GenderEnum.valueOf((String) rawAttention[8])),
                new CoverageActivityInfoBo((String) rawAttention[9]),
                ScopeEnum.valueOf((String) rawAttention[10]),
                (rawAttention[11] != null) ? new InternmentBo((String) rawAttention[0], (LocalDate) rawAttention[2], (LocalDate) rawAttention[11]) : new InternmentBo(),
                new ProfessionalBo((Integer) rawAttention[12], (String) rawAttention[13], (String) rawAttention[14], (String) rawAttention[15], (String) rawAttention[16]));
    }

}
