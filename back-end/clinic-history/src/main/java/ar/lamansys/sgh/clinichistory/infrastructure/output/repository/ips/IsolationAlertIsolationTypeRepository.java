package ar.lamansys.sgh.clinichistory.infrastructure.output.repository.ips;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.ips.entity.isolation.IsolationAlertIsolationType;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.ips.entity.isolation.IsolationAlertIsolationTypePK;

@Repository
public interface IsolationAlertIsolationTypeRepository extends
	JpaRepository<IsolationAlertIsolationType, IsolationAlertIsolationTypePK> {

}
