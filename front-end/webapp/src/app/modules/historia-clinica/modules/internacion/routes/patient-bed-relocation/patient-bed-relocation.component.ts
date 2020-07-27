import { Component, OnInit, ElementRef } from "@angular/core";
import { Observable } from "rxjs";
import { PatientBasicData } from '@presentation/components/patient-card/patient-card.component';
import { InternmentEpisodeSummary } from '@presentation/components/internment-episode-summary/internment-episode-summary.component';
import { PatientService } from '@api-rest/services/patient.service';
import { InternacionService } from '@api-rest/services/internacion.service';
import { MapperService } from '@presentation/services/mapper.service';
import { ActivatedRoute, Router } from '@angular/router';
import { BasicPatientDto, InternmentSummaryDto, CompletePatientDto, PersonalInformationDto, PatientBedRelocationDto } from '@api-rest/api-model';
import { map } from 'rxjs/operators';
import { FormGroup, FormBuilder, Validators, FormControl } from '@angular/forms';
import { InternacionMasterDataService } from '@api-rest/services/internacion-master-data.service';
import { MatDialog } from '@angular/material/dialog';
import { TranslateService } from '@ngx-translate/core';
import { SnackBarService } from '@presentation/services/snack-bar.service';
import { scrollIntoError, hasError, futureTimeValidation, MinTimeValidator, TIME_PATTERN } from '@core/utils/form.utils';
import { ConfirmDialogComponent } from '@core/dialogs/confirm-dialog/confirm-dialog.component';
import { ContextService } from '@core/services/context.service';
import { BedService } from '@api-rest/services/bed.service';
import { newMoment, momentFormat, DateFormat, momentParseDateTime, momentParseDate, dateToMoment } from '@core/utils/moment.utils';
import { Moment } from 'moment';
import { PersonService } from '@api-rest/services/person.service';


const ROUTE_INTERNMENT = 'internaciones/internacion/';

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
	public specialties;
	public beds;
	public patientId: number;
	public today: Moment = newMoment();
	public internmentEpisode: InternmentSummaryDto;
	public patientTypeData;
	public patientBasicData;
	public personalInformation;
	private internmentId;
	private minDateTimeValidator : MinTimeValidator;
	private readonly routePrefix;

	constructor(
		private readonly formBuilder: FormBuilder,
		private readonly patientService: PatientService,
		private readonly internmentService: InternacionService,
		private readonly mapperService: MapperService,
		private readonly el: ElementRef,
		private readonly router: Router,
		private readonly internacionMasterDataService: InternacionMasterDataService,
		private readonly bed: BedService,
		public dialog: MatDialog,
		public translator: TranslateService,
		private readonly snackBarService: SnackBarService,
		private readonly route: ActivatedRoute,
		private readonly contextService: ContextService,
		private readonly personService: PersonService,
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

				this.internmentService.getInternmentEpisodeSummary(this.internmentId).subscribe(ies => {
					this.internmentEpisode = ies;
					this.form.addControl('originBedId', new FormControl({ value: this.internmentEpisode.bed.bedNumber, disabled: true }, [Validators.required]));

					this.bed.getLastPatientBedRelocation(this.internmentId).subscribe(patientBedRelocation => {
						const lastRelocationDate = patientBedRelocation ? momentParseDateTime(patientBedRelocation.relocationDate).toDate() : null;
						const iesEntryDateTime =momentParseDate(ies.entryDate.toString()).toDate();
						this.minDate = dateToMoment(lastRelocationDate && lastRelocationDate > iesEntryDateTime ? lastRelocationDate : iesEntryDateTime);
						this.minDateTimeValidator = new MinTimeValidator(this.minDate);
						this.minTimeStr = momentFormat(this.minDate, DateFormat.HOUR_MINUTE);
					})
				});

			});

		this.form = this.formBuilder.group({
			destinationSpecialtyId: [null, [Validators.required]],
			destinationBedId: [{ value: null, disabled: true }, [Validators.required]],
			relocationDate: [this.today, [Validators.required]],
			relocationTime: [momentFormat(this.today, DateFormat.HOUR_MINUTE), [Validators.required, Validators.pattern(TIME_PATTERN), futureTimeValidation]],

		});

		this.form.controls['relocationDate'].valueChanges.subscribe(
			(selectedDate: Moment) => {
				const selectedDateString = momentFormat(selectedDate, DateFormat.API_DATE);
				const todayDateString = momentFormat(this.today, DateFormat.API_DATE);
				const minDateString = momentFormat(this.minDate, DateFormat.API_DATE);
				if (selectedDateString === todayDateString) {
					this.form.controls['relocationTime'].setValidators([Validators.required, Validators.pattern(TIME_PATTERN), futureTimeValidation]);
					this.form.controls['relocationTime'].updateValueAndValidity();
				} else if (selectedDateString === minDateString) {
					this.form.controls['relocationTime'].setValidators([Validators.required, Validators.pattern(TIME_PATTERN), this.minDateTimeValidator.minTimeValidation.bind(this.minDateTimeValidator)]);
					this.form.controls['relocationTime'].updateValueAndValidity();
				} else {
					this.form.controls['relocationTime'].setValidators([Validators.required, Validators.pattern(TIME_PATTERN)]);
					this.form.controls['relocationTime'].updateValueAndValidity();
				}
			}
		);

		this.internacionMasterDataService.getClinicalSpecialty().subscribe(data => {
			this.specialties = data;
		});

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
	}

	setBeds() {
		const bedCategoryId = this.form.controls.destinationSpecialtyId.value;
		this.bed.getAllBedsByCategory(bedCategoryId).subscribe(data => {
			this.beds = data;
			this.form.controls.destinationBedId.markAsTouched();
		});
		this.form.controls.destinationBedId.reset();
		this.form.controls.destinationBedId.enable();
	}

	save(): void {
		if (this.form.valid) {
			this.openDialog();
		} else {
			scrollIntoError(this.form, this.el);
		}
	}

	getRelocationDateTime(): any {
		const newTime: string = this.form.controls['relocationTime'].value;
		const newDatetime: Moment = this.form.controls['relocationDate'].value;
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
								const url = `${this.routePrefix}${ROUTE_INTERNMENT}${this.internmentId}/paciente/${this.patientId}`;
								this.router.navigate([url]);
								this.snackBarService.showSuccess('internaciones.bed-relocation.messages.SUCCESS');
							}
						}, _ => this.snackBarService.showError('internaciones.bed-relocation.messages.ERROR'));
				}
			});
		});

	}

	mapToPatientBedRelocationRequest(): PatientBedRelocationDto {
		return {
			originBedId: this.internmentEpisode.bed.id,
			destinationBedId: this.form.controls['destinationBedId'].value,
			internmentEpisodeId: this.internmentId,
			originBedFree: true,
			relocationDate: this.getRelocationDateTime(),
		}
	}

}
