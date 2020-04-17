package net.pladema.internation.repository.masterdata;

import net.pladema.internation.repository.masterdata.entity.ObservationStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ObservationStatusRepository extends JpaRepository<ObservationStatus, String> {

}
