import { DestinationInstitutionInformationService } from '@access-management/services/destination-institution-information.service';
import { toAdressProjection } from '@access-management/utils/reference-edition.utils';
import { Component, EventEmitter, Input, Output } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { InstitutionBasicInfoDto, ReferenceInstitutionDto } from '@api-rest/api-model';
import { AddressProjection } from '@api-rest/services/address-master-data.service';
import { TypeaheadOption } from '@presentation/components/typeahead/typeahead.component';
import { objectToTypeaheadOption } from '@presentation/utils/typeahead.mapper.utils';
import { Observable } from 'rxjs';

@Component({
	selector: 'app-destination-institution-information',
	templateUrl: './destination-institution-information.component.html',
	styleUrls: ['./destination-institution-information.component.scss'],
	providers: [DestinationInstitutionInformationService]
})
export class DestinationInstitutionInformationComponent {

	_destinationInstitutionInfo: DestinationInstitutionInformation;
	form: FormGroup<DestinationInstitutionInformationForm>;
	departments$: Observable<TypeaheadOption<AddressProjection>[]>;
	institutions$: Observable<TypeaheadOption<InstitutionBasicInfoDto>[]>;
	defaultDepartment: TypeaheadOption<AddressProjection>;
	defaultInstitution: TypeaheadOption<InstitutionBasicInfoDto>;
	institutionsDisable = false;

	@Input() submitForm: boolean;
	@Input()
	set destinationInstitutionInfo(destinationInstitutionInfo: DestinationInstitutionInformation) {
		this._destinationInstitutionInfo = destinationInstitutionInfo;
		if (destinationInstitutionInfo.referenceInstitution?.id)
			this.setDestinationDataForm();
		this.setDepartmentsTypeaheadOption();
	};
	@Output() institutionIdSelected = new EventEmitter<number>();

	constructor(
		private readonly destinationInstitutionInformationService: DestinationInstitutionInformationService,
	) {
		this.createForm();
		this.subscribeToFormChangesAndEmit();
	}

	setInstitutionsByDepartment(department: AddressProjection) {
		this.form.controls.department.setValue(department?.id);
		if (department) {
			this.setInstitutionsTypeaheadOption();
			this.institutionsDisable = false;
		}
		else
			this.resetInstitutionsControls();
	}

	setInstitutionAndLoadAvailableAppointments(institution: InstitutionBasicInfoDto) {
		this.form.controls.institution.setValue(institution?.id);
		this.updateDestinationInstitutionInformation();
	}

	private createForm() {
		this.form = new FormGroup<DestinationInstitutionInformationForm>({
			department: new FormControl(null, Validators.required),
			institution: new FormControl(null, Validators.required),
		});
	}

	private subscribeToFormChangesAndEmit() {
		this.form.controls.institution.valueChanges.subscribe(institutionId => this.institutionIdSelected.emit(institutionId));
	}

	private setDestinationDataForm() {
		this.setDefaultDepartment();
		this.setDefaultInstitution();
	}

	private setDefaultDepartment() {
		const adressProjectionDepartment = toAdressProjection(this._destinationInstitutionInfo.referenceInstitution.departmentId, this._destinationInstitutionInfo.referenceInstitution.departmentName);
		this.defaultDepartment = objectToTypeaheadOption(adressProjectionDepartment, 'description');
		this.form.controls.department.setValue(this._destinationInstitutionInfo.referenceInstitution.departmentId);
	}

	private setDefaultInstitution() {
		const institution = toInstitutionBasicInfoDto(this._destinationInstitutionInfo.referenceInstitution.id, this._destinationInstitutionInfo.referenceInstitution.description);
		this.defaultInstitution = objectToTypeaheadOption(institution, 'name');
		this.form.controls.institution.setValue(this._destinationInstitutionInfo.referenceInstitution.id);
	}

	private setDepartmentsTypeaheadOption() {
		this.departments$ = this.destinationInstitutionInformationService.getDepartmentsByDestinationDataFilter(this._destinationInstitutionInfo.practiceId, this._destinationInstitutionInfo.clinicalSpecialtiesIds, this._destinationInstitutionInfo.careLineId)
	}

	private setInstitutionsTypeaheadOption() {
		this.institutions$ = this.destinationInstitutionInformationService.getInstitutionsTypeaheadOptionsByDepartment(this.form.value.department, this._destinationInstitutionInfo.practiceId, this._destinationInstitutionInfo.clinicalSpecialtiesIds, this._destinationInstitutionInfo.careLineId)
	}

	private resetInstitutionsControls() {
		this.form.controls.institution.setValue(null);
		this.defaultInstitution = undefined;
		this.institutionsDisable = true;
	}

	private updateDestinationInstitutionInformation() {
		this._destinationInstitutionInfo = {
			...this._destinationInstitutionInfo,
			referenceInstitution: {
				...this._destinationInstitutionInfo.referenceInstitution,
				id: this.form.value.institution,
				departmentId: this.form.value.department
			}
		}
	}

}

export interface DestinationInstitutionInformation {
	referenceInstitution: ReferenceInstitutionDto;
	careLineId: number;
	practiceId: number;
	clinicalSpecialtiesIds: number[];
}

interface DestinationInstitutionInformationForm {
	department: FormControl<number>;
	institution: FormControl<number>;
}


function toInstitutionBasicInfoDto(id: number, name: string): InstitutionBasicInfoDto {
	return { id, name }
}