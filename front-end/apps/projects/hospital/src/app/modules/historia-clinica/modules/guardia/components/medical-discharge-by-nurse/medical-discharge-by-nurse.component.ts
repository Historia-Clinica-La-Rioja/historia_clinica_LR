import { Component, EventEmitter, OnDestroy, OnInit, Output } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { MasterDataDto, SnomedDto } from '@api-rest/api-model';
import { DischargeTypes } from '@api-rest/masterdata';
import { EmergencyCareMasterDataService } from '@api-rest/services/emergency-care-master-data.service';
import { ToFormGroup } from '@core/utils/form.utils';
import { Observable, Subscription } from 'rxjs';

@Component({
	selector: 'app-medical-discharge-by-nurse',
	templateUrl: './medical-discharge-by-nurse.component.html',
	styleUrls: ['./medical-discharge-by-nurse.component.scss']
})


export class MedicalDischargeByNurseComponent implements OnInit, OnDestroy {

	form: FormGroup<ToFormGroup<MedicalDischargeByNurseForm>>;
	dischargeTypes$: Observable<MasterDataDto[]>;
	formSubscription: Subscription;
	@Output() medicalDischargeByNurse = new EventEmitter<MedicalDischargeByNurse>;

	constructor(
		private readonly emergencyCareMasterDataService: EmergencyCareMasterDataService,
	) { }

	ngOnInit(): void {
		this.form = new FormGroup<ToFormGroup<MedicalDischargeByNurseForm>>({
			problemDescription: new FormControl(null, Validators.required),
			dischargeTypeId: new FormControl(null, Validators.required),
			observations: new FormControl(null)
		});

		this.formSubscription = this.form.valueChanges.subscribe((formValueChanges: MedicalDischargeByNurseForm) => {
			const medicalDischargeByNurse = this.buildNursingDischarge(formValueChanges)
			this.medicalDischargeByNurse.emit(medicalDischargeByNurse);
		});

		this.initializeDataInForm();

		this.dischargeTypes$ = this.emergencyCareMasterDataService.getNursingDischargeType();
	}

	ngOnDestroy(): void {
		this.formSubscription.unsubscribe();
	}


	private initializeDataInForm() {
		this.form.controls.problemDescription.setValue(NURSING_PROBLEM.pt);
		this.form.controls.problemDescription.disable();
		this.form.controls.dischargeTypeId.setValue(DischargeTypes.ALTA_HOSPITALARIA);
	}

	private buildNursingDischarge(formValues: MedicalDischargeByNurseForm): MedicalDischargeByNurse {
		return {
			problem: NURSING_PROBLEM,
			dischargeTypeId: formValues.dischargeTypeId,
			observations: formValues.observations,
		}
	}

}

interface MedicalDischargeByNurseForm {
	problemDescription?: string;
	dischargeTypeId: number;
	observations: string;
}

export interface MedicalDischargeByNurse extends MedicalDischargeByNurseForm {
	problem: SnomedDto;
}

const NURSING_PROBLEM: SnomedDto = {
	sctid: '9632001',
	pt: 'Atención de enfermería'
}
