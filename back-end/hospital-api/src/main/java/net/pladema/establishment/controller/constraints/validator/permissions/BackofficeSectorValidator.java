package net.pladema.establishment.controller.constraints.validator.permissions;

import net.pladema.establishment.repository.HierarchicalUnitSectorRepository;
import net.pladema.establishment.repository.SectorRepository;
import net.pladema.establishment.repository.entity.Sector;
import net.pladema.medicalconsultation.doctorsoffice.repository.DoctorsOfficeRepository;
import net.pladema.medicalconsultation.doctorsoffice.repository.domain.DoctorsOfficeVo;
import net.pladema.permissions.repository.enums.ERole;
import net.pladema.sgx.backoffice.permissions.BackofficePermissionValidator;
import net.pladema.sgx.backoffice.rest.ItemsAllowed;
import net.pladema.sgx.exceptions.BackofficeValidationException;
import net.pladema.sgx.exceptions.PermissionDeniedException;
import net.pladema.user.controller.BackofficeAuthoritiesValidator;

import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class BackofficeSectorValidator implements BackofficePermissionValidator<Sector, Integer> {
	private static final Short CUIDADOS_PROGRESIVOS = 2;

	public static final String NO_CUENTA_CON_SUFICIENTES_PRIVILEGIOS = "No cuenta con suficientes privilegios";
	private final SectorRepository repository;

	private final DoctorsOfficeRepository doctorsOfficeRepository;

	private final HierarchicalUnitSectorRepository hierarchicalUnitSectorRepository;

	private final BackofficeAuthoritiesValidator authoritiesValidator;

	private final PermissionEvaluator permissionEvaluator;

	public BackofficeSectorValidator(SectorRepository repository,
									 DoctorsOfficeRepository doctorsOfficeRepository,
									 HierarchicalUnitSectorRepository hierarchicalUnitSectorRepository,
									 BackofficeAuthoritiesValidator backofficeAuthoritiesValidator,
									 PermissionEvaluator permissionEvaluator) {
		this.repository = repository;
		this.doctorsOfficeRepository = doctorsOfficeRepository;
		this.hierarchicalUnitSectorRepository = hierarchicalUnitSectorRepository;
		this.authoritiesValidator = backofficeAuthoritiesValidator;
		this.permissionEvaluator = permissionEvaluator;
	}


	@Override
	@PreAuthorize("hasPermission(#entity.institutionId, 'ADMINISTRADOR_INSTITUCIONAL_BACKOFFICE') || hasAnyAuthority('ROOT', 'ADMINISTRADOR')")
	public void assertGetList(Sector entity) {
		// nothing to do
	}

	@Override
	public List<Integer> filterIdsByPermission(List<Integer> ids) {
		if (authoritiesValidator.hasRole(ERole.ROOT) || authoritiesValidator.hasRole(ERole.ADMINISTRADOR))
			return ids;
		return ids.stream().filter(id -> {
			try {
				Integer institutionId = repository.getInstitutionId(id);
				hasPermissionByInstitution(institutionId);
				return true;
			} catch (Exception e) {
				return false;
			}
		}).collect(Collectors.toList());
	}

	@Override
	public void assertGetOne(Integer id) {
		if (authoritiesValidator.hasRole(ERole.ROOT) || authoritiesValidator.hasRole(ERole.ADMINISTRADOR))
			return;
		Integer institutionId = repository.getInstitutionId(id);
		hasPermissionByInstitution(institutionId);
	}

	@Override
	@PreAuthorize("hasPermission(#entity.institutionId, 'ADMINISTRADOR_INSTITUCIONAL_BACKOFFICE') || hasAnyAuthority('ROOT', 'ADMINISTRADOR')")
	public void assertCreate(Sector entity) {
		assertParentSector(entity, entity.getId());
		assertCareType(entity);
		assertNotExists(entity);
	}

	@Override
	@PreAuthorize("hasPermission(#entity.institutionId, 'ADMINISTRADOR_INSTITUCIONAL_BACKOFFICE') || hasAnyAuthority('ROOT', 'ADMINISTRADOR')")
	public void assertUpdate(Integer id, Sector entity) {
		assertParentSector(entity, id);
		assertCareType(entity);
		assertNotExists(entity);
	}

	private void assertParentSector(Sector sector, Integer id){
		if(sector.getSectorId() != null && sector.getSectorId().equals(id)){
			throw new BackofficeValidationException("sector.parentOfItself");
		}
	}

	private void assertCareType(Sector sector){
		if(sector.getSectorOrganizationId() != null &&
				sector.getSectorOrganizationId().equals(CUIDADOS_PROGRESIVOS) &&
				sector.getCareTypeId() == null)
			throw new BackofficeValidationException("sector.mandatoryCareType");
	}

	private void assertNotExists(Sector sector){
		var entity = repository.findByInstitutionIdAndDescription(sector.getInstitutionId(), sector.getDescription());
		if (entity.isPresent() && !entity.get().getId().equals(sector.getId())){
			throw new BackofficeValidationException("sector.institution.exists");
		}
	}

	@Override
	public void assertDelete(Integer id) {
		if (authoritiesValidator.hasRole(ERole.ROOT) || authoritiesValidator.hasRole(ERole.ADMINISTRADOR))
			return;
		Integer institutionId = repository.getInstitutionId(id);
		hasPermissionByInstitution(institutionId);
		assertSectorNoDoctorsOffice(id);
		assertSectorNoChilds(id);
		assertSectorNoHierarchicalUnits(id);
	}

	@Override
	public ItemsAllowed itemsAllowedToList(Sector entity) {
		if (authoritiesValidator.hasRole(ERole.ROOT) || authoritiesValidator.hasRole(ERole.ADMINISTRADOR))
			return new ItemsAllowed<>();
		List<Integer> allowedInstitutions = authoritiesValidator.allowedInstitutionIds(Arrays.asList(ERole.ADMINISTRADOR_INSTITUCIONAL_BACKOFFICE));
		if (allowedInstitutions.isEmpty())
			return new ItemsAllowed<>(false, Collections.emptyList());
		List<Integer> idsAllowed = repository.getAllIdsByInstitutionsId(allowedInstitutions);
		return new ItemsAllowed<>(false, idsAllowed);
	}

	@Override
	public ItemsAllowed itemsAllowedToList() {
		if (authoritiesValidator.hasRole(ERole.ROOT) || authoritiesValidator.hasRole(ERole.ADMINISTRADOR))
			return new ItemsAllowed<>();
		List<Integer> allowedInstitutions = authoritiesValidator.allowedInstitutionIds(Arrays.asList(ERole.ADMINISTRADOR_INSTITUCIONAL_BACKOFFICE));
		if (allowedInstitutions.isEmpty())
			return new ItemsAllowed<>(false, Collections.emptyList());
		List<Integer> idsAllowed = repository.getAllIdsByInstitutionsId(allowedInstitutions);
		return new ItemsAllowed<>(false, idsAllowed);
	}

	private void hasPermissionByInstitution(Integer institutionId) {
		if (institutionId == null)
			throw new PermissionDeniedException(NO_CUENTA_CON_SUFICIENTES_PRIVILEGIOS);
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication == null || !authentication.isAuthenticated() || authentication.getPrincipal().equals("anonymousUser"))
			throw new PermissionDeniedException(NO_CUENTA_CON_SUFICIENTES_PRIVILEGIOS);
		if (!permissionEvaluator.hasPermission(authentication, institutionId, "ADMINISTRADOR_INSTITUCIONAL_BACKOFFICE"))
			throw new PermissionDeniedException(NO_CUENTA_CON_SUFICIENTES_PRIVILEGIOS);
	}

	private void assertSectorNoDoctorsOffice(Integer id) {
		Integer institutionId = repository.getInstitutionId(id);
		List<DoctorsOfficeVo> doctorsOffices = doctorsOfficeRepository.findAllBy(institutionId, id);
		if (!doctorsOffices.isEmpty())
			throw new BackofficeValidationException("sector.have.doctors-office");
	}

	private void assertSectorNoChilds(Integer sectorId){
		List<Sector> childSectors = repository.getChildSectorsBySectorId(sectorId);
		if(!childSectors.isEmpty())
			throw new BackofficeValidationException("sector.have.sector");
	}

	private void assertSectorNoHierarchicalUnits(Integer sectorId){
		List<Integer> hierarchicalUnits = hierarchicalUnitSectorRepository.getHierarchicalUnitsBySectorId(sectorId);
		if(!hierarchicalUnits.isEmpty())
			throw new BackofficeValidationException("sector.have.hierarchical-unit");
	}

}
