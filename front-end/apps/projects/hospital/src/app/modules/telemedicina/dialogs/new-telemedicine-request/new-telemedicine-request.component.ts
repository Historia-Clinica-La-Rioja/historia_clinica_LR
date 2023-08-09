import { Component, OnInit, ViewChild } from '@angular/core';
import { UntypedFormBuilder, UntypedFormGroup, Validators } from '@angular/forms';
import { MatStepper } from '@angular/material/stepper';
import { PersonPhotoDto } from '@api-rest/api-model';
import { Patient } from '@pacientes/component/search-patient/search-patient.component';
import { PatientBasicData } from '@presentation/components/patient-card/patient-card.component';
import { MapperService } from '@presentation/services/mapper.service';

@Component({
	selector: 'app-new-telemedicine-request',
	templateUrl: './new-telemedicine-request.component.html',
	styleUrls: ['./new-telemedicine-request.component.scss']
})
export class NewTelemedicineRequestComponent implements OnInit {
	@ViewChild('stepper', { static: false }) stepper: MatStepper;
	requestInformationData: any;
	selectedPatient: PatientBasicData;
	photoSelectedPatient: PersonPhotoDto;
	firstStepForm: UntypedFormGroup;
	secondStepForm: UntypedFormGroup;

	constructor(private mapperService: MapperService, private readonly formBuilder: UntypedFormBuilder) { }

	ngOnInit(): void {
		this.firstStepForm = this.formBuilder.group({
			selectedPatient: ['', Validators.required],
		});
		this.secondStepForm = this.formBuilder.group({
			secondCtrl: ['', Validators.required],
		});
	}

	onSelectedPatient(selectedPatient: Patient) {
		if (selectedPatient) {
			this.firstStepForm.controls.selectedPatient.setValue(this.mapperService.toPatientBasicData(selectedPatient.basicData));
			this.photoSelectedPatient = selectedPatient.photo;
			this.stepper.next();
		} else {
			this.firstStepForm.controls.selectedPatient.setValue(null);
			this.photoSelectedPatient = null;
		}

	}
}
