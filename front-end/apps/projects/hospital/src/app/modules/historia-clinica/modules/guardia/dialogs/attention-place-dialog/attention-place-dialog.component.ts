import { Component, Inject, OnInit } from '@angular/core';
import { AttentionPlacesQuantityDto, DoctorsOfficeDto, MasterDataInterface, ShockroomDto } from '@api-rest/api-model';
import { DoctorsOfficeService } from '@api-rest/services/doctors-office.service';
import { EmergencyCareMasterDataService } from '@api-rest/services/emergency-care-master-data.service';
import { hasError } from '@core/utils/form.utils';
import { AttentionPlace } from '@historia-clinica/constants/summaries';
import { Observable, of } from 'rxjs';
import { TypeaheadOption } from '@presentation/components/typeahead/typeahead.component';
import { map } from 'rxjs/operators';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { ShockroomService } from '@api-rest/services/shockroom.service';
import { AbstractControl, FormBuilder, FormGroup, Validators } from '@angular/forms';
import { AttendPlace } from '../../components/emergency-care-dashboard-actions/emergency-care-dashboard-actions.component';
import { EstadosEpisodio, SECTOR_GUARDIA } from '../../constants/masterdata';
import { EmergencyCareStateChangedService } from '@historia-clinica/modules/ambulatoria/services/emergency-care-state-changed.service';
import { AttentionPlaceData } from '@historia-clinica/services/emergency-care-episode-call-or-attend.service';
import { objectToTypeaheadOption } from '@presentation/utils/typeahead.mapper.utils';

const CONFIRM: ButtonData = {
	text: 'guardia.dialog.attention_place.CONFIRM',
	id: 'confirm'
}

const BED_ASSIGN = {
	text: 'guardia.dialog.attention_place.BED_ASSIGN',
	id: 'bed-assign'
}
@Component({
	selector: 'app-attention-place-dialog',
	templateUrl: './attention-place-dialog.component.html',
	styleUrls: ['./attention-place-dialog.component.scss'],
})
export class AttentionPlaceDialogComponent implements OnInit {

	dialogTitle: string;
	places$: Observable<MasterDataInterface<number>[]>;
	offices$: Observable<DoctorsOfficeDto[]>;
	officesTypeaheadOptions$: Observable<TypeaheadOption<DoctorsOfficeDto>[]>;
	shockrooms$: Observable<ShockroomDto[]>;
	shockroomsTypeaheadOptions$: Observable<TypeaheadOption<ShockroomDto>[]>
	currentButton: ButtonData = CONFIRM;
	form: FormGroup;
	hasError = hasError;

	preselectedOffice: TypeaheadOption<DoctorsOfficeDto>;
	preselectedShockroom: TypeaheadOption<ShockroomDto>;

	constructor(
		private readonly emergencyCareMasterDataService: EmergencyCareMasterDataService,
		private readonly doctorsOfficeService: DoctorsOfficeService,
		private readonly shockroomService: ShockroomService,
		private readonly formBuilder: FormBuilder,
		private readonly dialogRef: MatDialogRef<AttentionPlaceDialogComponent>,
		@Inject(MAT_DIALOG_DATA) public data: {
			quantity: AttentionPlacesQuantityDto,
			isCall: boolean,
			lastAttentionPlace: AttentionPlaceData },
		private readonly emergencyCareStateChangedService: EmergencyCareStateChangedService
	) { }

	ngOnInit(): void {
		this.setDialogTittle();
		this.places$ = this.emergencyCareMasterDataService.getEmergencyEpisodeSectorType();
		this.filterPlaces();
		this.setForm();
		this.verifyPlaceType();
	}

	private setDialogTittle(){
		this.dialogTitle = this.data.isCall
            ? 'guardia.dialog.attention_place.TITLE_CALL'
            : 'guardia.dialog.attention_place.TITLE_ATTEND';
	}

