import { Component, OnInit, ElementRef } from '@angular/core';
import { Observable } from 'rxjs';
import { PatientBasicData } from '@presentation/components/patient-card/patient-card.component';
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
import { FormGroup, FormBuilder, Validators, FormControl } from '@angular/forms';
import { MatDialog } from '@angular/material/dialog';
import { TranslateService } from '@ngx-translate/core';
import { SnackBarService } from '@presentation/services/snack-bar.service';
import { scrollIntoError, hasError, futureTimeValidation, MinTimeValidator, TIME_PATTERN } from '@core/utils/form.utils';
import { ConfirmDialogComponent } from '@presentation/dialogs/confirm-dialog/confirm-dialog.component';
import { ContextService } from '@core/services/context.service';
import { BedService } from '@api-rest/services/bed.service';
import { newMoment, momentFormat, DateFormat, momentParseDateTime, momentParseDate, dateToMoment } from '@core/utils/moment.utils';
import { Moment } from 'moment';
import { PersonService } from '@api-rest/services/person.service';
import { BedAssignmentComponent } from '@historia-clinica/dialogs/bed-assignment/bed-assignment.component';
import { PatientMedicalCoverageService } from '@api-rest/services/patient-medical-coverage.service';
import { InternmentEpisodeSummary } from "@historia-clinica/modules/ambulatoria/modules/internacion/components/internment-episode-summary/internment-episode-summary.component";


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
	public minDate;
	public form: FormGroup;
	public patientId: number;
	public today: Moment = newMoment();
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
		private readonly formBuilder: FormBuilder,
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
					this.form.addControl('originBedId', new FormControl({ value: this.internmentEpisode.bed.bedNumber, disabled: true }, [Validators.required]));

					this.bed.getLastPatientBedRelocation(this.internmentId).subscribe(patientBedRelocation => {
						const lastRelocationDate = patientBedRelocation ? momentParseDateTime(patientBedRelocation.relocationDate).toDate() : null;
						const iesEntryDateTime = momentParseDate(ies.entryDate.toString()).toDate();
						this.minDate = dateToMoment(lastRelocationDate && lastRelocationDate > iesEntryDateTime ? lastRelocationDate : iesEntryDateTime);
						this.minDateTimeValidator = new MinTimeValidator(this.minDate);
						this.minTimeStr = momentFormat(this.minDate, DateFormat.HOUR_MINUTE);
					});
				});

			});

		this.form = this.formBuilder.group({
			relocationDate: [this.today, [Validators.required]],
			relocationTime: [momentFormat(this.today, DateFormat.HOUR_MINUTE), [Validators.required, Validators.pattern(TIME_PATTERN), futureTimeValidation]],
		});

		this.form.controls.relocationDate.valueChanges.subscribe(
			(selectedDate: Moment) => {
				const selectedDateString = momentFormat(selectedDate, DateFormat.API_DATE);
				const todayDateString = momentFormat(this.today, DateFormat.API_DATE);
				const minDateString = momentFormat(this.minDate, DateFormat.API_DATE);
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
		const newDatetime: Moment = this.form.controls.relocationDate.value;
		newDatetime.set({
			hour: +newTime.substr(0, 2),
			minute: +newTime.substr(3, 2),
			second: 0,
			millisecond: 0,
		});
		return newDatetime;
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
							}
						}, _ => this.snackBarService.showError('internaciones.bed-relocation.messages.ERROR'));
				}
			});
		});

	}

	openBedAssignmentDialog(): void {

		const dialogRef = this.dialog.open(BedAssignmentComponent, {
			width: '80%'
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
