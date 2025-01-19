package ar.lamansys.sgh.clinichistory.application.medicationrequest;

import ar.lamansys.sgh.clinichistory.application.document.CommonContextBuilder;
import ar.lamansys.sgh.clinichistory.application.ports.output.MedicationStatementPort;
import ar.lamansys.sgh.clinichistory.domain.CommercialPrescriptionDataBo;
import ar.lamansys.sgh.clinichistory.domain.document.impl.MedicationRequestBo;
import ar.lamansys.sgh.clinichistory.domain.ips.CommercialMedicationPrescriptionBo;
import ar.lamansys.sgh.clinichistory.domain.ips.MedicationBo;
import ar.lamansys.sgh.clinichistory.domain.ips.SnomedBo;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.DocumentType;
import ar.lamansys.sgh.shared.infrastructure.input.service.BasicPatientDto;
import ar.lamansys.sgh.shared.infrastructure.input.service.SharedAddressPort;
import ar.lamansys.sgh.shared.infrastructure.input.service.SharedPersonPort;
import ar.lamansys.sgh.shared.infrastructure.input.service.staff.LicenseNumberDto;
import ar.lamansys.sgh.shared.infrastructure.input.service.staff.ProfessionCompleteDto;
import ar.lamansys.sgh.shared.infrastructure.input.service.staff.ProfessionSpecialtyDto;
import ar.lamansys.sgh.shared.infrastructure.input.service.staff.ProfessionalCompleteDto;
import ar.lamansys.sgx.shared.files.pdf.GenerateDocumentContext;

