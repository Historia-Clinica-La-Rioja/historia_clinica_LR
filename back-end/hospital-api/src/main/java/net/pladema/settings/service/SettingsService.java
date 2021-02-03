package net.pladema.settings.service;

import org.springframework.web.multipart.MultipartFile;

public interface SettingsService {
    void execute(String fileName, MultipartFile file);
}
