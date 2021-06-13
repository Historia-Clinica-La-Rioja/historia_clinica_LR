package net.pladema.person.repository;

import net.pladema.patient.repository.entity.Occupation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface OccupationRepository extends JpaRepository<Occupation, Integer> {

    @Query(" SELECT o " +
            " FROM Occupation o " +
            " WHERE o.active = TRUE " +
            " ORDER BY o.description ")
    List<Occupation> findAllActive();
}
