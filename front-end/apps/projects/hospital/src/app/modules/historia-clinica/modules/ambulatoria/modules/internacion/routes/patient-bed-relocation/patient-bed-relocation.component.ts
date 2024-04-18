import { Component, OnInit, ElementRef } from '@angular/core';
import { Observable } from 'rxjs';
import { PatientBasicData } from '@presentation/utils/patient.utils';
import { PatientService } from '@api-rest/services/patient.service';
import { InternacionService } from '@api-rest/services/internacion.service';
import { MapperService } from '@presentation/services/mapper.service';
import { ActivatedRoute, Router } from '@angular/router';
import {
	BasicPatientDto,
	InternmentSummaryDto,
	CompletePatientDto,
	PersonalInformationDto,
	PatientBedRelocationDto,
	BedInfoDto,
	PatientMedicalCoverageDto,
	PersonPhotoDto
} from '@api-rest/api-model';
import { map } from 'rxjs/operators';
import { UntypedFormGroup, UntypedFormBuilder, Validators, UntypedFormControl } from '@angular/forms';
import { MatDialog } from '@angular/material/dialog';
import { TranslateService } from '@ngx-translate/core';
import { SnackBarService } from '@presentation/services/snack-bar.service';
import { scrollIntoError, hasError, futureTimeValidation, MinTimeValidator, TIME_PATTERN } from '@core/utils/form.utils';
import { ConfirmDialogComponent } from '@presentation/dialogs/confirm-dialog/confirm-dialog.component';
import { ContextService } from '@core/services/context.service';
import { BedService } from '@api-rest/services/bed.service';
import { newDate, dateISOParseDate, buildFullDateFromDate } from '@core/utils/moment.utils';
import { PersonService } from '@api-rest/services/person.service';
import { BedAssignmentComponent } from '@historia-clinica/dialogs/bed-assignment/bed-assignment.component';
import { PatientMedicalCoverageService } from '@api-rest/services/patient-medical-coverage.service';
import { InternmentEpisodeSummary } from "@historia-clinica/modules/ambulatoria/modules/internacion/components/internment-episode-summary/internment-episode-summary.component";
import { INTERNMENT_SECTOR } from '@historia-clinica/modules/guardia/constants/masterdata';
import { toApiFormat } from '@api-rest/mapper/date.mapper';
import { toHourMinute } from '@core/utils/date.utils';


const ROUTE_PROFILE = 'pacientes/profile/';

@Component({
	selector: 'app-patient-bed-relocation',
	templateUrl: './patient-bed-relocation.component.html',
	styleUrls: ['./patient-bed-relocation.component.scss']
})
export class PatientBedRelocationComponent implements OnInit {

	public patient$: Observable<PatientBasicData>;
	public internmentEpisodeSummary$: Observable<InternmentEpisodeSummary>;
	hasError = hasError;
	public minTimeStr;
	public minDate: Date;
	public form: UntypedFormGroup;
	public patientId: number;
	public today: Date = newDate();
	public internmentEpisode: InternmentSummaryDto;
	public patientTypeData;
	public patientBasicData;
	public personalInformation;
	public personPhoto: PersonPhotoDto;
	public patientMedicalCoverage: PatientMedicalCoverageDto[];
	public selectedBedInfo: BedInfoDto;
	private internmentId;
	private minDateTimeValidator: MinTimeValidator;
	private readonly routePrefix;

	constructor(
		private readonly formBuilder: UntypedFormBuilder,
		private readonly patientService: PatientService,
		private readonly internmentService: InternacionService,
		private readonly mapperService: MapperService,
		private readonly el: ElementRef,
		private readonly router: Router,
		private readonly bed: BedService,
		public dialog: MatDialog,
		public translator: TranslateService,
		private readonly snackBarService: SnackBarService,
		private readonly route: ActivatedRoute,
		private readonly contextService: ContextService,
		private readonly personService: PersonService,
		private readonly patientMedicalCoverageService: PatientMedicalCoverageService,

	) {
		this.routePrefix = `institucion/${this.contextService.institutionId}/`;
	}

