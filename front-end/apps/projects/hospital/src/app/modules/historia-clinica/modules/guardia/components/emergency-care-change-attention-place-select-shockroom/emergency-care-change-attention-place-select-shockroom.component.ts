import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { FormControl, FormGroup } from '@angular/forms';
import { ShockroomDto } from '@api-rest/api-model';
import { Observable } from 'rxjs';
import { EmergencyCareAttentionPlaceService } from '../../services/emergency-care-attention-place.service';

@Component({
	selector: 'app-emergency-care-change-attention-place-select-shockroom',
	templateUrl: './emergency-care-change-attention-place-select-shockroom.component.html',
	styleUrls: ['./emergency-care-change-attention-place-select-shockroom.component.scss']
})
export class EmergencyCareChangeAttentionPlaceSelectShockroomComponent implements OnInit {

	form: FormGroup<ShockroomForm>;
	availableShockrooms$: Observable<ShockroomDto[]>;

	@Input() sectorId: number;
	@Output() selectedShockroom = new EventEmitter<ShockroomDto>();

	constructor(
		private emergencyCareAttentionPlaceService: EmergencyCareAttentionPlaceService,
	) {
		this.createForm();
		this.subscribeToChangesAndEmit();
	}

	ngOnInit() {
		this.availableShockrooms$ = this.emergencyCareAttentionPlaceService.getFreeShockrooms(this.sectorId);
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
