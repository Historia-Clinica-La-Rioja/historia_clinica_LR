import { TabsService } from "./tabs.service"
import { TabsLabel } from "@turnos/constants/tabs";
import { AppFeature, ERole } from "@api-rest/api-model";
import { PermissionsService } from "@core/services/permissions.service";
import { FeatureFlagService } from "@core/services/feature-flag.service";
import { of } from "rxjs/internal/observable/of";

describe('TabsService', () => {

	let tabsService: TabsService;
	let permissionServiceMock: jasmine.SpyObj<PermissionsService>;
	let featureFlagServiceMock: jasmine.SpyObj<FeatureFlagService>;

	beforeEach(() => {
		permissionServiceMock = jasmine.createSpyObj('PermissionsService', ['contextAssignments$']);
		featureFlagServiceMock = jasmine.createSpyObj('FeatureFlagService', ['filterItems$']);

		permissionServiceMock.contextAssignments$.and.returnValue(of([]));
		featureFlagServiceMock.filterItems$.and.returnValue(of([]));

		tabsService = new TabsService(permissionServiceMock, featureFlagServiceMock);
	});

	it('should be created', () => {
		expect(tabsService).toBeTruthy();
	});

	it('should set professional tab with nurse role', () => {

		const roles: ERole[] = [ERole.ENFERMERO];
		const result: TabsLabel[] = [TabsLabel.PROFESSIONAL];

		permissionServiceMock.contextAssignments$.and.returnValue(of(roles));
		featureFlagServiceMock.filterItems$.and.returnValue(of([]));

		tabsService.getPermissionsAndSetAvailableTabs();

		expect(result).toEqual(tabsService.availableTabs);

	});

	it("should set professional, institution, care network and requests tabs with administrative role", () => {

		const roles: ERole[] = [ERole.ADMINISTRATIVO];
		const result: TabsLabel[] = [TabsLabel.PROFESSIONAL, TabsLabel.INSTITUTION, TabsLabel.CARE_NETWORK, TabsLabel.REQUESTS];

		permissionServiceMock.contextAssignments$.and.returnValue(of(roles));
		featureFlagServiceMock.filterItems$.and.returnValue(of([]));

		tabsService.getPermissionsAndSetAvailableTabs();

		expect(result).toEqual(tabsService.availableTabs);

	});

	it("should set professional, institution, care network and requests tabs with doctor role", () => {

		const roles: ERole[] = [ERole.ESPECIALISTA_MEDICO];
		const result: TabsLabel[] = [TabsLabel.PROFESSIONAL, TabsLabel.CARE_NETWORK, TabsLabel.REQUESTS];

		permissionServiceMock.contextAssignments$.and.returnValue(of(roles));
		featureFlagServiceMock.filterItems$.and.returnValue(of([]));

		tabsService.getPermissionsAndSetAvailableTabs();

		expect(result).toEqual(tabsService.availableTabs);

	});

	it("should set professional, institution, care network and requests tabs with health professional role", () => {

		const roles: ERole[] = [ERole.PROFESIONAL_DE_SALUD];
		const result: TabsLabel[] = [TabsLabel.PROFESSIONAL, TabsLabel.CARE_NETWORK, TabsLabel.REQUESTS];

		permissionServiceMock.contextAssignments$.and.returnValue(of(roles));
		featureFlagServiceMock.filterItems$.and.returnValue(of([]));

		tabsService.getPermissionsAndSetAvailableTabs();

		expect(result).toEqual(tabsService.availableTabs);

	});

	it("should set professional, institution, care network and requests tabs with dentist role", () => {

		const roles: ERole[] = [ERole.ESPECIALISTA_EN_ODONTOLOGIA];
		const result: TabsLabel[] = [TabsLabel.PROFESSIONAL, TabsLabel.CARE_NETWORK, TabsLabel.REQUESTS];

		permissionServiceMock.contextAssignments$.and.returnValue(of(roles));
		featureFlagServiceMock.filterItems$.and.returnValue(of([]));

		tabsService.getPermissionsAndSetAvailableTabs();

		expect(result).toEqual(tabsService.availableTabs);

	});

	it('should set professional and image network tabs with diary manager role and active image network feature flag', () => {

		const roles: ERole[] = [ERole.ADMINISTRADOR_AGENDA];
		const ffActives = [{ featureFlag: [AppFeature.HABILITAR_DESARROLLO_RED_IMAGENES] }];
		const result: TabsLabel[] = [TabsLabel.PROFESSIONAL, TabsLabel.IMAGE_NETWORK];

		permissionServiceMock.contextAssignments$.and.returnValue(of(roles));
		featureFlagServiceMock.filterItems$.and.returnValue(of(ffActives));

		tabsService.getPermissionsAndSetAvailableTabs();

		expect(result).toEqual(tabsService.availableTabs);

	});

	it('should set professional tab with diary manager role and inactive image network feature flag', () => {

		const roles: ERole[] = [ERole.ADMINISTRADOR_AGENDA];
		const result: TabsLabel[] = [TabsLabel.PROFESSIONAL];

		permissionServiceMock.contextAssignments$.and.returnValue(of(roles));
		featureFlagServiceMock.filterItems$.and.returnValue(of([]));

		tabsService.getPermissionsAndSetAvailableTabs();

		expect(result).toEqual(tabsService.availableTabs);

	});

	it('should set image network tab with image network administrative role and active image network feature flag', () => {

		const roles: ERole[] = [ERole.ADMINISTRATIVO_RED_DE_IMAGENES];
		const ffActives = [{ featureFlag: [ AppFeature.HABILITAR_DESARROLLO_RED_IMAGENES ]}];
		const result: TabsLabel[] = [TabsLabel.IMAGE_NETWORK];

		permissionServiceMock.contextAssignments$.and.returnValue(of(roles));
		featureFlagServiceMock.filterItems$.and.returnValue(of(ffActives));

		tabsService.getPermissionsAndSetAvailableTabs();

		expect(result).toEqual(tabsService.availableTabs);

	});

	it('should set non tabs with image network administrative role and inactive image network feature flag', () => {

		const roles: ERole[] = [ERole.ADMINISTRATIVO_RED_DE_IMAGENES];
		const result: TabsLabel[] = [];

		permissionServiceMock.contextAssignments$.and.returnValue(of(roles));
		featureFlagServiceMock.filterItems$.and.returnValue(of([]));

		tabsService.getPermissionsAndSetAvailableTabs();

		expect(result).toEqual(tabsService.availableTabs);

	});

	it("should set requests tab with violence role and active reference report's feature flag", () => {

		const roles: ERole[] = [ERole.ABORDAJE_VIOLENCIAS];
		const result: TabsLabel[] = [TabsLabel.REQUESTS];

		permissionServiceMock.contextAssignments$.and.returnValue(of(roles));
		featureFlagServiceMock.filterItems$.and.returnValue(of([]));

		tabsService.getPermissionsAndSetAvailableTabs();

		expect(result).toEqual(tabsService.availableTabs);

	});

})
