package net.pladema.violencereport.infrastructure.output.repository;

import net.pladema.violencereport.infrastructure.output.repository.embedded.VictimKeeperReportPlacePK;
import net.pladema.violencereport.infrastructure.output.repository.entity.VictimKeeperReportPlace;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VictimKeeperReportPlaceRepository extends JpaRepository<VictimKeeperReportPlace, VictimKeeperReportPlacePK> {
}
