package net.pladema.establishment.repository;

import net.pladema.establishment.repository.entity.SectorOrganization;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SectorOrganizationRepository extends JpaRepository<SectorOrganization, Short> {
}
