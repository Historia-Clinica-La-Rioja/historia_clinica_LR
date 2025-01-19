package net.pladema.reports.infrastructure.output;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.pladema.reports.application.ports.InstitutionReportStorage;

import net.pladema.reports.repository.InstitutionInfo;

import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class InstitutionReportStorageImpl implements InstitutionReportStorage {

	private final EntityManager entityManager;

	@Override
	public InstitutionInfo getInstitutionInfo(Integer institutionId) {
		log.debug("Input parameter -> {} ", institutionId);
		String sqlString = "SELECT p.description, d.description, i.sisaCode, i.name " +
				"FROM Institution i " +
				"JOIN Address a ON (i.addressId = a.id) " +
				"JOIN City c ON (a.cityId = c.id) " +
				"JOIN Department d ON (c.departmentId = d.id) " +
				"JOIN Province p ON (d.provinceId = p.id) " +
				"WHERE i.id = :institutionId";
		List queryResult = entityManager.createQuery(sqlString)
				.setParameter("institutionId", institutionId)
				.getResultList();
		Object[] resultSearch = queryResult.size() == 1 ? (Object[]) queryResult.get(0) : null;
		assert resultSearch != null;
		return new InstitutionInfo(
				(resultSearch[0]!=null)? (String) resultSearch[0] : null,
				(resultSearch[1]!=null)? (String) resultSearch[1] : null,
				(resultSearch[2]!=null)? (String) resultSearch[2] : null,
				(resultSearch[3]!=null)? (String) resultSearch[3] : null);
	}
}
