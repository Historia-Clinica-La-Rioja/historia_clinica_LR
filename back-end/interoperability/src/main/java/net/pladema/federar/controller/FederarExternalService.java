package net.pladema.federar.controller;

import net.pladema.federar.services.domain.FederarResourceAttributes;
import net.pladema.federar.services.exceptions.FederarApiException;

import java.util.Optional;

public interface FederarExternalService {

    Optional<Integer> federatePatient(FederarResourceAttributes attributes, Integer localId) throws FederarApiException;
}
