package ar.lamansys.mqtt;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import ar.lamansys.mqtt.application.ports.MqttClientService;
import ar.lamansys.mqtt.infraestructure.output.mqtt.MqttClientServiceImpl;
import ar.lamansys.mqtt.infraestructure.output.mqtt.MqttClientServiceVoid;

@Configuration
@ComponentScan(basePackages = "ar.lamansys.mqtt")
@EnableJpaRepositories(basePackages = {"ar.lamansys.mqtt"})
@EntityScan(basePackages = {"ar.lamansys.mqtt"})
@PropertySource(value = "classpath:sgx-mqtt.properties", ignoreResourceNotFound = true)
public class MqttCallAutoConfiguration {

	@Bean
	public MqttClientService connect(
			@Value("${mqtt.server_address}") String mqttServerAddress,
			@Value("${mqtt.client_connection}") boolean mqttClientConnection,
			@Value("${mqtt.client_username}") String mqttClientUsername,
			@Value("${mqtt.client_password}") String mqttClientPassword
	) {
		String mqttPublisherId = "HSI-"+UUID.randomUUID();
		return mqttClientConnection ? new MqttClientServiceImpl(mqttServerAddress, mqttPublisherId, mqttClientUsername, mqttClientPassword)
				: new MqttClientServiceVoid();
	}
}
