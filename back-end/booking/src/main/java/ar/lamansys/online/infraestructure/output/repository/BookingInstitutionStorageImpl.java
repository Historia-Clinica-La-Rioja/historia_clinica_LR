package ar.lamansys.online.infraestructure.output.repository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;

import ar.lamansys.online.domain.integration.BookingInstitutionExtendedBo;

import org.springframework.stereotype.Service;

import ar.lamansys.online.application.integration.BookingInstitutionStorage;
import ar.lamansys.online.domain.integration.BookingInstitutionBo;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class BookingInstitutionStorageImpl implements BookingInstitutionStorage {

    private final EntityManager entityManager;

    public BookingInstitutionStorageImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public List<BookingInstitutionBo> findBookingInstitutions() {
        log.debug("Find booking institutions");
        String sqlString = "" +
                "SELECT bi.institution_id AS id, " +
                "i.name AS description " +
                "FROM booking_institution bi " +
                "JOIN institution i ON (i.id = bi.institution_id)";

        List<Object[]> rows = entityManager.createNativeQuery(sqlString).getResultList();
        return createInstitutionBoList(rows);
    }

	@Override
	public List<BookingInstitutionExtendedBo> findBookingInstitutionsExtended() {
		log.debug("Find booking institutions extended");
		String sqlString = "" +
				"SELECT distinct bi.institution_id AS id, " +
				"i.name AS description, " +
				"i.sisa_code AS refset, " +
				"CASE WHEN d.description IS NOT NULL THEN d.description ELSE "+ " 'No informada' " + " END AS dependency, " +
				"CONCAT(ad.street, ' ', ad.number) AS address, " +
				"c.description AS city, " +
				"d2.description AS department, " +
				"cs.name, " +
				"di.alias " +
				"FROM booking_institution bi " +
				"JOIN institution i ON (i.id = bi.institution_id) " +
				"LEFT JOIN dependency d ON (d.id = i.dependency_id) " +
				"LEFT JOIN address ad ON (i.address_id = ad.id) " +
				"LEFT JOIN city c ON (ad.city_id = c.id) " +
				"LEFT JOIN department d2 ON (c.department_id = d2.id) " +
				"JOIN doctors_office dof ON (dof.institution_id = i.id) " +
				"JOIN diary di ON (di.doctors_office_id = dof.id) " +
				"JOIN diary_opening_hours doh ON (doh.diary_id = di.id) " +
				"JOIN clinical_specialty cs ON (cs.id = di.clinical_specialty_id) " +
				"WHERE di.deleted IS NOT TRUE AND di.end_date > CURRENT_DATE " +
				"AND cs.clinical_specialty_type_id = 2 " +
				"AND doh.external_appointments_allowed = true " +
				"AND doh.medical_attention_type_id = 1 " +
				"ORDER BY i.name, cs.name";

		List<Object[]> rows = entityManager.createNativeQuery(sqlString).getResultList();
		return createInstitutionExtendedBoList(rows);
	}
    private List<BookingInstitutionBo> createInstitutionBoList(List<Object[]> rows) {
        if (rows.isEmpty())
            return Collections.emptyList();
        return rows.stream().map(row ->
                new BookingInstitutionBo((Integer) row[0], (String) row[1])
        ).collect(Collectors.toList());
    }

	private List<BookingInstitutionExtendedBo> createInstitutionExtendedBoList(List<Object[]> rows) {

		List<BookingInstitutionExtendedBo> result = new ArrayList<>(Collections.emptyList());

		for (Object[] row : rows) {
			var toInsert = BookingInstitutionExtendedBo.builder()
					.id((Integer) row[0])
					.description((String) row[1])
					.sisaCode((String) row[2])
					.dependency((String) row[3])
					.address((String) row[4])
					.city((String) row[5])
					.department((String) row[6])
					.clinicalSpecialtiesNames(new ArrayList<>(List.of((String) row[7])))
					.aliases(row[8] == null ? null : new ArrayList<>(List.of((String) row[8])))
					.build();

			var index = result.indexOf(toInsert);

			if (index == -1) {
				result.add(toInsert);
			} else {
				//there's already a record for that institution
				//so we add the clinical specialty
				var existing = result.get(index);
				var names = existing.getClinicalSpecialtiesNames();
				if(!names.contains((String) row[7])) {
					names.add((String) row[7]);
				}

				//and we add the alias of the diary, if it exists
				if(toInsert.getAliases() != null) {
					if(existing.getAliases() == null) {
						existing.setAliases(toInsert.getAliases());
					} else {
						existing.getAliases().add(toInsert.getAliases().get(0));
					}
				}
			}
		}

		return result;
	}

}
