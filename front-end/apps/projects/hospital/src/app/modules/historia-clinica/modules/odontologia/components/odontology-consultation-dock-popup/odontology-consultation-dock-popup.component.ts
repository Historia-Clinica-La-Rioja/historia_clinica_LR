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
import { newMoment } from "@core/utils/moment.utils";
import { ClinicalSpecialtyDto } from '@api-rest/api-model';
import { ClinicalSpecialtyService } from '@api-rest/services/clinical-specialty.service';
import { ProblemasService } from '@historia-clinica/services/problemas-nueva-consulta.service';
import { ActionsNewConsultationService } from '../../services/actions-new-consultation.service';
import { OdontogramService } from '../../services/odontogram.service';
import { ConceptsFacadeService } from '../../services/concepts-facade.service';
import { SurfacesNamesFacadeService } from '../../services/surfaces-names-facade.service';
import { ActionType } from '../../services/actions.service';

@Component({
	selector: 'app-odontology-consultation-dock-popup',
	templateUrl: './odontology-consultation-dock-popup.component.html',
	styleUrls: ['./odontology-consultation-dock-popup.component.scss'],
	providers: [
		ConceptsFacadeService
	]
})
export class OdontologyConsultationDockPopupComponent implements OnInit {

	reasonNewConsultationService: MotivoNuevaConsultaService;
	otherDiagnosticsNewConsultationService: ProblemasService;
	severityTypes: any[];
	allergiesNewConsultationService: AlergiasNuevaConsultaService;
	criticalityTypes: any[];
	personalHistoriesNewConsultationService: PersonalHistoriesNewConsultationService;
	medicationsNewConsultationService: MedicacionesNuevaConsultaService;
	form: FormGroup;
	clinicalSpecialties: ClinicalSpecialtyDto[];

	diagnosticsNewConsultationService: ActionsNewConsultationService;


	public readonly TEXT_AREA_MAX_LENGTH = TEXT_AREA_MAX_LENGTH;
	errors: string[] = [];
	public hasError = hasError;
	public today = newMoment();

	constructor(
		@Inject(OVERLAY_DATA) public data: OdontologyConsultationData,
		public dockPopupRef: DockPopupRef,
		private readonly snomedService: SnomedService,
		private readonly formBuilder: FormBuilder,
		private readonly internmentMasterDataService: InternacionMasterDataService,
		private readonly clinicalSpecialtyService: ClinicalSpecialtyService,
		private readonly odontogramService: OdontogramService,
		private readonly conceptsFacadeService: ConceptsFacadeService,
		private readonly surfacesNamesFacadeService: SurfacesNamesFacadeService
	) {
		this.reasonNewConsultationService = new MotivoNuevaConsultaService(formBuilder, this.snomedService);
		this.allergiesNewConsultationService = new AlergiasNuevaConsultaService(formBuilder, this.snomedService);
		this.medicationsNewConsultationService = new MedicacionesNuevaConsultaService(formBuilder, this.snomedService);
		this.personalHistoriesNewConsultationService = new PersonalHistoriesNewConsultationService(formBuilder, this.snomedService);
		this.otherDiagnosticsNewConsultationService = new ProblemasService(formBuilder, this.snomedService);
		this.diagnosticsNewConsultationService = new ActionsNewConsultationService(this.odontogramService, this.surfacesNamesFacadeService, ActionType.DIAGNOSTIC, this.conceptsFacadeService);
	}

	ngOnInit(): void {

		this.form = this.formBuilder.group({
			evolution: [null, [Validators.maxLength(this.TEXT_AREA_MAX_LENGTH)]],
			clinicalSpecialty: []
		});

		this.clinicalSpecialtyService.getLoggedInProfessionalClinicalSpecialties().subscribe(clinicalSpecialties => {
			this.form.patchValue({ clinicalSpecialty: clinicalSpecialties[0].id });
			this.clinicalSpecialties = clinicalSpecialties;
		});

		this.internmentMasterDataService.getAllergyCriticality().subscribe(allergyCriticalities => {
			this.criticalityTypes = allergyCriticalities;
			this.allergiesNewConsultationService.setCriticalityTypes(allergyCriticalities);
		});

		this.internmentMasterDataService.getHealthSeverity().subscribe(healthConditionSeverities => {
			this.severityTypes = healthConditionSeverities;
			this.otherDiagnosticsNewConsultationService.setSeverityTypes(healthConditionSeverities);
		});


		this.reasonNewConsultationService.error$.subscribe(reasonError => {
			this.errors[0] = reasonError;
		});

		this.otherDiagnosticsNewConsultationService.error$.subscribe(otherDiagnosticsError => {
			this.errors[1] = otherDiagnosticsError;
		});
	}

	private addErrorMessage(): void {
		this.errors[2] = hasError(this.form, 'maxlength', 'evolution') ?
			'La nota de evolución debe tener como máximo 1024 caracteres'
			: undefined;
	}

}

export interface OdontologyConsultationData {
	patientId: number;
}
