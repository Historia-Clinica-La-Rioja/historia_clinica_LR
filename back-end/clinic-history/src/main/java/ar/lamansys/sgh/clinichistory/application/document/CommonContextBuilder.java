package ar.lamansys.sgh.clinichistory.application.document;

import ar.lamansys.sgh.clinichistory.domain.document.IDocumentBo;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.generateFile.ClinicalSpecialtyFinder;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.generateFile.DocumentAuthorFinder;
import ar.lamansys.sgh.shared.infrastructure.input.service.BasicPatientDto;
import ar.lamansys.sgh.shared.infrastructure.input.service.ClinicalSpecialtyDto;
import ar.lamansys.sgh.shared.infrastructure.input.service.SharedPatientPort;
import ar.lamansys.sgh.shared.infrastructure.input.service.institution.InstitutionInfoDto;
import ar.lamansys.sgh.shared.infrastructure.input.service.institution.SharedInstitutionPort;
import ar.lamansys.sgh.shared.infrastructure.input.service.medicalcoverage.PatientMedicalCoverageService;
import ar.lamansys.sgh.shared.infrastructure.input.service.staff.ProfessionalCompleteDto;
import ar.lamansys.sgx.shared.featureflags.AppFeature;
import ar.lamansys.sgx.shared.featureflags.application.FeatureFlagsService;
import ar.lamansys.sgx.shared.filestorage.infrastructure.input.rest.StoredFileBo;
import net.pladema.assets.service.AssetsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.ZoneId;
import java.util.Base64;
import java.util.Map;
import java.util.function.Function;

@Service
public class CommonContextBuilder {

    private final Logger logger;
    private final Function<Integer, BasicPatientDto> basicDataFromPatientLoader;
    private final Function<Long, ProfessionalCompleteDto> authorFromDocumentFunction;
    private final Function<Integer, ClinicalSpecialtyDto> clinicalSpecialtyDtoFunction;
    private final FeatureFlagsService featureFlagsService;

    private final SharedInstitutionPort sharedInstitutionPort;

    private final AssetsService assetsService;

    private final PatientMedicalCoverageService patientMedicalCoverageService;

    public CommonContextBuilder(
            SharedPatientPort sharedPatientPort,
            DocumentAuthorFinder documentAuthorFinder,
            ClinicalSpecialtyFinder clinicalSpecialtyFinder,
            FeatureFlagsService featureFlagsService,
            SharedInstitutionPort sharedInstitutionPort,
            AssetsService assetsService,
            PatientMedicalCoverageService patientMedicalCoverageService) {
        this.sharedInstitutionPort = sharedInstitutionPort;
        this.logger = LoggerFactory.getLogger(getClass());
        this.basicDataFromPatientLoader = sharedPatientPort::getBasicDataFromPatient;
        this.authorFromDocumentFunction = documentAuthorFinder::getAuthor;
        this.clinicalSpecialtyDtoFunction = clinicalSpecialtyFinder::getClinicalSpecialty;
        this.featureFlagsService = featureFlagsService;
        this.assetsService = assetsService;
        this.patientMedicalCoverageService = patientMedicalCoverageService;
    }

    public void run(IDocumentBo document, Map<String, Object> contextMap) {
        logger.debug("Input parameters -> document {}", document);

        Integer patientId = document.getPatientId();
        this.addPatientInfo(contextMap, patientId);
        contextMap.put("clinicalSpecialty", clinicalSpecialtyDtoFunction.apply(document.getClinicalSpecialtyId()));
        contextMap.put("performedDate", document.getPerformedDate().atZone(ZoneId.of("UTC")).withZoneSameInstant(ZoneId.of("UTC-3")));
        contextMap.put("nameSelfDeterminationFF", featureFlagsService.isOn(AppFeature.HABILITAR_DATOS_AUTOPERCIBIDOS));

        contextMap.put("appLogo", generatePdfImage("pdf/hsi-footer-118x21.png"));
        contextMap.put("institutionLogo", generatePdfImage("pdf/hsi-header-250x72.png"));

        contextMap.put("encounterId", document.getEncounterId());
        InstitutionInfoDto institutionInfoDto = sharedInstitutionPort.fetchInstitutionById(document.getInstitutionId());
        contextMap.put("institution", institutionInfoDto);
        contextMap.put("institutionName", institutionInfoDto.getName());

        var author = authorFromDocumentFunction.apply(document.getId());
        contextMap.put("author", author);

        var patientCoverage = patientMedicalCoverageService.getCoverage(document.getMedicalCoverageId());

        patientCoverage.ifPresent(sharedPatientMedicalCoverageBo -> contextMap.put("patientCoverage", sharedPatientMedicalCoverageBo));

        logger.debug("Built context for patient {} and document {} is {}", patientId, document.getId(), contextMap);
    }

    private String calculatePatientAge(BasicPatientDto patientDto) {
        if (patientDto.getPerson() == null || patientDto.getPerson().getPersonAge() == null)
            return "";
        var personAge = patientDto.getPerson().getPersonAge();
        if (personAge.getTotalDays() < 46)
            return personAge.getTotalDays() + " días";
        if (personAge.getYears() > 0)
            return personAge.getYears() + (personAge.getYears() == 1 ? " año" : " años");
        return personAge.getMonths() + (personAge.getMonths() == 1 ? " mes " : " meses ") + personAge.getDays() + (personAge.getDays() == 1 ? " día" : " días" );
    }

    private void addPatientInfo(Map<String,Object> contextMap, Integer patientId) {
        var patientDto = basicDataFromPatientLoader.apply(patientId);
        contextMap.put("patient", patientDto);
        contextMap.put("patientCompleteName", patientDto.getCompletePersonName(featureFlagsService.isOn(AppFeature.HABILITAR_DATOS_AUTOPERCIBIDOS)));
        contextMap.put("patientAge", calculatePatientAge(patientDto));

        contextMap.put("selfPerceivedFF", featureFlagsService.isOn(AppFeature.HABILITAR_DATOS_AUTOPERCIBIDOS));
    }

    private String generatePdfImage(String path) {
        StoredFileBo asset = assetsService.getFile(path);
        try {
            var image = ImageIO.read(asset.getResource().getStream());
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            ImageIO.write(image, "png", outputStream);
            return Base64.getEncoder().encodeToString(outputStream.toByteArray());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
