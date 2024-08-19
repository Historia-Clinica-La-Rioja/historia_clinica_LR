package net.pladema.hl7.supporting.exchange.database;

import net.pladema.hl7.dataexchange.model.adaptor.FhirAddress;
import net.pladema.hl7.dataexchange.model.domain.AllergyIntoleranceVo;
import net.pladema.hl7.dataexchange.model.domain.BundleVo;
import net.pladema.hl7.dataexchange.model.domain.ConditionVo;
import net.pladema.hl7.dataexchange.model.domain.CoverageVo;
import net.pladema.hl7.dataexchange.model.domain.ImmunizationVo;
import net.pladema.hl7.dataexchange.model.domain.MedicationRequestVo;
import net.pladema.hl7.dataexchange.model.domain.MedicationVo;
import net.pladema.hl7.dataexchange.model.domain.OrganizationVo;
import net.pladema.hl7.dataexchange.model.domain.PatientVo;
import net.pladema.hl7.dataexchange.model.domain.PractitionerVo;
import net.pladema.hl7.dataexchange.model.domain.ServiceRequestVo;
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
import java.util.UUID;

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

	@Transactional(readOnly = true)
	public MedicationVo getMedication(@NotNull Integer medicationStatementId) {
		try {
			return (MedicationVo) entityManager.createNamedQuery("HCE.getMedication")
					.setParameter("medicationStatementId",medicationStatementId)
					.getSingleResult();
		}
		catch (NoResultException ex) {
			return new MedicationVo();
		}
	}

	@Transactional(readOnly = true)
	public OrganizationVo getOrganizationFromId(@NotNull Integer institutionId) {
		try {
			OrganizationVo data = (OrganizationVo) entityManager.createNamedQuery("HCE.getOrganizationFromId")
					.setParameter("institutionId",institutionId)
					.getSingleResult();
			data.setFullAddress(getAddress(data.getAddressId(), Address.AddressUse.WORK));
			return data;
		}
		catch (NoResultException ex) {
			return new OrganizationVo();
        }
    }

    @Transactional(readOnly = true)
	public PractitionerVo getPractitioner(@NotNull String healthcareProfessionalId) {
		try {
			PractitionerVo data = (PractitionerVo) entityManager.createNamedQuery("HCE.getPractitioner")
					.setParameter("doctorId", Integer.valueOf(healthcareProfessionalId))
					.getSingleResult();
			data.setFullAddress(getAddress(data.getAddressId(), Address.AddressUse.HOME));
			return data;
		}
		catch (NoResultException ex) {
			return new PractitionerVo();
		}
	}

	@Transactional(readOnly = true)
	public CoverageVo getCoverage(@NotNull String patientMedicalCoverageId) {
		try {
			CoverageVo data = (CoverageVo) entityManager.createNamedQuery("HCE.getCoverage")
					.setParameter("patientMedicalCoverageId", Integer.valueOf(patientMedicalCoverageId))
					.getSingleResult();
			return data;
		}
		catch (NoResultException ex) {
			return new CoverageVo();
		}
	}

	@Transactional(readOnly = true)
	public List<MedicationRequestVo> getMedicationRequest(@NotNull UUID requestUuid, @NotNull String identificationNumber) {
		return entityManager.createNamedQuery("HCE.getMedicationRequest")
				.setParameter("medicationRequestUuid", requestUuid)
				.setParameter("identificationNumber", identificationNumber)
				.getResultList();
	}

	@Transactional
	public void setMedicationRequestDispensed(@NotNull List<Integer> ids, @NotNull Integer lineState) {
		entityManager.createNamedQuery("HCE.setLineStateMedication")
				.setParameter("medicationStatementIds", ids)
				.setParameter("lineStateId", lineState)
				.executeUpdate();
	}

	@Transactional(readOnly = true)
	public MedicationRequestVo getMedicationDataForValidation(@NotNull UUID medicationRequestUuid, @NotNull UUID lineUuid) {
		try {
			return (MedicationRequestVo) entityManager.createNamedQuery("HCE.getMedicationDataForValidation")
					.setParameter("medicationRequestUuid", medicationRequestUuid)
				    .setParameter("lineUuid", lineUuid)
				    .getSingleResult();
		}
		catch (NoResultException ex) {
			return null;
		}
	}

	@Transactional
	public boolean isMedicationRequestCompleted(@NotNull Integer medicationRequestId) {
		return (boolean) entityManager.createNamedQuery("HCE.isMedicationRequestCompleted")
				.setParameter("medicationRequestId", medicationRequestId)
				.getSingleResult();
	}

	@Transactional
	public void updateMedicationRequestCompleted(@NotNull Integer medicationRequestId) {
		entityManager.createNamedQuery("HCE.setMedicationRequestCompleted")
				.setParameter("medicationRequestId", medicationRequestId)
				.executeUpdate();
    }
    
	@Transactional(readOnly = true)
	public List<ServiceRequestVo> getServiceRequest(@NotNull Integer serviceRequestId, @NotNull String identificationNumber) {
		return entityManager.createNamedQuery("HCE.getServiceRequest")
				.setParameter("serviceRequestId", serviceRequestId)
				.setParameter("identificationNumber", identificationNumber)
				.getResultList();
	}

	@Transactional(readOnly = true)
	public ServiceRequestVo getServiceRequestDataForValidation(@NotNull UUID serviceRequestUuid, @NotNull UUID diagnosticReportUuid) {
		try {
		return (ServiceRequestVo) entityManager.createNamedQuery("HCE.getServiceRequestDataForValidation")
				.setParameter("serviceRequestUuid", serviceRequestUuid)
				.setParameter("diagnosticReportUuid", diagnosticReportUuid)
				.getSingleResult();
		} catch (NoResultException ex) {
			return null;
		}
	}

	@Transactional
	public void setDiagnosticReportStatus(@NotNull UUID diagnosticReportUuid, @NotNull String statusId) {
		entityManager.createNamedQuery("HCE.setDiagnosticReportStatus")
				.setParameter("diagnosticReportUuid", diagnosticReportUuid)
				.setParameter("statusId", statusId)
				.executeUpdate();
	}

	@Transactional(readOnly = true)
	public boolean isServiceRequestCompleted(@NotNull Integer serviceRequestId) {
		return (boolean) entityManager.createNamedQuery("HCE.isServiceRequestCompleted")
				.setParameter("serviceRequestId", serviceRequestId)
				.getSingleResult();
	}

	@Transactional
	public void setServiceRequestCompleted(@NotNull Integer serviceRequestId) {
		entityManager.createNamedQuery("HCE.setServiceRequestCompleted")
				.setParameter("serviceRequestId", serviceRequestId)
				.executeUpdate();
	}

}