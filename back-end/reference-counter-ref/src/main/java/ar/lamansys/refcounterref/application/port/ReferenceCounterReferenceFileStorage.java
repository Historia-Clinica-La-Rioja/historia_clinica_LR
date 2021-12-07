package ar.lamansys.refcounterref.application.port;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ReferenceCounterReferenceFileStorage {

    Integer save(Integer institutionId, Integer patientId, MultipartFile file, Integer type);

    void updateReferenceCounterReferenceId(Integer referenceCounterReferenceId, List<Integer> filesIds);

}