	verifyPlaceType() {
		this.clearData();
		const id: number = Number(this.form.get('place').value);
		if (id === AttentionPlace.CONSULTORIO) {
			this.offices$ = this.doctorsOfficeService.getBySectorType(SECTOR_GUARDIA);
			const office: AbstractControl = this.form.get('office');
			this.officesTypeaheadOptions$ = this.getTypeaheadOptions$(this.offices$);
			office.addValidators(Validators.required);
			office.updateValueAndValidity();
			this.offices$.subscribe(offices => {
				if (this.data.lastAttentionPlace?.doctorsOfficeId) {
					const selectedOffice = offices.find(office => office.id === this.data.lastAttentionPlace.doctorsOfficeId);
					this.form.get('office').setValue(selectedOffice);
					this.preselectedOffice = objectToTypeaheadOption(selectedOffice, 'description');
				}
			});
		}

		if (id === AttentionPlace.SHOCKROOM) {
			const shockroom: AbstractControl = this.form.get('shockroom');
			this.shockrooms$ = this.shockroomService.getShockrooms();
			this.shockroomsTypeaheadOptions$ = this.getTypeaheadOptions$(this.shockrooms$);
			shockroom.addValidators(Validators.required);
			shockroom.updateValueAndValidity();
			this.shockrooms$.subscribe(shockrooms => {
				if (this.data.lastAttentionPlace?.shockroomId) {
					const selectedShockroom = shockrooms.find(shockroom => shockroom.id === this.data.lastAttentionPlace.shockroomId);
					this.form.get('shockroom').setValue(selectedShockroom);
					this.preselectedShockroom = objectToTypeaheadOption(selectedShockroom, 'description');
				}
			});
		}

		if (id === AttentionPlace.HABITACION)
			this.currentButton = BED_ASSIGN;
	}

	setOffice(value: Event) {
		this.form.get('office').setValue(value);
	}

	setShockroom(value: Event) {
		this.form.get('shockroom').setValue(value);
	}

	confirm() {
		if (this.form.invalid) return;

		const id: number = Number(this.form.get('place').value);
		let attendPlace: AttendPlace;
		if (id === AttentionPlace.CONSULTORIO) {
			attendPlace = {
				id: this.form.get('office').value.id,
				attentionPlace: AttentionPlace.CONSULTORIO
			}
		}
		if (id === AttentionPlace.SHOCKROOM) {
			attendPlace = {
				id: this.form.get('shockroom').value.id,
				attentionPlace: AttentionPlace.SHOCKROOM
			}
		}
		if (id === AttentionPlace.HABITACION) {
			attendPlace = {
				id: null,
				attentionPlace: AttentionPlace.HABITACION
			}
		}
		this.dialogRef.close(attendPlace);

		this.data.isCall
		? this.emergencyCareStateChangedService.emergencyCareStateChanged(EstadosEpisodio.LLAMADO)
		: this.emergencyCareStateChangedService.emergencyCareStateChanged(EstadosEpisodio.EN_ATENCION)
	}

	private filterPlaces() {
		this.places$.subscribe((places: MasterDataInterface<number>[]) => {
			if (this.data.quantity.doctorsOffice == 0)
				this.deletePlace(AttentionPlace.CONSULTORIO, places);

			if (this.data.quantity.shockroom == 0)
				this.deletePlace(AttentionPlace.SHOCKROOM, places);

			if (this.data.quantity.bed == 0)
				this.deletePlace(AttentionPlace.HABITACION, places);

			this.places$ = of(places);
		});
	}

	private deletePlace(attentionPlace: AttentionPlace, places: MasterDataInterface<number>[]) {
		const index: number = places.indexOf(places.find(val => val.id == attentionPlace));
		places.splice(index, 1);
	}

	private getTypeaheadOptions$(attentionPlace$): Observable<TypeaheadOption<any>[]> {
		return attentionPlace$.pipe(map(toTypeaheadOptionList));
		function toTypeaheadOptionList(list: any[]): TypeaheadOption<any>[] {
			return list.map(toTypeaheadOption);

			function toTypeaheadOption(item: any): TypeaheadOption<any> {
				return {
					compareValue: !item.available ? `${item.description} - OCUPADO` : item.description,
					value: item,
					disabled: !item.available
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
		this.currentButton = CONFIRM;
	}

	private setForm() {
		this.form = this.formBuilder.group({
			place: [this.data.lastAttentionPlace?.place || null, Validators.required],
			office: [this.data.lastAttentionPlace?.doctorsOfficeId || null],
			shockroom: [this.data.lastAttentionPlace?.shockroomId || null]
		});
	}
}

interface ButtonData {
	text: string,
	id: string
}
