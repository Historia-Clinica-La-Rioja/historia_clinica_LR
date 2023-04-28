import { Component } from '@angular/core';
import { DoctorsOfficeDto, MasterDataInterface, ShockroomDto } from '@api-rest/api-model';
import { DoctorsOfficeService } from '@api-rest/services/doctors-office.service';
import { EmergencyCareMasterDataService } from '@api-rest/services/emergency-care-master-data.service';
import { hasError } from '@core/utils/form.utils';
import { AttentionPlace } from '@historia-clinica/constants/summaries';
import { Observable } from 'rxjs';
import { SECTOR_AMBULATORIO } from '../../constants/masterdata';
import { TypeaheadOption } from '@presentation/components/typeahead/typeahead.component';
import { map } from 'rxjs/operators';
import { MatDialogRef } from '@angular/material/dialog';
import { ShockroomService } from '@api-rest/services/shockroom.service';
import { AbstractControl, FormBuilder, FormGroup, Validators } from '@angular/forms';
import { AttendPlace } from '../../routes/home/home.component';

@Component({
	selector: 'app-attention-place-dialog',
	templateUrl: './attention-place-dialog.component.html',
	styleUrls: ['./attention-place-dialog.component.scss'],
})
export class AttentionPlaceDialogComponent {
	places$: Observable<MasterDataInterface<number>[]>;
	offices$: Observable<DoctorsOfficeDto[]>;
	officesTypeaheadOptions$: Observable<TypeaheadOption<DoctorsOfficeDto>[]>;
	shockrooms$: Observable<ShockroomDto[]>;
	shockroomsTypeaheadOptions$: Observable<TypeaheadOption<ShockroomDto>[]>
	form: FormGroup;
	hasError = hasError;

	constructor(
		private readonly emergencyCareMasterDataService: EmergencyCareMasterDataService,
		private readonly doctorsOfficeService: DoctorsOfficeService,
		private readonly shockroomService: ShockroomService,
		private readonly formBuilder: FormBuilder,
		private readonly dialogRef: MatDialogRef<AttentionPlaceDialogComponent>,
	) {
		this.places$ = this.emergencyCareMasterDataService.getEmergencyEpisodeSectorType();
		this.setForm();
	}

	verifyPlaceType() {
		this.clearData();
		const id: number = Number(this.form.get('place').value);
		if (id === AttentionPlace.CONSULTORIO) {
			const office: AbstractControl = this.form.get('office');
			this.offices$ = this.doctorsOfficeService.getBySectorType(SECTOR_AMBULATORIO);
			this.officesTypeaheadOptions$ = this.getOfficesTypeaheadOptions$();
			office.addValidators(Validators.required);
			office.updateValueAndValidity();
		}

		if (id === AttentionPlace.SHOCKROOM) {
			const shockroom: AbstractControl = this.form.get('shockroom');
			this.shockrooms$ = this.shockroomService.getShockrooms();
			this.shockroomsTypeaheadOptions$ = this.getShockroomsTypeaheadOptions$();
			shockroom.addValidators(Validators.required);
			shockroom.updateValueAndValidity();
		}
	}

	setOffice(value: Event) {
		this.form.get('office').setValue(value);
	}

	setShockroom(value: Event) {
		this.form.get('shockroom').setValue(value);
	}

	attend() {
		if (this.form.invalid) return;

		const id: number = Number(this.form.get('place').value);
		if (id === AttentionPlace.CONSULTORIO) {
			const attendPlace: AttendPlace = {
				id: this.form.get('office').value.id,
				attentionPlace: AttentionPlace.CONSULTORIO
			}
			this.dialogRef.close(attendPlace);
		}

		if (id === AttentionPlace.SHOCKROOM) {
			const attendPlace: AttendPlace = {
				id: this.form.get('shockroom').value.id,
				attentionPlace: AttentionPlace.SHOCKROOM
			}
			this.dialogRef.close(attendPlace);
		}
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

	private getShockroomsTypeaheadOptions$(): Observable<TypeaheadOption<ShockroomDto>[]> {
		return this.shockrooms$.pipe(map(toTypeaheadOptionList));
		function toTypeaheadOptionList(prosBySpecialtyList: ShockroomDto[]): TypeaheadOption<ShockroomDto>[] {
			return prosBySpecialtyList.map(toTypeaheadOption);

			function toTypeaheadOption(s: ShockroomDto): TypeaheadOption<ShockroomDto> {
				return {
					compareValue: s.description,
					value: s,
				};
			}
		}
	}

	private clearData() {
		this.offices$ = undefined;
		this.shockrooms$ = undefined;
		const office: AbstractControl = this.form.get('office');
		const shockroom: AbstractControl = this.form.get('shockroom');
		office.clearValidators();
		office.updateValueAndValidity();
		office.setValue(null);
		shockroom.clearValidators();
		shockroom.updateValueAndValidity();
		shockroom.setValue(null);
	}

	private setForm() {
		this.form = this.formBuilder.group({
			place: [null, Validators.required],
			office: [null],
			shockroom: [null]
		});
	}
}
