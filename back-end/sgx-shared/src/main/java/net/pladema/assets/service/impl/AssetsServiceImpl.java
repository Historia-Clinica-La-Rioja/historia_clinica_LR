package net.pladema.assets.service.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import ar.lamansys.sgx.shared.exceptions.NotFoundException;

import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import ar.lamansys.sgx.shared.files.FileService;
import ar.lamansys.sgx.shared.files.exception.FileServiceEnumException;
import ar.lamansys.sgx.shared.files.exception.FileServiceException;
import ar.lamansys.sgx.shared.filestorage.application.FileContentBo;
import ar.lamansys.sgx.shared.filestorage.infrastructure.input.rest.StoredFileBo;
import ar.lamansys.sgx.shared.filestorage.infrastructure.output.repository.BlobStorage;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.pladema.assets.service.AssetsService;
import net.pladema.assets.service.domain.Assets;

@Slf4j
@AllArgsConstructor
@Service
public class AssetsServiceImpl implements AssetsService {

    private static final String ORIGINAL_PATH = "/assets/webapp/";
    private static final String CUSTOM_PATH = "/assets/custom/";
    private final String INPUT_LOG = "Input parameters -> fileName {}";
    private static final Assets SPONSOR_LOGO = new Assets("image/png", "sponsor-logo-512x128.png");
    private static final Assets FAVICON = new Assets("image/x-icon", "favicon.ico");
    private static final Assets ICON_72 = new Assets("image/png", "icons/icon-72x72.png");
    private static final Assets ICON_96 = new Assets("image/png", "icons/icon-96x96.png");
    private static final Assets ICON_128 = new Assets("image/png", "icons/icon-128x128.png");
    private static final Assets ICON_144 = new Assets("image/png", "icons/icon-144x144.png");
    private static final Assets ICON_152 = new Assets("image/png", "icons/icon-152x152.png");
    private static final Assets ICON_192 = new Assets("image/png", "icons/icon-192x192.png");
    private static final Assets ICON_384 = new Assets("image/png", "icons/icon-384x384.png");
    private static final Assets ICON_512 = new Assets("image/png", "icons/icon-512x512.png");

    private static final Assets FOOTER_LEFT = new Assets("image/png", "footer/footer_left.png");
    private static final Assets FOOTER_CENTER = new Assets("image/png", "footer/footer_center.png");
    private static final Assets FOOTER_RIGHT = new Assets("image/png", "footer/footer_right.png");

    private static final Assets APP_LOGO = new Assets("image/svg+xml", "app_logo.svg");

	private static final Assets DIGITAL_PRESCRIPTION_HEADER = new Assets("image/png", "pdf/digital_recipe_header_logo.png");

	private static final Assets DIGITAL_PRESCRIPTION_LOGO = new Assets("image/png", "pdf/digital_recipe_logo.png");

	private static final Assets HSI_HEADER_250x72 =  new Assets("image/png", "pdf/hsi-header-250x72.png");
	private static final Assets HSI_FOOTER_118x21 =  new Assets("image/png", "pdf/hsi-footer-118x21.png");

	private final FileService fileService;
    private final BlobStorage blobStorage;

    private final List<Assets> assetsList = new ArrayList<>(Arrays.asList(
            SPONSOR_LOGO, FAVICON,
            ICON_72, ICON_96, ICON_128, ICON_144, ICON_152, ICON_192, ICON_384, ICON_512,
            FOOTER_LEFT, FOOTER_CENTER, FOOTER_RIGHT, APP_LOGO, DIGITAL_PRESCRIPTION_HEADER,
			DIGITAL_PRESCRIPTION_LOGO, HSI_HEADER_250x72, HSI_FOOTER_118x21));

    @Override
    public Optional<Assets> findByName(String name) {
        log.debug(INPUT_LOG, name);
        return this.assetsList.stream().filter(a -> a.getNameFile().equals(name)).findAny();
    }

    @Override
    public StoredFileBo getFile(String fileName) {
        log.debug(INPUT_LOG, fileName);

        Assets newAsset = this.findByName(fileName).orElseThrow(() -> new NotFoundException("asset-not-exists", String.format("El archivo %s no existe", fileName)));
        String partialPath = CUSTOM_PATH.concat(newAsset.getNameFile());
        var path = fileService.buildCompletePath(partialPath);
        
        if (this.blobStorage.existFile(path)) {
			log.debug("Using custom {}", fileName);
            return new StoredFileBo(
                    this.fileService.loadFile(path),
                    newAsset.getContentType(),
					newAsset.getNameFile()
			);
        }

        String newPartialPath = ORIGINAL_PATH.concat(newAsset.getNameFile());
		log.debug("Using original {}", fileName);

		return new StoredFileBo(
				fromClassPath(newPartialPath),
				newAsset.getContentType(),
				newAsset.getNameFile()
		);
    }

	private static FileContentBo fromClassPath(String partialPath) {
		try {
			var resource = new ClassPathResource(partialPath);
			return FileContentBo.fromResource(resource);
		}  catch (IOException e){
			log.error(e.getMessage(), e);
			throw new FileServiceException(
					FileServiceEnumException.SAVE_IOEXCEPTION,
					String.format("La lectura del siguiente archivo %s tuvo el siguiente error %s", partialPath, e)
			);
		}
	}
}
