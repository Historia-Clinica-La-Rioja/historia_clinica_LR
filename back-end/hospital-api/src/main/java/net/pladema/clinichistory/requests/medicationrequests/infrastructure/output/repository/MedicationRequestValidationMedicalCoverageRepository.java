package net.pladema.clinichistory.requests.medicationrequests.infrastructure.output.repository;

import lombok.RequiredArgsConstructor;

import net.pladema.patient.domain.GetMedicalCoverageHealthInsuranceValidationDataBo;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

import java.util.List;

@RequiredArgsConstructor
@Repository
public class MedicationRequestValidationMedicalCoverageRepository {

	private final EntityManager entityManager;

	@Transactional(readOnly = true)
	public Short fetchFundingNumberByMedicalCoverageName(GetMedicalCoverageHealthInsuranceValidationDataBo medicalCoverage) {
		String queryString = "SELECT mrvmc.funderNumber " +
				"FROM MedicationRequestValidationMedicalCoverage mrvmc " +
				"WHERE mrvmc.cuit = :cuit";
		List<?> funderNumber = entityManager.createQuery(queryString)
				.setParameter("cuit", medicalCoverage.getCuit())
				.getResultList();
		if (!funderNumber.isEmpty())
			return (Short) funderNumber.get(0);

		queryString = "SELECT mrvmc.funderNumber " +
				"FROM MedicationRequestValidationMedicalCoverage mrvmc " +
				"WHERE mrvmc.commercialName = :acronym";
		funderNumber = entityManager.createQuery(queryString)
				.setParameter("acronym", medicalCoverage.getAcronym())
				.getResultList();
		if (!funderNumber.isEmpty())
			return (Short) funderNumber.get(0);

		queryString = "SELECT mrvmc.funderNumber, ts_rank(to_tsvector(:medicalCoverageName), plainto_tsquery(mrvmc.commercialName), 2) AS rank " +
				"FROM MedicationRequestValidationMedicalCoverage mrvmc " +
				"WHERE fts(:medicalCoverageName, mrvmc.commercialName) = TRUE " +
				"ORDER BY rank DESC";
		funderNumber = entityManager.createQuery(queryString)
				.setParameter("medicalCoverageName", medicalCoverage.getMedicalCoverageName())
				.getResultList();
		if (!funderNumber.isEmpty())
			return (Short) ((Object[]) funderNumber.get(0))[0];
		return null;
	}

}
