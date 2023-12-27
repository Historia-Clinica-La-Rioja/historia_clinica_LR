package net.pladema.violencereport.infrastructure.output.repository;

import net.pladema.violencereport.infrastructure.output.repository.embedded.MunicipalStateDevicePK;
import net.pladema.violencereport.infrastructure.output.repository.entity.MunicipalStateDevice;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MunicipalStateDeviceRepository extends JpaRepository<MunicipalStateDevice, MunicipalStateDevicePK> {
}
