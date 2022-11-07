package net.pladema.settings.service.impl;

import java.io.IOException;
import java.util.Optional;
import java.util.UUID;

import org.apache.http.MethodNotSupportedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import ar.lamansys.sgx.shared.files.FileService;
import net.pladema.assets.service.domain.Assets;
import net.pladema.settings.service.SettingsService;

@Service
public class SettingsServiceImpl implements SettingsService {

    private static final String PATH = "/assets/custom/";
    private final Logger logger;

    private final FileService fileService;

    public SettingsServiceImpl(FileService fileService) {
        super();
        this.fileService = fileService;
        this.logger = LoggerFactory.getLogger(getClass());
    }

    @Override
    public boolean uploadFile(Optional<Assets> newAsset, MultipartFile file) throws MethodNotSupportedException, IOException {
        logger.debug("Input parameters -> fileName {}", newAsset);

        if (newAsset.isPresent()) {
            String partialPath = PATH.concat(newAsset.get().getNameFile());
			var fileCreated = fileService.transferMultipartFile(partialPath, UUID.randomUUID().toString(), "ASSETS_FILE", file);
            return fileCreated.getId() != null;
        }

        throw new MethodNotSupportedException("Icono/Logo no soportado por el momento");
    }

    @Override
    public boolean deleteFile(Optional<Assets> newAsset) throws MethodNotSupportedException {
        logger.debug("Input parameters -> fileName {}", newAsset);

        if (newAsset.isPresent()) {
            String partialPath = PATH.concat(newAsset.get().getNameFile());
            return fileService.deleteFile(partialPath);
        }

        throw new MethodNotSupportedException("Icono/Logo no soportado por el momento");
    }
}
