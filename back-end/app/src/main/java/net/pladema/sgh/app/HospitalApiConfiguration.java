package net.pladema.sgh.app;


import ar.lamansys.odontology.configuration.EnableOdontology;
import ar.lamansys.sgx.shared.EnableSharedLibrary;
import ar.lamansys.sgx.cubejs.infrastructure.configuration.EnableCubeJs;
import net.pladema.EnableHospitalLib;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;


@Configuration
@EnableCubeJs
@EnableOdontology
@EnableHospitalLib
@EnableSharedLibrary
@ComponentScan(basePackages = {"net.pladema.sgh.app"})
@EnableJpaRepositories(basePackages = {"net.pladema.sgh.app"})
@EntityScan(basePackages = {"net.pladema.sgh.app"})
public class HospitalApiConfiguration {
}