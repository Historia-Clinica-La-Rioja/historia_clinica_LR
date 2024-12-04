package net.pladema.clinichistory.requests.medicationrequests.infrastructure.output.repository;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;

@RequiredArgsConstructor
@Service
public class SnomedCTRepository {

	private final EntityManager entityManager;

	public String fetchSnomedSctidWithPresentationFromCommercialMedication(String commercialSctid, Short presentationQuantity) {
		String queryString = "SELECT cpt.id " +
				"FROM snomedct.relationship r " +
				"JOIN snomedct.concept_term cpt ON (r.sourceid = cpt.id) " +
				"WHERE r.destinationid = :commercialSctid " +
				"AND r.active = '1' " +
				"AND cpt.type = 'fsn' " +
				"AND cpt.term LIKE '%" + presentationQuantity + " UNIDADES%'";
		try {
			return  (String) entityManager.createNativeQuery(queryString)
					.setParameter("commercialSctid", commercialSctid)
					.getSingleResult();
		}
		catch (Exception e) {
			return null;
		}
	}

	public String fetchSnomedSctidWithPresentationFromGenericMedication(String genericSctid, Short presentationQuantity) {
		String queryString = "SELECT cpt.id " +
				"FROM snomedct.relationship r " +
				"JOIN snomedct.concept_term cpt ON (r.sourceid = cpt.id) " +
				"WHERE r.destinationid IN ( " +
				"SELECT ct.id " +
				"FROM snomedct.relationship rf " +
				"JOIN snomedct.concept_term ct ON (rf.sourceid = ct.id) " +
				"JOIN snomedct.concept_term ct2 ON (rf.typeid = ct2.id) " +
				"JOIN snomedct.concept_term ct3 ON (rf.destinationid = ct3.id) " +
				"WHERE rf.destinationid = :genericSctid " +
				"AND rf.active = '1' " +
				"AND ct.type = 'fsn' " +
				"AND ct2.type = 'fsn' " +
				"AND ct3.type = 'fsn' " +
				") " +
				"AND r.active = '1' " +
				"AND cpt.type = 'fsn' " +
				"AND cpt.term LIKE '%" + presentationQuantity +" UNIDADES%' " +
				"LIMIT 1";
		try {
			return (String) entityManager.createNativeQuery(queryString)
					.setParameter("genericSctid", genericSctid)
					.getSingleResult();
		}
		catch (Exception e) {
			return null;
		}
	}

}
