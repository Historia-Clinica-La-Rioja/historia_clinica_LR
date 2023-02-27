package ar.lamansys.sgx.shared.files;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.function.Consumer;

import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;

import ar.lamansys.sgx.shared.filestorage.application.FileContentBo;

public class StreamsUtils {

	public static Resource writeToInputStream(Consumer<OutputStream> writer) {
		try (ByteArrayOutputStream os = new ByteArrayOutputStream()) {
			writer.accept(os);
			return new ByteArrayResource( os.toByteArray());
		} catch (IOException e){
			throw streamException(e);
		}
	}

	public static FileContentBo writeToContent(Consumer<OutputStream> writer) {
		return fromResource(
				writeToInputStream(writer)
		);
	}

	public static FileContentBo fromResource(Resource resource) {
		try {
			return FileContentBo.fromResource(resource);
		} catch (IOException e){
			throw streamException(e);
		}
	}

	public static RuntimeException streamException(Exception e) {
		return new RuntimeException(e);
	}
}
