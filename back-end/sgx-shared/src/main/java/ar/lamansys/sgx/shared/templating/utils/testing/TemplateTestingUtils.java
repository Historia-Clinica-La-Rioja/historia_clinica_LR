package ar.lamansys.sgx.shared.templating.utils.testing;

import static java.nio.charset.StandardCharsets.UTF_8;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UncheckedIOException;
import java.util.function.BiConsumer;

import org.apache.commons.lang3.StringUtils;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.util.FileCopyUtils;

import ar.lamansys.sgx.shared.notifications.domain.RecipientBo;
import ar.lamansys.sgx.shared.templating.impl.NotificationTemplateEngine;

public class TemplateTestingUtils {

	public static RecipientBo GENERIC_RECIPIENT = new RecipientBo("Neil", "deGrasse Tyson", "neil@lamansys.com", "+5492494321098");

	public static BiConsumer<String, String> createExpectedResultAsserter(String channel, String template) {
		return (String scenario, String result) -> {
			var cleanResult = StringUtils.deleteWhitespace(result);
			var cleanExpectedResult = StringUtils.deleteWhitespace(classpathTemplateResultContent(channel, template, scenario));
			assert cleanResult.equals(cleanExpectedResult) : buildDiff(cleanResult, cleanExpectedResult, result);
		};
	}

	private static String buildDiff(String cleanResult, String cleanExpectedResult, String result) {
		StringBuffer diff = new StringBuffer("\nExpected: {");
		diff.append(cleanExpectedResult);
		diff.append("}\nActual:   {");
		diff.append(cleanResult);
		diff.append("}\n");
		diff.append("========= RAW Result ========= \n");
		diff.append(result);
		diff.append("\n========= RAW Result ========= ");
		return diff.toString();
	}

	public static String classpathTemplateResultContent(String channel, String template, String scenario) {
		return classpathFileContent(String.format("templates/notifications/%s/%s-%s.html", channel, template, scenario));
	}
	public static String classpathFileContent(String path) {
		Resource resource = new ClassPathResource(path);
		return asString(resource);
	}

	private static String asString(Resource resource) {
		try (Reader reader = new InputStreamReader(resource.getInputStream(), UTF_8)) {
			return FileCopyUtils.copyToString(reader);
		} catch (IOException e) {
			throw new UncheckedIOException(e);
		}
	}
}
