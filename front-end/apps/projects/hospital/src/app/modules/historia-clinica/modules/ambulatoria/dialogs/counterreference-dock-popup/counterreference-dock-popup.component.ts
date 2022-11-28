import { Component, ElementRef, Inject, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { CounterReferenceDto, DateDto, ReferenceCounterReferenceFileDto } from '@api-rest/api-model';
import { AppFeature } from '@api-rest/api-model';
import { InternacionMasterDataService } from '@api-rest/services/internacion-master-data.service';
import { TEXT_AREA_MAX_LENGTH } from '@core/constants/validation-constants';
import { MIN_DATE } from '@core/utils/date.utils';
import { Procedimiento, ProcedimientosService } from '@historia-clinica/services/procedimientos.service';
import { SnomedService } from '@historia-clinica/services/snomed.service';
import { OVERLAY_DATA } from '@presentation/presentation-model';
import { DockPopupRef } from '@presentation/services/dock-popup-ref';
import { SnackBarService } from '@presentation/services/snack-bar.service';
import { hasError, scrollIntoError } from '@core/utils/form.utils';
import { Alergia, AlergiasNuevaConsultaService } from '../../services/alergias-nueva-consulta.service';
import { Medicacion, MedicacionesNuevaConsultaService } from '../../services/medicaciones-nueva-consulta.service';
import { CounterreferenceService } from '@api-rest/services/counterreference.service';
import { ReferenceFileService } from '@api-rest/services/reference-file.service';
import { CounterreferenceFileService } from '@api-rest/services/counterreference-file.service';
import { Color } from '@presentation/colored-label/colored-label.component';
import { NewConsultationProcedureFormComponent } from '@historia-clinica/dialogs/new-consultation-procedure-form/new-consultation-procedure-form.component';
import { MatDialog } from '@angular/material/dialog';
import { NewConsultationAllergyFormComponent } from '@historia-clinica/dialogs/new-consultation-allergy-form/new-consultation-allergy-form.component';
import { FeatureFlagService } from '@core/services/feature-flag.service';
import { NewConsultationMedicationFormComponent } from '@historia-clinica/dialogs/new-consultation-medication-form/new-consultation-medication-form.component';
import { ReferenceMasterDataService } from '@api-rest/services/reference-master-data.service';
@Component({
	selector: 'app-counterreference-dock-popup',
	templateUrl: './counterreference-dock-popup.component.html',
	styleUrls: ['./counterreference-dock-popup.component.scss']
})
export class CounterreferenceDockPopupComponent implements OnInit {

	public minDate = MIN_DATE;
	public readonly TEXT_AREA_MAX_LENGTH = TEXT_AREA_MAX_LENGTH;
	readonly Color = Color;
	medicacionesNuevaConsultaService: MedicacionesNuevaConsultaService;
	procedimientoNuevaConsultaService: ProcedimientosService;
	alergiasNuevaConsultaService: AlergiasNuevaConsultaService;
	criticalityTypes: any[];
	formReferenceClosure: FormGroup;
	hasError = hasError;
	selectedFiles: File[] = [];
	selectedFilesShow: any[] = [];
	closureTypes: any[] = [];
	collapsedCounterReference = false;
	searchConceptsLocallyFFIsOn = false;

	constructor(
		@Inject(OVERLAY_DATA) public data: any,
		public dockPopupRef: DockPopupRef,
		private readonly formBuilder: FormBuilder,
		private readonly snomedService: SnomedService,
		private readonly snackBarService: SnackBarService,
		private readonly internacionMasterDataService: InternacionMasterDataService,
		private readonly counterreferenceService: CounterreferenceService,
		private readonly dialog: MatDialog,
		private readonly featureFlagService: FeatureFlagService,
		private readonly referenceFileService: ReferenceFileService,
		private readonly counterreferenceFileService: CounterreferenceFileService,
		private readonly referenceMasterDataService: ReferenceMasterDataService,
		private readonly el: ElementRef,
	) {
		this.medicacionesNuevaConsultaService = new MedicacionesNuevaConsultaService(formBuilder, this.snomedService, this.snackBarService);
		this.procedimientoNuevaConsultaService = new ProcedimientosService(formBuilder, this.snomedService, this.snackBarService);
		this.alergiasNuevaConsultaService = new AlergiasNuevaConsultaService(formBuilder, this.snomedService, this.snackBarService);
	}

	ngOnInit(): void {
		this.formReferenceClosure = this.formBuilder.group({
			closureType: [null, [Validators.required]],
			description: [null, [Validators.required]]
		});

		this.internacionMasterDataService.getAllergyCriticality().subscribe(allergyCriticalities => {
			this.criticalityTypes = allergyCriticalities;
			this.alergiasNuevaConsultaService.setCriticalityTypes(allergyCriticalities);
		});

		this.featureFlagService.isActive(AppFeature.HABILITAR_BUSQUEDA_LOCAL_CONCEPTOS).subscribe(isOn => {
			this.searchConceptsLocallyFFIsOn = isOn;
		});

		this.referenceMasterDataService.getClosureTypes().subscribe( closureTypes => {
			this.closureTypes = closureTypes;
		})

	}

	save(): void {
		if (this.formReferenceClosure.valid) {
			let fileIds: number[] = [];
			let longFiles = 0;
			if (!this.selectedFiles.length) {
				const counterreference: CounterReferenceDto = this.buildCounterReferenceDto(fileIds);
				this.createCounterreference(counterreference);
				return;
			}
			for (let file of this.selectedFiles) {
				this.counterreferenceFileService.uploadCounterreferenceFiles(this.data.patientId, file).subscribe(
					fileId => {
						longFiles = longFiles + 1;
						fileIds.push(fileId);
						if (this.selectedFiles.length === longFiles) {
							const counterreference: CounterReferenceDto = this.buildCounterReferenceDto(fileIds);
							this.createCounterreference(counterreference);
						}
					},
					() => {
						this.snackBarService.showError('ambulatoria.paciente.counterreference.messages.ERROR');
						this.counterreferenceFileService.deleteCounterreferenceFiles(fileIds);
					}
				)
			}
		}

		else {
			this.formReferenceClosure.controls['closureType'].markAsTouched();
			this.formReferenceClosure.controls['description'].markAsTouched();
			this.collapsedCounterReference = false;
			setTimeout(() => {
				scrollIntoError(this.formReferenceClosure, this.el)
			}, 300);
		}
	}

	addProcedure(): void {
		this.dialog.open(NewConsultationProcedureFormComponent, {
			data: {
				procedureService: this.procedimientoNuevaConsultaService,
				searchConceptsLocallyFF: this.searchConceptsLocallyFFIsOn,
			},
			autoFocus: false,
			width: '35%',
			disableClose: true,
		});
	}

	addAllergy(): void {
		this.dialog.open(NewConsultationAllergyFormComponent, {
			data: {
				allergyService: this.alergiasNuevaConsultaService,
				searchConceptsLocallyFF: this.searchConceptsLocallyFFIsOn,
			},
			autoFocus: false,
			width: '35%',
			disableClose: true,
		});
	}

	addMedication(): void {
		this.dialog.open(NewConsultationMedicationFormComponent, {
			data: {
				medicationService: this.medicacionesNuevaConsultaService,
				searchConceptsLocallyFF: this.searchConceptsLocallyFFIsOn,
			},
			autoFocus: false,
			width: '35%',
			disableClose: true,
		});
	}

	private buildCounterReferenceDto(fileIds): CounterReferenceDto {

		return {
			referenceId: this.data.reference.id,
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
			clinicalSpecialtyId: this.data.reference.clinicalSpecialty.id,
			counterReferenceNote: this.formReferenceClosure.value.description,
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
			fileIds: fileIds,
			closureTypeId: this.formReferenceClosure.value.closureType.id,
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
		const hasProblems = this.data.reference.problems.length;
		this.counterreferenceService.createCounterReference(this.data.patientId, counterreference).subscribe(
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
				problems: !!hasProblems,
			};
		}
	}

	downloadReferenceFile(file: ReferenceCounterReferenceFileDto) {
		this.referenceFileService.downloadReferenceFiles(file.fileId, file.fileName);
	}

	onSelectFileFormData($event): void {
		Array.from($event.target.files).forEach((file: File) => {
			this.selectedFiles.push(file);
			this.selectedFilesShow.push(file.name);
		});
	}

	removeSelectedFile(index): void {
		this.selectedFiles.splice(index, 1);
		this.selectedFilesShow.splice(index, 1);
	}

}
