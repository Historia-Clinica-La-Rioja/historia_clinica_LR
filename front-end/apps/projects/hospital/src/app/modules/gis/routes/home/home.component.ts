import { Component, OnInit } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { GetSanitaryResponsibilityAreaInstitutionAddressDto, GlobalCoordinatesDto, InstitutionDto, SaveInstitutionAddressDto } from '@api-rest/api-model';
import { AddressMasterDataService, AddressProjection } from '@api-rest/services/address-master-data.service';
import { GisService } from '@api-rest/services/gis.service';
import { InstitutionService } from '@api-rest/services/institution.service';
import { ContextService } from '@core/services/context.service';
import { DEFAULT_COUNTRY_ID, hasError } from '@core/utils/form.utils';
import { ButtonType } from '@presentation/components/button/button.component';
import { TypeaheadOption } from '@presentation/components/typeahead/typeahead.component';
import { SnackBarService } from '@presentation/services/snack-bar.service';
import { listToTypeaheadOptions } from '@presentation/utils/typeahead.mapper.utils';
import { finalize, forkJoin, map } from 'rxjs';

interface InstitutionAddress {
    stateId: FormControl<number>;
    departmentId: FormControl<number>;
	cityId: FormControl<number>;
    streetName: FormControl<string>;
    houseNumber: FormControl<string>;
	coordinates: FormControl<string>;
}

const INSTITUTION_ADDRESS_STEP = 0;
const MAP_POSITION_INDEX = 1;

@Component({
	selector: 'app-home',
	templateUrl: './home.component.html',
	styleUrls: ['./home.component.scss']
})
export class HomeComponent implements OnInit {

	hasError = hasError;
	institution: InstitutionDto;
	showMap = false;
	ButtonType = ButtonType;
	institutionAddressForm: FormGroup<InstitutionAddress>;

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

	isLoading = false;
	isSaving = false;

	constructor(private readonly gisService: GisService,
				private readonly addressMasterDataService: AddressMasterDataService,
				private readonly institutionService: InstitutionService,
				private readonly contextService: ContextService,
				private readonly snackBarService: SnackBarService) {}

	ngOnInit(): void {
		this.setInstitution();
		this.setUpInstitutionAddressForm();
		this.setInstitutionData();
	}

	setState = (state: AddressProjection) => {
		this.stateCurrentValue = state;
		this.institutionAddressForm.controls.stateId.setValue(state?.id);
		if (!state) {
			this.institutionAddressForm.controls.departmentId.setValue(null);
			this.institutionAddressForm.controls.cityId.setValue(null);
			this.departments = [];
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
		if ($event.selectedIndex === INSTITUTION_ADDRESS_STEP) {
			this.showMap = false;
		}

		if ($event.selectedIndex === MAP_POSITION_INDEX) {
			this.stepToMapPosition();
		}
	}

	stepToMapPosition = () => {
		this.isLoading = true;
		const address: string = this.toStringify();
		this.gisService.getInstitutionCoordinatesFromAddress(address)
			.pipe(finalize(() => this.isLoading = false))
			.subscribe((coordinates: GlobalCoordinatesDto) => {
				this.coordinatesCurrentValue = coordinates;
				if (!coordinates) return;

				this.showMap = true;
				this.institutionAddressForm.controls.coordinates.setValue(`${coordinates.latitude}, ${coordinates.longitude}`);
			});
	}

	confirm = () => {
		this.isSaving = true;
		forkJoin(
			[
				this.gisService.saveInstitutionCoordinates(this.coordinatesCurrentValue),
				this.gisService.saveInstitutionAddress(this.mapToSaveInstitutionAddressDto())
			]
		).pipe(finalize(() => this.isSaving = false))
		.subscribe((_) => this.snackBarService.showSuccess("gis.status.UPDATE_DATA_SUCCESS"));
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

	private mapToSaveInstitutionAddressDto = (): SaveInstitutionAddressDto => {
		return {
			stateId: this.stateId,
			departmentId: this.departmentId,
			cityId: this.cityId,
			streetName: this.street,
			houseNumber: this.houseNumber
		}
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

	private setInstitution = () => {
		this.institutionService.getInstitutions([this.contextService.institutionId]).subscribe((institutions: InstitutionDto[]) => this.institution = institutions[0]);
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

	private setInstitutionData = () => {
		forkJoin([
			this.gisService.getInstitutionCoordinatesByInstitutionId(),
			this.gisService.getInstitutionAddressById()
		]).subscribe(([coordinates, address]: [GlobalCoordinatesDto, GetSanitaryResponsibilityAreaInstitutionAddressDto]) => {
			this.setInstitutionAddressFormData(address);


		})
	}

	private setInstitutionAddressFormData = (address: GetSanitaryResponsibilityAreaInstitutionAddressDto) => {
		this.setStates();
		this.institutionAddressForm.controls.stateId.setValue(address.stateId);
		this.institutionAddressForm.controls.departmentId.setValue(address.departmentId);
		this.institutionAddressForm.controls.cityId.setValue(address.cityId);
		this.institutionAddressForm.controls.streetName.setValue(address.streetName);
		this.institutionAddressForm.controls.houseNumber.setValue(address.houseNumber);
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
		this.addressMasterDataService.getCitiesByDepartment(departmentId)
			.pipe(map(cities => listToTypeaheadOptions(cities, 'description')))
			.subscribe((cities: TypeaheadOption<AddressProjection>[]) => {
				this.cities = cities;
				this.cityInitValue = this.setInitValue(this.cities, this.institutionAddressForm.value.cityId);
			});
	}

	private setInitValue = (list: TypeaheadOption<AddressProjection>[], id: number) => {
		return list.find(value => value.value.id === id);
	}
}