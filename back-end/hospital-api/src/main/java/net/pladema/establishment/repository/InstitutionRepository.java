package net.pladema.establishment.repository;

import net.pladema.establishment.repository.entity.Institution;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface InstitutionRepository extends JpaRepository<Institution, Integer> {

    @Transactional(readOnly = true)
    @Query("SELECT i.id "+
            "FROM Institution AS i ")
    List<Integer> getAllIds();

    @Query("SELECT i " +
            "FROM Institution i " +
            "WHERE i.name LIKE :name " +
            "AND i.cuit LIKE :cuit")
    Optional<Institution> findInstitution(@Param("name") String name, @Param("cuit")  String cuit);
}
