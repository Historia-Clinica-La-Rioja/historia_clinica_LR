import { Component, OnInit, ViewChild } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { BasicPatientDto, CHDocumentSummaryDto, DateDto, PersonPhotoDto } from '@api-rest/api-model';
import { PatientService } from '@api-rest/services/patient.service';
import { AdditionalInfo } from '@pacientes/pacientes.model';
import { PatientBasicData } from '@presentation/components/patient-card/patient-card.component';
import { MapperService } from '@presentation/services/mapper.service';
import { FormBuilder, FormControl, FormGroup, Validators } from '@angular/forms';
import { momentToDateDto } from '@core/utils/moment.utils';
import * as moment from 'moment';
import { EncounterTypes, DocumentTypes, ROUTE_HISTORY_CLINIC, mockedTable } from '../../constants/print-ambulatoria-masterdata';
import { ECHEncounterType } from "@api-rest/api-model";
import { AppRoutes } from 'projects/hospital/src/app/app-routing.module';
import { ContextService } from '@core/services/context.service';
import { dateDtoToDate } from '@api-rest/mapper/date-dto.mapper';
import { DatePipeFormat } from '@core/utils/date.utils';
import { DatePipe } from '@angular/common';
import { MatPaginator } from '@angular/material/paginator';
import { MatTableDataSource } from '@angular/material/table';
import { SelectionModel } from '@angular/cdk/collections';

@Component({
	selector: 'app-print-ambulatoria',
	templateUrl: './print-ambulatoria.component.html',
	styleUrls: ['./print-ambulatoria.component.scss']
})
export class PrintAmbulatoriaComponent implements OnInit {

	patient: PatientBasicData;
	patientId: number;
	personInformation: AdditionalInfo[] = [];
	personPhoto: PersonPhotoDto;

	dateRange: {
		start: DateDto,
		end: DateDto,
	}

	maxDate = moment();

	dateRangeForm = new FormGroup({
		start: new FormControl(null, Validators.required),
		end: new FormControl(null, Validators.required),
	});

	encounterTypeForm: FormGroup;
	encounterTypes = EncounterTypes;

	documentTypeForm: FormGroup;
	documentTypes = DocumentTypes;
	allChecked = true;
	showDocuments = true;

	documentSummary: CHDocumentSummaryDto[] = mockedTable;
	showTable = true;

	columns = [
		{
			columnDef: 'start',
			header: 'Inicio',
			cell: (element: CHDocumentSummaryDto) => `${element.startDate}`,
		},
		{
			columnDef: 'end',
			header: 'Fin',
			cell: (element: CHDocumentSummaryDto) => `${element.endDate}`,
		},
		{
			columnDef: 'encounterType',
			header: 'Tipo de encuentro',
			cell: (element: CHDocumentSummaryDto) => `${element.encounterType}`,
		},
		{
			columnDef: 'problem',
			header: 'Problema',
			cell: (element: CHDocumentSummaryDto) => `${element.problems}`,
		},
		{
			columnDef: 'institution',
			header: 'Institucion',
			cell: (element: CHDocumentSummaryDto) => `${element.institution}`,
		},
		{
			columnDef: 'professional',
			header: 'Profesional',
			cell: (element: CHDocumentSummaryDto) => `${element.professional}`,
		}
	];

	displayedColumns = ['select'].concat(this.columns.map(c => c.columnDef).concat('download'));
	dataSource = new MatTableDataSource<CHDocumentSummaryDto>();
	selection = new SelectionModel<CHDocumentSummaryDto>(true, []);

	constructor(
		private readonly route: ActivatedRoute,
		private readonly patientService: PatientService,
		private readonly mapperService: MapperService,
		private readonly formBuilder: FormBuilder,
		private readonly contextService: ContextService,
		private readonly router: Router,
		readonly datePipe: DatePipe,
	) {
		this.route.paramMap.subscribe(
			(params) => {
				this.patientId = Number(params.get('idPaciente'));
				this.patientService.getPatientBasicData<BasicPatientDto>(this.patientId).subscribe(
					patient => {
						this.personInformation.push({ description: patient.person.identificationType, data: patient.person.identificationNumber });
						this.patient = this.mapperService.toPatientBasicData(patient);
					}
				);
				this.patientService.getPatientPhoto(this.patientId)
					.subscribe((personPhotoDto: PersonPhotoDto) => { this.personPhoto = personPhotoDto; });
			}
		);
	}

	@ViewChild(MatPaginator) paginator: MatPaginator;

	ngOnInit(): void {
		this.dateRangeForm.valueChanges.subscribe(range => {
			if (range.start <= range.end)
				this.dateRangeChange(range)
		});

		const encounterTypeControls = {};
		this.encounterTypes.forEach(encounterType => {
			encounterTypeControls[encounterType.value] = this.formBuilder.control(true);
		});

		this.encounterTypeForm = this.formBuilder.group(encounterTypeControls, { validators: this.atLeastOneChecked });

		const documentTypeControls = {};
		documentTypeControls["all"] = this.formBuilder.control(true);
		this.documentTypes.forEach(documentType => {
			documentTypeControls[documentType.value] = this.formBuilder.control(true);
		});

		this.documentTypeForm = this.formBuilder.group(documentTypeControls, { validators: this.atLeastOneChecked });
	}

	dateRangeChange(range): void {
		this.dateRange = {
			start: momentToDateDto(range.start),
			end: momentToDateDto(range.end)
		}
	}

	private atLeastOneChecked(formGroup: FormGroup) {
		const values = Object.values(formGroup.value);
		const isChecked = values.some((value) => value);
		return isChecked ? null : { atLeastOneChecked: true };
	}

	onAllCheckedChange() {
		const allChecked = this.documentTypeForm.get('all').value;
		this.documentTypes.forEach(documentType => {
			this.documentTypeForm.get(documentType.value).setValue(allChecked);
		});
	}

	onDocumentTypeCheckedChange() {
		const allChecked = this.documentTypes.every(documentType => {
			return this.documentTypeForm.get(documentType.value).value;
		});
		this.documentTypeForm.get('all').setValue(allChecked);
	}

	encounterCheckedChange() {
		this.documentTypes = [];
		if (!this.atLeastOneChecked(this.encounterTypeForm)){
			this.documentTypes = DocumentTypes;
			this.showDocuments = true;
		}
		else
			this.showDocuments = false;
	}

	isAllTableSelected() {
		const numSelected = this.selection.selected.length;
		const numRows = this.dataSource.data.length;
		return numSelected === numRows;
	}

	toggleAllRows() {
		if (this.isAllTableSelected()) {
			this.selection.clear();
			return;
		}
		this.selection.select(...this.dataSource.data);
	}

	goBack() {
		const url = `${AppRoutes.Institucion}/${this.contextService.institutionId}/${ROUTE_HISTORY_CLINIC}`;
		this.router.navigate([url]);
	}

	search() {
		const selectedEncounterTypes = this.encounterTypes.filter(e => this.encounterTypeForm.get(e.value).value);
		const selectedDocumentTypes = this.documentTypes.filter(d => this.documentTypeForm.get(d.value).value)
		const data = {
			date: {
				start: this.dateRange.start,
				end: this.dateRange.end
			},
			encounterTypes: selectedEncounterTypes,
			documentTypes: selectedDocumentTypes
		}

		this.dataSource.data = mockedTable;
		this.dataSource.paginator = this.paginator;
		document.getElementById("encounter-list").style.display = "block";
		this.toggleAllRows();
	}
}
