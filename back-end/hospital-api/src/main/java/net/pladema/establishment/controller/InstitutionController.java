package net.pladema.establishment.controller;

import ar.lamansys.sgx.shared.security.UserInfo;
import io.swagger.v3.oas.annotations.tags.Tag;
import net.pladema.address.controller.dto.AddressDto;
import net.pladema.address.controller.mapper.AddressMapper;
import net.pladema.address.controller.service.AddressExternalService;
import net.pladema.address.controller.service.domain.AddressBo;
import net.pladema.establishment.controller.dto.InstitutionAddressDto;
import net.pladema.establishment.controller.dto.InstitutionBasicInfoDto;
import net.pladema.establishment.controller.dto.InstitutionDto;
import net.pladema.establishment.controller.mapper.InstitutionMapper;
import net.pladema.establishment.repository.InstitutionRepository;
import net.pladema.establishment.repository.entity.Institution;
import net.pladema.establishment.service.InstitutionService;
import net.pladema.establishment.service.domain.InstitutionBasicInfoBo;
import net.pladema.establishment.service.domain.InstitutionBo;
import net.pladema.establishment.service.fetchInstitutions.FetchAllInstitutions;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@Tag(name = "Institution", description = "Institution")
@RequestMapping("institution")
public class InstitutionController {

	private final AddressExternalService addressExternalService;
	private final InstitutionRepository repository;
	private final InstitutionMapper mapper;
	private final AddressMapper addressMapper;
	private final FetchAllInstitutions fetchAllInstitutions;
	private final Logger logger;
	private final InstitutionService institutionService;

	private final InstitutionMapper institutionMapper;

	public InstitutionController(InstitutionRepository repository,
								 InstitutionMapper mapper,
								 AddressExternalService addressExternalService,
								 FetchAllInstitutions fetchAllInstitutions,
								 InstitutionService institutionService,
								 AddressMapper addressMapper, InstitutionMapper institutionMapper) {
		this.repository = repository;
		this.mapper = mapper;
		this.addressExternalService = addressExternalService;
		this.fetchAllInstitutions = fetchAllInstitutions;
		this.institutionMapper = institutionMapper;
		this.logger = LoggerFactory.getLogger(this.getClass());
		this.institutionService = institutionService;
		this.addressMapper = addressMapper;
	}

	@GetMapping(params = "ids")
	public @ResponseBody
	List<InstitutionDto> getMany(@RequestParam List<Integer> ids) {
		List<Integer> allowedIds = ids;

		List<Institution> institutions = repository.findAllById(allowedIds);
		List<InstitutionDto> institutionDtoList = mapper.toListInstitutionDto(institutions);

		List<Integer> addressesIds = institutions.stream().map(Institution::getAddressId).collect(Collectors.toList());
		List<AddressDto> addresses = addressExternalService.getAddressesByIds(addressesIds);

		Map<Integer,InstitutionAddressDto> institutionAddressHashDtoMap=
				addresses.stream()
						.map(InstitutionAddressDto::new)
						.collect(Collectors.toMap(InstitutionAddressDto::getAddressId, item -> item));


		institutionDtoList.forEach(institutionDto -> institutionDto.setInstitutionAddressDto(institutionAddressHashDtoMap.get(institutionDto.getInstitutionAddressDto().getAddressId())));
		return institutionDtoList ;

	}

	@GetMapping("/all")
	public @ResponseBody
	List<InstitutionBasicInfoDto> getAll() {
		List<InstitutionBo> institutionBos = fetchAllInstitutions.run();
		var result = mapper.toListInstitutionBasicInfoDto(institutionBos);
		logger.debug("Ids results -> {}", result.stream().map(InstitutionBasicInfoDto::getId));
		logger.trace("Results -> {}", result);
		return result.stream().sorted(Comparator.comparing(InstitutionBasicInfoDto::getName)).collect(Collectors.toList());
	}

	@GetMapping("/{institutionId}/address")
	public @ResponseBody
	AddressDto getInstitutionAddress(@PathVariable(name = "institutionId") Integer institutionId) {
		logger.debug("Input parameter -> institutionId {}", institutionId);
		AddressBo institutionAddressBo = institutionService.getAddress(institutionId);
		var result = addressMapper.fromAddressBo(institutionAddressBo);
		logger.trace("result -> {}", result);
		return result;
	}

	@GetMapping("/department/{departmentId}")
	public @ResponseBody
	List<InstitutionBasicInfoDto> findByDepartmentId(@PathVariable(name = "departmentId") Short departmentId) {
		logger.debug("Input parameter -> departmentId {}", departmentId);
		List<InstitutionBasicInfoBo> institutionBasicInfoBoList = fetchAllInstitutions.findByDepartmentId(departmentId);
		var result = mapper.fromListInstitutionBasicInfoBo(institutionBasicInfoBoList);
		logger.trace("result -> {}", result);
		return result;
	}

