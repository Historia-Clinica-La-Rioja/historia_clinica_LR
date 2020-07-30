package net.pladema.address.repository;

import net.pladema.address.repository.domain.AddressVo;
import net.pladema.address.repository.entity.Address;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface AddressRepository extends JpaRepository<Address, Integer> {

    @Transactional(readOnly = true)
    @Query("SELECT new net.pladema.address.repository.domain.AddressVo(a.id,a.street,a.number,a.floor,a.apartment,a.postcode,c.id,c.description) " +
            "FROM Address a LEFT JOIN City c ON (a.cityId = c.id) " +
            "WHERE a.id IN :addressIds")
    List<AddressVo> findByIds(@Param("addressIds") List<Integer> addressIds);
}
