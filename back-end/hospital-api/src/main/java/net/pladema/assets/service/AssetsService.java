package net.pladema.assets.service;

import net.pladema.assets.service.domain.AssetsFileBo;
import net.pladema.assets.service.domain.Assets;

import java.util.Optional;

public interface AssetsService {

    Optional<Assets> findByName(String name);

    AssetsFileBo getFile(String fileName);
}
