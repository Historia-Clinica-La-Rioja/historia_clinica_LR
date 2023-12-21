package net.pladema.reports.repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import net.pladema.establishment.application.hierarchicalunits.FetchDescendantsByHierarchicalUnitId;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public class QueryFactory {

    @PersistenceContext
    private final EntityManager entityManager;

	private final FetchDescendantsByHierarchicalUnitId fetchDescendantsByHierarchicalUnitId;

    public QueryFactory(EntityManager entityManager, FetchDescendantsByHierarchicalUnitId fetchDescendantsByHierarchicalUnitId){
        this.entityManager = entityManager;
    	this.fetchDescendantsByHierarchicalUnitId = fetchDescendantsByHierarchicalUnitId;
	}

    @SuppressWarnings("unchecked")
    public List<ConsultationDetail> query(Integer institutionId, LocalDate start, LocalDate end,
                                          Integer clinicalSpecialtyId, Integer doctorId,
										  Integer hierarchicalUnitTypeId, Integer hierarchicalUnitId,
										  boolean includeHierarchicalUnitDescendants) {

		LocalDateTime startDate = LocalDateTime.of(start.getYear(), start.getMonth(), start.getDayOfMonth(), 0,0);
		LocalDateTime endDate = LocalDateTime.of(end.getYear(), end.getMonth(), end.getDayOfMonth(), 23,59,59).plusHours(3);

		Query outpatientQuery = entityManager.createNamedQuery("Reports.ConsultationDetail");
        outpatientQuery.setParameter("institutionId", institutionId);
        outpatientQuery.setParameter("startDate", startDate);
        outpatientQuery.setParameter("endDate", endDate);
        List<ConsultationDetailWithoutInstitution> data = outpatientQuery.getResultList();

		Query odontologyQuery = entityManager.createNamedQuery("Reports.OdontologyConsultationDetail");
		odontologyQuery.setParameter("institutionId", institutionId);
		odontologyQuery.setParameter("startDate", startDate);
		odontologyQuery.setParameter("endDate", endDate);
		List<ConsultationDetailWithoutInstitution> odontologyData = formatProblemsAndProcedures(odontologyQuery.getResultList());

		Query nursingQuery = entityManager.createNamedQuery("Reports.NursingConsultationDetail");
		nursingQuery.setParameter("institutionId", institutionId);
		nursingQuery.setParameter("startDate", startDate);
		nursingQuery.setParameter("endDate", endDate);

		data.addAll(odontologyData);
		data.addAll(nursingQuery.getResultList());
		data.sort(Comparator.comparing(ConsultationDetailWithoutInstitution::getPatientSurname));


		List<ConsultationDetail> result = mapToConsultationDetail(data, institutionId);

		result = result.stream()
       			.filter(cd -> doctorId == null || Objects.equals(doctorId, cd.getProfessionalId()))
				.filter(cd -> clinicalSpecialtyId == null || Objects.equals(clinicalSpecialtyId, cd.getClinicalSpecialtyId()))
				.filter(cd -> hierarchicalUnitTypeId == null || Objects.equals(hierarchicalUnitTypeId, cd.getHierarchicalUnitTypeId()))
				.collect(Collectors.toList());

		if (!result.isEmpty() && hierarchicalUnitId != null) {
			List<Integer> hierarchicalUnitIds = new ArrayList<>();
			hierarchicalUnitIds.add(hierarchicalUnitId);
			if (includeHierarchicalUnitDescendants)
				hierarchicalUnitIds.addAll(fetchDescendantsByHierarchicalUnitId.run(hierarchicalUnitId));
			result = result.stream().filter(c -> hierarchicalUnitIds.contains(c.getHierarchicalUnitId())).collect(Collectors.toList());
		}

		return result;
	}

	private List<ConsultationDetail> mapToConsultationDetail(List<ConsultationDetailWithoutInstitution> data, Integer institutionId){
		InstitutionInfo institutionInfo = getInstitutionInfo(institutionId);
		List<ConsultationDetail> result = new ArrayList<>();
		for (ConsultationDetailWithoutInstitution row : data) {
			result.add(new ConsultationDetail(row, institutionInfo));
		}
		return result;
	}

	private InstitutionInfo getInstitutionInfo(Integer institutionId){
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
	private List<ConsultationDetailWithoutInstitution> formatProblemsAndProcedures(List<ConsultationDetailWithoutInstitution> list){
		for(ConsultationDetailWithoutInstitution fila : list){
			if(fila.getProblems() != null) {
				String problems = StringUtils.stripAccents(fila.getProblems());
				List withRepeated = List.of(problems.split("/split/"));
				Set<String> noRepeated = new HashSet<String>(withRepeated);
				fila.setProblems(this.reduceAndFormat(withRepeated, noRepeated));
			}
			if(fila.getProcedures() != null) {
				String procedures = StringUtils.stripAccents(fila.getProcedures());
				List withRepeated = List.of(procedures.split("/split/"));
				Set<String> noRepeated = new HashSet<String>(withRepeated);
				fila.setProcedures(this.reduceAndFormat(withRepeated, noRepeated));
			}
		}
		return list;
	}

	private String reduceAndFormat(List withRepeated, Set<String> noRepeated){
		String result = "";
		for (String item : noRepeated) {
			int repetitions = Collections.frequency(withRepeated, item);
			if (result.equals("")) {
				if(repetitions > 1)
					result += "(" + repetitions + ") " + item;
				else
					result += item;
			}
			else {
				if (repetitions > 1)
					result += ", (" + repetitions + ") " + item;
				else
					result += ", " + item;
			}
		}
		return result;
	}

    @SuppressWarnings("unchecked")
    @Transactional(readOnly = true)
    public List<ConsultationSummary> fetchConsultationSummaryData(Integer institutionId, LocalDate startDate, LocalDate endDate){
		List<ConsultationSummary> result = new ArrayList<>();
		result.addAll(entityManager.createNamedQuery("Reports.ConsultationSummary")
				.setParameter("institutionId", institutionId)
				.setParameter("from", startDate)
				.setParameter("to", endDate)
				.getResultList());
		result.addAll(entityManager.createNamedQuery("Reports.OdontologyConsultationSummary")
				.setParameter("institutionId", institutionId)
				.setParameter("from", startDate)
				.setParameter("to", endDate)
				.getResultList());
		result.addAll(entityManager.createNamedQuery("Reports.NursingConsultationSummary")
				.setParameter("institutionId", institutionId)
				.setParameter("from", startDate)
				.setParameter("to", endDate)
				.getResultList());
		return result;
    }
}
