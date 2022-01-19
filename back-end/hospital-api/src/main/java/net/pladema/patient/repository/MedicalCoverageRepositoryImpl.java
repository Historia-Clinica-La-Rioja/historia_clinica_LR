package net.pladema.patient.repository;

import net.pladema.patient.repository.entity.MedicalCoverage;
import net.pladema.patient.repository.entity.PrivateHealthInsurance;
import net.pladema.person.repository.entity.HealthInsurance;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.sql.Timestamp;
import java.util.Optional;

@Repository
public class MedicalCoverageRepositoryImpl {

    @PersistenceContext
    private EntityManager entityManager;

    private PrivateHealthInsuranceRepository privateHealthInsuranceRepository;

    public MedicalCoverageRepositoryImpl(EntityManager entityManager, PrivateHealthInsuranceRepository privateHealthInsuranceRepository) {
        super();
        this.entityManager = entityManager;
        this.privateHealthInsuranceRepository = privateHealthInsuranceRepository;
    }


    public Optional<? extends MedicalCoverage> findById(Integer id) {
        String sqlString = "SELECT * FROM medical_coverage mc WHERE mc.id = :id ";

        Object[] queryResult = (Object[]) entityManager.createNativeQuery(sqlString)
                .setParameter("id", id)
                .getSingleResult();
        Timestamp deletedOn = (queryResult[8] != null) ? (Timestamp) queryResult[8] : null;
        Optional<PrivateHealthInsurance> entity = privateHealthInsuranceRepository.findById((Integer) queryResult[0]);
        if (entity.isPresent())
            return entity;
        return Optional.of(
                new HealthInsurance(
                        (Integer) queryResult[0],
                        (String) queryResult[1],
                        (Integer) queryResult[2],
                        (Timestamp) queryResult[3],
                        (Integer) queryResult[4],
                        (Timestamp) queryResult[5],
                        (Boolean) queryResult[6],
                        (Integer) queryResult[7],
                        deletedOn,
                        (String) queryResult[9]));
    }

}
