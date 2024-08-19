package net.pladema.address.repository;

import net.pladema.address.controller.service.domain.AddressBo;
import net.pladema.address.repository.domain.AddressVo;
import net.pladema.address.repository.entity.Address;
import net.pladema.establishment.sanitaryresponsibilityarea.getinstitutionaddress.domain.GetSanitaryResponsibilityAreaInstitutionAddressBo;
import net.pladema.nominatim.fetchglobalcoordinatesbyaddress.domain.GlobalCoordinatesBo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface AddressRepository extends JpaRepository<Address, Integer> {

    @Transactional(readOnly = true)
    @Query("SELECT new net.pladema.address.repository.domain.AddressVo(a.id,a.street,a.number,a.floor,a.apartment,a.postcode,c.id,c.description, a.countryId, a.provinceId, a.departmentId, p.description) " +
            "FROM Address a " +
			"LEFT JOIN City c ON (a.cityId = c.id) " +
			"LEFT JOIN Province p ON (p.id = a.provinceId) " +
            "WHERE a.id IN :addressIds")
    List<AddressVo> findByIds(@Param("addressIds") List<Integer> addressIds);

    @Query("SELECT a " +
            "FROM Address a " +
            "WHERE a.street LIKE :street " +
            "AND a.number LIKE :number " +
            "AND a.cityId = :cityId " +
            "AND a.postcode LIKE :postcode")
    Optional<Address> findAddress(@Param("street") String street, @Param("number") String number,
                                  @Param("cityId") Integer cityId, @Param("postcode") String postcode);

	@Transactional(readOnly = true)
	@Query("SELECT new net.pladema.address.controller.service.domain.AddressBo(a.id, a.street, a.number, a.floor, " +
			"a.apartment, a.postcode, c.id, c.description, a.countryId, a.provinceId, c.departmentId," +
			"d.description, co.description, c.bahraCode) " +
			"FROM Institution i " +
			"JOIN Address a ON (i.addressId = a.id)" +
			"JOIN City c ON (a.cityId = c.id) " +
			"JOIN Department d ON (c.departmentId = d.id) " +
			"JOIN Province p ON (d.provinceId = p.id) " +
			"JOIN Country co ON (p.countryId = co.id) " +
			"WHERE i.id = :institutionId")
    Optional<AddressBo> findByInstitutionId(@Param("institutionId") Integer institutionId);

	@Transactional(readOnly = true)
	@Query("SELECT new ar.lamansys.sgh.shared.domain.general.AddressBo(a.street, a.number, a.floor, a.apartment, a.postcode, c.description, p.description) " +
			"FROM Institution i " +
			"JOIN Address a ON (i.addressId = a.id) " +
			"LEFT JOIN Province p ON (a.provinceId = p.id) " +
			"JOIN City c ON (a.cityId = c.id) " +
			"WHERE i.id = :institutionId")
	Optional<ar.lamansys.sgh.shared.domain.general.AddressBo> findAddressDataByInstitutionId(@Param("institutionId") Integer institutionId);

	@Transactional(readOnly = true)
	@Query("SELECT NEW net.pladema.nominatim.fetchglobalcoordinatesbyaddress.domain.GlobalCoordinatesBo(a.latitude, a.longitude) " +
			"FROM Institution i " +
			"JOIN Address a ON (a.id = i.addressId) " +
			"WHERE i.id = :institutionId")
	GlobalCoordinatesBo fetchInstitutionGlobalCoordinates(@Param("institutionId") Integer institutionId);

	@Transactional(readOnly = true)
	@Query("SELECT NEW net.pladema.establishment.sanitaryresponsibilityarea.getinstitutionaddress.domain.GetSanitaryResponsibilityAreaInstitutionAddressBo(p.id, p.description, " +
			"d.id, d.description, c.id, c.description, a.street, a.number) " +
			"FROM Address a " +
			"JOIN Institution i ON (i.addressId = a.id) " +
			"LEFT JOIN City c ON (c.id = a.cityId) " +
			"LEFT JOIN Department d ON (d.id = c.departmentId) " +
			"LEFT JOIN Province p ON (p.id = d.provinceId) " +
			"WHERE i.id = :institutionId")
	GetSanitaryResponsibilityAreaInstitutionAddressBo fetchGetSanitaryResponsibilityAreaInstitutionAddressPort(@Param("institutionId") Integer institutionId);

	@Transactional
	@Modifying
	@Query("UPDATE Address a " +
			"SET a.provinceId = :stateId, a.departmentId = :departmentId, a.cityId = :cityId, a.street = :streetName, a.number = :houseNumber " +
			"WHERE a.id = (SELECT i.addressId " +
			"FROM Institution i " +
			"WHERE i.id = :institutionId)")
	void saveInstitutionAddress(@Param("institutionId") Integer institutionId, @Param("stateId") Short stateId, @Param("departmentId") Short departmentId,
								@Param("cityId") Integer cityId, @Param("streetName") String streetName, @Param("houseNumber") String houseNumber);

	@Transactional
	@Modifying
	@Query("UPDATE Address a " +
			"SET a.latitude = :latitude, a.longitude = :longitude " +
			"WHERE a.id = (SELECT i.addressId " +
			"FROM Institution i " +
			"WHERE i.id = :institutionId)")
	void saveInstitutionGlobalCoordinates(@Param("institutionId") Integer institutionId, @Param("latitude") Double latitude, @Param("longitude") Double longitude);
	
	@Transactional(readOnly = true)
	@Query("SELECT new net.pladema.address.repository.domain.AddressVo(a.id,a.street,a.number,a.floor,a.apartment,a.postcode,c.id,c.description, a.countryId, a.provinceId, a.departmentId, pr.description) " +
			"FROM Patient p " +
			"JOIN Person pe ON (pe.id = p.personId) " +
			"JOIN PersonExtended pex ON (pex.id = pe.id) " +
			"LEFT JOIN Address a ON (a.id = pex.addressId) " +
			"LEFT JOIN City c ON (a.cityId = c.id) " +
			"LEFT JOIN Province pr ON (pr.id = a.provinceId) " +
			"WHERE p.id = :patientId")
	AddressVo findByPatientId(@Param("patientId") Integer patientId);

}
