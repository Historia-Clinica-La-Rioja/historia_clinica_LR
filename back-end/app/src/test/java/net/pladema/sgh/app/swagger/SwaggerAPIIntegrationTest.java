package net.pladema.sgh.app.swagger;

import net.pladema.sgh.app.IntegrationTest;
import org.apache.commons.io.IOUtils;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.io.File;
import java.io.FileWriter;
import java.io.Writer;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@TestPropertySource("classpath:integration-test.properties")
class SwaggerAPIIntegrationTest extends IntegrationTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	protected WebApplicationContext webApplicationContext;

	@BeforeEach
	void setup() {
		mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
	}

	@Test
	void contextLoads() throws Exception {
		// @formatter:off
		String swaggerJson = mockMvc
				.perform(
						get("/v3/api-docs")
						.accept(MediaType.APPLICATION_JSON)
				)
				.andExpect(status().isOk())
				.andReturn()
				.getResponse().getContentAsString();

		try (Writer writer = new FileWriter(new File("target/generated-sources/swagger.json"))) {
			JSONObject json = new JSONObject(swaggerJson); // Convert text to object
			IOUtils.write(json.toString(4), writer);
		}
		// @formatter:on
	}
}