	ngOnInit(): void {
		this.route.paramMap.subscribe(
			(params) => {
				this.patientId = Number(params.get('idPaciente'));
				this.internmentId = Number(params.get('idInternacion'));

				this.patient$ = this.patientService.getPatientBasicData<BasicPatientDto>(this.patientId).pipe(
					map(patient => this.mapperService.toPatientBasicData(patient))
				);

				this.patientService.getPatientPhoto(this.patientId)
					.subscribe((personPhotoDto: PersonPhotoDto) => {
						this.personPhoto = personPhotoDto;
					});

				this.internmentService.getInternmentEpisodeSummary(this.internmentId).subscribe(ies => {
					this.internmentEpisode = ies;
					this.form.addControl('originBedId', new UntypedFormControl({ value: this.internmentEpisode.bed.bedNumber, disabled: true }, [Validators.required]));

					this.bed.getLastPatientBedRelocation(this.internmentId).subscribe(patientBedRelocation => {
						const lastRelocationDate = patientBedRelocation ? dateISOParseDate(patientBedRelocation.relocationDate) : null;
						const iesEntryDateTime = dateISOParseDate(ies.entryDate.toString());
						this.minDate = lastRelocationDate && lastRelocationDate > iesEntryDateTime ? lastRelocationDate : iesEntryDateTime;
						this.minDateTimeValidator = new MinTimeValidator(this.minDate);
						this.minTimeStr = toHourMinute(this.minDate);
					});
				});

			});

		this.form = this.formBuilder.group({
			relocationDate: [this.today, [Validators.required]],
			relocationTime: [toHourMinute(this.today), [Validators.required, Validators.pattern(TIME_PATTERN), futureTimeValidation]],
		});

		this.form.controls.relocationDate.valueChanges.subscribe(
			(selectedDate: Date) => {
				const selectedDateString = toApiFormat(selectedDate);
				const todayDateString = toApiFormat(this.today);
				const minDateString = toApiFormat(this.minDate);				
				if (selectedDateString === todayDateString) {
					this.form.controls.relocationTime.setValidators([Validators.required, Validators.pattern(TIME_PATTERN), futureTimeValidation]);
					this.form.controls.relocationTime.updateValueAndValidity();
				} else if (selectedDateString === minDateString) {
					this.form.controls.relocationTime.setValidators([Validators.required, Validators.pattern(TIME_PATTERN), this.minDateTimeValidator.minTimeValidation.bind(this.minDateTimeValidator)]);
					this.form.controls.relocationTime.updateValueAndValidity();
				} else {
					this.form.controls.relocationTime.setValidators([Validators.required, Validators.pattern(TIME_PATTERN)]);
					this.form.controls.relocationTime.updateValueAndValidity();
				}
			}
		);

		this.patientService.getPatientCompleteData<CompletePatientDto>(this.patientId)
			.subscribe(completeData => {
				this.patientTypeData = this.mapperService.toPatientTypeData(completeData.patientType);
				this.patientBasicData = this.mapperService.toPatientBasicData(completeData);
				this.personService.getPersonalInformation<PersonalInformationDto>(completeData.person.id)
					.subscribe(personInformationData => {
						this.personalInformation =
							this.mapperService.toPersonalInformationData(completeData, personInformationData);
					});
			});

		this.patientMedicalCoverageService.getActivePatientMedicalCoverages(this.patientId)
			.subscribe(patientMedicalCoverageDto => this.patientMedicalCoverage = patientMedicalCoverageDto);
	}

	relocationDateDateChanged(date: Date) {
		this.form.controls.relocationDate.setValue(date);
	}

	save(): void {
		if (this.form.valid && this.selectedBedInfo) {
			this.openDialog();
		} else {
			if (!this.selectedBedInfo) {
				this.snackBarService.showError('internaciones.new-internment.messages.ERROR_BED_ASSIGNMENT');
			}
			scrollIntoError(this.form, this.el);
		}
	}

	getRelocationDateTime(): any {
		const newTime: string = this.form.controls.relocationTime.value;
		const newDatetime: Date = this.form.controls.relocationDate.value;
		return buildFullDateFromDate(newTime, newDatetime)
	}

	openDialog(): void {
		this.translator.get('internaciones.bed-relocation.messages.CONFIRMACION').subscribe((res: string) => {
			const dialogRef = this.dialog.open(ConfirmDialogComponent, {
				width: '450px',
				data: {
					title: 'Nuevo pase de cama',
					content: `${res}`,
					okButtonLabel: 'Confirmar pase'
				}
			});

			dialogRef.afterClosed().subscribe(result => {
				if (result) {
					const patientBedRelocation = this.mapToPatientBedRelocationRequest();
					this.bed.relocatePatientBed(patientBedRelocation)
						.subscribe(data => {
							if (data) {
								const url = this.routePrefix + ROUTE_PROFILE + this.patientId;
								this.router.navigate([url]);
								this.snackBarService.showSuccess('internaciones.bed-relocation.messages.SUCCESS');
							} else {
								this.snackBarService.showError('internaciones.bed-relocation.messages.ERROR_DATE');
							}
						}, _ => this.snackBarService.showError('internaciones.bed-relocation.messages.ERROR'));
				}
			});
		});

	}

	openBedAssignmentDialog(): void {

		const dialogRef = this.dialog.open(BedAssignmentComponent, {
			data: {
				sectorsType: [INTERNMENT_SECTOR]
			}
		});

		dialogRef.afterClosed().subscribe((bedInfo: BedInfoDto) => {
			if (bedInfo) {
				this.selectedBedInfo = bedInfo;
			}
		});

	}

	mapToPatientBedRelocationRequest(): PatientBedRelocationDto {
		return {
			originBedId: this.internmentEpisode.bed.id,
			destinationBedId: this.selectedBedInfo.bed.id,
			internmentEpisodeId: this.internmentId,
			originBedFree: true,
			relocationDate: this.getRelocationDateTime(),
		};
	}

}
