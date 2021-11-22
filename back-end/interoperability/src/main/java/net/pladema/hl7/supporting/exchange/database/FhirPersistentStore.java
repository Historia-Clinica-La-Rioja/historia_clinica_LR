package net.pladema.hl7.supporting.exchange.database;

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
import javax.persistence.PersistenceContext;
import javax.validation.constraints.NotNull;
import java.util.Arrays;
import java.util.List;

@Repository
public class FhirPersistentStore {

    private static final String PATIENTID = "patientId";
    private static final String STATUSID = "statusId";
    private static final String DOCUMENTTYPEID = "documentTypeId";

    @PersistenceContext
    private final EntityManager entityManager;

    public FhirPersistentStore(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Transactional(readOnly = true)
    public BundleVo getDocumentReference(String id){
        try {
            return (BundleVo) entityManager
                    .createNamedQuery("HCE.getDocumentReference")
                    .setParameter(PATIENTID, Integer.valueOf(id))
                    .getSingleResult();
        }
        catch (NoResultException ex){
            return new BundleVo();
        }
    }

    @Transactional(readOnly = true)
    public PatientVo getPatient(@NotNull String patientId){
        try {
            PatientVo data = (PatientVo) entityManager
                    .createNamedQuery("HCE.getPatient")
                    .setParameter(PATIENTID, Integer.valueOf(patientId))
                    .getSingleResult();
            data.setFullAddress(getAddress(data.getAddressId(), Address.AddressUse.HOME));
            return data;
        }
        catch(NoResultException ex){
            return new PatientVo();
        }
    }

    @Transactional(readOnly = true)
    public FhirAddress getAddress(Integer addressId, Address.AddressUse addressUse){
        if(addressId != null) {
            Object[] queryResult = (Object[]) entityManager
                    .createNamedQuery("HCE.getAddress")
                    .setParameter("addressId", addressId)
                    .getSingleResult();
            return new FhirAddress(addressUse).setAll(queryResult);
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    @Transactional(readOnly = true)
    public List<ConditionVo> findAllCondition(@NotNull String patientId){
        return entityManager
                .createNamedQuery("HCE.findAllCondition")
                .setParameter(PATIENTID, Integer.valueOf(patientId))
                .setParameter("docStatusId", CodingCode.DocumentReference.FINAL_STATUS)
                .setParameter("diagnosisId", Arrays.asList(
                        CodingCode.Condition.PROBLEM, CodingCode.Condition.CHRONIC))
                .setParameter(STATUSID, ResourceStatus.ENTERED_IN_ERROR)
                .setParameter(DOCUMENTTYPEID, CodingCode.DocumentReference.OUTPATIENT_TYPE)
                .getResultList();
    }

    @SuppressWarnings("unchecked")
    @Transactional(readOnly = true)
    public List<ImmunizationVo> findAllImmunizations(@NotNull String patientId){
        return entityManager.createNamedQuery("HCE.findAllImmunizations")
                .setParameter("docStatusId", CodingCode.DocumentReference.FINAL_STATUS)
                .setParameter(STATUSID, ResourceStatus.ENTERED_IN_ERROR)
                .setParameter(PATIENTID, Integer.valueOf(patientId))
                .setParameter(DOCUMENTTYPEID, Arrays.asList(8, CodingCode.DocumentReference.OUTPATIENT_TYPE))
                .getResultList();
    }

    @SuppressWarnings("unchecked")
    @Transactional(readOnly = true)
    public List<AllergyIntoleranceVo> findAllAllergies(@NotNull String patientId){
        return entityManager.createNamedQuery("HCE.findAllAllergies")
                .setParameter(PATIENTID, Integer.valueOf(patientId))
                .setParameter("documentStatusId", CodingCode.DocumentReference.FINAL_STATUS)
                .setParameter(DOCUMENTTYPEID, CodingCode.DocumentReference.OUTPATIENT_TYPE)
                .setParameter("allergyIntoleranceStatus", ResourceStatus.ENTERED_IN_ERROR)
                .getResultList();
    }

    @SuppressWarnings("unchecked")
    @Transactional(readOnly = true)
    public List<MedicationVo> findAllMedications(@NotNull String patientId){
        return entityManager.createNamedQuery("HCE.findAllMedications")
                .setParameter(PATIENTID, Integer.valueOf(patientId))
                .setParameter("documentStatusId", CodingCode.DocumentReference.FINAL_STATUS)
                .setParameter(STATUSID, ResourceStatus.ACTIVE)
                .setParameter(DOCUMENTTYPEID, List.of(
                        CodingCode.DocumentReference.OUTPATIENT_TYPE, CodingCode.DocumentReference.RECIPE))
                .getResultList();
    }

    @Transactional(readOnly = true)
    public OrganizationVo getOrganization(@NotNull String patientId){
        try {
            OrganizationVo data = (OrganizationVo) entityManager
                    .createNamedQuery("HCE.getOrganization")
                    .setParameter(PATIENTID, Integer.valueOf(patientId))
                    .getSingleResult();
            data.setFullAddress(getAddress(data.getAddressId(), Address.AddressUse.WORK));
            return data;
        }
        catch (NoResultException ex){
            return new OrganizationVo();
        }
    }
}