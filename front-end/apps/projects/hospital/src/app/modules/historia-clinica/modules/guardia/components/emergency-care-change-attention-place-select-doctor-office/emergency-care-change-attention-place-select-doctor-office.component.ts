import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { FormControl, FormGroup } from '@angular/forms';
import { map, Observable } from 'rxjs';
import { EmergencyCareAttentionPlaceService } from '../../services/emergency-care-attention-place.service';
import { EmergencyCareDoctorsOfficeDto } from '@api-rest/api-model';

@Component({
	selector: 'app-emergency-care-change-attention-place-select-doctor-office',
	templateUrl: './emergency-care-change-attention-place-select-doctor-office.component.html',
	styleUrls: ['./emergency-care-change-attention-place-select-doctor-office.component.scss']
})
export class EmergencyCareChangeAttentionPlaceSelectDoctorOfficeComponent implements OnInit {

	form: FormGroup<DoctorOfficeForm>;
	availableDoctorsOffices$: Observable<EmergencyCareDoctorsOfficeDto[]>;

	@Input() sectorId: number;
	@Output() selectedDoctorOffice = new EventEmitter<EmergencyCareDoctorsOfficeDto>();
	@Output() noAvailableDoctorOffices = new EventEmitter<boolean>();

	constructor(
		private emergencyCareAttentionPlaceService: EmergencyCareAttentionPlaceService,
	) {
		this.createForm();
		this.subscribeToChangesAndEmit();
	}

	ngOnInit() {
		this.availableDoctorsOffices$ = this.emergencyCareAttentionPlaceService.getFreeDoctorsOffices(this.sectorId);
		this.availableDoctorsOffices$.pipe(map(doctorsOffices => doctorsOffices.length === 0)).subscribe(isEmpty => this.noAvailableDoctorOffices.emit(isEmpty));
	}

	private createForm() {
		this.form = new FormGroup<DoctorOfficeForm>({
			doctorOffice: new FormControl(null),
		});
	}

	private subscribeToChangesAndEmit() {
		this.form.controls.doctorOffice.valueChanges.subscribe(selectedDoctorOffice => this.selectedDoctorOffice.emit(selectedDoctorOffice));
	}
}

interface DoctorOfficeForm {
	doctorOffice: FormControl<EmergencyCareDoctorsOfficeDto>;
}
