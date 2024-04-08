package ar.lamansys.sgx.shared.files.application;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.File;
import java.io.IOException;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import ar.lamansys.sgx.shared.filestorage.application.FilePathBo;

@Disabled
class FilePathBoTest {

	private void assertPath(
			String baseDir,
			String relativePath,
			String targetRelative,
			String targetFull
	) {
		var path = new FilePathBo(baseDir, relativePath);
		assertThat(path)
				.hasFieldOrPropertyWithValue("relativePath", targetRelative);

		assertThat(path.fullPath.toString()).isEqualTo(targetFull);
	}

	@Test
	void relativePath() throws IOException {
		assertPath(
				"/mnt/",
				"/institution/3/kjk0-df.pdf",
				"institution/3/kjk0-df.pdf",
				"/mnt/institution/3/kjk0-df.pdf"
		);

		assertPath(
				"/mnt/",
				"institution/3/kjk0-df.pdf",
				"institution/3/kjk0-df.pdf",
				"/mnt/institution/3/kjk0-df.pdf"
		);

		assertPath(
				"/mnt/",
				"/mnt/institution/3/kjk0-df.pdf",
				"institution/3/kjk0-df.pdf",
				"/mnt/institution/3/kjk0-df.pdf"
		);

		assertPath(
				"/mnt",
				"/mnt/institution/3/kjk0-df.pdf",
				"institution/3/kjk0-df.pdf",
				"/mnt/institution/3/kjk0-df.pdf"
		);

		File f = new File("../../docker-data/");
		String root = f.getCanonicalPath();

		assertPath(
				"../../docker-data/",
				"/institution/3/kjk0-df.pdf",
				"institution/3/kjk0-df.pdf",
				root + "/institution/3/kjk0-df.pdf"
		);
	}

}