package net.pladema.hl7.supporting.exchange.database;

import net.pladema.hl7.dataexchange.model.adaptor.Cast;
import net.pladema.hl7.dataexchange.model.adaptor.FhirAddress;
import net.pladema.hl7.dataexchange.model.domain.AllergyIntoleranceVo;
import net.pladema.hl7.dataexchange.model.domain.BundleVo;
import net.pladema.hl7.dataexchange.model.domain.ConditionVo;
import net.pladema.hl7.dataexchange.model.domain.ImmunizationVo;
import net.pladema.hl7.dataexchange.model.domain.MedicationVo;
import net.pladema.hl7.dataexchange.model.domain.OrganizationVo;
import net.pladema.hl7.dataexchange.model.domain.PatientVo;
import net.pladema.hl7.foundation.lifecycle.ResourceStatus;
import net.pladema.hl7.supporting.terminology.coding.CodingCode;
import org.hl7.fhir.r4.model.Address;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Repository
public class FhirPersistentStore {

    private final EntityManager entityManager;

    public FhirPersistentStore(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Transactional(readOnly = true)
    public BundleVo getDocumentReference(String id){
        String sqlQuery = "select pat.id, first_name, middle_names, last_name, bool_and(op.id is not null) as has_documents"
                + " from patient pat "
                + " join person per on ( pat.person_id = per.id )"
                + " left join outpatient_consultation op on ( pat.id = op.patient_id )"
                + " where pat.id = :id"
                + " group by pat.id, first_name, middle_names, last_name";
        try {
            Object[] queryResult = (Object[]) entityManager
                    .createNativeQuery(sqlQuery).setParameter("id", Integer.valueOf(id))
                    .getSingleResult();
            return new BundleVo(queryResult);
        }
        catch (NoResultException ex){
            return new BundleVo();
        }
    }

    @Transactional(readOnly = true)
    public PatientVo getPatient(@NotNull String patientId){
        String sqlQuery = "select p.firstName, p.middleNames, p.lastName,"
                + " p.otherLastNames, pe.mothersLastName,"
                + " p.identificationNumber, p.genderId, p.birthDate, pe.phoneNumber, pe.addressId"
                + " from Patient pat "
                + " join Person p on ( pat.personId = p.id )"
                + " left join PersonExtended pe on ( p.id = pe.id )"
                + " where pat.id = :patientId";
        try {
            Object[] queryResult = (Object[]) entityManager
                    .createQuery(sqlQuery).setParameter("patientId", Integer.valueOf(patientId))
                    .getSingleResult();
            PatientVo data = new PatientVo(queryResult);
            data.setFullAddress(getAddress(Cast.toInteger(queryResult[9]), Address.AddressUse.HOME));
            return data;
        }
        catch(NoResultException ex){
            return new PatientVo();
        }
    }

    @Transactional(readOnly = true)
    public FhirAddress getAddress(Integer addressId, Address.AddressUse addressUse){
        if(addressId != null) {
            String sqlQuery = "select a.street, a.number, a.floor, a.apartment, a.postcode,"
                    + " ci.description, p.description, co.description "
                    + " from Address a"
                    + " left join City ci on ( a.cityId = ci.id )"
                    + " left join Department d on ( ci.departmentId = d.id )"
                    + " left join Province p on ( d.provinceId = p.id )"
                    + " left join Country co on ( p.countryId = co.id )"
                    + " where a.id = :addressId";
            Object[] queryResult = (Object[]) entityManager
                    .createQuery(sqlQuery).setParameter("addressId", addressId)
                    .getSingleResult();
            return new FhirAddress(addressUse).setAll(queryResult);
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    @Transactional(readOnly = true)
    public List<ConditionVo> findAllCondition(@NotNull String patientId){
        String sqlQuery = "WITH t AS(" +
                " SELECT hc.id, sctid_code, hc.status_id, verification_status_id, start_date, hc.created_on, hc.updated_on," +
                " row_number() over (partition by sctid_code order by hc.updated_on desc) as rw" +
                " FROM document d" +
                " JOIN document_health_condition dhc on d.id = dhc.document_id" +
                " JOIN health_condition hc on dhc.health_condition_id = hc.id " +
                " WHERE d.status_id = :docStatusId" +
                " AND d.type_id = :documentType " +
                " AND hc.patient_id = :patientId " +
                " AND NOT hc.problem_id = :diagnosisId" +
                ")" +
                " SELECT t.id as id, s.id as code, s.pt as term, status_id as clinical_status, " +
                " verification_status_id, start_date, created_on" +
                " FROM t " +
                " JOIN snomed s ON sctid_code = s.sctid" +
                " WHERE rw = 1" +
                " AND NOT verification_status_id = :statusId";
        List<Object[]> queryResult = entityManager
                .createNativeQuery(sqlQuery)
                .setParameter("patientId", Integer.valueOf(patientId))
                .setParameter("docStatusId", CodingCode.DocumentReference.FINAL_STATUS)
                .setParameter("diagnosisId", CodingCode.Condition.DIAGNOSIS)
                .setParameter("statusId", ResourceStatus.ENTERED_IN_ERROR)
                .setParameter("documentType", CodingCode.DocumentReference.OUTPATIENT_TYPE)
                .getResultList();
        List<ConditionVo> data = new ArrayList<>();
        queryResult.forEach((tuple)->data.add(new ConditionVo(tuple)));
        return data;
    }

    @SuppressWarnings("unchecked")
    @Transactional(readOnly = true)
    public List<ImmunizationVo> findAllImmunizations(@NotNull String patientId){
        String sqlString = "WITH t AS (" +
                "   SELECT inm.id, sctid_code, inm.status_id, administration_date, expiration_date, inm.created_on, inm.updated_on, " +
                "   row_number() over (partition by sctid_code, administration_date order by inm.updated_on desc) as rw  " +
                "   FROM document d " +
                "   JOIN document_inmunization di on d.id = di.document_id " +
                "   JOIN inmunization inm on di.inmunization_id = inm.id " +
                "   WHERE d.status_id = :docStatusId " +
                "   AND d.type_id = :documentType" +
                "   AND inm.patient_id = :patientId " +
                "   " +
                ") " +
                "SELECT t.id as id, s.id as sctid, s.pt, status_id, administration_date, created_on, expiration_date " +
                "FROM t " +
                "JOIN snomed s ON sctid_code = s.sctid " +
                "WHERE rw = 1 " +
                "AND NOT status_id = :statusId " +
                "ORDER BY t.updated_on DESC";

        List<Object[]> queryResult = entityManager.createNativeQuery(sqlString)
                .setParameter("docStatusId", CodingCode.DocumentReference.FINAL_STATUS)
                .setParameter("statusId", ResourceStatus.ENTERED_IN_ERROR)
                .setParameter("patientId", Integer.valueOf(patientId))
                .setParameter("documentType", CodingCode.DocumentReference.OUTPATIENT_TYPE)
                .getResultList();
        List<ImmunizationVo> data = new ArrayList<>();
        queryResult.forEach((tuple)->data.add(new ImmunizationVo(tuple)));
        return data;
    }

    @SuppressWarnings("unchecked")
    @Transactional(readOnly = true)
    public List<AllergyIntoleranceVo> findAllAllergies(@NotNull String patientId){

        String sqlString = "with temporal as (" +
                "SELECT DISTINCT ai.id, ai.sctid_code, ai.status_id, ai.verification_status_id, " +
                " ai.category_id, ai.start_date, ai.updated_on, " +
                " row_number() over (partition by ai.sctid_code order by ai.updated_on desc) as rw " +
                " FROM document d " +
                " JOIN document_allergy_intolerance dai ON d.id = dai.document_id " +
                " JOIN allergy_intolerance ai ON dai.allergy_intolerance_id = ai.id " +
                " WHERE d.type_id = :documentType "+
                " AND d.status_id = :documentStatusId " +
                " AND ai.patient_id = :patientId " +
                " ) " +
                " SELECT t.id AS id, s.id AS sctid, s.pt, t.status_id, t.verification_status_id, " +
                " t.category_id, t.start_date " +
                " FROM temporal t " +
                " JOIN snomed s ON t.sctid_code = s.sctid " +
                " WHERE rw = 1 AND NOT status_id = :allergyIntoleranceStatus " +
                " ORDER BY t.updated_on desc ";

        List<Object[]> queryResult = entityManager.createNativeQuery(sqlString)
                .setParameter("patientId", Integer.valueOf(patientId))
                .setParameter("documentStatusId", CodingCode.DocumentReference.FINAL_STATUS)
                .setParameter("documentType", CodingCode.DocumentReference.OUTPATIENT_TYPE)
                .setParameter("allergyIntoleranceStatus", ResourceStatus.ENTERED_IN_ERROR)
                .getResultList();

        List<AllergyIntoleranceVo> data = new ArrayList<>();
        queryResult.forEach((tuple)->data.add(new AllergyIntoleranceVo(tuple)));
        return data;
    }

    @SuppressWarnings("unchecked")
    @Transactional(readOnly = true)
    public List<MedicationVo> findAllMedications(@NotNull String patientId){
        String sqlString = "with temporal as (" +
                "SELECT DISTINCT " +
                "ms.id, ms.sctid_code, ms.status_id, ms.updated_on, " +
                "row_number() OVER (PARTITION by ms.sctid_code ORDER BY ms.updated_on desc) AS rw " +
                "FROM document d " +
                "JOIN document_medicamention_statement dms ON d.id = dms.document_id " +
                "JOIN medication_statement ms ON dms.medication_statement_id = ms.id " +
                "WHERE ms.patient_id = :patientId  " +
                "AND d.type_id = :documentType "+
                "AND d.status_id = :documentStatusId " +
                ") " +
                "SELECT t.id AS id, s.id AS sctid, s.pt, status_id " +
                "FROM temporal t " +
                "JOIN snomed s ON t.sctid_code = s.sctid " +
                "WHERE rw = 1 AND NOT status_id = :statusId " +
                "ORDER BY t.updated_on";
        List<Object[]> queryResult = entityManager.createNativeQuery(sqlString)
                .setParameter("documentStatusId", CodingCode.DocumentReference.FINAL_STATUS)
                .setParameter("statusId", ResourceStatus.ENTERED_IN_ERROR)
                .setParameter("patientId", Integer.valueOf(patientId))
                .setParameter("documentType", CodingCode.DocumentReference.OUTPATIENT_TYPE)
                .getResultList();
        List<MedicationVo> data = new ArrayList<>();
        queryResult.forEach((tuple)->data.add(new MedicationVo(tuple)));
        return data;
    }

    @Transactional(readOnly = true)
    public OrganizationVo getOrganization(@NotNull String patientId){
        String sqlQuery = "select distinct sisa_code, name, phone_number, address_id" +
                " from institution i " +
                " where id = (" +
                " select distinct institution_id" +
                " from (" +
                " select institution_id from internment_episode ie where patient_id = :patientId" +
                " union" +
                " select institution_id from outpatient_consultation oc where patient_id = :patientId" +
                " ) as subquery" +
                " limit 1" +
                ")";
        try {
            Object[] queryResult = getSingleResult(sqlQuery, patientId);
            OrganizationVo data = new OrganizationVo(queryResult);
            data.setFullAddress(getAddress(Cast.toInteger(queryResult[3]), Address.AddressUse.WORK));
            return data;
        }
        catch (NoResultException ex){
            return new OrganizationVo();
        }
    }

    private Object[] getSingleResult(String sqlQuery, String id){
        try{
            return (Object[]) entityManager
                    .createNativeQuery(sqlQuery)
                    .setParameter("patientId", Integer.valueOf(id))
                    .getSingleResult();
        }
        catch(NoResultException ex){
            return new Object[selectedColums(sqlQuery)];
        }
    }

    private int selectedColums(@NotNull String sqlQuery){
        return sqlQuery.substring(0, sqlQuery.indexOf("from"))
                .split(",")
                .length;
    }
}
