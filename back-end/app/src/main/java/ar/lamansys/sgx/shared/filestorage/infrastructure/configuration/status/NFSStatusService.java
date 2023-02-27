package ar.lamansys.sgx.shared.filestorage.infrastructure.configuration.status;

import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

import org.apache.commons.io.FileUtils;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Service;

import ar.lamansys.sgx.shared.filestorage.infrastructure.output.repository.BlobStorage;
import ar.lamansys.sgx.shared.filestorage.infrastructure.output.repository.nfs.FileConfiguration;
import ar.lamansys.sgx.shared.filestorage.infrastructure.output.repository.nfs.NFSBlobStorage;
import net.pladema.hsi.extensions.configuration.features.FeatureProperty;
import net.pladema.hsi.extensions.configuration.features.FeatureStatusService;

@Service
@Order(5)
@ConditionalOnProperty(value="app.files.mode", havingValue="nfs",matchIfMissing=true)
public class NFSStatusService extends FeatureStatusService {

	public NFSStatusService(
			FileConfiguration configuration,
			NFSBlobStorage nfsBlobStorage
	) {
		super(
				"app.files.folder",
				listProperties(configuration),
				fetchStatusData(nfsBlobStorage)
		);
	}

	private static Supplier<Map<String, Object>> fetchStatusData(
			BlobStorage bucketStorage
	) {
		return () -> Map.of(
				"status", bucketStorage.status()
		);
	}
	private static Supplier<List<FeatureProperty>> listProperties(FileConfiguration configuration) {
		return () -> List.of(
				new FeatureProperty("documents.location", configuration.getDocumentsLocation().getAbsolutePath()),
				new FeatureProperty("multipart.location", configuration.getMultipartLocation().getAbsolutePath()),
				new FeatureProperty("freespace.minimum", FileUtils.byteCountToDisplaySize(configuration.getMinimumFreeSpace()))
		);
	}

}
