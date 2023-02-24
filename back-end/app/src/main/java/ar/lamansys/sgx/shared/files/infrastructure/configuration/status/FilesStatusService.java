package ar.lamansys.sgx.shared.files.infrastructure.configuration.status;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

import org.apache.commons.io.FileUtils;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Service;

import ar.lamansys.sgx.shared.files.FileConfiguration;
import ar.lamansys.sgx.shared.filestorage.infrastructure.output.repository.BlobStorage;
import ar.lamansys.sgx.shared.filestorage.infrastructure.output.repository.nfs.NFSUtils;
import net.pladema.hsi.extensions.configuration.features.FeatureProperty;
import net.pladema.hsi.extensions.configuration.features.FeatureStatusService;

@Service
@Order(5)
public class FilesStatusService extends FeatureStatusService {

	public FilesStatusService(
			FileConfiguration configuration,
			BlobStorage blobStorage
	) {
		super(
				"app.files.folder",
				listProperties(configuration),
				fetchStatusData(blobStorage)
		);
	}

	private static Supplier<Map<String, Object>> fetchStatusData(BlobStorage blobStorage) {
		Map<String, Object> map = new LinkedHashMap<>();
		map.put("status", blobStorage.status());
		return () -> map;
	}
	private static Supplier<List<FeatureProperty>> listProperties(FileConfiguration configuration) {
		return () -> List.of(
				new FeatureProperty("documents.location", configuration.getDocumentsLocation().getAbsolutePath()),
				new FeatureProperty("multipart.location", configuration.getMultipartLocation().getAbsolutePath()),
				new FeatureProperty("freespace.minimum", FileUtils.byteCountToDisplaySize(configuration.getMinimumFreeSpace()))
		);
	}

}
