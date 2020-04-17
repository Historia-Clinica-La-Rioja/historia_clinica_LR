package net.pladema.internation.repository.masterdata;

import net.pladema.internation.repository.masterdata.entity.InmunizationStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InmunizationStatusRepository extends JpaRepository<InmunizationStatus, String> {

}
