import { Component, EventEmitter, OnInit, Output } from '@angular/core';
import { UntypedFormBuilder } from '@angular/forms';
import { MatDialog } from '@angular/material/dialog';
import { AppFeature, DateTimeDto, DiagnosisDto } from '@api-rest/api-model';
import { stringToTimeDto } from '@api-rest/mapper/date-dto.mapper';
import { FeatureFlagService } from '@core/services/feature-flag.service';
import { momentToDateDto } from '@core/utils/moment.utils';
import { NewConsultationProcedureFormComponent } from '@historia-clinica/dialogs/new-consultation-procedure-form/new-consultation-procedure-form.component';
import { DiagnosisCreationEditionComponent } from '@historia-clinica/modules/ambulatoria/modules/internacion/dialogs/diagnosis-creation-edition/diagnosis-creation-edition.component';
import { ProcedimientosService } from '@historia-clinica/services/procedimientos.service';
import { SnomedService } from '@historia-clinica/services/snomed.service';
import { SnackBarService } from '@presentation/services/snack-bar.service';
import { Moment } from 'moment';

@Component({
	selector: 'app-surgical-report-procedures',
	templateUrl: './surgical-report-procedures.component.html',
	styleUrls: ['./surgical-report-procedures.component.scss']
})

export class SurgicalReportProceduresComponent implements OnInit {

	procedureService = new ProcedimientosService(this.formBuilder, this.snomedService, this.snackBarService);
	searchConceptsLocallyFF = false;
	diagnosis: DiagnosisDto[] = [];

	@Output() startDateTimeChange = new EventEmitter();
	@Output() endDateTimeChange = new EventEmitter();
	@Output() proceduresChange = new EventEmitter();
	@Output() postoperativeDiagnosisChange = new EventEmitter();
	@Output() descriptionChange = new EventEmitter();

	startDateTime: DateTimeDto = {
		date: {
			day: 0,
			month: 0,
			year: 0
		},
		time: {
			hours: 0,
			minutes: 0
		}
	};

	endDateTime: DateTimeDto = {
		date: {
			day: 0,
			month: 0,
			year: 0
		},
		time: {
			hours: 0,
			minutes: 0
		}
	};

	skinOpeningTime: string;
	skinClosureTime: string;
	description: string;

	constructor(
		private readonly snackBarService: SnackBarService,
		private readonly snomedService: SnomedService,
		private readonly formBuilder: UntypedFormBuilder,
		private readonly dialog: MatDialog,
		private readonly featureFlagService: FeatureFlagService
	) {
		this.featureFlagService.isActive(AppFeature.HABILITAR_BUSQUEDA_LOCAL_CONCEPTOS).subscribe(isOn => {
			this.searchConceptsLocallyFF = isOn;
		})
		this.procedureService.procedimientos$.subscribe(procedures => this.changeProcedure(procedures));
	}

	ngOnInit(): void {
	}

	addProcedure() {
		this.dialog.open(NewConsultationProcedureFormComponent, {
			data: {
				procedureService: this.procedureService,
				searchConceptsLocallyFF: this.searchConceptsLocallyFF,
				hideDate: true
			},
			autoFocus: false,
			width: '35%',
			disableClose: true,
		});
	}

	addDiagnosis(): void {
		const dialogRef = this.dialog.open(DiagnosisCreationEditionComponent, {
			width: '450px',
			data: {
				type: 'CREATION',
				isMainDiagnosis: false
			}
		});

		dialogRef.afterClosed().subscribe(diagnose => {
			if (diagnose) {
				this.diagnosis.push(diagnose);
				this.postoperativeDiagnosisChange.emit(this.diagnosis);
			}
		});
	}

	deleteDiagnosis(index: number): void {
		this.diagnosis.splice(index, 1);
	}

	changeStartDateTime(date: Moment, time: string): void {
		if (date)
			this.startDateTime.date = momentToDateDto(date);
		if (time)
			this.startDateTime.time = stringToTimeDto(time);
		this.startDateTimeChange.emit(this.startDateTime);
	}

	changeEndDateTime(date: Moment, time: string): void {
		if (date)
			this.endDateTime.date = momentToDateDto(date);
		if (time)
			this.endDateTime.time = stringToTimeDto(time);
		this.endDateTimeChange.emit(this.endDateTime);
	}

	changeDescription(description): void {
		this.descriptionChange.emit(description);
	}

	private changeProcedure(procedures): void {
		this.proceduresChange.emit(procedures);
	}
}
