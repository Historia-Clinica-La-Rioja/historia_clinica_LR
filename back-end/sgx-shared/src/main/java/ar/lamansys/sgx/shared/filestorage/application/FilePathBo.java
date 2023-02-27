package ar.lamansys.sgx.shared.filestorage.application;

import java.io.File;
import java.net.URI;
import java.nio.file.Path;
import java.nio.file.Paths;

public class FilePathBo {
	public final Path fullPath;
	public final String relativePath;

	public FilePathBo(String baseDir, String relativePath) {
		this.relativePath = removeFirstSlash(
				relativePath.replaceAll("^"+baseDir, "")
		);
		this.fullPath = Paths.get(baseDir, this.relativePath).toAbsolutePath().normalize();
	}

	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder("FilePathBo{");
		sb.append("fullPath='").append(fullPath).append('\'');
		sb.append(", relativePath='").append(relativePath).append('\'');
		sb.append('}');
		return sb.toString();
	}

	private static String removeFirstSlash(String path) {
		return path.startsWith("/") ? path.substring(1) : path;
	}

	public File toFile() {
		return this.fullPath.toFile();
	}
	public URI toUri() {
		return this.fullPath.toUri();
	}
}
