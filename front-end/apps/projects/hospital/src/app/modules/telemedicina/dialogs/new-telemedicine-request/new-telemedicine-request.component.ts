import { Component, OnInit, ViewChild } from '@angular/core';
import { UntypedFormBuilder, UntypedFormGroup, Validators } from '@angular/forms';
import { MatDialogRef } from '@angular/material/dialog';
import { MatStepper } from '@angular/material/stepper';
import { PersonPhotoDto, VirtualConsultationRequestDto } from '@api-rest/api-model';
import { VirtualConstultationService } from '@api-rest/services/virtual-constultation.service';
import { ContextService } from '@core/services/context.service';
import { Patient } from '@pacientes/component/search-patient/search-patient.component';
import { MapperService } from '@presentation/services/mapper.service';
import { ReplaySubject } from 'rxjs';

@Component({
	selector: 'app-new-telemedicine-request',
	templateUrl: './new-telemedicine-request.component.html',
	styleUrls: ['./new-telemedicine-request.component.scss']
})
export class NewTelemedicineRequestComponent implements OnInit {
	@ViewChild('stepper', { static: false }) stepper: MatStepper;
	virtualConsultationRequest: VirtualConsultationRequestDto;
	photoSelectedPatient: PersonPhotoDto;
	firstStepForm: UntypedFormGroup;
	secondStepForm: UntypedFormGroup;
	confirmAndValidateForm = new ReplaySubject<boolean>();
	secondStepForReset = false;

	constructor(public dialogRef: MatDialogRef<NewTelemedicineRequestComponent>, private mapperService: MapperService, private readonly formBuilder: UntypedFormBuilder, private virtualConstultationService: VirtualConstultationService,
		private contextService: ContextService) { }

	ngOnInit(): void {
		this.firstStepForm = this.formBuilder.group({
			selectedPatient: ['', Validators.required],
		});
		this.secondStepForm = this.formBuilder.group({
			requestInformationData: [null, Validators.required],
		});
	}

	onStepChange(stepper: MatStepper) {
		if (stepper.selectedIndex === 0) {
			this.secondStepForReset = true
		} else if (stepper.selectedIndex === 1) {
			this.secondStepForReset = false;
		}
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

	validateForm() {
		if (this.secondStepForm.valid) {
			this.saveVirtualConsultationRequest();
		} else {
			this.confirmAndValidateForm.next(true);
		}
	}

	prepareVirtualConsultationRequest(informationConsultation: any) {
		this.secondStepForm.controls.requestInformationData.setValue(informationConsultation);
		if (informationConsultation) {
			this.virtualConsultationRequest = this.mapToVirtualConsultationRequestDto(informationConsultation);
		}
	}
	saveVirtualConsultationRequest() {
		this.virtualConstultationService.saveVirtualConsultationRequest(this.contextService.institutionId, this.virtualConsultationRequest).subscribe(res => {
			this.dialogRef.close();
		})
	}

	mapToVirtualConsultationRequestDto(informationConsultation: any): VirtualConsultationRequestDto {
		return {
			careLineId: informationConsultation.careLine.id,
			clinicalSpecialtyId: informationConsultation.specialty.id,
			motive: informationConsultation.motive[0].snomed,
			patientId: this.firstStepForm.controls.selectedPatient.value.id,
			priority: informationConsultation.priority,
			problem: informationConsultation.problem[0] ? informationConsultation.problem[0].snomed : null,
		}

	}
}
