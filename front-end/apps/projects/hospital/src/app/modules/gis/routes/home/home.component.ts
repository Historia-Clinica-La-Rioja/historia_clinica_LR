import { Component, OnInit } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { GetSanitaryResponsibilityAreaInstitutionAddressDto, GlobalCoordinatesDto, InstitutionDto } from '@api-rest/api-model';
import { AddressMasterDataService, AddressProjection } from '@api-rest/services/address-master-data.service';
import { GisService } from '@api-rest/services/gis.service';
import { InstitutionService } from '@api-rest/services/institution.service';
import { ContextService } from '@core/services/context.service';
import { DEFAULT_COUNTRY_ID, hasError } from '@core/utils/form.utils';
import { ButtonType } from '@presentation/components/button/button.component';
import { TypeaheadOption } from '@presentation/components/typeahead/typeahead.component';
import { listToTypeaheadOptions } from '@presentation/utils/typeahead.mapper.utils';
import { finalize, forkJoin, map } from 'rxjs';
import { InstitutionDescription } from '../../components/institution-description/institution-description.component';
import { transformCoordinates } from '../../constants/coordinates.utils';
import { SnackBarService } from '@presentation/services/snack-bar.service';
import { GisLayersService } from '../../services/gis-layers.service';

interface InstitutionAddress {
    stateId: FormControl<number>;
    departmentId: FormControl<number>;
	cityId: FormControl<number>;
    streetName: FormControl<string>;
    houseNumber: FormControl<string>;
	coordinates: FormControl<string>;
}

const INSTITUTION_ADDRESS_STEP = 0;
const GEOPOSITION_STEP = 1;
const RESPONSABILITY_AREA_STEP = 2;

@Component({
	selector: 'app-home',
	templateUrl: './home.component.html',
	styleUrls: ['./home.component.scss']
})
export class HomeComponent implements OnInit {

	hasError = hasError;
	institution: InstitutionDto;
	ButtonType = ButtonType;

	address: GetSanitaryResponsibilityAreaInstitutionAddressDto;

	institutionAddressForm: FormGroup<InstitutionAddress>;
	institutionDescription: InstitutionDescription;

	states: TypeaheadOption<AddressProjection>[] = [];
	departments: TypeaheadOption<AddressProjection>[] = [];
	cities: TypeaheadOption<AddressProjection>[] = [];

	stateInitValue: TypeaheadOption<AddressProjection>;
	departmentInitValue: TypeaheadOption<AddressProjection>;
	cityInitValue: TypeaheadOption<AddressProjection>;

	stateCurrentValue: AddressProjection;
	departmentCurrentValue: AddressProjection;
	cityCurrentValue: AddressProjection;
	coordinatesCurrentValue: GlobalCoordinatesDto;

	showMap = false;
	editMode = false;
	stepperMode = false;
	isLoading = false;

	currentStepperIndex = INSTITUTION_ADDRESS_STEP;
	RESPONSABILITY_AREA_STEP = RESPONSABILITY_AREA_STEP;
	GEOPOSITION_STEP = GEOPOSITION_STEP;

	area: GlobalCoordinatesDto[] = [];

	constructor(private readonly gisService: GisService,
				private readonly addressMasterDataService: AddressMasterDataService,
				private readonly institutionService: InstitutionService,
				private readonly contextService: ContextService,
				private readonly snackBarService: SnackBarService,
				private readonly gisLayersService: GisLayersService) {}

	ngOnInit(): void {
		this.setUpInstitutionAddressForm();
		this.setInstitutionData();
	}

	setState = (state: AddressProjection) => {
		this.stateCurrentValue = state;
		this.institutionAddressForm.controls.stateId.setValue(state?.id);
		if (!state) {
			this.institutionAddressForm.controls.departmentId.setValue(null);
			this.departments = [];
			this.institutionAddressForm.controls.cityId.setValue(null);
			return this.cities = [];
		}
		this.setDepartments(state.id);
	}
	
