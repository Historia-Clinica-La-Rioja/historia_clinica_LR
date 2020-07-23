import { InternacionMasterDataService } from '@api-rest/services/internacion-master-data.service';
import { FormGroup, FormBuilder } from '@angular/forms';
import { MasterDataInterface } from '@api-rest/api-model';

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

}