	@GetMapping("/province/{provinceId}")
	public @ResponseBody
	List<InstitutionBasicInfoDto> findByProvinceId(@PathVariable(name = "provinceId") Short provinceId) {
		logger.debug("Input parameter -> provinceId {}", provinceId);
		List<InstitutionBasicInfoBo> institutionBasicInfoBoList = fetchAllInstitutions.findByProvinceId(provinceId);
		var result = mapper.fromListInstitutionBasicInfoBo(institutionBasicInfoBoList);
		logger.trace("result -> {}", result);
		return result;
	}

	@GetMapping("/imageSector")
	public @ResponseBody
	List<InstitutionBasicInfoDto> findByImageSectors() {
		List<InstitutionBasicInfoDto> result = institutionMapper.fromListInstitutionBasicInfoBo(institutionService.getInstitutionsByImageSectors());
		logger.trace("result -> {}", result);
		return result;
	}

	@GetMapping("/{institutionId}/by-reference-clinical-specialty-filter")
	@PreAuthorize("hasPermission(#institutionId, 'ESPECIALISTA_MEDICO, PROFESIONAL_DE_SALUD, ESPECIALISTA_EN_ODONTOLOGIA, ENFERMERO, GESTOR_DE_ACCESO_INSTITUCIONAL') || " +
			"hasAnyAuthority('GESTOR_DE_ACCESO_DE_DOMINIO', 'GESTOR_DE_ACCESO_REGIONAL', 'GESTOR_DE_ACCESO_LOCAL')")
	public List<InstitutionBasicInfoDto> getInstitutionsByReferenceByClinicalSpecialtyFilter(@PathVariable("institutionId") Integer institutionId,
																							 @RequestParam("departmentId") Short departmentId,
																							 @RequestParam("clinicalSpecialtyIds") List<Integer> clinicalSpecialtyIds,
																							 @RequestParam(name="careLineId", required = false) Integer careLineId)
	{
		logger.debug("Input parameter -> departmentId {}, clinicalSpecialtyIds {}, careLineId {}", departmentId, clinicalSpecialtyIds, careLineId);
		List<InstitutionBasicInfoBo> institutions = institutionService.getInstitutionsByReferenceByClinicalSpecialtyFilter(departmentId, clinicalSpecialtyIds, careLineId);
		var result = institutionMapper.fromListInstitutionBasicInfoBo(institutions);
		logger.trace("result -> {}", result);
		return result;
	}

	@GetMapping("/virtual-consultation")
	public List<InstitutionBasicInfoDto> getVirtualConsultationInstitutions() {
		List<InstitutionBasicInfoDto> result = institutionMapper.fromListInstitutionBasicInfoBo(institutionService.getVirtualConsultationInstitutions());
		logger.debug("Output -> {}", result);
		return result;
	}

	@GetMapping("/{institutionId}/by-reference-practice-filter")
	@PreAuthorize("hasPermission(#institutionId, 'ESPECIALISTA_MEDICO, PROFESIONAL_DE_SALUD, ESPECIALISTA_EN_ODONTOLOGIA, ENFERMERO, GESTOR_DE_ACCESO_INSTITUCIONAL' ) || " +
			"hasAnyAuthority('GESTOR_DE_ACCESO_DE_DOMINIO', 'GESTOR_DE_ACCESO_REGIONAL', 'GESTOR_DE_ACCESO_LOCAL')")
	public ResponseEntity<List<InstitutionBasicInfoDto>> getInstitutionsByReferenceByPracticeFilter(@PathVariable("institutionId") Integer institutionId,
																									@RequestParam("practiceSnomedId") Integer practiceSnomedId,
																									@RequestParam("departmentId") Short departmentId,
																								 	@RequestParam(name="careLineId", required = false) Integer careLineId,
																								 	@RequestParam(name="clinicalSpecialtyIds", required = false) List<Integer> clinicalSpecialtyIds) {
		logger.debug("Input parameter -> institutionId {}, practiceSnomedId {}, departmentId {}, careLineId {}, clinicalSpecialtyIds {}", institutionId, practiceSnomedId, departmentId, careLineId, clinicalSpecialtyIds);
		var institutions = institutionService.getInstitutionsByReferenceByPracticeFilter(departmentId, practiceSnomedId, clinicalSpecialtyIds, careLineId);
		var result = institutionMapper.fromListInstitutionBasicInfoBo(institutions);
		logger.trace("result -> {}", result);
		return ResponseEntity.ok(result);
	}

	@GetMapping("/by-manager")
	@PreAuthorize("hasAnyAuthority('GESTOR_DE_ACCESO_DE_DOMINIO', 'GESTOR_DE_ACCESO_REGIONAL', 'GESTOR_DE_ACCESO_LOCAL')")
	public ResponseEntity<List<InstitutionBasicInfoDto>> getInstitutionsByManagerUser(){
		logger.debug("Get institutions by manager user");
		List<InstitutionBasicInfoBo> institutions = institutionService.getInstitutionsByManagerUser();
		List<InstitutionBasicInfoDto> result = institutionMapper.fromListInstitutionBasicInfoBo(institutions);
		logger.debug("Output -> {}", result);
		return ResponseEntity.ok(result);
	}
	
}
