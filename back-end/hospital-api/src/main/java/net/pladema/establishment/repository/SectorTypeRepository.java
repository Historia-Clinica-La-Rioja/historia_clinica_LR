package net.pladema.establishment.repository;

import net.pladema.establishment.repository.entity.SectorType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SectorTypeRepository extends JpaRepository<SectorType, Short> {
}
