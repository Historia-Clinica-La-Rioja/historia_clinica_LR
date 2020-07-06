package net.pladema.establishment.controller;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import net.pladema.address.controller.dto.AddressDto;
import net.pladema.address.controller.service.AddressExternalService;
import net.pladema.establishment.controller.dto.InstitutionAddressDto;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import net.pladema.establishment.controller.dto.InstitutionDto;
import net.pladema.establishment.controller.mapper.InstitutionMapper;
import net.pladema.establishment.repository.InstitutionRepository;
import net.pladema.establishment.repository.entity.Institution;

@RestController
@Api(value = "Institution", tags = { "Institution" })
@RequestMapping("institution")
public class InstitutionController {

	private final AddressExternalService addressExternalService;
	private final InstitutionRepository repository;
	private final InstitutionMapper mapper;

	public InstitutionController(InstitutionRepository repository, InstitutionMapper mapper,AddressExternalService addressExternalService) {
		this.repository = repository;
		this.mapper = mapper;
		this.addressExternalService = addressExternalService;
	}

	@GetMapping(params = "ids")
	public @ResponseBody
	List<InstitutionDto> getMany(@RequestParam List<Integer> ids) {
		List<Integer> idsConAcceso = ids; //TODO ver permisos

		List<Institution> institutions = repository.findAllById(idsConAcceso);
		List<InstitutionDto> institutionDtos = mapper.toListInstitutionDto(institutions);

		List<Integer> addressesIds = institutions.stream().map(institution -> institution.getAddressId()).collect(Collectors.toList());
		List<AddressDto> addresses = addressExternalService.getAddressesByIds(addressesIds);

		Map<Integer,InstitutionAddressDto> institutionAddressHashDtos=
				addresses.stream()
						.map(addressDto -> new InstitutionAddressDto(addressDto))
						.collect(Collectors.toMap(InstitutionAddressDto::getAddressId, item -> item));


		institutionDtos.forEach(institutionDto -> institutionDto.setInstitutionAddressDto(institutionAddressHashDtos.get(institutionDto.getInstitutionAddressDto().getAddressId())));
		return institutionDtos ;

	}

}
