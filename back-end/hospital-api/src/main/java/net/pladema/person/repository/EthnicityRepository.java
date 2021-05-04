package net.pladema.person.repository;

import net.pladema.person.repository.entity.Ethnicity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EthnicityRepository extends JpaRepository<Ethnicity, Integer> {

    @Query(" SELECT e " +
            " FROM Ethnicity e " +
            " WHERE e.active = TRUE " +
            " ORDER BY e.pt ")
    List<Ethnicity> findAllActive();

}
