package net.pladema.sgh.app;


import ar.lamansys.immunization.EnableImmunization;
import ar.lamansys.mqtt.EnableMqttCall;
import ar.lamansys.refcounterref.EnableReferenceCounterReference;
import ar.lamansys.sgh.publicapi.EnableHospitalPublicApi;
import ar.lamansys.nursing.EnableNursing;
import ar.lamansys.sgx.auth.EnableAuth;
import ar.lamansys.sgx.shared.EnableSharedLibrary;
import ar.lamansys.odontology.EnableOdontology;
import ar.lamansys.sgx.cubejs.infrastructure.configuration.EnableCubeJs;
import net.pladema.EnableHospitalLib;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;


@Configuration
@EnableCubeJs
@EnableOdontology
@EnableImmunization
@EnableNursing
@EnableMqttCall
@EnableAuth
@EnableHospitalLib
@EnableSharedLibrary
@EnableHospitalPublicApi
@EnableReferenceCounterReference
@ServletComponentScan(basePackages = "net.pladema")
@ComponentScan(basePackages = {"net.pladema.sgh.app"})
@EnableJpaRepositories(basePackages = {"net.pladema.sgh.app"})
@EntityScan(basePackages = {"net.pladema.sgh.app"})
public class HospitalApiConfiguration {
}