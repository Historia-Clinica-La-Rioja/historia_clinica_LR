package ar.lamansys.online.infraestructure.output.repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;

import org.springframework.stereotype.Service;

import ar.lamansys.online.application.specialty.BookingSpecialtyStorage;
import ar.lamansys.online.domain.specialty.BookingSpecialtyBo;
import ar.lamansys.online.domain.specialty.PracticeBo;
import ar.lamansys.online.infraestructure.output.entity.VBookingClinicalSpecialty;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class BookingSpecialtyStorageImpl implements BookingSpecialtyStorage {

    private static final short SPECIALTY = 2;

    private final BookingClinicalSpecialtyJpaRepository repository;
    private final EntityManager entityManager;

    public BookingSpecialtyStorageImpl(
            BookingClinicalSpecialtyJpaRepository repository,
            EntityManager entityManager // quitar
    ) {
        this.repository = repository;
        this.entityManager = entityManager;
    }

    @Override
    public Optional<List<PracticeBo>> findBySpecialtyAndHealthInsurance(
            Integer clinicalSpecialtyId,
            Integer medicalCoverageId,
            boolean all
    ) {

        log.debug(
                "Find practice by Specialty {} and HealthInsurance {}",
                clinicalSpecialtyId,
				medicalCoverageId
        );

        String sqlString = all ?
                "SELECT csmmp.mandatory_medical_practice_id AS practice_id, " +
                        "mmp.description AS practice_name, " +
                        "CASE WHEN hip.medical_coverage_id = :medicalCoverageId THEN 1 ELSE 0 END AS has_coverage, " +
                        "hip.coverage_information as coverage_info, " +
                        "mmp.snomed_id as snomed_id " +
                        "FROM v_booking_clinical_specialty cs " +
                        "JOIN clinical_specialty_mandatory_medical_practice csmmp ON(cs.id = csmmp.clinical_specialty_id) " +
                        "JOIN mandatory_medical_practice mmp ON (mmp.mandatory_medical_practice_id = csmmp.mandatory_medical_practice_id) " +
                        "LEFT JOIN health_insurance_practice hip ON (hip.clinical_specialty_mandatory_medical_practice_id = csmmp.id) " +
                        "WHERE csmmp.clinical_specialty_id = :clinicalSpecialtyId"
                :
                "SELECT csmmp.mandatory_medical_practice_id AS practice_id, " +
                        "mmp.description AS practice_name, " +
                        "1 AS has_coverage, " +
                        "hip.coverage_information as coverage_info, " +
                        "mmp.snomed_id as snomed_id " +
                        "FROM v_booking_clinical_specialty cs " +
                        "JOIN clinical_specialty_mandatory_medical_practice csmmp ON(cs.id = csmmp.clinical_specialty_id) " +
                        "JOIN mandatory_medical_practice mmp ON (mmp.mandatory_medical_practice_id = csmmp.mandatory_medical_practice_id) " +
                        "JOIN health_insurance_practice hip ON (hip.clinical_specialty_mandatory_medical_practice_id = csmmp.id) " +
                        "WHERE csmmp.clinical_specialty_id = :clinicalSpecialtyId " +
                        "AND hip.medical_coverage_id = :medicalCoverageId";

        List<Object[]> rows = entityManager.createNativeQuery(sqlString)
                .setParameter("clinicalSpecialtyId", clinicalSpecialtyId)
                .setParameter("medicalCoverageId", medicalCoverageId)
                .getResultList();
        List<PracticeBo> result = createPracticeBoList(rows);
        log.debug("Find practice by Specialty and HealthInsurance -> {}", result);
        return Optional.ofNullable(result);
    }

    @Override
    public Optional<List<PracticeBo>> findByProfessionalAndHealthInsurance(
            Integer healthcareProfessionalId,
            Integer medicalCoverageId,
            Integer clinicalSpecialtyId,
            Boolean all
    ) {

        log.debug(
                "Find practice by healthcareProfessionalId {} and by HealthInsurance {} and clinical specialty {} depending on all: {}",
                healthcareProfessionalId,
				medicalCoverageId,
                clinicalSpecialtyId,
                all
        );

        String sqlString = all ?
                "SELECT DISTINCT csmmp.mandatory_medical_practice_id AS practice_id, " +
                "mmp.description AS practice_name, " +
                "CASE WHEN hip.medical_coverage_id = :medicalCoverageId THEN 1 ELSE 0 END AS has_coverage, " +
                "hip.coverage_information as coverage_info, " +
                "mmp.snomed_id as snomed_id " +
                "FROM mandatory_medical_practice mmp " +
                "JOIN clinical_specialty_mandatory_medical_practice csmmp ON (mmp.mandatory_medical_practice_id = csmmp.mandatory_medical_practice_id) " +
                "JOIN mandatory_professional_practice_free_days mmpd ON (mmpd.clinical_specialty_mandatory_medical_practice_id = csmmp.id) " +
                "LEFT JOIN health_insurance_practice hip ON (hip.clinical_specialty_mandatory_medical_practice_id = csmmp.id) " +
                "WHERE mmpd.healthcare_professional_id = :healthcareProfessionalId " +
                "AND csmmp.clinical_specialty_id = :clinicalSpecialtyId "
                :
                "SELECT DISTINCT csmmp.mandatory_medical_practice_id AS practice_id, " +
                        "mmp.description AS practice_name, " +
                        "CASE WHEN hip.medical_coverage_id = :medicalCoverageId THEN 1 ELSE 0 END AS has_coverage, " +
                        "hip.coverage_information as coverage_info, " +
                        "mmp.snomed_id as snomed_id " +
                        "FROM mandatory_professional_practice_free_days mmpd " +
                        "JOIN clinical_specialty_mandatory_medical_practice csmmp ON (mmpd.clinical_specialty_mandatory_medical_practice_id = csmmp.id) " +
                        "JOIN mandatory_medical_practice mmp ON (mmp.mandatory_medical_practice_id = csmmp.mandatory_medical_practice_id) " +
                        "JOIN health_insurance_practice hip ON (hip.clinical_specialty_mandatory_medical_practice_id = csmmp.id) " +
                        "WHERE mmpd.healthcare_professional_id = :healthcareProfessionalId " +
                        "AND hip.medical_coverage_id = :medicalCoverageId " +
                        "AND csmmp.clinical_specialty_id = :clinicalSpecialtyId";

        List<Object[]> rows = entityManager.createNativeQuery(sqlString)
                .setParameter("healthcareProfessionalId", healthcareProfessionalId)
                .setParameter("medicalCoverageId", medicalCoverageId)
                .setParameter("clinicalSpecialtyId", clinicalSpecialtyId)
                .getResultList();
        List<PracticeBo> result = createPracticeBoList(rows);
        log.debug("Find practice by Specialty and HealthInsurance -> {}", result);
        return Optional.ofNullable(result);
    }

    @Override
    public List<BookingSpecialtyBo> findAllSpecialties() {
        log.debug("Find all specialties");
        var result = repository
                .findAllAssociatedWithAProfessional()
                .stream()
				.distinct()
                .filter(specialty -> specialty.getType().equals(SPECIALTY))
                .map(this::createPracticesList)
                .collect(Collectors.toList());
        log.debug("Find all specialties = {}", result.size());
        return result;
    }

    private BookingSpecialtyBo createPracticesList(VBookingClinicalSpecialty row) {
        return new BookingSpecialtyBo(row.getId(), row.getDescription());
    }

    @Override
    public Optional<List<BookingSpecialtyBo>> findAllSpecialtiesByProfessional(Integer healthcareProfessionalId) {
        log.debug("Find all specialties by professional");
        String sqlString =
                "SELECT bcs.id, bcs.name " +
                        "FROM v_booking_clinical_specialty bcs " +
                        "JOIN v_booking_healthcare_professional_specialty bhps ON (bcs.id = bhps.clinical_specialty_id) " +
                        "WHERE bhps.healthcare_professional_id = :healthcareProfessionalId " +
                        "ORDER BY bcs.name";

        List<Object[]> rows = entityManager.createNativeQuery(sqlString)
                .setParameter("healthcareProfessionalId", healthcareProfessionalId)
                .getResultList();
        var result = createPracticesList(rows);
        log.debug("Find all specialties by professional");
        return Optional.ofNullable(result);
    }

    private List<BookingSpecialtyBo> createPracticesList(List<Object[]> rows) {
        if (rows.isEmpty()) return null;
        return rows.stream().map(row ->
                new BookingSpecialtyBo((Integer) row[0], (String) row[1])
        ).distinct().collect(Collectors.toList());
    }

    private List<PracticeBo> createPracticeBoList(List<Object[]> rows) {
        if (rows.isEmpty()) return null;
        var practices = rows.stream().map(row ->
                new PracticeBo((Integer) row[0], (String) row[1], row[2].equals(1), (String) row[3], (Integer) row[4])
        ).distinct().collect(Collectors.toList());

		return practices.stream()
				.filter(mc -> isDuplicatedWithDifferentMedicalCoverage(practices, mc))
				.collect(Collectors.toList());
	}

	private boolean isDuplicatedWithDifferentMedicalCoverage(List<PracticeBo> rows, PracticeBo pb) {
		return !(pb.getCoverage().equals(Boolean.FALSE) && rows.stream()
				.anyMatch(el -> el.getCoverage().equals(Boolean.TRUE) && el.getId().equals(pb.getId())));
	}
}