import ar.lamansys.sgx.shared.filestorage.infrastructure.input.rest.StoredFileBo;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.client.j2se.MatrixToImageConfig;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.oned.Code128Writer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import net.pladema.assets.service.AssetsService;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class GenerateMedicationRequestDocumentContext implements GenerateDocumentContext<MedicationRequestBo> {

	@Value("${prescription.domain.number}")
	private Integer recipeDomain;

	private final CommonContextBuilder commonContextBuilder;

	private final AssetsService assetsService;

	private final SharedAddressPort sharedAddressPort;

	private final SharedPersonPort sharedPersonPort;

	private final MedicationStatementPort medicationStatementPort;

	@Override
	public Map<String, Object> run(MedicationRequestBo data) {
		Map<String, Object> result = new HashMap<>();
		commonContextBuilder.run(data, result);
		if (data.getDocumentType() == DocumentType.DIGITAL_RECIPE)
			addDigitalRecipeContextDocumentData(result, data);
		else
			addRecipeContextDocumentData(result, data);
		return result;
	}

	private void addRecipeContextDocumentData(Map<String, Object> ctx, MedicationRequestBo document) {
		ctx.put("recipe", true);
		ctx.put("order", false);
		ctx.put("request", document);
		ProfessionalCompleteDto professional = (ProfessionalCompleteDto) ctx.get("author");
		ctx.put("professional", professional);
		ctx.put("professionalName", sharedPersonPort.getCompletePersonNameById(professional.getPersonId()));

		ctx.put("contactInfo", sharedPersonPort.getPersonContactInfoById(((BasicPatientDto) ctx.get("patient")).getPerson().getId()));

		String date = document.getPerformedDate().format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));
		ctx.put("requestDate", date);
	}

	private void addDigitalRecipeContextDocumentData(Map<String, Object> ctx, MedicationRequestBo document) {
		String date = document.getPerformedDate().format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));
		String dateUntil = document.getPerformedDate().plusDays(30).format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));
		ctx.put("requestDate", date);
		ctx.put("dateUntil", dateUntil);

		String recipeNumberWithDomain = recipeDomain + "-" + document.getEncounterId().toString();
		String recipeNumberBarCode = generateDigitalRecipeBarCode(recipeNumberWithDomain);
		String recipeNumberWithIdentificationNumber = String.format("%s-%s-%s",
				((BasicPatientDto) ctx.get("patient")).getIdentificationNumber(),
				recipeDomain,
				document.getEncounterId().toString());
		String recipeNumberBarCodeWithIdentificationNumber = generateDigitalRecipeBarCode(recipeNumberWithIdentificationNumber);
		ctx.put("recipeNumberBarCode", recipeNumberBarCode);
		ctx.put("recipeNumberBarCodeWithIdentificationNumber", recipeNumberBarCodeWithIdentificationNumber);
		ctx.put("recipeNumber", recipeNumberWithDomain);

		String recipeUuidWithDomain = completeDomain(recipeDomain.toString()) + "-" + document.getUuid().toString();
		String recipeUuidBarCode = generateDigitalRecipeBarCode(recipeUuidWithDomain);
		ctx.put("recipeUuidBarCode", recipeUuidBarCode);
		ctx.put("recipeUuid", recipeUuidWithDomain);

		ProfessionalCompleteDto professionalInformation = (ProfessionalCompleteDto) ctx.get("author");
		Optional<ProfessionCompleteDto> professionalRelatedProfession = professionalInformation.getProfessions().stream()
				.filter(profession -> profession.getSpecialties().stream().anyMatch(specialty -> specialty.getSpecialty().getId().equals(document.getClinicalSpecialtyId())))
				.findFirst();

		ctx.put("professional", professionalInformation); //Is "author" which was already loaded. Must change old templates to remove
		setMedications(ctx, document);
		ctx.put("professionalProfession", professionalRelatedProfession.<Object>map(ProfessionCompleteDto::getDescription).orElse(null));

		professionalRelatedProfession.ifPresent(professionCompleteDto -> addProfessionalProfessionData(ctx, document, professionCompleteDto));

		String logo = String.join(",","data:image/png;charset=utf-8;base64", generatePdfImage("pdf/digital_recipe_logo.png"));
		String headerLogo = String.join(",","data:image/png;charset=utf-8;base64", generatePdfImage("pdf/digital_recipe_header_logo.png"));
		ctx.put("logo", logo);
		ctx.put("headerLogos", headerLogo);
		ctx.put("isArchived", document.getIsArchived());
		ctx.put("patientAddress", sharedAddressPort.fetchPatientCompleteAddress(document.getPatientId()));

		String patientIdentificationNumberBarCode = generateDigitalRecipeBarCode(((BasicPatientDto)ctx.get(("patient"))).getIdentificationNumber());
		ctx.put("patientIdentificationNumberBarCode", patientIdentificationNumberBarCode);
	}

	private void setMedications(Map<String, Object> ctx, MedicationRequestBo document) {
		List<MedicationBo> medications = document.getMedications();
		List<Integer> medicationRequestIds = medications.stream()
				.peek(this::adaptMedicationUnit)
				.map(MedicationBo::getId)
				.collect(Collectors.toList());
		medicationStatementPort.getCommercialPrescriptionDataByIds(medicationRequestIds)
				.forEach(digitalPrescriptionData -> handleDigitalPrescriptionData(digitalPrescriptionData, medications));
		ctx.put("medications", medications);
	}

	private void adaptMedicationUnit(MedicationBo medication) {
		switch (medication.getDosage().getQuantity().getUnit()) {
			case "comprimido":
				medication.getDosage().getQuantity().setUnit("comprimido/s");
				break;
			case "cápsula":
				medication.getDosage().getQuantity().setUnit("cápsula/s");
				break;
			case "ampolla":
				medication.getDosage().getQuantity().setUnit("ampolla/s");
				break;
			case "goma de mascar":
				medication.getDosage().getQuantity().setUnit("goma/s de mascar");
				break;
			case "apósito":
				medication.getDosage().getQuantity().setUnit("apósito/s");
				break;
			case "óvulo vaginal":
				medication.getDosage().getQuantity().setUnit("óvulo/s vaginal/es");
				break;
			case "pastilla":
				medication.getDosage().getQuantity().setUnit("pastilla/s");
				break;
			case "supositorio":
				medication.getDosage().getQuantity().setUnit("supositorio/s");
				break;
			case "parche":
				medication.getDosage().getQuantity().setUnit("parche/s");
				break;
			case "implante":
				medication.getDosage().getQuantity().setUnit("implante/s");
				break;
			case "aplicador":
				medication.getDosage().getQuantity().setUnit("aplicador/es");
				break;
			case "comprimido de disolución oral":
				medication.getDosage().getQuantity().setUnit("comprimido/s de disolución oral");
				break;
			case "píldora pequeña":
				medication.getDosage().getQuantity().setUnit("píldora/s pequeña/s");
				break;
			case "aplicación":
				medication.getDosage().getQuantity().setUnit("aplicación/es");
				break;
		}
	}

	private void handleDigitalPrescriptionData(CommercialPrescriptionDataBo digitalPrescriptionData, List<MedicationBo> medications) {
		medications.stream()
				.filter(medication -> medication.getId().equals(digitalPrescriptionData.getMedicationStatementId()))
				.findFirst()
				.ifPresent(medication -> addDigitalPrescriptionData(digitalPrescriptionData, medication));
	}

	private void addDigitalPrescriptionData(CommercialPrescriptionDataBo digitalPrescriptionData, MedicationBo medication) {
		medication.setCommercialMedicationPrescription(CommercialMedicationPrescriptionBo.builder()
				.medicationPackQuantity(digitalPrescriptionData.getMedicationPackQuantity())
				.presentationUnitQuantity(digitalPrescriptionData.getPresentationUnitQuantity())
				.build());
		medication.setSuggestedCommercialMedication(new SnomedBo(null, digitalPrescriptionData.getSuggestedCommercialMedicationPt()));
	}

	private void addProfessionalProfessionData(Map<String, Object> ctx, MedicationRequestBo document, ProfessionCompleteDto professionalRelatedProfession) {
		Optional<ProfessionSpecialtyDto> clinicalSpecialty = professionalRelatedProfession.getSpecialties()
				.stream()
				.filter(specialty -> specialty.getSpecialty().getId().equals(document.getClinicalSpecialtyId()))
				.findFirst();
		ctx.put("clinicalSpecialty", clinicalSpecialty.<Object>map(professionSpecialtyDto -> professionSpecialtyDto.getSpecialty().getName()).orElse(null));

		Optional<LicenseNumberDto> nationalLicenseData = professionalRelatedProfession.getAllLicenses()
				.stream()
				.filter(license -> license.getType().equals("MN"))
				.findFirst();
		nationalLicenseData.ifPresent(licenseNumberDto -> ctx.put("nationalLicense", licenseNumberDto.getNumber()));

		Optional<LicenseNumberDto> stateProvinceData = professionalRelatedProfession.getAllLicenses()
				.stream()
				.filter(license -> license.getType().equals("MP"))
				.findFirst();
		stateProvinceData.ifPresent(licenseNumberDto -> ctx.put("stateLicense", licenseNumberDto.getNumber()));
	}

	private String generateDigitalRecipeBarCode(String dataToEncode) {
		Code128Writer writer = new Code128Writer();
		BitMatrix barCode = writer.encode(dataToEncode, BarcodeFormat.CODE_128, 200, 100);
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		try {
			MatrixToImageWriter.writeToStream(barCode, "JPEG" , outputStream, new MatrixToImageConfig());
			return Base64.getEncoder().encodeToString(outputStream.toByteArray());
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	private String completeDomain(String domain) {
		return String.format("%0" + (11 - domain.length()) + "d%s", 0, domain);
	}

	//TODO: Must be inside a utils service
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
