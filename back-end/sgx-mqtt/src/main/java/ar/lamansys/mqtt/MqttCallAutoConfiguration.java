package ar.lamansys.mqtt;

import org.eclipse.paho.client.mqttv3.IMqttClient;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@ComponentScan(basePackages = "ar.lamansys.mqtt")
@EnableJpaRepositories(basePackages = {"ar.lamansys.mqtt"})
@EntityScan(basePackages = {"ar.lamansys.mqtt"})
@PropertySource(value = "classpath:sgx-mqtt.properties", ignoreResourceNotFound = true)
public class MqttCallAutoConfiguration {

    @Value("${mqtt.publisher_id}")
    private String mqttPublisherId;

    @Value("${mqtt.server_address}")
    private String mqttServerAddress;

    @Value("${mqtt.client_connection}")
    private boolean mqttClientConnection;

    @Bean
    public IMqttClient connect() {
        MqttClient client = null;
        try {
            client = new MqttClient(mqttServerAddress, mqttPublisherId);
            MqttConnectOptions options = new MqttConnectOptions();
            options.setAutomaticReconnect(true);
            options.setCleanSession(true);
            options.setConnectionTimeout(10);
            if(mqttClientConnection) {
                client.connect(options);
            }
        } catch (MqttException e) {
            e.printStackTrace();
        }


        return client;
    }
}
