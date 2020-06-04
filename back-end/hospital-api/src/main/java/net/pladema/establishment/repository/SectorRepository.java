package net.pladema.establishment.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import net.pladema.establishment.repository.entity.Sector;

@Repository
public interface SectorRepository extends JpaRepository<Sector, Integer> {

    @Transactional(readOnly = true)
    @Query("SELECT s "+
            "FROM Sector AS s " +
            "WHERE s.institutionId = :institutionId ")
    List<Sector> getSectorsByInstitution(@Param("institutionId") Integer institutionId);
    
}
