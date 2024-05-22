package net.pladema.assets.controller.service;

import net.pladema.assets.service.AssetsService;
import net.pladema.assets.service.domain.Assets;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AssetsExternalServiceImpl implements AssetsExternalService {

    private final AssetsService assetsService;

    public AssetsExternalServiceImpl(AssetsService assetsService) {
        this.assetsService = assetsService;
    }

    @Override
    public Optional<Assets> findByName(String name) {
        return assetsService.findByName(name);
    }
}
