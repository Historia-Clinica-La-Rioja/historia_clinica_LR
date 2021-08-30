package net.pladema.multimodule.service;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.springframework.boot.autoconfigure.SpringBootApplication;

public class MyServiceTest {

  private MyService myService;

  @BeforeEach
  void setUp() {
    ServiceProperties serviceProperties = new ServiceProperties();
    serviceProperties.setMessage("Hola?");
    myService = new MyService(serviceProperties);
  }

  @Test
  void contextLoads() {
    assertThat(myService.message()).isNotNull();
  }

  @SpringBootApplication
  static class TestConfiguration {
  }

}