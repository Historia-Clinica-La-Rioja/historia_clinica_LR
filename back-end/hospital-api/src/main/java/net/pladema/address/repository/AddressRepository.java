package net.pladema.address.repository;

import net.pladema.address.controller.service.domain.AddressBo;
import net.pladema.address.repository.domain.AddressVo;
import net.pladema.address.repository.entity.Address;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface AddressRepository extends JpaRepository<Address, Integer> {

    @Transactional(readOnly = true)
    @Query("SELECT new net.pladema.address.repository.domain.AddressVo(a.id,a.street,a.number,a.floor,a.apartment,a.postcode,c.id,c.description, a.countryId, a.provinceId, a.departmentId) " +
            "FROM Address a LEFT JOIN City c ON (a.cityId = c.id) " +
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
}
