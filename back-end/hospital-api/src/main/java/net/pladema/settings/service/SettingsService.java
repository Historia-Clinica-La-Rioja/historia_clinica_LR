package net.pladema.settings.service;

import org.springframework.web.multipart.MultipartFile;

public interface SettingsService {
    boolean execute(String fileName, MultipartFile file);

    boolean deleteFile(String fileName);
}
