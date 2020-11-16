package net.pladema.establishment.repository;

import net.pladema.establishment.repository.entity.CareType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CareTypeRepository extends JpaRepository<CareType, Short> {
}
