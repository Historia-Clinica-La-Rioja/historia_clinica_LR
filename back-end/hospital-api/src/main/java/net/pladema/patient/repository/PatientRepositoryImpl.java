package net.pladema.patient.repository;

import net.pladema.patient.controller.dto.PatientSearchFilter;
import net.pladema.patient.repository.domain.PatientMedicalCoverageVo;
import net.pladema.patient.service.domain.PatientSearch;
import net.pladema.sgx.repository.QueryPart;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import static net.pladema.patient.repository.PatientSearchQuery.AND_JOINING_OPERATOR;
import static net.pladema.patient.repository.PatientSearchQuery.LIKE_COMPARATOR;

@Repository
public class PatientRepositoryImpl implements PatientRepositoryCustom {

    @PersistenceContext
    private EntityManager entityManager;

    public PatientRepositoryImpl(EntityManager entityManager){
        super();
        this.entityManager = entityManager;
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<PatientSearch> getAllByOptionalFilter(PatientSearchFilter searchFilter) {
        PatientSearchQuery patientSearchQuery = new PatientSearchQuery(searchFilter);
        QueryPart queryPart = new QueryPart(
                "SELECT ")
                .concatPart(patientSearchQuery.select())
                .concat(" FROM ")
                .concatPart(patientSearchQuery.from())
                .concat("WHERE ")
                .concatPart(patientSearchQuery.whereWithAllAttributes(AND_JOINING_OPERATOR, LIKE_COMPARATOR));
        Query query = entityManager.createQuery(queryPart.toString());
        queryPart.configParams(query);
        return patientSearchQuery.construct(query.getResultList());
    }

    @Override
    @Transactional(readOnly = true)
    public PatientMedicalCoverageVo getPatientCoverage(Integer patientMedicalCoverageId) {
        String sqlString = "SELECT pmc.id as pmcid, pmc.affiliate_number, pmc.vigency_date, mc.id, mc.name, hi.rnos, hi.acronym, phi.plan, phid.id as phid, phid.start_date, phid.end_date " +
                "FROM patient_medical_coverage pmc " +
                "JOIN medical_coverage mc ON (pmc.medical_coverage_id = mc.id) " +
                "LEFT JOIN health_insurance hi ON (mc.id = hi.id) " +
                "LEFT JOIN private_health_insurance phi ON (mc.id = phi.id) "+
                "LEFT JOIN private_health_insurance_details phid ON (pmc.private_health_insurance_details_id = phid.id) "+
                "WHERE pmc.active = true " +
                "AND pmc.id = :patientMedicalCoverageId ";

        List<Object[]> queryResult = entityManager.createNativeQuery(sqlString)
                .setParameter("patientMedicalCoverageId", patientMedicalCoverageId)
                .getResultList();
        List<PatientMedicalCoverageVo> result = new ArrayList<>();
        queryResult.forEach(h ->
                result.add(
                        new PatientMedicalCoverageVo(
                                (Integer) h[0],
                                (String) h[1],
                                h[2] != null ? ((Date) h[2]).toLocalDate() : null,
                                (Integer) h[3],
                                (String) h[4],
                                (Integer) h[5],
                                (String) h[6],
                                (String) h[7],
                                (Integer) h[8],
                                h[9] != null ? ((Date) h[9]).toLocalDate() : null,
                                h[10] != null ? ((Date) h[10]).toLocalDate() : null))
        );
        return result.get(0);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PatientMedicalCoverageVo> getPatientCoverages(Integer patientId) {
        String sqlString = "SELECT pmc.id as pmcid, pmc.affiliate_number, pmc.vigency_date, mc.id, mc.name, hi.rnos, hi.acronym, phi.plan, phid.id as phid, phid.start_date, phid.end_date " +
                "FROM patient_medical_coverage pmc " +
                "JOIN medical_coverage mc ON (pmc.medical_coverage_id = mc.id) " +
                "LEFT JOIN health_insurance hi ON (mc.id = hi.id) " +
                "LEFT JOIN private_health_insurance phi ON (mc.id = phi.id) "+
                "LEFT JOIN private_health_insurance_details phid ON (pmc.private_health_insurance_details_id = phid.id) "+
                "WHERE pmc.active = true " +
                "AND pmc.patient_id = :patientId ";

        List<Object[]> queryResult = entityManager.createNativeQuery(sqlString)
                .setParameter("patientId", patientId)
                .getResultList();
        List<PatientMedicalCoverageVo> result = new ArrayList<>();
        queryResult.forEach(h ->
                result.add(
                        new PatientMedicalCoverageVo(
                                (Integer) h[0],
                                (String) h[1],
                                h[2] != null ? ((Date) h[2]).toLocalDate() : null,
                                (Integer) h[3],
                                (String) h[4],
                                (Integer) h[5],
                                (String) h[6],
                                (String) h[7],
                                (Integer) h[8],
                                h[9] != null ? ((Date) h[9]).toLocalDate() : null,
                                h[10] != null ? ((Date) h[10]).toLocalDate() : null))
        );
        return result;
    }


    @Override
    @Transactional(readOnly = true)
    public List<PatientMedicalCoverageVo> getPatientHealthInsurances(Integer patientId) {
        String sqlString = "SELECT pmc.id as pmcid, pmc.affiliate_number, pmc.vigency_date, mc.id, mc.name, hi.rnos, hi.acronym " +
                "FROM patient_medical_coverage pmc " +
                "JOIN medical_coverage mc ON (pmc.medical_coverage_id = mc.id) " +
                "JOIN health_insurance hi ON (mc.id = hi.id) " +
                "WHERE pmc.active = true " +
                "AND pmc.patient_id = :patientId ";

        List<Object[]> queryResult = entityManager.createNativeQuery(sqlString)
                .setParameter("patientId", patientId)
                .getResultList();
        List<PatientMedicalCoverageVo> result = new ArrayList<>();
        queryResult.forEach(h ->
                result.add(
                        new PatientMedicalCoverageVo(
                                (Integer) h[0],
                                (String) h[1],
                                h[2] != null ? ((Date) h[2]).toLocalDate() : null,
                                (Integer) h[3],
                                (String) h[4],
                                (Integer) h[5],
                                (String) h[6]))
        );
        return result;
    }

    @Override
    @Transactional(readOnly = true)
    public List<PatientMedicalCoverageVo> getPatientPrivateHealthInsurances(Integer patientId) {
        String sqlString = "SELECT pmc.id as pmcid, pmc.affiliate_number, pmc.vigency_date, mc.id as mcid, mc.name, phid.id as phid, phid.start_date, phid.end_date, phi.plan " +
                "FROM patient_medical_coverage pmc " +
                "JOIN medical_coverage mc ON (pmc.medical_coverage_id = mc.id) " +
                "JOIN private_health_insurance phi ON (mc.id = phi.id) " +
                "JOIN private_health_insurance_details phid ON (pmc.private_health_insurance_details_id = phid.id) "+
                "WHERE pmc.active = true " +
                "AND pmc.patient_id = :patientId ";

        List<Object[]> queryResult = entityManager.createNativeQuery(sqlString)
                .setParameter("patientId", patientId)
                .getResultList();
        List<PatientMedicalCoverageVo> result = new ArrayList<>();
        queryResult.forEach(h ->
                result.add(
                        new PatientMedicalCoverageVo(
                                (Integer) h[0],
                                (String) h[1],
                                h[2] != null ? ((Date) h[2]).toLocalDate() : null,
                                (Integer) h[3],
                                (String) h[4],
                                (Integer) h[5],
                                h[6] != null ? ((Date) h[6]).toLocalDate() : null,
                                h[7] != null ? ((Date) h[7]).toLocalDate() : null,
                                (String) h[8]))

        );
        return result;
    }
}
