package net.pladema.settings.service.impl;

import net.pladema.clinichistory.requests.servicerequests.controller.ServiceRequestController;
import net.pladema.settings.service.SettingsService;
import net.pladema.settings.service.domain.Assets;
import net.pladema.sgx.files.FileService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class SettingsServiceImpl implements SettingsService {

    private static final String PATH = "/assets/custom/";
    private static final Logger LOG = LoggerFactory.getLogger(ServiceRequestController.class);
    private static final Assets SPONSOR_LOGO = new Assets(0, "Logo sponsor", "", "sponsor_logo", ".png");
    private static final Assets FAVICON = new Assets(1, "Favicon", "", "favicon", ".ico");
    private static final Assets ICON_72 = new Assets(2, "Logo 72 x 72 pixeles", "", "72x72_logo", ".png");
    private static final Assets ICON_96 = new Assets(3, "Logo 96 x 96 pixeles", "", "96x96_logo", ".png");
    private static final Assets ICON_128 = new Assets(4, "Logo 128 x 128 pixeles", "", "128x128_logo", ".png");
    private static final Assets ICON_144 = new Assets(5, "Logo 144 x 144 pixeles", "", "144x144_logo", ".png");
    private static final Assets ICON_152 = new Assets(6, "Logo 152 x 152 pixeles", "", "152x152_logo", ".png");
    private static final Assets ICON_192 = new Assets(7, "Logo 192 x 192 pixeles", "", "192x192_logo", ".png");
    private static final Assets ICON_384 = new Assets(8, "Logo 384 x 384 pixeles", "", "384x384_logo", ".png");
    private static final Assets ICON_512 = new Assets(9, "Logo 512 x 512 pixeles", "", "512x512_logo", ".png");

    private final FileService fileService;

    public SettingsServiceImpl(FileService fileService) {
        super();
        this.fileService = fileService;
    }

    @Override
    public boolean execute(String fileName, MultipartFile file) {
        String newFileName = this.createFileName(fileName.split("\\.")[0]);
        String partialPath = PATH.concat(newFileName);
        String completePath = fileService.buildRelativePath(partialPath);
        return fileService.saveFile(completePath, true, file);
    }

    @Override
    public boolean deleteFile(String fileName) {
        String newFileName = this.createFileName(fileName.split("\\.")[0]);
        String partialPath = PATH.concat(newFileName);
        String completePath = fileService.buildRelativePath(partialPath);
        return fileService.deleteFile(completePath);
    }

    private String createFileName(String fileName) {
        switch (fileName) {
            case "sponsor_logo":
                return SPONSOR_LOGO.getNameFile().concat(SPONSOR_LOGO.getExtension());
            case "favicon":
                return FAVICON.getNameFile().concat(FAVICON.getExtension());
            case "72x72_logo":
                return ICON_72.getNameFile().concat(ICON_72.getExtension());
            case "96x96_logo":
                return ICON_96.getNameFile().concat(ICON_96.getExtension());
            case "128x128_logo":
                return ICON_128.getNameFile().concat(ICON_128.getExtension());
            case "144x144_logo":
                return ICON_144.getNameFile().concat(ICON_144.getExtension());
            case "152x152_logo":
                return ICON_152.getNameFile().concat(ICON_152.getExtension());
            case "192x192_logo":
                return ICON_192.getNameFile().concat(ICON_192.getExtension());
            case "384x384_logo":
                return ICON_384.getNameFile().concat(ICON_384.getExtension());
            case "512x512_logo":
                return ICON_512.getNameFile().concat(ICON_512.getExtension());
        }
        return null;
    }
}
