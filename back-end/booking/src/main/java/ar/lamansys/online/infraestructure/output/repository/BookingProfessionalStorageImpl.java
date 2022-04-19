package ar.lamansys.online.infraestructure.output.repository;

import ar.lamansys.online.domain.professional.BookingProfessionalBo;
import ar.lamansys.online.application.professional.BookingProfessionalStorage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class BookingProfessionalStorageImpl implements BookingProfessionalStorage {

	private final Logger logger;

	private final EntityManager entityManager;

	public BookingProfessionalStorageImpl(EntityManager entityManager) {
		this.logger = LoggerFactory.getLogger(this.getClass());
		this.entityManager = entityManager;
	}

	@Override
	public Optional<List<BookingProfessionalBo>> findBookingProfessionals(Integer institutionId, Integer medicalCoverageId, boolean all) {
		logger.debug("Find professionals by medicalCoverageId {} and institution {}", medicalCoverageId, institutionId);

		String joinSentences = all ? "LEFT JOIN healthcare_professional_health_insurance AS hphi " +
				"ON (hphi.healthcare_professional_id = hp.id) " +
				"JOIN v_booking_person p ON (p.id = hp.person_id) " +
				"JOIN v_booking_user_person up ON (p.id = up.person_id) " +
				"JOIN v_booking_user_role ur ON (ur.user_id = up.user_id) " +
				"WHERE ur.institution_id = :institutionId " :
				"JOIN healthcare_professional_health_insurance AS hphi ON (hphi.healthcare_professional_id = hp.id) " +
				"JOIN v_booking_person p ON (p.id = hp.person_id) " +
				"JOIN v_booking_user_person up ON (p.id = up.person_id) " +
				"JOIN v_booking_user_role ur ON (ur.user_id = up.user_id) " +
				"WHERE ur.institution_id = :institutionId " +
				"AND hphi.medical_coverage_id = :medicalCoverageId ";

		String sqlString = "SELECT DISTINCT hp.id, " +
				"p.first_name, " +
				"p.last_name, " +
				"hphi.medical_coverage_id AS coverage " +
				"FROM v_booking_healthcare_professional AS hp " +
				joinSentences +
				"ORDER BY p.last_name ";

		List<Object[]> rows = all ?
				entityManager.createNativeQuery(sqlString)
						.setParameter("institutionId", institutionId)
						.getResultList() :
				entityManager.createNativeQuery(sqlString)
						.setParameter("medicalCoverageId", medicalCoverageId)
						.setParameter("institutionId", institutionId)
						.getResultList();

		List<BookingProfessionalBo> result = createBookingProfessionalBoList(rows, medicalCoverageId);
		logger.debug("Find practice by Specialty and HealthInsurance -> {}", result);
		return Optional.ofNullable(result);
	}

	private List<BookingProfessionalBo> createBookingProfessionalBoList(List<Object[]> rows, Integer medicalCoverageId) {
		if (rows.isEmpty())
			return new ArrayList<>();
		var medicalCoverages = rows.stream().map(row ->
				new BookingProfessionalBo((Integer) row[0],
						row[2] + ", " + row[1],
						row[3] != null && row[3].equals(medicalCoverageId))
		).distinct().collect(Collectors.toList());

		return medicalCoverages.stream()
				.filter(mc -> isDuplicatedWithDifferentMedicalCoverage(medicalCoverages, mc))
				.collect(Collectors.toList());
	}

	private boolean isDuplicatedWithDifferentMedicalCoverage(List<BookingProfessionalBo> rows, BookingProfessionalBo bpbo) {
		return !(bpbo.getCoverage().equals(Boolean.FALSE) && rows.stream()
				.anyMatch(el -> el.getCoverage().equals(Boolean.TRUE) && el.getId().equals(bpbo.getId())));
	}
}
