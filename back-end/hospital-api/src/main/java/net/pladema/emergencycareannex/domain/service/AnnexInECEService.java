package net.pladema.emergencycareannex.domain.service;

import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import net.pladema.emergencycare.repository.EmergencyCareTypeRepository;
import net.pladema.emergencycare.repository.entity.EmergencyCareType;
import net.pladema.establishment.repository.InstitutionRepository;
import net.pladema.establishment.repository.entity.Institution;
import net.pladema.person.repository.HealthInsuranceRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import ar.lamansys.sgx.shared.files.pdf.PDFDocumentException;
import ar.lamansys.sgx.shared.files.pdf.PdfService;
import ar.lamansys.sgx.shared.filestorage.application.FileContentBo;
import net.pladema.emergencycare.repository.EmergencyCareEpisodeRepository;
import net.pladema.emergencycare.repository.entity.EmergencyCareEpisode;
import net.pladema.patient.repository.MedicalCoverageRepository;
import net.pladema.patient.repository.entity.MedicalCoverage;
import net.pladema.person.repository.GenderRepository;
import net.pladema.person.repository.IdentificationTypeRepository;
import net.pladema.person.repository.PersonRepository;
import net.pladema.person.repository.entity.Gender;
import net.pladema.person.repository.entity.HealthInsurance;
import net.pladema.person.repository.entity.IdentificationType;
import net.pladema.person.repository.entity.Person;

@Service
public class AnnexInECEService {

	private static final Logger logger = LoggerFactory.getLogger(AnnexInECEService.class);

	private final EmergencyCareEpisodeRepository emergencyCareEpisodeRepository;
	private final PersonRepository personRepository;
	private final IdentificationTypeRepository identificationTypeRepository;
	private final GenderRepository genderRepository;
	private final MedicalCoverageRepository medicalCoverageRepository;
	private final HealthInsuranceRepository healthInsuranceRepository;
	private final EmergencyCareTypeRepository emergencyCareTypeRepository;
	private final InstitutionRepository institutionRepository;
	private final PdfService pdfService;

	public AnnexInECEService(EmergencyCareEpisodeRepository emergencyCareEpisodeRepository, PersonRepository personRepository, PdfService pdfService, IdentificationTypeRepository identificationTypeRepository, GenderRepository genderRepository, MedicalCoverageRepository medicalCoverageRepository, HealthInsuranceRepository healthInsuranceRepository, EmergencyCareTypeRepository emergencyCareTypeRepository, InstitutionRepository institutionRepository) {
		this.emergencyCareEpisodeRepository = emergencyCareEpisodeRepository;
		this.personRepository = personRepository;
		this.pdfService = pdfService;
		this.identificationTypeRepository = identificationTypeRepository;
		this.genderRepository = genderRepository;
		this.medicalCoverageRepository = medicalCoverageRepository;
		this.healthInsuranceRepository = healthInsuranceRepository;
		this.emergencyCareTypeRepository = emergencyCareTypeRepository;
		this.institutionRepository = institutionRepository;
	}

	public FileContentBo generateEpisodePdf(Integer episodeId) throws PDFDocumentException {
		logger.info("Executing query with episodeId {}", episodeId);

		Map<String, Object> contextMap;
		try {
			contextMap = fetchEpisodeContext(episodeId);
			logger.info("Fetched context map: {}", contextMap);
		} catch (Exception e) {
			logger.error("Error fetching episode context for episodeId {}", episodeId, e);
			throw e;
		}

		if (contextMap == null || contextMap.isEmpty()) {
			logger.warn("No data found for episode ID: {}", episodeId);
			throw new IllegalArgumentException("No data found for episode ID: " + episodeId);
		}

		String templateName = "annex_in_ece";

		try {
			return pdfService.generate(templateName, contextMap);
		} catch (Exception e) {
			logger.error("Error generating PDF for episodeId {} with context map {}", episodeId, contextMap, e);
			throw e;
		}
	}

	public Map<String, Object> fetchEpisodeContext(Integer episodeId) {
		Map<String, Object> context = new HashMap<>();

		Optional<EmergencyCareEpisode> episodeOptional = emergencyCareEpisodeRepository.findById(episodeId);
		if (episodeOptional.isPresent()) {
			EmergencyCareEpisode episode = episodeOptional.get();

			Optional<Person> personOptional = personRepository.findPersonByPatientId(episode.getPatientId());
			personOptional.ifPresent(person -> {
				String patientName = Stream.of(
						person.getLastName(),
						person.getOtherLastNames(),
						person.getFirstName(),
						person.getMiddleNames()
				).filter(Objects::nonNull)
						.collect(Collectors.joining(" "))
						.trim();
				context.put("patientName", patientName.isEmpty() ? null : patientName);

				context.put("identificationNumber", person.getIdentificationNumber());

				Optional<IdentificationType> identificationTypeOptional = Optional.ofNullable(person.getIdentificationTypeId())
						.flatMap(identificationTypeRepository::findById);
				context.put("identificationType", identificationTypeOptional
						.map(IdentificationType::getDescription)
						.orElse(null));

				Optional<Gender> genderOptional = Optional.ofNullable(person.getGenderId())
						.flatMap(genderRepository::findById);
				context.put("gender", genderOptional
						.map(Gender::getDescription)
						.orElse(null));

				context.put("patientAge", Optional.ofNullable(person.getBirthDate())
						.flatMap(birthDate -> Optional.ofNullable(episode.getCreatedOn())
								.map(createdOn -> ChronoUnit.YEARS.between(birthDate, createdOn.toLocalDate())))
						.orElse(null));
			});

			Optional<MedicalCoverage> medicalCoverageOptional = Optional.ofNullable(episode.getPatientMedicalCoverageId())
					.flatMap(medicalCoverageRepository::findById);
			context.put("medicalCoverageName", medicalCoverageOptional
					.map(MedicalCoverage::getName)
					.orElse(null));

			Optional<HealthInsurance> healthInsuranceOptional = medicalCoverageOptional
					.flatMap(medicalCoverage -> healthInsuranceRepository.findById(medicalCoverage.getId()));
			context.put("medicalCoverageRnos", healthInsuranceOptional
					.map(HealthInsurance::getRnos)
					.orElse(null));

			context.put("attentionDate", episode.getCreatedOn() != null ?
					episode.getCreatedOn().atZone(ZoneId.of("UTC-3")).format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) : null);
			context.put("diagnosis", episode.getReason());

			Optional<EmergencyCareType> emergencyCareTypeOptional = Optional.ofNullable(episode.getEmergencyCareTypeId())
					.flatMap(emergencyCareTypeRepository::findById);
			context.put("clinicalSpecialty", emergencyCareTypeOptional
					.map(EmergencyCareType::getDescription)
					.orElse(null));

			Optional<Institution> institutionOptional = institutionRepository.findById(episode.getInstitutionId());
			institutionOptional.ifPresent(institution -> {
				context.put("institutionName", institution.getName());
				context.put("institutionSisaCode", institution.getSisaCode());
			});
		}

		return context;
	}
}
