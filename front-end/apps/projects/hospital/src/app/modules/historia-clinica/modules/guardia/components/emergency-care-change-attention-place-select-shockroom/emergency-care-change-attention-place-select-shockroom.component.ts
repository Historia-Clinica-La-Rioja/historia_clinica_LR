import { Component, EventEmitter, Input, Output } from '@angular/core';
import { FormControl, FormGroup } from '@angular/forms';
import { ShockroomDto } from '@api-rest/api-model';
import { map, Observable } from 'rxjs';
import { EmergencyCareAttentionPlaceService } from '../../services/emergency-care-attention-place.service';

@Component({
	selector: 'app-emergency-care-change-attention-place-select-shockroom',
	templateUrl: './emergency-care-change-attention-place-select-shockroom.component.html',
	styleUrls: ['./emergency-care-change-attention-place-select-shockroom.component.scss']
})
export class EmergencyCareChangeAttentionPlaceSelectShockroomComponent{

	private _sectorId: number;
	form: FormGroup<ShockroomForm>;
	availableShockrooms$: Observable<ShockroomDto[]>;

	@Input() set sectorId(sectorId: number) {
		this._sectorId = sectorId;
		if (this._sectorId) {
			this.loadAvailableShockrooms();
		}
	}
	@Output() selectedShockroom = new EventEmitter<ShockroomDto>();
	@Output() noAvailableShockrooms = new EventEmitter<boolean>();

	constructor(
		private emergencyCareAttentionPlaceService: EmergencyCareAttentionPlaceService,
	) {
		this.createForm();
		this.subscribeToChangesAndEmit();
	}

	private loadAvailableShockrooms(){
		this.availableShockrooms$ = this.emergencyCareAttentionPlaceService.getFreeShockrooms(this._sectorId);
		this.availableShockrooms$.pipe(map(shockrooms => shockrooms.length === 0)).subscribe(isEmpty => this.noAvailableShockrooms.emit(isEmpty));
	}

	private createForm() {
		this.form = new FormGroup<ShockroomForm>({
			shockroom: new FormControl(null),
		});
	}

	private subscribeToChangesAndEmit() {
		this.form.controls.shockroom.valueChanges.subscribe(selectedShockroom => this.selectedShockroom.emit(selectedShockroom));
	}
}

interface ShockroomForm {
	shockroom: FormControl<ShockroomDto>;
}
