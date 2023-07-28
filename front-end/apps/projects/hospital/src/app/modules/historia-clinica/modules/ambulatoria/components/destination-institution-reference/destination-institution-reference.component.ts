import { Component, Input, Output, EventEmitter, OnInit } from '@angular/core';
import { AddressDto, DepartmentDto, InstitutionBasicInfoDto } from '@api-rest/api-model';
import { AddressMasterDataService } from '@api-rest/services/address-master-data.service';
import { InstitutionService } from '@api-rest/services/institution.service';
import { TypeaheadOption } from '@presentation/components/typeahead/typeahead.component';
import { ReferenceOriginInstitutionService } from '../../services/reference-origin-institution.service';
import { DiaryAvailableAppointmentsSearchService } from '@turnos/services/diary-available-appointments-search.service';
import { BehaviorSubject } from 'rxjs';

@Component({
	selector: 'app-destination-institution-reference',
	templateUrl: './destination-institution-reference.component.html',
	styleUrls: ['./destination-institution-reference.component.scss']
})
export class DestinationInstitutionReferenceComponent implements OnInit {

	@Input() submitForm: boolean;
	@Input() provinceId: number;
	@Input() set careLine(careLineId: number) {

		if (careLineId && this.specialtyId) {
			this.adressMasterData.getActiveDiariesInInstitutionByClinicalSpecialty(this.provinceId, careLineId, this.specialtyId).subscribe(e => {
				this.careLineId = careLineId;
				this.departments = this.toTypeaheadOptions(e, 'description');
			});
		}
		this.onSpecialtySelectionChange();

	};

	@Input() set clinicalSpecialty(specialty: number) {
		this.specialtyId = specialty;
		if (specialty) {
			this.adressMasterData.getDepartmentsBySpecialy(this.provinceId, specialty).subscribe(e => {
				this.departmentDisable = false;
				this.departments = this.toTypeaheadOptions(e, 'description');
			});
			this.adressMasterData.getDepartmentsByProvinceHavingClinicalSpecialty(this.provinceId, specialty).subscribe(e =>
				this.institutions = this.toTypeaheadOptions(e, 'description')
			)

		}
	};

	@Input() set updateFormFields(updateDepartamentsAndInstitution: boolean) {
		if (updateDepartamentsAndInstitution) {
			this.onSpecialtySelectionChange();

		}
	}

	@Output() departmentSelected = new EventEmitter<number>();
	@Output() institutionSelected = new EventEmitter<number>();
	@Output() resetControls = new EventEmitter();

	provinceValue: number;
	departments: TypeaheadOption<any>[];
	institutions: TypeaheadOption<any>[];
	defaultProvince: TypeaheadOption<any>;
	defaultDepartment: TypeaheadOption<any>;
	defaultInstitution: TypeaheadOption<any>;
	departmentDisable = false;
	originalDestinationDepartment: TypeaheadOption<any>;
	institutionDestinationId: number;
	originDepartment: DepartmentDto;
	originInstitutionInfo: AddressDto;
	provinces: TypeaheadOption<any>[];
	specialtyId: number;

	day = new BehaviorSubject<number>(0);
	isDayGreaterThanZero = false;
	institutionSelection = false;
	careLineId: number;
	dis = true;
	constructor(
		private readonly institutionService: InstitutionService,
		private readonly adressMasterData: AddressMasterDataService,
		private readonly referenceOriginInstitutionService: ReferenceOriginInstitutionService,
		private readonly diaryAvailableAppointmentsSearchService: DiaryAvailableAppointmentsSearchService,
	) { }

	ngOnInit(): void {
		this.institutionSelection = false;

		this.referenceOriginInstitutionService.originInstitutionInfo$.subscribe(info => this.originInstitutionInfo = info);
		this.day.subscribe((value) => {
			this.isDayGreaterThanZero = value > 0;
		});
	}

	private getAllInstitutions() {
		this.institutionService.getAllInstitutions().subscribe((institutions: InstitutionBasicInfoDto[]) => {
			this.institutions = this.toTypeaheadOptions(institutions, 'name');
		});
	}

	private toTypeaheadOptions(response: any[], attribute: string): TypeaheadOption<any>[] {
		return response.map(r => {
			return {
				value: r.id,
				compareValue: r[attribute],
				viewValue: r[attribute]
			}
		})
	}

	private clearTypeahead() {
		return { value: null, viewValue: null, compareValue: null }
	}


	onSpecialtySelectionChange() {
		this.defaultDepartment = this.clearTypeahead();
		this.defaultInstitution = this.clearTypeahead();
		this.departmentSelected.emit(null);
		this.institutionSelected.emit(null);
		;
	}
	onDepartmentSelectionChange(departmentId: number) {
		this.defaultInstitution = this.clearTypeahead();
		if (departmentId) {
			this.institutionService.findByDepartmentId(departmentId).subscribe((institutions: InstitutionBasicInfoDto[]) => {
				this.institutions = this.toTypeaheadOptions(institutions, 'name');
			});
		} else {
			this.setInstitutions();
		}
		this.departmentSelected.emit(departmentId);
	}

	private setInstitutionsByProvince(province: number) {
		this.institutionService.findByProvinceId(province).subscribe((institutions: InstitutionBasicInfoDto[]) => {
			this.institutions = this.toTypeaheadOptions(institutions, 'name');
		});
	}

	private setInstitutions() {
		if (this.provinceValue) {
			this.setInstitutionsByProvince(this.provinceValue);
		} else {
			this.getAllInstitutions();
		}
	}

	onInstitutionSelectionChange(institutionId: number) {
		if (institutionId) {
			this.institutionSelection = true;
			this.diaryAvailableAppointments(institutionId);
		} else {
			this.institutionSelection = false;
		}
		this.institutionDestinationId = institutionId;
		this.institutionSelected.emit(institutionId);
	}

	diaryAvailableAppointments(institutionId: number) {
		this.diaryAvailableAppointmentsSearchService.getActiveDiariesInInstitutionByClinicalSpecialty(institutionId, this.specialtyId, this.careLineId).subscribe(e =>
			this.day.next(e)
		);
	}

}
