import { Injectable } from '@angular/core';
import { FormControl, FormGroup, UntypedFormBuilder, Validators } from '@angular/forms';
import { AppFeature, ClinicalSpecialtyDto } from '@api-rest/api-model';
import { NUMBER_PATTERN } from '@core/utils/form.utils';
import { FeatureFlagService } from '@core/services/feature-flag.service';
import { PatientMedicalCoverage } from '@pacientes/dialogs/medical-coverage/medical-coverage.component';

const POSDATADAS_DEFAULT = 0;
const POSDATADAS_MIN = 1;
const POSDATADAS_MAX = 11;
const MAX_PHONE_PREFIX: number = 10;
const MAX_PHONE_NUMBER: number = 15;

@Injectable({
    providedIn: 'root'
})
export class StatePrescripcionService {

    prescriptionForm: FormGroup<PrescriptionForm>;
    isHabilitarRecetaDigitalEnabled: boolean = false;

    constructor(
        private readonly formBuilder: UntypedFormBuilder,
        private readonly featureFlagService: FeatureFlagService,
    ) {
        this.featureFlagService.isActive(AppFeature.HABILITAR_RECETA_DIGITAL)
				.subscribe((result: boolean) => this.isHabilitarRecetaDigitalEnabled = result);
        this.formConfiguration();
    }

    getForm(): FormGroup<PrescriptionForm> {
        return this.prescriptionForm;
    }

    updateForm(newForm: FormGroup<PrescriptionForm>) {
        this.prescriptionForm = newForm;
    }

    resetForm() {
        this.prescriptionForm.reset();
        this.prescriptionForm.controls.posdatadas.disable();
		this.prescriptionForm.controls.posdatadas.setValue(POSDATADAS_DEFAULT);
    }

    private formConfiguration() {
		this.prescriptionForm = this.formBuilder.group({
			patientMedicalCoverage: [null],
			withoutRecipe: [false],
			evolucion: [],
			clinicalSpecialty: [null, [Validators.required]],
			prolongedTreatment: [false],
			posdatadas: [{value: POSDATADAS_DEFAULT, disabled: true}, [Validators.min(POSDATADAS_MIN), Validators.max(POSDATADAS_MAX)]],
			archived: [false],
			patientData: this.setPatientDataGroup(),
		});
	}

	private setPatientDataGroup(): FormGroup | null {
		return (this.isHabilitarRecetaDigitalEnabled)
			? this.formBuilder.group({
				phonePrefix: [null, [Validators.required, Validators.maxLength(MAX_PHONE_PREFIX), Validators.pattern(NUMBER_PATTERN)]],
				country: [{value: null, disabled: true}, [Validators.required]],
				phoneNumber: [null, [Validators.required, Validators.maxLength(MAX_PHONE_NUMBER), Validators.pattern(NUMBER_PATTERN)]],
				province: [null, Validators.required],
				locality: [null, Validators.required],
				city: [null, Validators.required],
				street: [null, Validators.required],
				streetNumber: [null, Validators.required]
			})
			: null;
	}
}

export interface PrescriptionForm {
	patientMedicalCoverage: FormControl<PatientMedicalCoverage>,
	withoutRecipe: FormControl<boolean>,
	evolucion: FormControl<[]>,
	clinicalSpecialty: FormControl<ClinicalSpecialtyDto>,
	prolongedTreatment: FormControl<boolean>,
	posdatadas: FormControl<number>,
	archived: FormControl<boolean>,
	patientData: FormControl<PatientData>,
}

export interface Prescription {
	patientMedicalCoverage: PatientMedicalCoverage,
	withoutRecipe: boolean,
	evolucion: [],
	clinicalSpecialty: ClinicalSpecialtyDto,
	prolongedTreatment: boolean,
	posdatadas: number,
	archived: boolean,
	patientData: PatientData,
}

export interface PatientData {
	phonePrefix: string,
	country: number,
	phoneNumber: string,
	province: number,
	locality: number,
	city: number,
	street: string,
	streetNumber: string
}
