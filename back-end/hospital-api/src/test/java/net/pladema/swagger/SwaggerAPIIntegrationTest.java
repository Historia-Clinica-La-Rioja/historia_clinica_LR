package net.pladema.swagger;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.File;
import java.io.FileWriter;
import java.io.Writer;

import org.apache.commons.io.IOUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class SwaggerAPIIntegrationTest {

	@Autowired
	private MockMvc mockMvc;

	@Test
	public void contextLoads() throws Exception {
		// @formatter:off
		String swaggerJson = mockMvc
	            .perform(get("/v2/api-docs")
	                .accept(MediaType.APPLICATION_JSON))
	            .andExpect(status().isOk())
	            .andReturn()
	            .getResponse().getContentAsString();

		try (Writer writer = new FileWriter(new File("target/generated-sources/swagger.json"))) {
            IOUtils.write(swaggerJson, writer);
        }
		// @formatter:on
	}
}
