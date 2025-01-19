package net.pladema.establishment.sanitaryresponsibilityarea.institutionresponsibilityarea.infrastructure.output.repository;

import net.pladema.establishment.sanitaryresponsibilityarea.institutionresponsibilityarea.infrastructure.output.repository.entity.InstitutionResponsibilityArea;

import net.pladema.establishment.sanitaryresponsibilityarea.institutionresponsibilityarea.infrastructure.output.repository.entity.InstitutionResponsibilityAreaPK;

import net.pladema.nominatim.fetchglobalcoordinatesbyaddress.domain.GlobalCoordinatesBo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface InstitutionResponsibilityAreaRepository extends JpaRepository<InstitutionResponsibilityArea, InstitutionResponsibilityAreaPK> {

	@Transactional(readOnly = true)
	@Query("SELECT NEW net.pladema.nominatim.fetchglobalcoordinatesbyaddress.domain.GlobalCoordinatesBo(ira.latitude, ira.longitude) " +
			"FROM InstitutionResponsibilityArea ira " +
			"WHERE ira.pk.institutionId = :institutionId " +
			"ORDER BY ira.pk.orderId")
	List<GlobalCoordinatesBo> fetchInstitutionResponsibilityArea(@Param("institutionId") Integer institutionId);

	@Modifying
	@Transactional
	@Query("DELETE FROM InstitutionResponsibilityArea ira WHERE ira.pk.institutionId = :institutionId ")
    void deleteAllFromInstitution(@Param("institutionId") Integer institutionId);
	
	@Transactional
	@Modifying
	@Query("DELETE FROM InstitutionResponsibilityArea ira WHERE ira.pk.institutionId IN (SELECT i.id FROM Institution i WHERE i.addressId = :addressId) ")
    void deleteByAddressId(@Param("addressId") Integer addressId);

}
