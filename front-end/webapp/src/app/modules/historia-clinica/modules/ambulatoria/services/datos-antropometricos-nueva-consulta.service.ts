import { InternacionMasterDataService } from '@api-rest/services/internacion-master-data.service';
import { FormBuilder, FormGroup } from '@angular/forms';
import { ClinicalObservationDto, MasterDataInterface } from '@api-rest/api-model';

export interface DatosAntropometricos {
	bloodType?: ClinicalObservationDto;
	bmi?: ClinicalObservationDto;
	height?: ClinicalObservationDto;
	weight?: ClinicalObservationDto;
}

export class DatosAntropometricosNuevaConsultaService {

	private form: FormGroup;
	private bloodTypes: MasterDataInterface<string>[];

	constructor(
		private readonly formBuilder: FormBuilder,
		private readonly internacionMasterDataService: InternacionMasterDataService
	) {
		this.form = this.formBuilder.group({
			bloodType: [null],
			height: [null],
			weight: [null]
		});
		this.internacionMasterDataService.getBloodTypes().subscribe(bloodTypes => this.bloodTypes = bloodTypes);
	}

	getForm(): FormGroup {
		return this.form;
	}

	getBloodTypes(): MasterDataInterface<string>[] {
		return this.bloodTypes;
	}

	getDatosAntropometricos(): DatosAntropometricos {
		return {
			bloodType: this.form.value.bloodType ? {
				id: this.form.value.bloodType.id,
				value: this.form.value.bloodType.description
			} : undefined,
			height: this.form.value.height ? { value: this.form.value.height } : undefined,
			weight: this.form.value.weight ? { value: this.form.value.weight } : undefined
		};
	}
}
