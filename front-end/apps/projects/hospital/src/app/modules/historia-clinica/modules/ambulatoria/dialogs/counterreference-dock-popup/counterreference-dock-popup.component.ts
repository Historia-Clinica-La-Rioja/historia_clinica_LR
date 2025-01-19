import { Component, ElementRef, Inject, OnInit } from '@angular/core';
import { UntypedFormBuilder, UntypedFormGroup, Validators } from '@angular/forms';
import { ClinicalSpecialtyDto, CounterReferenceDto, DateDto, ReferenceCounterReferenceFileDto, ReferenceDataDto } from '@api-rest/api-model';
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
import { EpisodeData } from '@historia-clinica/components/episode-data/episode-data.component';
import { HierarchicalUnitService } from '@historia-clinica/services/hierarchical-unit.service';
import { ClinicalSpecialtyService } from '@api-rest/services/clinical-specialty.service';
import { ConceptsList } from '@historia-clinica/components/concepts-list/concepts-list.component';
import { DateFormatPipe } from '@presentation/pipes/date-format.pipe';
import { dateToDateDto } from '@api-rest/mapper/date-dto.mapper';
import { CLOSURE_OPTIONS } from '@access-management/constants/reference';
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
	formReferenceClosure: UntypedFormGroup;
	hasError = hasError;
	selectedFiles: File[] = [];
	selectedFilesShow: any[] = [];
	closureTypes: any[] = [];
	collapsedCounterReference = false;
	searchConceptsLocallyFFIsOn = false;
	disableConfirmButton = false;
	episodeData: EpisodeData;
	professionalSpecialties: ClinicalSpecialtyDto[] = [];
	isAllergyNoRefer: boolean = true;
	allergyContent: ConceptsList = {
		id: 'allergy-checkbox-concepts-list',
		header: {
			text: 'ambulatoria.paciente.nueva-consulta.alergias.TITLE',
			icon: 'cancel'
		},
		titleList: 'ambulatoria.paciente.nueva-consulta.alergias.table.TITLE',
		actions: {
			button: 'ambulatoria.paciente.nueva-consulta.alergias.ADD',
			checkbox: 'ambulatoria.paciente.nueva-consulta.alergias.NO_REFER',
		}
	}

	constructor(
		@Inject(OVERLAY_DATA) public data: CounterreferenceData,
		public dockPopupRef: DockPopupRef,
		private readonly formBuilder: UntypedFormBuilder,
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
		private readonly hierarchicalUnitFormService: HierarchicalUnitService,
		private readonly clinicalSpecialtyService: ClinicalSpecialtyService,
		private readonly dateFormatPipe: DateFormatPipe
	) {
		this.medicacionesNuevaConsultaService = new MedicacionesNuevaConsultaService(formBuilder, this.snomedService, this.snackBarService);
		this.procedimientoNuevaConsultaService = new ProcedimientosService(formBuilder, this.snomedService, this.snackBarService, this.dateFormatPipe);
		this.alergiasNuevaConsultaService = new AlergiasNuevaConsultaService(formBuilder, this.snomedService, this.snackBarService, this.internacionMasterDataService);
	}

	ngOnInit(): void {
		this.formReferenceClosure = this.formBuilder.group({
			closureType: [null, [Validators.required]],
			description: [null, [Validators.required]],
			specialty: [null, [Validators.required]]
		});

		this.internacionMasterDataService.getAllergyCriticality().subscribe(allergyCriticalities => {
			this.criticalityTypes = allergyCriticalities;
			this.alergiasNuevaConsultaService.setCriticalityTypes(allergyCriticalities);
		});

		this.featureFlagService.isActive(AppFeature.HABILITAR_BUSQUEDA_LOCAL_CONCEPTOS).subscribe(isOn => {
			this.searchConceptsLocallyFFIsOn = isOn;
		});

		this.referenceMasterDataService.getClosureTypes().subscribe(closureTypes => {
			this.closureTypes = closureTypes.filter(closureType => closureType.description !== CLOSURE_OPTIONS[5].description);
		})

		this.setProfessionalSpecialties();
	}

	save(): void {
		if (this.hierarchicalUnitFormService.isValidForm()) {
			setTimeout(() => {
				scrollIntoError(this.hierarchicalUnitFormService.getForm(), this.el)
			}, 300);
		} else {
			if (this.formReferenceClosure.valid) {
				this.disableConfirmButton = true;
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
				this.disableConfirmButton = false;
			}


			else {

				this.formReferenceClosure.controls['closureType'].markAsTouched();
				this.formReferenceClosure.controls['description'].markAsTouched();
				this.formReferenceClosure.controls.specialty.markAllAsTouched();
				this.collapsedCounterReference = false;
				setTimeout(() => {
					scrollIntoError(this.formReferenceClosure, this.el)
				}, 300);
			}
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

	checkAllergyEvent($event) {
		if ($event.addPressed) {
			this.addAllergy();
		}
		this.isAllergyNoRefer = !$event.checkboxSelected;
	}

	private setProfessionalSpecialties() {
		this.clinicalSpecialtyService.getLoggedInProfessionalClinicalSpecialties()
			.subscribe((specialties: ClinicalSpecialtyDto[]) => {
				this.professionalSpecialties = specialties.filter(professionalSpecialty =>
					this.data.reference.destinationClinicalSpecialties.some(referenceSpecialty => referenceSpecialty.id === professionalSpecialty.id)
				);
				this.formReferenceClosure.controls.specialty.setValue(this.professionalSpecialties[0]);
			});
	}

	private buildCounterReferenceDto(fileIds): CounterReferenceDto {

		return {
			referenceId: this.data.reference.id,
			allergies: {
				isReferred: (this.isAllergyNoRefer && this.alergiasNuevaConsultaService.getAlergias().length === 0) ? null: this.isAllergyNoRefer,
				content: this.alergiasNuevaConsultaService.getAlergias().map((allergy: Alergia) => {
					return {
						categoryId: null,
						criticalityId: allergy.criticalityId,
						snomed: allergy.snomed,
						startDate: null,
						statusId: null,
						verificationId: null,
					};
				}),
			},
			clinicalSpecialtyId: this.formReferenceClosure.controls.specialty.value.id,
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
			patientMedicalCoverageId: this.episodeData?.medicalCoverageId,
			hierarchicalUnitId: this.episodeData?.hierarchicalUnitId,
		};
	}

	private buildDateDto(date: Date): DateDto {
		return date ? dateToDateDto(date) : null
	}

	private createCounterreference(counterreference: CounterReferenceDto): void {
		const hasProblems = this.data.reference.problems.length;
		this.counterreferenceService.createCounterReference(this.data.patientId, counterreference).subscribe(
			success => {
				this.snackBarService.showSuccess('ambulatoria.paciente.counterreference.messages.SUCCESS');
				this.dockPopupRef.close(mapToFieldsToUpdate());
			},
			error => {
				this.disableConfirmButton = false;
				this.snackBarService.showError('ambulatoria.paciente.counterreference.messages.ERROR');
			}
		);

		function mapToFieldsToUpdate() {
			return {
				allergies: !!counterreference.allergies?.content.length,
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

interface CounterreferenceData {
	reference: ReferenceDataDto;
	patientId: number;
}
