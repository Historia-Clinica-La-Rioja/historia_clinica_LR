package net.pladema.person.repository;

import net.pladema.patient.repository.entity.EducationLevel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EducationLevelRepository extends JpaRepository<EducationLevel, Integer> {

    @Query(" SELECT e " +
            " FROM EducationLevel e " +
            " WHERE e.active = TRUE " +
            " ORDER BY e.description ")
    List<EducationLevel> findAllActive();
}
