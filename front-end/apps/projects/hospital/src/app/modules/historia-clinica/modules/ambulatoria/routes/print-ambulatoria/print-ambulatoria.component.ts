import { Component, OnInit, ViewChild } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { BasicPatientDto, PersonPhotoDto } from '@api-rest/api-model';
import { CHDocumentSummaryDto } from '@api-rest/api-model';
import { CHSearchFilterDto } from '@api-rest/api-model';
import { ECHDocumentType } from '@api-rest/api-model';
import { AppFeature } from '@api-rest/api-model';
import { PatientService } from '@api-rest/services/patient.service';
import { AdditionalInfo } from '@pacientes/pacientes.model';
import { PatientBasicData } from '@presentation/utils/patient.utils';
import { MapperService } from '@presentation/services/mapper.service';
import { FormBuilder, FormControl, FormGroup, Validators } from '@angular/forms';import { toApiFormat } from '@api-rest/mapper/date.mapper';
import { newDate } from '@core/utils/moment.utils';
import { fixDate } from '@core/utils/date/format';
import { EncounterTypes, DocumentTypes, ROUTE_HISTORY_CLINIC, EncounterType, DocumentType, TableColumns } from '../../constants/print-ambulatoria-masterdata';
import { ECHEncounterType } from "@api-rest/api-model";
import { AppRoutes } from 'projects/hospital/src/app/app-routing.module';
import { ContextService } from '@core/services/context.service';
import { fromStringToDate } from '@core/utils/date.utils';
import { DatePipe } from '@angular/common';
import { MatPaginator } from '@angular/material/paginator';
import { MatTableDataSource } from '@angular/material/table';
import { SelectionModel } from '@angular/cdk/collections';
import { FeatureFlagService } from '@core/services/feature-flag.service';
import { PrintAmbulatoryService } from '@api-rest/services/print-ambulatory.service';
import { dateTimeDtotoLocalDate, mapDateWithHypenToDateWithSlash } from '@api-rest/mapper/date-dto.mapper';
import { finalize, Observable, take } from 'rxjs';
import { MatSort, MatSortable } from '@angular/material/sort';
import { ButtonType } from '@presentation/components/button/button.component';
import { SnackBarService } from '@presentation/services/snack-bar.service';

@Component({
	selector: 'app-print-ambulatoria',
	templateUrl: './print-ambulatoria.component.html',
	styleUrls: ['./print-ambulatoria.component.scss']
})
export class PrintAmbulatoriaComponent implements OnInit {

	nowDate: string;
	userLastDownload: string;
	dateLastDownload: Date;
	nameSelfDeterminationFF: boolean;

	patient: PatientBasicData;
	patientId: number;
	patientDni: string;
	personInformation: AdditionalInfo[] = [];
	personPhoto$: Observable<PersonPhotoDto>

	dateRange: {
		start: string,
		end: string,
	}

	maxDate = newDate();

	dateRangeForm = new FormGroup({
		start: new FormControl(null, Validators.required),
		end: new FormControl(null, Validators.required),
	});

	encounterTypeForm: FormGroup;
	encounterTypes = EncounterTypes;

	documentTypeForm: FormGroup;
	documentTypes = DocumentTypes;
    filteredDocumentTypes = DocumentTypes;
	allChecked = true;
	showDocuments = true;

	showLastPrinted = false;

	noInfo = false;
	loadingTable = false;

	displayedColumns: string[] = TableColumns;
	dataSource: MatTableDataSource<CHDocumentSummaryDto>;

	selection = new SelectionModel<CHDocumentSummaryDto>(true, []);

	isDownloadingDocuments = false;
	ButtonType = ButtonType.RAISED;
	MAX_DOCUMENTS_TO_BE_SELECTED = 20;

	constructor(
		private readonly route: ActivatedRoute,
		private readonly patientService: PatientService,
		private readonly mapperService: MapperService,
		private readonly formBuilder: FormBuilder,
		private readonly contextService: ContextService,
		private readonly router: Router,
		readonly datePipe: DatePipe,
		private featureFlagService: FeatureFlagService,
		private readonly printAmbulatoryService: PrintAmbulatoryService,
		private readonly snackBar: SnackBarService
	) {
		this.route.paramMap.pipe(take(1)).subscribe(
			(params) => {
				this.patientId = Number(params.get('idPaciente'));
				this.patientService.getPatientBasicData<BasicPatientDto>(this.patientId).subscribe(
					patient => {
						this.personInformation.push({ description: patient.person.identificationType, data: patient.person.identificationNumber });
						this.patient = this.mapperService.toPatientBasicData(patient);
						this.patientDni = patient.person.identificationNumber;
					}
				);
				this.personPhoto$ = this.patientService.getPatientPhoto(this.patientId);
			}
		);
		this.featureFlagService.isActive(AppFeature.HABILITAR_DATOS_AUTOPERCIBIDOS).subscribe(isOn => {
			this.nameSelfDeterminationFF = isOn
		});

		this.featureFlagService.isActive(AppFeature.HABILITAR_PARTE_ANESTESICO_EN_DESARROLLO).pipe(take(1)).subscribe(isOn => {
			if (!isOn) {
				this.documentTypes = this.documentTypes.filter(e => e.value !== ECHDocumentType.ANESTHETIC_REPORTS);
                this.filteredDocumentTypes = this.documentTypes;
			}
		});
	}

