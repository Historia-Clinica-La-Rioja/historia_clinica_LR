package net.pladema.establishment.repository;

import net.pladema.establishment.repository.entity.Institution;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface InstitutionRepository extends JpaRepository<Institution, Integer> {

    @Transactional(readOnly = true)
    @Query("SELECT i.id "+
            "FROM Institution AS i ")
    List<Integer> getAllIds();
}
