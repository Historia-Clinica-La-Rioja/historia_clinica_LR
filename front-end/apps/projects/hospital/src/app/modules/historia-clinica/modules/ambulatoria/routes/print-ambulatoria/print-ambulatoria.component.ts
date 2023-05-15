import { Component, OnInit, ViewChild } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { BasicPatientDto, PersonPhotoDto } from '@api-rest/api-model';
import { CHDocumentSummaryDto } from '@api-rest/api-model';
import { CHSearchFilterDto } from '@api-rest/api-model';
import { ECHDocumentType } from '@api-rest/api-model';
import { AppFeature } from '@api-rest/api-model';
import { PatientService } from '@api-rest/services/patient.service';
import { AdditionalInfo } from '@pacientes/pacientes.model';
import { PatientBasicData } from '@presentation/components/patient-card/patient-card.component';
import { MapperService } from '@presentation/services/mapper.service';
import { FormBuilder, FormControl, FormGroup, Validators } from '@angular/forms';
import { DateFormat, momentFormat} from '@core/utils/moment.utils';
import * as moment from 'moment';
import { EncounterTypes, DocumentTypes, ROUTE_HISTORY_CLINIC, EncounterType } from '../../constants/print-ambulatoria-masterdata';
import { ECHEncounterType } from "@api-rest/api-model";
import { AppRoutes } from 'projects/hospital/src/app/app-routing.module';
import { ContextService } from '@core/services/context.service';
import { DatePipeFormat } from '@core/utils/date.utils';
import { DatePipe } from '@angular/common';
import { MatPaginator } from '@angular/material/paginator';
import { MatTableDataSource } from '@angular/material/table';
import { SelectionModel } from '@angular/cdk/collections';
import { AccountService } from '@api-rest/services/account.service';
import { mapToFullName} from '@api-presentation/mappers/user-person-dto.mapper';
import { FeatureFlagService } from '@core/services/feature-flag.service';
import { PrintAmbulatoryService } from '@api-rest/services/print-ambulatory.service';
import { mapDateWithHypenToDateWithSlash } from '@api-rest/mapper/date-dto.mapper';

@Component({
	selector: 'app-print-ambulatoria',
	templateUrl: './print-ambulatoria.component.html',
	styleUrls: ['./print-ambulatoria.component.scss']
})
export class PrintAmbulatoriaComponent implements OnInit {

	datePipeFormat = DatePipeFormat;
	nowDate: string;
	userFullName: string;
	nameSelfDeterminationFF: boolean;

	patient: PatientBasicData;
	patientId: number;
	personInformation: AdditionalInfo[] = [];
	personPhoto: PersonPhotoDto;

	dateRange: {
		start: string,
		end: string,
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

	showTable = true;
	showLastPrinted = false;

	noInfo = false;

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
		private featureFlagService: FeatureFlagService,
		private readonly accountService: AccountService,
		private readonly printAmbulatoryService: PrintAmbulatoryService,
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
		this.featureFlagService.isActive(AppFeature.HABILITAR_DATOS_AUTOPERCIBIDOS).subscribe(isOn => {
			this.nameSelfDeterminationFF = isOn
		});
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
		this.accountService.getInfo()
			.subscribe(userInfo =>
				this.userFullName = mapToFullName(userInfo.personDto, this.nameSelfDeterminationFF)
			);
	}

	dateRangeChange(range): void {
		this.dateRange = {
			start: momentFormat(range.start, DateFormat.API_DATE),
			end: momentFormat(range.end, DateFormat.API_DATE),
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
		if (!this.atLeastOneChecked(this.encounterTypeForm)) {
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
		const selectedEncounterTypes: ECHEncounterType[] = [];
		this.encounterTypes.forEach(elem => {
			if (this.encounterTypeForm.get(elem.value).value)
				selectedEncounterTypes.push(elem.value);
		})

		const selectedDocumentTypes: ECHDocumentType[] = [];
		this.documentTypes.forEach(elem => {
			if (this.documentTypeForm.get(elem.value).value)
				selectedDocumentTypes.push(elem.value);
		})

		const searchFilterStr: CHSearchFilterDto = {
			documentTypeList: selectedDocumentTypes,
			encounterTypeList: selectedEncounterTypes
		}

		this.printAmbulatoryService.getPatientClinicHistory(this.patientId, this.dateRange.start, this.dateRange.end, searchFilterStr)
			.subscribe(response => {
				console.log(response);

				this.noInfo = response.length > 0 ? false : true;
				this.dataSource.data = response.map(data => this.mapToDocumentSummary(data));
				this.dataSource.paginator = this.paginator;
				document.getElementById("encounter-list").style.display = "block";
				this.toggleAllRows();
			});
	}

	private mapToDocumentSummary(data): CHDocumentSummaryDto {
		if (data) {
			return {
				id: data.id,
				startDate: mapDateWithHypenToDateWithSlash(data.startDate.slice(0, 10)),
				endDate: mapDateWithHypenToDateWithSlash(data.endDate.slice(0, 10)),
				encounterType: EncounterType[data.encounterType],
				institution: data.institution,
				problems: data.problems,
				professional: data.professional
			}
		}
		return null;
	}

	download() {
		this.nowDate = this.datePipe.transform(Date.now(), DatePipeFormat.SHORT);
		this.showLastPrinted = true;
	}
}
