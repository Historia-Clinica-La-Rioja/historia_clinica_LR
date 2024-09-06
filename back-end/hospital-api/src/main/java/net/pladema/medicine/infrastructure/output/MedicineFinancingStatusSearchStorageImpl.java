package net.pladema.medicine.infrastructure.output;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.pladema.medicine.application.port.MedicineFinancingStatusSearchStorage;

import net.pladema.medicine.domain.InstitutionMedicineFinancingStatusBo;
import net.pladema.medicine.domain.MedicineFinancingStatusBo;

import net.pladema.medicine.domain.MedicineFinancingStatusFilterBo;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@AllArgsConstructor
@Service
public class MedicineFinancingStatusSearchStorageImpl implements MedicineFinancingStatusSearchStorage {

	private final EntityManager entityManager;

	@Override
	public Page<MedicineFinancingStatusBo> findAllByFilter(MedicineFinancingStatusFilterBo filter, Pageable pageable) {

		String sqlSelectStatement = "SELECT mfs.id, s.sctid, s.pt, mfs.financed ";

		String sqlFromStatement = "FROM {h-schema}medicine_financing_status mfs " +
				"JOIN {h-schema}snomed s ON (s.id = mfs.id) ";

		String sqlWhereStatement = getWhereStatement(filter, null);

		String sqlOrderByStatement = getOrderByStatement(pageable);

		List<MedicineFinancingStatusBo> resultData = new ArrayList<>();

		List<Object[]> resultQuery = entityManager.createNativeQuery(sqlSelectStatement + sqlFromStatement + sqlWhereStatement + sqlOrderByStatement)
				.setMaxResults(pageable.getPageSize()) //LIMIT
				.setFirstResult(pageable.getPageNumber() * pageable.getPageSize()) //OFFSET
				.getResultList();

		resultQuery.forEach(o -> resultData.add(new MedicineFinancingStatusBo(
				(Integer) o[0], (String) o[1], (String) o[2], (Boolean) o[3]
		)));

		long totalResult = countTotalAmountOfElements(sqlFromStatement + sqlWhereStatement);

		return new PageImpl<>(resultData, pageable, totalResult);
	}

	@Override
	public Page<InstitutionMedicineFinancingStatusBo> findAllByFilter(Integer institutionId, MedicineFinancingStatusFilterBo filter, Pageable pageable){

		String sqlSelectStatement = "SELECT imfs.id, imfs.institution_id, imfs.financed as financed_by_institution, mfs.id as medicine_id, s.sctid, s.pt, mfs.financed as financed_by_domain ";

		String sqlFromStatement = "FROM {h-schema}institution_medicine_financing_status imfs " +
				"JOIN {h-schema}medicine_financing_status mfs ON (imfs.medicine_id = mfs.id) " +
				"JOIN {h-schema}snomed s ON (s.id = mfs.id) ";

		String sqlWhereStatement = getWhereStatement(filter, institutionId);

		String sqlOrderByStatement = getOrderByStatement(pageable);

		List<InstitutionMedicineFinancingStatusBo> result = new ArrayList<>();

		List<Object[]> queryResult = entityManager.createNativeQuery(sqlSelectStatement + sqlFromStatement + sqlWhereStatement + sqlOrderByStatement)
				.setMaxResults(pageable.getPageSize()) //LIMIT
				.setFirstResult(pageable.getPageNumber() * pageable.getPageSize()) //OFFSET
				.getResultList();

		queryResult.forEach(o -> result.add(new InstitutionMedicineFinancingStatusBo(
				(Integer) o[0],
				(Integer) o[1],
				(Boolean) o[2],
				(Integer) o[3],
				(String) o[4],
				(String) o[5],
				(Boolean) o[6]))
		);

		long totalResult = countTotalAmountOfElements(sqlFromStatement + sqlWhereStatement);

		return new PageImpl<>(result, pageable, totalResult);
	}

	@Override
	public Page<InstitutionMedicineFinancingStatusBo> findAllFinancedInInstitution(Integer institutionId, String conceptPt){

		String sqlSelectStatement = "SELECT imfs.id, imfs.institution_id, imfs.financed as financed_by_institution, mfs.id as medicine_id, s.sctid, s.pt, mfs.financed as financed_by_domain ";

		String sqlFromStatement = "FROM {h-schema}institution_medicine_financing_status imfs " +
				"JOIN {h-schema}medicine_financing_status mfs ON (imfs.medicine_id = mfs.id) " +
				"JOIN {h-schema}snomed s ON (s.id = mfs.id) ";

		String sqlWhereStatement = "WHERE (mfs.financed IS TRUE OR imfs.financed IS TRUE) " +
				"AND imfs.institution_id = " + institutionId +
				(conceptPt != null ? " AND s.pt LIKE %" + conceptPt + "% " : " ");

		String sqlOrderByStatement = "ORDER BY s.pt ASC";

		List<Object[]> queryResult = entityManager.createNativeQuery(sqlSelectStatement + sqlFromStatement + sqlWhereStatement + sqlOrderByStatement)
				.setMaxResults(100) //LIMIT
				.getResultList();

		List<InstitutionMedicineFinancingStatusBo> result = new ArrayList<>();

		queryResult.forEach(o -> result.add(new InstitutionMedicineFinancingStatusBo(
				(Integer) o[0],
				(Integer) o[1],
				(Boolean) o[2],
				(Integer) o[3],
				(String) o[4],
				(String) o[5],
				(Boolean) o[6]))
		);

		return new PageImpl<>(result);
	}



	private String getWhereStatement(MedicineFinancingStatusFilterBo filter, Integer institutionId){
		return "WHERE mfs.deleted IS FALSE " +
		(institutionId != null ? "AND imfs.institution_id = " + institutionId + " " : "") +
		(filter.getConceptSctid() != null ? "AND LOWER(s.sctid) LIKE '%" + filter.getConceptSctid().toLowerCase() + "%' " : "") +
		(filter.getConceptPt() != null ? "AND LOWER(s.pt) LIKE '%" + filter.getConceptPt().toLowerCase() + "%' " : "") +
		(filter.getFinancedByDomain() != null ? "AND mfs.financed " + (filter.getFinancedByDomain().equals(Boolean.TRUE) ? "IS TRUE " : "IS FALSE ") : "") +
		(filter.getFinancedByInstitution() != null ? "AND imfs.financed" + (filter.getFinancedByInstitution().equals(Boolean.TRUE) ? "IS TRUE " : "IS FALSE ") : "");
	}

	private String getOrderByStatement(Pageable pageable){
		if (pageable.getSort().getOrderFor("conceptPt") != null){
			return " ORDER BY s.pt " + (pageable.getSort().getOrderFor("conceptPt").isAscending() ? "ASC " : "DESC ");
		}
		if (pageable.getSort().getOrderFor("conceptSctid") != null){
			return " ORDER BY s.sctid " + (pageable.getSort().getOrderFor("conceptSctid").isAscending() ? "ASC " : "DESC ");
		}
		return " ORDER BY s.pt ASC";
	}

	private long countTotalAmountOfElements(String fromAndWhereStatement) {
		String sqlCountSelectStatement = "SELECT COUNT(1) ";
		return ((BigInteger) entityManager.createNativeQuery(sqlCountSelectStatement + fromAndWhereStatement).getSingleResult()).longValue();
	}

}
