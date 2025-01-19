package net.pladema.assets.controller.service;

import net.pladema.assets.service.domain.Assets;

import java.util.Optional;

public interface AssetsExternalService {

    Optional<Assets> findByName(String name);

}
