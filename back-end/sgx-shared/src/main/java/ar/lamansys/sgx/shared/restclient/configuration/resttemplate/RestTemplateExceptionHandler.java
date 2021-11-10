package ar.lamansys.sgx.shared.restclient.configuration.resttemplate;

import ar.lamansys.sgx.shared.restclient.configuration.resttemplate.exception.RestTemplateApiException;
import lombok.SneakyThrows;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.util.ObjectUtils;
import org.springframework.web.client.DefaultResponseErrorHandler;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public class RestTemplateExceptionHandler extends DefaultResponseErrorHandler {

	private static final Logger LOG = LoggerFactory.getLogger(RestTemplateExceptionHandler.class);
	
	@SneakyThrows
	@Override
	public void handleError(ClientHttpResponse httpResponse) {
		LOG.error("Error en Cliente: {}", httpResponse);
		try {
			String body = getErrorMessage(httpResponse);
			LOG.error("Body error: {}", body);
			throw new RestTemplateApiException(httpResponse.getStatusCode(), body);
		} catch (RestTemplateApiException e){
			throw e;
		} catch (Exception e){
			LOG.error("SnowstormApi error", e);
			throw new RestTemplateApiException(HttpStatus.INTERNAL_SERVER_ERROR, "No se puede parsear el body");
		}
	}

	protected String getErrorMessage(ClientHttpResponse httpResponse) throws IOException {
		int rawStatusCode = httpResponse.getRawStatusCode();
		String statusText = httpResponse.getStatusText();
		byte[] responseBody = this.getResponseBody(httpResponse);
		Charset charset = this.getCharset(httpResponse);
		String preface = rawStatusCode + " " + statusText + ": ";
		if (ObjectUtils.isEmpty(responseBody)) {
			return preface + "[no body]";
		} else {
			charset = charset == null ? StandardCharsets.UTF_8 : charset;
			int maxChars = 200;
			if (responseBody.length < maxChars * 2) {
				return preface + "[" + new String(responseBody, charset) + "]";
			} else {
				try {
					Reader reader = new InputStreamReader(new ByteArrayInputStream(responseBody), charset);
					CharBuffer buffer = CharBuffer.allocate(maxChars);
					reader.read(buffer);
					reader.close();
					buffer.flip();
					return preface + "[" + buffer.toString() + "... (" + responseBody.length + " bytes)]";
				} catch (IOException var9) {
					throw new IllegalStateException(var9);
				}
			}
		}
	}
}