	@ViewChild(MatPaginator) paginator: MatPaginator;
	@ViewChild(MatSort) sort: MatSort;

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
			start: toApiFormat(fixDate(range.start)),
			end: toApiFormat(fixDate(range.end)),
		}
		this.hideEncounterListSection();
	}

	private updateLastDownload(): void {
		this.printAmbulatoryService.getPatientClinicHistoryLastDownload(this.patientId).subscribe(response => {
			if (response.user && response.downloadDate) {
				this.showLastPrinted = true;
				this.userLastDownload = response.user;
				this.dateLastDownload = dateTimeDtotoLocalDate(response.downloadDate);
			}
		});
	}

	private atLeastOneChecked(formGroup: FormGroup) {
		const values = Object.values(formGroup.value);
		const isChecked = values.some((value) => value);
		return isChecked ? null : { atLeastOneChecked: true };
	}

	onAllCheckedChange(): void {
		const allChecked = this.documentTypeForm.get('all').value;
		this.documentTypes.forEach(documentType => {
			this.documentTypeForm.get(documentType.value).setValue(allChecked);
		});
		this.hideEncounterListSection();
	}

	onDocumentTypeCheckedChange(): void {
		const allChecked = this.documentTypes.every(documentType => {
			return this.documentTypeForm.get(documentType.value).value;
		});
		this.documentTypeForm.get('all').setValue(allChecked);
		this.hideEncounterListSection();
	}

	encounterCheckedChange(): void {
		this.documentTypes = [];
		if (!this.atLeastOneChecked(this.encounterTypeForm)) {
			this.documentTypes = this.filteredDocumentTypes;
			this.showDocuments = true;
		}
		else
			this.showDocuments = false;
		this.hideEncounterListSection();
	}

	goBack(): void {
		const url = `${AppRoutes.Institucion}/${this.contextService.institutionId}/${ROUTE_HISTORY_CLINIC}`;
		this.router.navigate([url]);
	}

	private hideEncounterListSection() {
		document.getElementById("encounterList").style.display = "none";
	}

	private showEncounterListSection() {
		document.getElementById("encounterList").style.display = "block";
	}

	search(): void {
		this.hideEncounterListSection();
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

		this.loadingTable = true;
		this.printAmbulatoryService.getPatientClinicHistory(this.patientId, this.dateRange.start, this.dateRange.end, searchFilterStr)
			.subscribe(response => {
				this.noInfo = response.length > 0 ? false : true;
				const tableData = response.map(data => this.mapToDocumentSummary(data));
				this.dataSource = new MatTableDataSource(tableData);
				this.dataSource.paginator = this.paginator;
				this.dataSource.sortingDataAccessor = (item, property) => {
					switch (property) {
						case 'startDate':
							return new Date(fromStringToDate(item.startDate));
						case 'endDate':
							return new Date(fromStringToDate(item.endDate));
						default: return item[property];
					}
				};
				this.sort?.sort(({ id: 'startDate', start: 'desc' }) as MatSortable);
				this.dataSource.sort = this.sort;
				this.showEncounterListSection();
				this.selection.clear();
				this.loadingTable = false;
				this.updateLastDownload();
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
				professional: data.professional,
				documentType: DocumentType[data.documentType]
			}
		}
		return null;
	}

	downloadSelected() {
		const activeSortColumn = this.sort.active;
		const activeSortDirection = this.sort.direction;
		const selectedItems = [...this.selection.selected];

		selectedItems.sort((a, b) => {
			const isAsc = activeSortDirection === 'asc';
			const dateA = fromStringToDate(a[activeSortColumn]);
			const dateB = fromStringToDate(b[activeSortColumn]);

			if (isAsc) {
				return dateA.getTime() - dateB.getTime();
			} else {
				return dateB.getTime() - dateA.getTime();
			}
		});

		const selectedIds = selectedItems.map(item => item.id);
		this.isDownloadingDocuments = true;
		this.printAmbulatoryService.downloadClinicHistory(this.patientDni, selectedIds)
			.pipe(finalize(() => this.isDownloadingDocuments = false))
			.subscribe(()=>this.updateLastDownload());
	}

	toggleCheck = (row) => {
		this.selection.toggle(row);
		this.checkMaxDocumentsQuantity();
	}

	private checkMaxDocumentsQuantity = () => {
		if (this.selection.selected.length >= this.MAX_DOCUMENTS_TO_BE_SELECTED) 
			this.snackBar.showError("ambulatoria.print.encounter-list.MAX_DOCUMENTS_SELECTED");
	}
}
