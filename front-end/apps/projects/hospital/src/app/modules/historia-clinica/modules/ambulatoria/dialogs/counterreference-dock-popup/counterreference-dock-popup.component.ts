import { Component, Inject, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { CounterReferenceDto, DateDto } from '@api-rest/api-model';
import { InternacionMasterDataService } from '@api-rest/services/internacion-master-data.service';
import { TEXT_AREA_MAX_LENGTH } from '@core/constants/validation-constants';
import { MIN_DATE } from '@core/utils/date.utils';
import { Procedimiento, ProcedimientosService } from '@historia-clinica/services/procedimientos.service';
import { SnomedService } from '@historia-clinica/services/snomed.service';
import { OVERLAY_DATA } from '@presentation/presentation-model';
import { DockPopupRef } from '@presentation/services/dock-popup-ref';
import { SnackBarService } from '@presentation/services/snack-bar.service';
import { hasError } from '@core/utils/form.utils';
import { Alergia, AlergiasNuevaConsultaService } from '../../services/alergias-nueva-consulta.service';
import { Medicacion, MedicacionesNuevaConsultaService } from '../../services/medicaciones-nueva-consulta.service';
import { CounterreferenceService } from '@api-rest/services/counterreference.service';

@Component({
	selector: 'app-counterreference-dock-popup',
	templateUrl: './counterreference-dock-popup.component.html',
	styleUrls: ['./counterreference-dock-popup.component.scss']
})
export class CounterreferenceDockPopupComponent implements OnInit {

	public minDate = MIN_DATE;
	public readonly TEXT_AREA_MAX_LENGTH = TEXT_AREA_MAX_LENGTH;
	public medicacionesNuevaConsultaService: MedicacionesNuevaConsultaService;
	public procedimientoNuevaConsultaService: ProcedimientosService;
	public alergiasNuevaConsultaService: AlergiasNuevaConsultaService;
	public criticalityTypes: any[];
	public formDescription: FormGroup;
	public hasError = hasError;

	constructor(
		@Inject(OVERLAY_DATA) public data: any,
		public dockPopupRef: DockPopupRef,
		private readonly formBuilder: FormBuilder,
		private readonly snomedService: SnomedService,
		private readonly snackBarService: SnackBarService,
		private readonly internacionMasterDataService: InternacionMasterDataService,
		private readonly counterreferenceService: CounterreferenceService,
	) {
		this.medicacionesNuevaConsultaService = new MedicacionesNuevaConsultaService(formBuilder, this.snomedService, this.snackBarService);
		this.procedimientoNuevaConsultaService = new ProcedimientosService(formBuilder, this.snomedService, this.snackBarService);
		this.alergiasNuevaConsultaService = new AlergiasNuevaConsultaService(formBuilder, this.snomedService, this.snackBarService);
	}

	ngOnInit(): void {
		this.formDescription = this.formBuilder.group({
			description: [null, [Validators.required]]
		});

		this.internacionMasterDataService.getAllergyCriticality().subscribe(allergyCriticalities => {
			this.criticalityTypes = allergyCriticalities;
			this.alergiasNuevaConsultaService.setCriticalityTypes(allergyCriticalities);
		});

	}

	save(): void {
		if (this.formDescription.valid) {
			const counterreference: CounterReferenceDto = this.buildCounterReferenceDto();
			this.createCounterreference(counterreference);
		}
		else {
			this.formDescription.controls['description'].markAsTouched();
		}
	}

	private buildCounterReferenceDto(): CounterReferenceDto {

		return {
			referenceId: this.data.data.reference.id,
			allergies: this.alergiasNuevaConsultaService.getAlergias().map((allergy: Alergia) => {
				return {
					categoryId: null,
					criticalityId: allergy.criticalityId,
					snomed: allergy.snomed,
					startDate: null,
					statusId: null,
					verificationId: null,
				};
			}),
			clinicalSpecialtyId: this.data.data.reference.clinicalSpecialty.id,
			counterReferenceNote: this.formDescription.value.description,
			medications: this.medicacionesNuevaConsultaService.getMedicaciones().map((medicacion: Medicacion) => {
				return {
					note: medicacion.observaciones,
					snomed: medicacion.snomed,
					suspended: medicacion.suspendido,
				};
			}
			),
			procedures: this.procedimientoNuevaConsultaService.getProcedimientos().map((procedure: Procedimiento) => {
				return {
					performedDate: this.buildDateDto(procedure.performedDate),
					snomed: procedure.snomed
				};
			}),
			fileIds: [],
		};
	}

	private buildDateDto(date: string): DateDto {
		if (date) {
			const dateSplit = date.split("-");
			return (
				{
					year: Number(dateSplit[0]),
					month: Number(dateSplit[1]),
					day: Number(dateSplit[2]),
				}
			)
		}
		return null;
	}

	private createCounterreference(counterreference: CounterReferenceDto): void {
		this.counterreferenceService.createCounterReference(this.data.data.patientId, counterreference).subscribe(
			success => {
				this.snackBarService.showSuccess('ambulatoria.paciente.counterreference.messages.SUCCESS');
				this.dockPopupRef.close(mapToFieldsToUpdate());
			},
			error => {
				this.snackBarService.showError('ambulatoria.paciente.counterreference.messages.ERROR');
			}
		);

		function mapToFieldsToUpdate() {
			return {
				allergies: !!counterreference.allergies?.length,
				medications: !!counterreference.medications?.length,
				procedures: !!counterreference.procedures?.length,
			};
		}
	}

}
