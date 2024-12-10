package net.pladema.clinichistory.requests.medicationrequests.infrastructure.output.repository;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;

@RequiredArgsConstructor
@Repository
public class CommercialMedicationSchemaRepository {

	private final EntityManager entityManager;

	public Integer fetchExternalProviderMedicationIdFromSnomedSctid(String snomedSctid) {
		String queryString = "SELECT a.id " +
				"FROM commercial_medication.article a " +
				"WHERE a.snomed_id = :snomedSctid";
		try {
			return (Integer) entityManager.createNativeQuery(queryString)
					.setParameter("snomedSctid", snomedSctid)
					.getSingleResult();
		}
		catch (Exception e) {
			return null;
		}
	}

}
