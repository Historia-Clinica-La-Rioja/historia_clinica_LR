package net.pladema.staff.repository;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import net.pladema.staff.service.domain.HealthcareProfessionalSpecialtyBo;

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
				"JOIN {h-schema}healthcare_professional hp ON (hp.id = hps.healthcare_professional_id)" +
                " WHERE hps.healthcare_professional_id = :healthcareProfessionalId " +
                " AND hps.clinical_specialty_id = :clinicalSpecialtyId " +
                " AND hp.professional_specialty_id = :professionalSpecialtyId ";
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
