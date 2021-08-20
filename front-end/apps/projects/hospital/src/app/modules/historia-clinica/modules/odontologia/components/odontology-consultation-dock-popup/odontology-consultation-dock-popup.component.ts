import { Component, Inject, OnInit } from '@angular/core';
import { DockPopupRef } from '@presentation/services/dock-popup-ref';
import { OVERLAY_DATA } from '@presentation/presentation-model';
import { MotivoNuevaConsultaService } from '@historia-clinica/modules/ambulatoria/services/motivo-nueva-consulta.service';
import { SnomedService } from '@historia-clinica/services/snomed.service';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { AlergiasNuevaConsultaService } from '@historia-clinica/modules/ambulatoria/services/alergias-nueva-consulta.service';
import { InternacionMasterDataService } from '@api-rest/services/internacion-master-data.service';
import { MedicacionesNuevaConsultaService } from "@historia-clinica/modules/ambulatoria/services/medicaciones-nueva-consulta.service";
import { TEXT_AREA_MAX_LENGTH } from '@core/constants/validation-constants';
import { hasError } from '@core/utils/form.utils';
import { PersonalHistoriesNewConsultationService } from "@historia-clinica/modules/ambulatoria/services/personal-histories-new-consultation.service";

@Component({
	selector: 'app-odontology-consultation-dock-popup',
	templateUrl: './odontology-consultation-dock-popup.component.html',
	styleUrls: ['./odontology-consultation-dock-popup.component.scss']
})
export class OdontologyConsultationDockPopupComponent implements OnInit {

	reasonNewConsultationService: MotivoNuevaConsultaService;
	allergiesNewConsultationService: AlergiasNuevaConsultaService;
	criticalityTypes: any[];
	personalHistoriesNewConsultationService: PersonalHistoriesNewConsultationService;
	medicationsNewConsultationService: MedicacionesNuevaConsultaService;
	formEvolucion: FormGroup;


	public readonly TEXT_AREA_MAX_LENGTH = TEXT_AREA_MAX_LENGTH;
	errores: string[] = [];
	public hasError = hasError;

	constructor(
		@Inject(OVERLAY_DATA) public data: OdontologyConsultationData,
		public dockPopupRef: DockPopupRef,
		private readonly snomedService: SnomedService,
		private readonly formBuilder: FormBuilder,
		private readonly internacionMasterDataService: InternacionMasterDataService,
	) {
		this.reasonNewConsultationService = new MotivoNuevaConsultaService(formBuilder, this.snomedService);
		this.allergiesNewConsultationService = new AlergiasNuevaConsultaService(formBuilder, this.snomedService);
		this.medicationsNewConsultationService = new MedicacionesNuevaConsultaService(formBuilder, this.snomedService);
		this.personalHistoriesNewConsultationService = new PersonalHistoriesNewConsultationService(formBuilder, this.snomedService);

	}

	ngOnInit(): void {
		this.internacionMasterDataService.getAllergyCriticality().subscribe(allergyCriticalities => {
			this.criticalityTypes = allergyCriticalities;
			this.allergiesNewConsultationService.setCriticalityTypes(allergyCriticalities);
		});

		this.formEvolucion = this.formBuilder.group({
			evolution: [null, [Validators.maxLength(this.TEXT_AREA_MAX_LENGTH)]],
			clinicalSpecialty: []
		});

		this.reasonNewConsultationService.error$.subscribe(reasonError => {
			this.errores[0] = reasonError;
		});
	}

	private addErrorMessage(): void {
		hasError(this.formEvolucion, 'maxlength', 'evolution') ?
			this.errores[1] =  'odontologia.nueva-consulta.errors.MAX_LENGTH_NOTA'
			: this.errores[1] = undefined;
	}

}

export interface OdontologyConsultationData {
	patientId: number;
}
