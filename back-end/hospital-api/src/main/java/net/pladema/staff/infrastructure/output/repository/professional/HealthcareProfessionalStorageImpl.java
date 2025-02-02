package net.pladema.staff.infrastructure.output.repository.professional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import ar.lamansys.sgh.shared.infrastructure.input.service.SharedPersonPort;
import ar.lamansys.sgx.shared.repositories.QueryPart;
import lombok.RequiredArgsConstructor;
import net.pladema.staff.domain.HealthcareProfessionalSearchBo;
import net.pladema.staff.repository.domain.HealthcareProfessionalSearchQuery;
import net.pladema.staff.repository.domain.HealthcareProfessionalVo;
import net.pladema.staff.service.domain.HealthcareProfessionalBo;

import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;
import net.pladema.staff.application.ports.HealthcareProfessionalStorage;
import net.pladema.staff.domain.LicenseNumberBo;
import net.pladema.staff.domain.ProfessionBo;
import net.pladema.staff.domain.ProfessionSpecialtyBo;
import net.pladema.staff.domain.ProfessionalCompleteBo;
import net.pladema.staff.service.domain.ClinicalSpecialtyBo;
import ar.lamansys.sgh.shared.domain.ELicenseNumberTypeBo;

@Slf4j
@RequiredArgsConstructor
@Service
public class HealthcareProfessionalStorageImpl implements HealthcareProfessionalStorage {

	private final EntityManager entityManager;
	private final SharedPersonPort sharedPersonPort;

	@Override
	public ProfessionalCompleteBo fetchProfessionalByUserId(Integer userId) {
		log.debug("fetchProfessionalByUserId -> userId={}", userId);
		String sqlString = "" +
				"SELECT hp.id, p.id, p.firstName, p.middleNames, p.lastName, pe.nameSelfDetermination, p.otherLastNames " +
				"FROM HealthcareProfessional AS hp " +
				"RIGHT JOIN UserPerson AS up ON (up.pk.personId = hp.personId) " +
				"JOIN Person p ON (up.pk.personId = p.id) " +
				"LEFT JOIN PersonExtended pe ON (p.id = pe.id) " +
				"WHERE up.pk.userId = :userId ";
		Query query = entityManager.createQuery(sqlString);
		query.setParameter("userId", userId);
		Object[] authorInfo = (Object[]) query.getSingleResult();
		ProfessionalCompleteBo result = new ProfessionalCompleteBo((Integer) authorInfo[0], (Integer) authorInfo[1],
				(String) authorInfo[2], (String) authorInfo[3], (String) authorInfo[4], (String) authorInfo[5], (String) authorInfo[6]);

		result.setProfessions(buildProfessions(result.getPersonId()));
		log.trace("execute result query -> {}", result);
		return result;
	}

	@Override
	public ProfessionalCompleteBo fetchProfessionalById(Integer professionalId) {
		log.debug("fetchProfessionalById -> professionalId={}", professionalId);
		String sqlString = "" +
				"SELECT hp.id, p.id, p.firstName, p.middleNames, p.lastName, pe.nameSelfDetermination, p.otherLastNames " +
				"FROM HealthcareProfessional AS hp " +
				"JOIN Person p ON (hp.personId = p.id) " +
				"JOIN PersonExtended pe ON (p.id = pe.id) " +
				"WHERE hp.id = :professionalId " +
				"";
		Query query = entityManager.createQuery(sqlString);
		query.setParameter("professionalId", professionalId);
		Object[] authorInfo = (Object[]) query.getSingleResult();
		ProfessionalCompleteBo result = new ProfessionalCompleteBo((Integer) authorInfo[0], (Integer) authorInfo[1],
				(String) authorInfo[2], (String) authorInfo[3], (String) authorInfo[4], (String) authorInfo[5], (String) authorInfo[6]);

		result.setProfessions(buildProfessions(result.getPersonId()));
		log.trace("execute result query -> {}", result);
		return result;
	}

	@Override
	public List<ProfessionalCompleteBo> fetchProfessionalsByIds(List<Integer> professionalIds) {
		log.debug("fetchProfessionalById -> professionalIds -> {}", professionalIds);
		String sqlString = "SELECT hp.id, p.id, p.firstName, p.middleNames, p.lastName, pe.nameSelfDetermination, p.otherLastNames " +
				"FROM HealthcareProfessional AS hp " +
				"JOIN Person p ON (hp.personId = p.id) " +
				"JOIN PersonExtended pe ON (p.id = pe.id) " +
				"WHERE hp.id IN :professionalIds";
		Query query = entityManager.createQuery(sqlString);
		query.setParameter("professionalIds", professionalIds);
		List<Object[]> queryResult = query.getResultList();
		List<ProfessionalCompleteBo> result = queryResult.stream().map(authorInfo -> new ProfessionalCompleteBo((Integer) authorInfo[0], (Integer) authorInfo[1],
				(String) authorInfo[2], (String) authorInfo[3], (String) authorInfo[4], (String) authorInfo[5], (String) authorInfo[6])).collect(Collectors.toList());
		result.forEach(professional -> professional.setProfessions(buildProfessions(professional.getPersonId())));
		log.trace("execute result query -> {}", result);
		return result;
	}

	@Override
	public List<HealthcareProfessionalBo> fetchAllProfessionalsByFilter(HealthcareProfessionalSearchBo searchCriteria, List<Short> professionalERolIds) {
		HealthcareProfessionalSearchQuery healthcareProfessionalSearchQuery = new HealthcareProfessionalSearchQuery(searchCriteria, professionalERolIds);
		QueryPart queryPart = buildQuery(healthcareProfessionalSearchQuery);
		Query query = entityManager.createNativeQuery(queryPart.toString());
		queryPart.configParams(query);
		List<HealthcareProfessionalVo> professionals = healthcareProfessionalSearchQuery.construct(query.getResultList());
		return professionals.stream()
				.map(this::mapToHealthcareProfessionalBo)
				.collect(Collectors.toList());
	}

