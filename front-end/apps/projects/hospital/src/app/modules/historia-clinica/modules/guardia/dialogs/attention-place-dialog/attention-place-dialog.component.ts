import { Component } from '@angular/core';
import {
	AbstractControl,
	FormBuilder,
	FormGroup,
	Validators,
} from '@angular/forms';
import { DoctorsOfficeDto, MasterDataInterface } from '@api-rest/api-model';
import { DoctorsOfficeService } from '@api-rest/services/doctors-office.service';
import { EmergencyCareMasterDataService } from '@api-rest/services/emergency-care-master-data.service';
import { hasError } from '@core/utils/form.utils';
import { AttentionPlace } from '@historia-clinica/constants/summaries';
import { Observable } from 'rxjs';
import { SECTOR_AMBULATORIO } from '../../constants/masterdata';
import { TypeaheadOption } from '@presentation/components/typeahead/typeahead.component';
import { map } from 'rxjs/operators';
import { MatDialogRef } from '@angular/material/dialog';

@Component({
	selector: 'app-attention-place-dialog',
	templateUrl: './attention-place-dialog.component.html',
	styleUrls: ['./attention-place-dialog.component.scss'],
})
export class AttentionPlaceDialogComponent {
	places$: Observable<MasterDataInterface<number>[]>;
	offices$: Observable<DoctorsOfficeDto[]>;
	officesTypeaheadOptions$: Observable<TypeaheadOption<DoctorsOfficeDto>[]>;
	form: FormGroup;
	hasError = hasError;

	constructor(
		private readonly emergencyCareMasterDataService: EmergencyCareMasterDataService,
		private readonly doctorsOfficeService: DoctorsOfficeService,
		private readonly formBuilder: FormBuilder,
		private readonly dialogRef: MatDialogRef<AttentionPlaceDialogComponent>,
	) {
		this.places$ = this.emergencyCareMasterDataService.getEmergencyEpisodeSectorType();
		this.setForm();
	}

	verifyPlaceType() {
		this.clearData();
		const id: number = Number(this.form.get('place').value);
		const office: AbstractControl = this.form.get('office');
		if (id === AttentionPlace.CONSULTORIO) {
			this.offices$ = this.doctorsOfficeService.getBySectorType(SECTOR_AMBULATORIO);
			this.officesTypeaheadOptions$ = this.getOfficesTypeaheadOptions$();
			office.addValidators(Validators.required);
			office.updateValueAndValidity();
		}
	}

	setOffice(value: Event) {
		this.form.get('office').setValue(value);
	}

	attend() {
		if (this.form.invalid) return;

		const id: number = Number(this.form.get('place').value);
		if (id === AttentionPlace.CONSULTORIO)
			this.dialogRef.close(this.form.get('office').value);
	}

	private getOfficesTypeaheadOptions$(): Observable<TypeaheadOption<DoctorsOfficeDto>[]> {
		return this.offices$.pipe(map(toTypeaheadOptionList));
		function toTypeaheadOptionList(prosBySpecialtyList: DoctorsOfficeDto[]): TypeaheadOption<DoctorsOfficeDto>[] {
			return prosBySpecialtyList.map(toTypeaheadOption);

			function toTypeaheadOption(s: DoctorsOfficeDto): TypeaheadOption<DoctorsOfficeDto> {
				return {
					compareValue: s.description,
					value: s,
				};
			}
		}
	}

	private clearData() {
		this.offices$ = undefined;
		const office: AbstractControl = this.form.get('office');
		office.clearValidators();
		office.updateValueAndValidity();
		office.setValue(null);
	}

	private setForm() {
		this.form = this.formBuilder.group({
			place: [null, Validators.required],
			office: [null],
		});
	}
}
