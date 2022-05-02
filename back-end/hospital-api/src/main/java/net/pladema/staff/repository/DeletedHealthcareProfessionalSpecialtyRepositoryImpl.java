package net.pladema.staff.repository;

import lombok.RequiredArgsConstructor;
import net.pladema.staff.service.domain.HealthcareProfessionalSpecialtyBo;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class DeletedHealthcareProfessionalSpecialtyRepositoryImpl {

    private final EntityManager entityManager;

    @Transactional(readOnly = true)
    public HealthcareProfessionalSpecialtyBo getHealthcareProfessionalSpecialty(
            Integer healthcareProfessionalId,
            Integer clinicalSpecialtyId,
            Integer professionalSpecialtyId) {
        String sqlString = "SELECT hps.id, hps.healthcare_professional_id," +
                "hps.professional_specialty_id, hps.clinical_specialty_id, hps.deleted " +
				"FROM {h-schema}healthcare_professional_specialty  hps " +
                " WHERE hps.healthcare_professional_id = :healthcareProfessionalId " +
                " AND hps.clinical_specialty_id = :clinicalSpecialtyId " +
                " AND hps.professional_specialty_id = :professionalSpecialtyId ";
        Query query = entityManager.createNativeQuery(sqlString);
        query.setParameter("healthcareProfessionalId", healthcareProfessionalId)
                .setParameter("clinicalSpecialtyId", clinicalSpecialtyId)
                .setParameter("professionalSpecialtyId", professionalSpecialtyId);
        List<Object[]> rows = query.getResultList();
        if (rows.isEmpty())
            return null;
        Object[] row = rows.get(0);
        return new HealthcareProfessionalSpecialtyBo(
                (Integer) row[0],
                (Integer) row[1],
                (Integer) row[2],
                (Integer) row[3],
                (Boolean) row[4]
        );
    }
}