	setDepartment = (department: AddressProjection) => {
		this.departmentCurrentValue = department;
		this.institutionAddressForm.controls.departmentId.setValue(department?.id);
		if (!department) {
			this.institutionAddressForm.controls.cityId.setValue(null);
			return this.cities = [];
		}
		this.setCities(department.id);
	}

	setCity = (city: AddressProjection) => {
		this.cityCurrentValue = city;
		this.institutionAddressForm.controls.cityId.setValue(city?.id);
	}

	changeStep = ($event) => {
		this.gisLayersService.toggleActions(false, false);
		this.gisLayersService.removeControls();
		this.gisLayersService.removeModifyInteraction();

		if ($event.selectedIndex === INSTITUTION_ADDRESS_STEP) 
			this.stepToInstitutionAddress();

		if ($event.selectedIndex === GEOPOSITION_STEP) 
			this.stepToMapPosition();

		if ($event.selectedIndex === RESPONSABILITY_AREA_STEP)
			this.stepToResponsabilityArea();
	}

	stepToInstitutionAddress = () => {
		this.institutionDescription = null;
		this.coordinatesCurrentValue = null;
		this.showMap = false;
		this.currentStepperIndex = INSTITUTION_ADDRESS_STEP;
	}

	stepToMapPosition = () => {
		this.gisLayersService.removeDrawnPolygon();
		this.currentStepperIndex = GEOPOSITION_STEP;
		this.isLoading = true;
		const address: string = this.toStringify();
		this.mapToInstitutionDescriptionPositionStep('gis.map-position.TITLE');
		this.gisService.getInstitutionCoordinatesFromAddress(address)
			.pipe(finalize(() => this.isLoading = false))
			.subscribe((coordinates: GlobalCoordinatesDto) => {
				this.coordinatesCurrentValue = coordinates;

				if (!coordinates) 
					return this.snackBarService.showError('gis.map-position.ERROR');

				this.showMap = true;
				this.mapToInstitutionDescriptionPositionStep('gis.map-position.TITLE');
				this.institutionAddressForm.controls.coordinates.setValue(transformCoordinates(coordinates));
			});
	}

	stepToResponsabilityArea = () => {
		this.currentStepperIndex = RESPONSABILITY_AREA_STEP;
	}

	setInstitutionData = () => {
		forkJoin([
			this.institutionService.getInstitutions([this.contextService.institutionId]),
			this.gisService.getInstitutionCoordinatesByInstitutionId(),
			this.gisService.getInstitutionAddressById(),
			this.gisService.getInstitutionArea()
		]).subscribe(([institutions, coordinates, address, area]: [InstitutionDto[], GlobalCoordinatesDto, GetSanitaryResponsibilityAreaInstitutionAddressDto, GlobalCoordinatesDto[]]) => {
			this.institution = institutions[0];
			this.area = area;
			this.coordinatesCurrentValue = coordinates;
			this.address = address;
			this.stepperMode = !this.hasCoordinates();
			this.showMap = this.hasCoordinates();
			if (this.hasCoordinates()) {
				this.mapToInstitutionDescriptionDetailed('gis.detailed-information.TITLE');
			} else {
				this.setInstitutionAddressFormData();
				this.setStates();
			}
		})
	}

	edit = () => {
		this.currentStepperIndex = INSTITUTION_ADDRESS_STEP;
		this.stepperMode = true;
		this.editMode = true;
		this.setInstitutionAddressFormData();
		this.setStates();
	}

	cancel = () => {
		this.editMode = false;
		this.stepperMode = false;
		this.setInstitutionData();
	}

	get street(): string {
		return this.institutionAddressForm.value.streetName;
	}

	get houseNumber(): string {
		return this.institutionAddressForm.value.houseNumber;
	}

	get stateId(): number {
		return this.institutionAddressForm.value.stateId;
	}

	get departmentId(): number {
		return this.institutionAddressForm.value.departmentId;
	}

