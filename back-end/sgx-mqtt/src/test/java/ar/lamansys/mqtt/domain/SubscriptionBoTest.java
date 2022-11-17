package ar.lamansys.mqtt.domain;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class SubscriptionBoTest {

	private SubscriptionBo subscriptionBo;

	@BeforeEach
	void setUp() {
	}

	void apply() {
		subscriptionBo =  new SubscriptionBo(null, "#", Collections.emptyList());
		Assertions.assertTrue(subscriptionBo.apply("HSI/HOSPITAL_API/PACIENTE_LLAMADO/CONSULTORIO_PRUEBA"));
		Assertions.assertTrue(subscriptionBo.apply("HSI/HOSPITAL_API/PACIENTE_LLAMADO/CONSULTORIO_PRUEBA/CONSULTORIO"));
		Assertions.assertTrue(subscriptionBo.apply("HSI/HOSPITAL_API/NUEVA_CONSULTA/CONSULTORIO_PRUEBA"));

		subscriptionBo =  new SubscriptionBo(null, "HSI/HOSPITAL_API/PACIENTE_LLAMADO/#", Collections.emptyList());
		Assertions.assertTrue(subscriptionBo.apply("HSI/HOSPITAL_API/PACIENTE_LLAMADO/CONSULTORIO_PRUEBA"));
		Assertions.assertTrue(subscriptionBo.apply("HSI/HOSPITAL_API/PACIENTE_LLAMADO/CONSULTORIO_PRUEBA/CONSULTORIO"));
		Assertions.assertFalse(subscriptionBo.apply("HSI/HOSPITAL_API/NUEVA_CONSULTA/CONSULTORIO_PRUEBA"));

		subscriptionBo =  new SubscriptionBo(null, "HSI/HOSPITAL_API/+/PACIENTE_LLAMADO/#", Collections.emptyList());
		Assertions.assertFalse(subscriptionBo.apply("HSI/HOSPITAL_API/PACIENTE_LLAMADO/CONSULTORIO_PRUEBA"));
		Assertions.assertTrue(subscriptionBo.apply("HSI/HOSPITAL_API/NUEVA_CONSULTA/PACIENTE_LLAMADO/CONSULTORIO_PRUEBA/CONSULTORIO"));
		Assertions.assertFalse(subscriptionBo.apply("HSI/HOSPITAL_API/NUEVA_CONSULTA/CONSULTORIO_PRUEBA"));
		Assertions.assertTrue(subscriptionBo.apply("HSI/HOSPITAL_API/NUEVA_CONSULTA/PACIENTE_LLAMADO/CONSULTORIO_PRUEBA"));

		subscriptionBo =  new SubscriptionBo(null, "HSI/HOSPITAL_API/+/PACIENTE_LLAMADO/+", Collections.emptyList());
		Assertions.assertFalse(subscriptionBo.apply("HSI/HOSPITAL_API/PACIENTE_LLAMADO/CONSULTORIO_PRUEBA"));
		Assertions.assertTrue(subscriptionBo.apply("HSI/HOSPITAL_API/NUEVA_CONSULTA/PACIENTE_LLAMADO/CONSULTORIO_PRUEBA"));
		Assertions.assertFalse(subscriptionBo.apply("HSI/HOSPITAL_API/NUEVA_CONSULTA/PACIENTE_LLAMADO/CONSULTORIO_PRUEBA/CONSULTORIO"));
	}
}