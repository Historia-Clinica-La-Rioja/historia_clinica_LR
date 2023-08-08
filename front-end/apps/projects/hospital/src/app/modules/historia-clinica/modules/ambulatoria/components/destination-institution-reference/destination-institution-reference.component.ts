import { Component, Input, Output, EventEmitter, OnInit } from '@angular/core';
import { AddressDto, DepartmentDto, InstitutionBasicInfoDto } from '@api-rest/api-model';
import { AddressMasterDataService } from '@api-rest/services/address-master-data.service';
import { InstitutionService } from '@api-rest/services/institution.service';
import { TypeaheadOption } from '@presentation/components/typeahead/typeahead.component';
import { ReferenceOriginInstitutionService } from '../../services/reference-origin-institution.service';
import { UntypedFormGroup } from '@angular/forms';

@Component({
	selector: 'app-destination-institution-reference',
	templateUrl: './destination-institution-reference.component.html',
	styleUrls: ['./destination-institution-reference.component.scss']
})
export class DestinationInstitutionReferenceComponent implements OnInit {
	@Input() formReference: UntypedFormGroup;
	@Input() submitForm: boolean;
	@Input() set careLine(careLineId: number) {
		this.specialtyId = null;
		this.careLineId = careLineId;
		this.setDepartaments();
		this.onSpecialtySelectionChange();
	};

	@Input() set clinicalSpecialty(specialty: number) {
		this.specialtyId = null;
		this.specialtyId = specialty;
		this.setDepartaments();
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
	departmentDisable = true;
	institutionsDisable = true;
	originalDestinationDepartment: TypeaheadOption<any>;
	institutionDestinationId: number;
	departmentId: number;
	originDepartment: DepartmentDto;
	originInstitutionInfo: AddressDto;
	provinces: TypeaheadOption<any>[];
	specialtyId: number;
	isDayGreaterThanZero = false;
	institutionSelection = false;
	careLineId: number;

	constructor(
		private readonly institutionService: InstitutionService,
		private readonly adressMasterData: AddressMasterDataService,
		private readonly referenceOriginInstitutionService: ReferenceOriginInstitutionService,
	) { }

	ngOnInit(): void {
		this.institutionSelection = false;

		this.referenceOriginInstitutionService.originInstitutionInfo$.subscribe(info => this.originInstitutionInfo = info);
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
			this.institutionService.getInstitutionsByDepartmentHavingClinicalSpecialty(departmentId, this.specialtyId, this.careLineId).subscribe((institutions: InstitutionBasicInfoDto[]) => {
				this.institutions = this.toTypeaheadOptions(institutions, 'name');
				this.institutionsDisable = false;
			});
		}
		this.departmentId = departmentId;
		this.departmentSelected.emit(departmentId);
	}

	onInstitutionSelectionChange(institutionId: number) {
		if (institutionId) {
			this.institutionSelection = true;
		} else {
			this.institutionSelection = false;
		}
		this.institutionDestinationId = institutionId;
		this.institutionSelected.emit(institutionId);
	}

	private setDepartaments() {
		this.cleanFields();
		if (this.specialtyId) {
			this.adressMasterData.getDepartmentsForReference(this.specialtyId, this.careLineId).subscribe(e => {
				this.departments = this.toTypeaheadOptions(e, 'description');
				this.departmentDisable = false;
			});
		}
	}

	private cleanFields() {
		this.defaultInstitution = this.clearTypeahead();
		this.defaultDepartment = this.clearTypeahead();
		this.departmentDisable = true;
		this.institutionsDisable = true;
		this.institutionSelection = false;
	}

}
