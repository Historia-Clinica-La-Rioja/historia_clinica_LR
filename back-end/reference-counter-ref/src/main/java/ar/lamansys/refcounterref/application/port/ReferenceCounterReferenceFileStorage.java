package ar.lamansys.refcounterref.application.port;

import ar.lamansys.refcounterref.domain.enums.EReferenceCounterReferenceType;
import ar.lamansys.refcounterref.domain.file.ReferenceCounterReferenceFileBo;
import ar.lamansys.sgx.shared.filestorage.infrastructure.input.rest.StoredFileBo;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public interface ReferenceCounterReferenceFileStorage {

    Integer save(Integer institutionId, Integer patientId, MultipartFile file, Integer type) throws IOException;

    void updateReferenceCounterReferenceId(Integer referenceCounterReferenceId, List<Integer> filesIds);

    Map<Integer, List<ReferenceCounterReferenceFileBo>> getFilesByReferenceCounterReferenceIdsAndType(List<Integer> ids, EReferenceCounterReferenceType type);

    StoredFileBo getFile(Integer referenceCounterReferenceId, Integer type);

    void deleteFiles(List<Integer> filesIds);

    List<ReferenceCounterReferenceFileBo> getFilesByReferenceCounterReferenceIdAndType(Integer referenceCounterReferenceId, Integer type);

	List<Integer> saveAll(Integer institutionId, Integer patientId, MultipartFile[] files, Integer type) throws IOException;
}