	private List<ProfessionBo> buildProfessions(Integer personId) {
		String sqlString = "" +
				"SELECT pp.id, " +
				"		ps.id as professionalId, ps.description " +
				"FROM HealthcareProfessional hp " +
				"INNER JOIN ProfessionalProfessions pp ON (pp.healthcareProfessionalId = hp.id) " +
				"INNER JOIN ProfessionalSpecialty ps ON (pp.professionalSpecialtyId = ps.id) " +
				"WHERE hp.personId = :personId " +
				"AND hp.deleteable.deleted = false " +
				"AND pp.deleteable.deleted = false " +
				"";

		List<Object[]> rows = new ArrayList<>();
		Integer searchTries = 0;
		Query query = entityManager.createQuery(sqlString);
		query.setParameter("personId", personId);

		while (rows.isEmpty() && (searchTries < 10)){
			rows = query.getResultList();
			searchTries++;
		}
		return rows.stream()
				.map(this::buildProfession)
				.collect(Collectors.toList());
	}

	private ProfessionBo buildProfession(Object[]  row) {
		var result = new ProfessionBo((Integer) row[0], (Integer) row[1],(String) row[2]);
		result.setLicenses(buildProfessionalLicenses(result.getId()));
		result.setSpecialties(buildSpecialties(result.getId()));
		return result;
	}

	private List<ProfessionSpecialtyBo> buildSpecialties(Integer professionalProfessionId) {
		String sqlString = "" +
				"SELECT hps.id, " +
				"		cs.id, cs.name " +
				"FROM HealthcareProfessionalSpecialty hps " +
				"INNER JOIN ClinicalSpecialty cs ON (cs.id = hps.clinicalSpecialtyId) " +
				"WHERE hps.professionalProfessionId = :professionalProfessionId " +
				"AND hps.deleteable.deleted = false " +
				"";

		Query query = entityManager.createQuery(sqlString);
		query.setParameter("professionalProfessionId", professionalProfessionId);
		List<Object[]> rows = query.getResultList();
		return rows.stream()
				.map(this::buildProfessionalSpecialty)
				.collect(Collectors.toList());
	}

	private ProfessionSpecialtyBo buildProfessionalSpecialty(Object[] row) {
		List<LicenseNumberBo> licenses =  buildSpecialtyLicenses((Integer) row[0]);
		return new ProfessionSpecialtyBo((Integer) row[0], new ClinicalSpecialtyBo((Integer) row[1], (String) row[2]), licenses);
	}
	private List<LicenseNumberBo> buildSpecialtyLicenses(Integer healthcareProfessionalSpecialtyId) {
		String sqlString = "" +
				"SELECT pln.id, " +
				"		pln.licenseNumber, pln.type " +
				"FROM ProfessionalLicenseNumber pln " +
				"WHERE pln.healthcareProfessionalSpecialtyId = :healthcareProfessionalSpecialtyId " +
				"";

		Query query = entityManager.createQuery(sqlString);
		query.setParameter("healthcareProfessionalSpecialtyId", healthcareProfessionalSpecialtyId);
		List<Object[]> rows = query.getResultList();
		return rows.stream()
				.map(this::buildLicense)
				.collect(Collectors.toList());
	}
	private List<LicenseNumberBo> buildProfessionalLicenses(Integer professionalProfessionId) {
		String sqlString = "" +
				"SELECT pln.id, " +
				"		pln.licenseNumber, pln.type " +
				"FROM ProfessionalLicenseNumber pln " +
				"WHERE pln.professionalProfessionId = :professionalProfessionId " +
				"";

		Query query = entityManager.createQuery(sqlString);
		query.setParameter("professionalProfessionId", professionalProfessionId);
		List<Object[]> rows = query.getResultList();
		return rows.stream()
				.map(this::buildLicense)
				.collect(Collectors.toList());
	}

	private LicenseNumberBo buildLicense(Object[] row) {
		return new LicenseNumberBo((Integer) row[0], (String) row[1], (ELicenseNumberTypeBo) row[2]);
	}

	private QueryPart buildQuery(HealthcareProfessionalSearchQuery healthcareProfessionalSearchQuery){

		QueryPart firstQuery = new QueryPart("SELECT ")
				.concatPart(healthcareProfessionalSearchQuery.select())
				.concat(" FROM ")
				.concatPart(healthcareProfessionalSearchQuery.from())
				.concat(" WHERE ")
				.concatPart(healthcareProfessionalSearchQuery.where());

		QueryPart secondQuery = new QueryPart("SELECT ")
				.concatPart(healthcareProfessionalSearchQuery.select())
				.concat(" FROM ")
				.concatPart(healthcareProfessionalSearchQuery.fromSecondQuery())
				.concat(" WHERE ")
				.concatPart(healthcareProfessionalSearchQuery.where());

		return firstQuery.concat(" UNION ").concatPart(secondQuery)
				.concat(" ORDER BY ")
				.concat("last_name");
	}

	private HealthcareProfessionalBo mapToHealthcareProfessionalBo(HealthcareProfessionalVo hcp) {
		String completePersonName = sharedPersonPort.parseFormalPersonName(
				hcp.getFirstName(),
				hcp.getMiddleNames(),
				hcp.getLastName(),
				hcp.getOtherLastNames(),
				hcp.getNameSelfDetermination()
		);
		return new HealthcareProfessionalBo(hcp, completePersonName);
	}
}
