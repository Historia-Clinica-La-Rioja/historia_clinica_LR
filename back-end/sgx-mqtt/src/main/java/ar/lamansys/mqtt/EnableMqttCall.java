package ar.lamansys.mqtt;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
@Documented
@Import(MqttCallAutoConfiguration.class)
@Configuration
public @interface EnableMqttCall {
}
