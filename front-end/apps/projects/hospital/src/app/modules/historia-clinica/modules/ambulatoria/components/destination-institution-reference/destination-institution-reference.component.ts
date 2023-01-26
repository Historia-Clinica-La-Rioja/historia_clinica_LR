import { Component, OnChanges, Input, Output, EventEmitter } from '@angular/core';
import { AddressDto, DepartmentDto, InstitutionBasicInfoDto } from '@api-rest/api-model';
import { AddressMasterDataService } from '@api-rest/services/address-master-data.service';
import { InstitutionService } from '@api-rest/services/institution.service';
import { TypeaheadOption } from '@presentation/components/typeahead/typeahead.component';

const COUNTRY = 14;

@Component({
    selector: 'app-destination-institution-reference',
    templateUrl: './destination-institution-reference.component.html',
    styleUrls: ['./destination-institution-reference.component.scss']
})
export class DestinationInstitutionReferenceComponent implements OnChanges {

    @Input() provinces: TypeaheadOption<any>[];
    @Input() submitForm: boolean;
    @Input() originInstitutionInfo: AddressDto;
    @Input() originDepartment: DepartmentDto;
    @Output() provinceSelected = new EventEmitter<number>();
    @Output() departmentSelected = new EventEmitter<number>();
    @Output() institutionSelected = new EventEmitter<number>();

    provinceValue: number;
    departments: TypeaheadOption<any>[];
    institutions: TypeaheadOption<any>[];
    defaultProvince: TypeaheadOption<any>;
    defaultDepartment: TypeaheadOption<any>;
    defaultInstitution: TypeaheadOption<any>;
    departmentDisable = false;
    originalDestinationDepartment: TypeaheadOption<any>;
    institutionDestinationId: number;

    constructor(
        private readonly institutionService: InstitutionService,
		private readonly adressMasterData: AddressMasterDataService) { }

    ngOnChanges(): void {
        if (!this.submitForm && this.provinces && this.originInstitutionInfo && this.originDepartment){
            this.loadDestinationInstitutionInformation();
        }
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
    
	private loadDestinationInstitutionInformation() {
		if (this.originInstitutionInfo?.provinceId) {
			this.setDestinationProvince(this.originInstitutionInfo.provinceId);
			this.setDefaultDestinationDepartment(this.originDepartment);
		}
		else {
			if (this.originInstitutionInfo?.departmentId) {
				if (this.originDepartment.provinceId) {
					this.setDestinationProvince(this.originDepartment.provinceId);
				}
				this.setDefaultDestinationDepartment(this.originDepartment);
			}
		}
	}

    private setDestinationProvince(provinceId: number) {
		const province = this.provinces.find(p => p.value === provinceId);
		this.defaultProvince = { value: province.value, compareValue: province.compareValue, viewValue: province.viewValue }
	}

    private setDefaultDestinationDepartment(department: DepartmentDto) {
		if (department) {
			this.originalDestinationDepartment = { value: department.id, viewValue: department.description, compareValue: department.description };
			this.departmentSelected.emit(department.id)
		}
	}

    private clearTypeahead() {
		return { value: null, viewValue: null, compareValue: null }
	}

	onProvinceSelectionChange(provinceId: number) {
        this.provinceValue = provinceId;
		this.defaultDepartment = this.clearTypeahead();
		this.setDepartmentsByProvince(provinceId);
        this.provinceSelected.emit(provinceId);
	}

    private setDepartmentsByProvince(provinceId: number) {
		if (provinceId) {
			this.adressMasterData.getDepartmentsByProvince(provinceId).subscribe(departments => {
				if (departments) {
					this.departmentDisable = false;
					this.departments = this.toTypeaheadOptions(departments, 'description');
					this.updateDepartmentValue();
				};
			})
		} else {
			this.departmentDisable = true;
		}
	}

	private updateDepartmentValue() {
		if (this.originalDestinationDepartment) {
			this.defaultDepartment = this.originalDestinationDepartment;
			this.originalDestinationDepartment = null;
		} else {
			this.defaultDepartment = this.clearTypeahead();
		}
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

    onInstitutionSelectionChange(institutionId: number){
        this.institutionDestinationId = institutionId;
        this.institutionSelected.emit(institutionId);
    }
}
