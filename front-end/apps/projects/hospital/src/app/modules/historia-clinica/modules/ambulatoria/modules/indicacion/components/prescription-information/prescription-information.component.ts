import { Component, Input, OnInit } from '@angular/core';
import { AbstractControl, FormGroup, Validators } from '@angular/forms';
import { ClinicalSpecialtyDto } from '@api-rest/api-model';
import { ClinicalSpecialtyService } from '@api-rest/services/clinical-specialty.service';
import {hasError} from '@core/utils/form.utils';
import { PrescriptionTypes } from '@historia-clinica/modules/ambulatoria/services/prescripciones.service';
import { NewPrescriptionData } from '../../dialogs/nueva-prescripcion/nueva-prescripcion.component';
import { PrescriptionForm, StatePrescripcionService } from '../../services/state-prescripcion.service';

const POSDATADAS_DEFAULT = 0;

@Component({
  selector: 'app-prescription-information',
  templateUrl: './prescription-information.component.html',
  styleUrls: ['./prescription-information.component.scss']
})
export class PrescriptionInformationComponent implements OnInit {

    @Input() prescriptionData: NewPrescriptionData;
    @Input() isHabilitarRecetaDigitalEnabled: boolean;

	prescriptionForm: FormGroup<PrescriptionForm>;
    specialties: ClinicalSpecialtyDto[];
    hasError = hasError;
    POSDATADAS_MIN = 1;
    POSDATADAS_MAX = 11;

    constructor(
        private readonly clinicalSpecialtyService: ClinicalSpecialtyService,
		private statePrescripcionService: StatePrescripcionService,
    ) { }

    ngOnInit(): void {
		this.prescriptionForm = this.statePrescripcionService.getForm();
        this.setProfessionalSpecialties();
    }

    setProfessionalSpecialties() {
		this.clinicalSpecialtyService.getLoggedInProfessionalClinicalSpecialties().subscribe(specialties => {
			this.setSpecialtyFields(specialties);
		});
	}

    setSpecialtyFields(specialtyArray) {
		this.specialties = specialtyArray;
		const clinicalSpecialty = this.prescriptionForm.get('clinicalSpecialty');
		clinicalSpecialty.setValue(specialtyArray[0]);
		if (this.specialties.length === 1)
			clinicalSpecialty.disable();
		this.prescriptionForm.controls['clinicalSpecialty'].markAsTouched();
		this.statePrescripcionService.updateForm(this.prescriptionForm);
	}

    setProlongedTreatment(isOn: boolean) {
		this.prescriptionForm.controls.prolongedTreatment.setValue(isOn);
		const posdatadas = this.prescriptionForm.controls.posdatadas;
		if (isOn) {
			posdatadas.enable()
			posdatadas.addValidators(Validators.required);
			this.prescriptionForm.controls.posdatadas.setValue(1);
		} else {
			this.prescriptionForm.controls.posdatadas.setValue(0);
			this.disablePosdatadas(posdatadas)
		}
		this.statePrescripcionService.updateForm(this.prescriptionForm);
	}

    disablePosdatadas(posdatadas: AbstractControl) {
		posdatadas.disable();
		posdatadas.setValue(POSDATADAS_DEFAULT);
	}

    isMedication(): boolean {
        return this.prescriptionData.prescriptionType === PrescriptionTypes.MEDICATION && ! this.isHabilitarRecetaDigitalEnabled;
    }

}