	get cityId(): number {
		return this.institutionAddressForm.value.cityId;
	}

	private toStringify = (): string => {
		return JSON.stringify(
			{
				houseNumber: this.institutionAddressForm.value.houseNumber,
				streetName: this.institutionAddressForm.value.streetName,
				stateName: this.stateCurrentValue.description,
				cityName: this.cityCurrentValue.description
			}
		)
	}

	private setUpInstitutionAddressForm = () => {
		this.institutionAddressForm = new FormGroup<InstitutionAddress>({
			stateId: new FormControl(null, Validators.required),
			departmentId: new FormControl(null, Validators.required),
			cityId: new FormControl(null, Validators.required),
			streetName: new FormControl(null, Validators.required),
			houseNumber: new FormControl(null, Validators.required),
			coordinates: new FormControl({value: null, disabled: true})
		});
	}

	private hasCoordinates = (): boolean => {
		return (this.coordinatesCurrentValue?.latitude !== undefined && this.coordinatesCurrentValue?.longitude !== undefined);
	}

	private setInstitutionAddressFormData = () => {
		this.institutionAddressForm.controls.stateId.setValue(this.address.state.id);
		this.institutionAddressForm.controls.departmentId.setValue(this.address.department.id);
		this.institutionAddressForm.controls.cityId.setValue(this.address.city.id);
		this.institutionAddressForm.controls.streetName.setValue(this.address.streetName);
		this.institutionAddressForm.controls.houseNumber.setValue(this.address.houseNumber);
	}

	private mapToInstitutionDescriptionPositionStep = (title: string) => {
		this.institutionDescription = {
			title: title,
			institution: this.institution.name,
			address: {
				streetName: this.institutionAddressForm.value.streetName,
				houseNumber: this.institutionAddressForm.value.houseNumber,
				state: this.states.find(state => state.value.id === this.institutionAddressForm.value.stateId).value,
				department: this.departments.find(department => department.value.id === this.institutionAddressForm.value.departmentId).value,
				city: this.cities.find(city => city.value.id === this.institutionAddressForm.value.cityId).value,
			},
			coordinates: this.coordinatesCurrentValue,
		}
	}

	private mapToInstitutionDescriptionDetailed = (title: string) => {
		this.institutionDescription = {
			title: title,
			institution: this.institution.name,
			address: {
				streetName: this.address.streetName,
				houseNumber: this.address.houseNumber,
				state: this.address.state,
				department: this.address.department,
				city: this.address.city,
			},
			coordinates: this.coordinatesCurrentValue,
		}
	}

	private setStates = () => {
		this.addressMasterDataService.getByCountry(DEFAULT_COUNTRY_ID)
			.pipe(map(states => listToTypeaheadOptions(states, 'description')))
			.subscribe((states: TypeaheadOption<AddressProjection>[]) => {
				this.states = states;
				this.stateInitValue = this.setInitValue(this.states, this.institutionAddressForm.value.stateId);
			});
	}

	private setDepartments = (stateId: number) => {
		this.addressMasterDataService.getDepartmentsByProvince(stateId)
			.pipe(map(departments => listToTypeaheadOptions(departments, 'description')))
			.subscribe((departments: TypeaheadOption<AddressProjection>[]) => {
				this.departments = departments;
				this.departmentInitValue = this.setInitValue(this.departments, this.institutionAddressForm.value.departmentId);
			});
	}

	private setCities = (departmentId: number) => {
		this.addressMasterDataService.getAllCitiesByDepartment(departmentId)
			.pipe(map(cities => listToTypeaheadOptions(cities, 'description')))
			.subscribe((cities: TypeaheadOption<AddressProjection>[]) => {
				this.cities = cities;
				this.cityInitValue = this.setInitValue(this.cities, this.institutionAddressForm.value.cityId);
			});
	}

	private setInitValue = (list: TypeaheadOption<AddressProjection>[], id: number): TypeaheadOption<AddressProjection> => {
		return list.find(value => value.value.id === id);
	}
}