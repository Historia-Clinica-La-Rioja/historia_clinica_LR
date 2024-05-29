import { Component, OnInit } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { GetSanitaryResponsibilityAreaInstitutionAddressDto, GlobalCoordinatesDto, InstitutionDto } from '@api-rest/api-model';
import { AddressMasterDataService, AddressProjection } from '@api-rest/services/address-master-data.service';
import { GisService } from '@api-rest/services/gis.service';
import { InstitutionService } from '@api-rest/services/institution.service';
import { ContextService } from '@core/services/context.service';
import { DEFAULT_COUNTRY_ID, hasError } from '@core/utils/form.utils';
import { TypeaheadOption } from '@presentation/components/typeahead/typeahead.component';
import { listToTypeaheadOptions } from '@presentation/utils/typeahead.mapper.utils';
import { forkJoin, map } from 'rxjs';

interface InstitutionAddress {
    stateId: FormControl<number>;
    departmentId: FormControl<number>;
	cityId: FormControl<number>;
    streetName: FormControl<string>;
    houseNumber: FormControl<string>;
}

@Component({
	selector: 'app-home',
	templateUrl: './home.component.html',
	styleUrls: ['./home.component.scss']
})
export class HomeComponent implements OnInit {

	hasError = hasError;
	institution: InstitutionDto;
	showMap = false;
	institutionAddressForm: FormGroup<InstitutionAddress>;
	states: TypeaheadOption<AddressProjection>[] = [];
	departments: TypeaheadOption<AddressProjection>[] = [];
	cities: TypeaheadOption<AddressProjection>[] = [];
	stateInitValue: TypeaheadOption<AddressProjection>;
	departmentInitValue: TypeaheadOption<AddressProjection>;
	cityInitValue: TypeaheadOption<AddressProjection>;

	constructor(private readonly gisService: GisService,
				private readonly addressMasterDataService: AddressMasterDataService,
				private readonly institutionService: InstitutionService,
				private readonly contextService: ContextService) {}

	ngOnInit(): void {
		this.setInstitution();
		this.setUpInstitutionAddressForm();
		this.setInstitutionData();
	}

	setState = (state: AddressProjection) => {
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
		this.institutionAddressForm.controls.departmentId.setValue(department?.id);
		if (!department) {
			this.institutionAddressForm.controls.cityId.setValue(null);
			return this.cities = [];
		}
		this.setCities(department.id);
	}

	setCity = (city: AddressProjection) => {
		this.institutionAddressForm.controls.cityId.setValue(city?.id);
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
				this.stateInitValue = this.states.find(state => state.value.id === this.institutionAddressForm.value.stateId);
			});
	}

	private setDepartments = (stateId: number) => {
		this.addressMasterDataService.getDepartmentsByProvince(stateId)
			.pipe(map(departments => listToTypeaheadOptions(departments, 'description')))
			.subscribe((departments: TypeaheadOption<AddressProjection>[]) => {
				this.departments = departments;
				this.departmentInitValue = this.departments.find(department => department.value.id === this.institutionAddressForm.value.departmentId);
			});
	}

	private setCities = (departmentId: number) => {
		this.addressMasterDataService.getCitiesByDepartment(departmentId)
			.pipe(map(cities => listToTypeaheadOptions(cities, 'description')))
			.subscribe((cities: TypeaheadOption<AddressProjection>[]) => {
				this.cities = cities;
				this.cityInitValue = this.cities.find(city => city.value.id === this.institutionAddressForm.value.cityId);
			});
	}
}