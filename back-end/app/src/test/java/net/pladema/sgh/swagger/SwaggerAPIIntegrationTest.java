package net.pladema.sgh.swagger;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.File;
import java.io.FileWriter;
import java.io.Writer;

import org.apache.commons.io.IOUtils;
import org.json.JSONObject;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@TestPropertySource("classpath:integration-test.properties")
public class SwaggerAPIIntegrationTest {

	@Autowired
	private MockMvc mockMvc;

	@Test
	public void contextLoads() throws Exception {
		// @formatter:off
		String swaggerJson = mockMvc
				.perform(
						get("/v2/api-docs")
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
