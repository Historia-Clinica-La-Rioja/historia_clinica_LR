package net.pladema.electronicjointsignature.healthcareprofessionallicense.infrastructure.output;

import lombok.AllArgsConstructor;

import net.pladema.electronicjointsignature.healthcareprofessionallicense.domain.ElectronicJointSignatureInstitutionProfessionalBo;

import net.pladema.electronicjointsignature.healthcareprofessionallicense.domain.ElectronicJointSignatureLicenseBo;

import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@AllArgsConstructor
@Repository
public class ElectronicJointSignatureInstitutionalProfessionalLicenseStorage {

	private EntityManager entityManager;

	public List<ElectronicJointSignatureInstitutionProfessionalBo> fetchInstitutionalProfessionalLicenseStorage(Integer institutionId, Integer excludedHealthcareProfessionalId) {
		List<Integer> healthcareProfessionalIds = fetchInstitutionProfessionals(institutionId, excludedHealthcareProfessionalId);
		List<ElectronicJointSignatureInstitutionProfessionalBo> result = fetchLicenseData(healthcareProfessionalIds);
		return result;
	}

	private List<ElectronicJointSignatureInstitutionProfessionalBo> fetchLicenseData(List<Integer> healthcareProfessionalIds) {
		String queryString =
				"SELECT DISTINCT hp.id, hp.person_id, pln.license_number, pln.type_license_number, cs.name " +
						"FROM {h-schema}professional_professions pp " +
						"JOIN {h-schema}healthcare_professional_specialty hps ON (hps.professional_profession_id = pp.id) " +
						"LEFT JOIN {h-schema}professional_license_numbers pln ON (pln.professional_profession_id = pp.id) " +
						"JOIN {h-schema}clinical_specialty cs ON (cs.id = hps.clinical_specialty_id) " +
						"JOIN {h-schema}healthcare_professional hp ON (hp.id = pp.healthcare_professional_id) " +
						"WHERE pp.healthcare_professional_id IN :healthcareProfessionalIds " +
						"AND pp.deleted IS FALSE " +
						"AND hps.deleted IS FALSE";
		Query query = entityManager.createNativeQuery(queryString).setParameter("healthcareProfessionalIds", healthcareProfessionalIds);
		List<Object[]> queryResult = query.getResultList();
		List<ElectronicJointSignatureInstitutionProfessionalBo> result = parseToInstitutionProfessionalBos(healthcareProfessionalIds, queryResult);
		return result;
	}

	private List<ElectronicJointSignatureInstitutionProfessionalBo> parseToInstitutionProfessionalBos(List<Integer> healthcareProfessionalIds, List<Object[]> queryResult) {
		List<ElectronicJointSignatureInstitutionProfessionalBo> result = new ArrayList<>();
		healthcareProfessionalIds.forEach(healthcareProfessionalId -> processProfessionalData(queryResult, healthcareProfessionalId, result));
		return result;
	}

	private void processProfessionalData(List<Object[]> queryResult, Integer healthcareProfessionalId, List<ElectronicJointSignatureInstitutionProfessionalBo> result) {
		Optional<ElectronicJointSignatureInstitutionProfessionalBo> professional = parseToInstitutionProfessionalBo(healthcareProfessionalId, queryResult);
		professional.ifPresent(result::add);
	}

	private Optional<ElectronicJointSignatureInstitutionProfessionalBo> parseToInstitutionProfessionalBo(Integer healthcareProfessionalId, List<Object[]> data) {
		List<Object[]> relatedLicensesData = data.stream().filter(element -> element[0].equals(healthcareProfessionalId)).collect(Collectors.toList());
		if (!relatedLicensesData.isEmpty())
			return Optional.of(getElectronicJointSignatureInstitutionProfessionalBo(relatedLicensesData));
		return Optional.empty();
	}

	private ElectronicJointSignatureInstitutionProfessionalBo getElectronicJointSignatureInstitutionProfessionalBo(List<Object[]> relatedLicensesData) {
		ElectronicJointSignatureInstitutionProfessionalBo result = new ElectronicJointSignatureInstitutionProfessionalBo();
		result.setPersonId((Integer) relatedLicensesData.get(0)[1]);
		result.setHealthcareProfessionalId((Integer) relatedLicensesData.get(0)[0]);
		Set<String> clinicalSpecialties = relatedLicensesData.stream().map(data -> (String) data[4]).collect(Collectors.toSet());
		result.setClinicalSpecialties(new ArrayList<>(clinicalSpecialties));
		result.setLicense(parseLicense(relatedLicensesData.get(0)));
		return result;
	}

	private ElectronicJointSignatureLicenseBo parseLicense(Object[] licenseData) {
		ElectronicJointSignatureLicenseBo result = new ElectronicJointSignatureLicenseBo();
		result.setNumber((String) licenseData[2]);
		result.setType((Short) licenseData[3]);
		return result;
	}

	private List<Integer> fetchInstitutionProfessionals(Integer institutionId, Integer excludedHealthcareProfessionalId) {
		String queryString =
				"SELECT DISTINCT hp.id " +
				"FROM {h-schema}healthcare_professional hp " +
				"JOIN {h-schema}user_person up ON (up.person_id = hp.person_id) " +
				"JOIN {h-schema}user_role ur ON (ur.user_id = up.user_id) " +
				"WHERE ur.institution_id = :institutionId " +
				"AND hp.id != :excludedHealthcareProfessionalId " +
				"AND ur.deleted IS FALSE " +
				"AND hp.deleted IS FALSE";
		Query query = entityManager.createNativeQuery(queryString)
				.setParameter("institutionId", institutionId)
				.setParameter("excludedHealthcareProfessionalId", excludedHealthcareProfessionalId);
		List<Integer> result = query.getResultList();
		return result;
	}

}
