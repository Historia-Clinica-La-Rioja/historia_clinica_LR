package net.pladema.assets.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import ar.lamansys.sgx.shared.files.FileService;
import ar.lamansys.sgx.shared.files.StreamFile;
import lombok.extern.slf4j.Slf4j;
import net.pladema.assets.service.AssetsService;
import net.pladema.assets.service.domain.Assets;
import net.pladema.assets.service.domain.AssetsFileBo;

@Slf4j
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


    private final FileService fileService;
    private final StreamFile streamFile;

    List<Assets> assetsList = new ArrayList<>(Arrays.asList(
            SPONSOR_LOGO, FAVICON,
            ICON_72, ICON_96, ICON_128, ICON_144, ICON_152, ICON_192, ICON_384, ICON_512,
            FOOTER_LEFT, FOOTER_CENTER, FOOTER_RIGHT, APP_LOGO));

    public AssetsServiceImpl(FileService fileService, StreamFile streamFile) {
        this.fileService = fileService;
        this.streamFile = streamFile;
    }

    @Override
    public Optional<Assets> findByName(String name) {
        log.debug(INPUT_LOG, name);
        return this.assetsList.stream().filter(a -> a.getNameFile().equals(name)).findAny();
    }

    @Override
    public AssetsFileBo getFile(String fileName) {
        log.debug(INPUT_LOG, fileName);

        Assets newAsset = this.findByName(fileName).get();
        String partialPath = CUSTOM_PATH.concat(newAsset.getNameFile());
        String completePath = fileService.buildRelativePath(partialPath);
        
        if (this.streamFile.existFile(completePath)) {
			log.debug("Using custom {}", fileName);
            return new AssetsFileBo(
                    this.fileService.loadFile(partialPath),
                    newAsset.getContentType()
			);
        }

        String newPartialPath = ORIGINAL_PATH.concat(newAsset.getNameFile());
		log.debug("Using original {}", fileName);
        return new AssetsFileBo(
                new ClassPathResource(newPartialPath),
                newAsset.getContentType()
		);
    }
}
