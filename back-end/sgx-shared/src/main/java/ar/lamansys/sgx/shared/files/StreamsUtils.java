package ar.lamansys.sgx.shared.files;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.function.Consumer;

import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;

public class StreamsUtils {

	public static Resource writeToInputStream(Consumer<OutputStream> writer) {
		try (ByteArrayOutputStream os = new ByteArrayOutputStream()) {
			writer.accept(os);
			return new InputStreamResource(
					new ByteArrayInputStream( os.toByteArray())
			);
		} catch (IOException e){
			throw streamException(e);
		}
	}

	public static RuntimeException streamException(Exception e) {
		return new RuntimeException(e);
	}
}
