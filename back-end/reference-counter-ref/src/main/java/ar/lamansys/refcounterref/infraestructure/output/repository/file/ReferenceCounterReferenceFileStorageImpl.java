package ar.lamansys.refcounterref.infraestructure.output.repository.file;

import ar.lamansys.refcounterref.application.port.ReferenceCounterReferenceFileStorage;
import ar.lamansys.sgx.shared.files.FileService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RequiredArgsConstructor
@Slf4j
@Service
public class ReferenceCounterReferenceFileStorageImpl implements ReferenceCounterReferenceFileStorage {

    private static final String RELATIVE_DIRECTORY = "/patient/{patiendId}/reference-counter-reference/";
    private final String OUTPUT = "Output -> {}";

    private final FileService fileService;
    private final ReferenceCounterReferenceFileRepository referenceCounterReferenceFileRepository;

    @Override
    public Integer save(Integer institutionId, Integer patientId, MultipartFile file, Integer type) {

        String newFileName = fileService.createFileName(FilenameUtils.getExtension(file.getOriginalFilename()));
        String partialPath = buildPartialPath(patientId, newFileName);
        String completePath = buildCompleteFilePath(partialPath);
        fileService.saveFile(completePath, false, file);

        Integer result = saveReferenceCounterReferenceFileMetadata(partialPath, file, type);
        log.debug(OUTPUT, result);
        return result;

    }

    @Override
    public void updateReferenceCounterReferenceId(Integer referenceCounterReferenceId, List<Integer> fileIds) {
        log.debug("Input parameters -> referenceCounterReferenceId {}, fileIds {}", referenceCounterReferenceId, fileIds);
        fileIds.stream().forEach(fileId -> {
            ReferenceCounterReferenceFile referenceFile = referenceCounterReferenceFileRepository.findById(fileId).get();
            referenceFile.setReferenceCounterReferenceId(referenceCounterReferenceId);
            referenceCounterReferenceFileRepository.save(referenceFile);
        });
    }

    private String buildPartialPath(Integer patientId, String relativeFilePath) {
        log.debug("Input parameters -> patientId {}, relativeFilePath {}", patientId, relativeFilePath);
        String result = RELATIVE_DIRECTORY
                .replace("{patiendId}", patientId.toString())
                .concat(relativeFilePath);
        log.debug(OUTPUT, result);
        return result;
    }

    private Integer saveReferenceCounterReferenceFileMetadata(String completePath, MultipartFile file, Integer type) {
        ReferenceCounterReferenceFile referenceCounterReferenceFile = new ReferenceCounterReferenceFile(
                completePath,
                file.getContentType(),
                file.getSize(),
                file.getOriginalFilename(),
                type
        );
        Integer result = referenceCounterReferenceFileRepository.save(referenceCounterReferenceFile).getId();
        log.debug(OUTPUT, result);
        return result;
    }

    private String buildCompleteFilePath(String partialPath) {
        log.debug("Input parameters -> partialPath {}", partialPath);
        String result = fileService.buildRelativePath(partialPath);
        log.debug(OUTPUT, result);
        return result;
    }

}