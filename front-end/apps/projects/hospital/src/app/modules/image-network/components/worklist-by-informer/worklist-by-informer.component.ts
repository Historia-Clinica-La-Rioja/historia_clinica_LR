import { Component, OnInit } from '@angular/core';
import { Worklist } from '../worklist/worklist.component';
import { FeatureFlagService } from '@core/services/feature-flag.service';
import { IdentificationTypeDto, MasterDataDto, ModalityDto, WorklistDto } from '@api-rest/api-model';
import { AppFeature } from '@api-rest/api-model';
import { ModalityService } from '@api-rest/services/modality.service';
import { Observable, combineLatest, map, of, skip, startWith } from 'rxjs';
import { WorklistService } from '@api-rest/services/worklist.service';
import { PersonMasterDataService } from '@api-rest/services/person-master-data.service';
import { MatSelectChange } from '@angular/material/select';
import { dateTimeDtotoLocalDate } from '@api-rest/mapper/date-dto.mapper';
import { InformerStatus, mapToState } from '../../utils/study.utils';
import { Router } from '@angular/router';
import { ContextService } from '@core/services/context.service';
import { FormControl, UntypedFormBuilder, UntypedFormGroup, Validators } from '@angular/forms';
import { NUMBER_PATTERN } from '@core/utils/form.utils';
import { REPORT_STATES_ID } from '../../constants/report';
import { WorklistFacadeService } from '../../services/worklist-facade.service';
import { toApiFormat } from '@api-rest/mapper/date.mapper';

const PAGE_SIZE_OPTIONS = [10];
const PAGE_MIN_SIZE = 10;
const PATIENT_INFORMATION = 'patientInformation';
const IDENTIFICATION = 'identification';
const INSTITUTION_NAME = 'institutionName';
const PATIENT_NAME = "fullName";

@Component({
	selector: 'app-worklist-by-informer',
	templateUrl: './worklist-by-informer.component.html',
	styleUrls: ['./worklist-by-informer.component.scss'],
    providers: [WorklistFacadeService]
})
export class WorklistByInformerComponent implements OnInit {

	filterForm: UntypedFormGroup;
	worklists: Worklist[] = [];
	workListsFiltered: Worklist[] = [];
	modalityId: number;
	nameSelfDeterminationFF = false;
	modalities$: Observable<ModalityDto[]>;
	identificationTypes: IdentificationTypeDto[] = [];
	routePrefix: string;

	pageSlice = [];
	pageSizeOptions = PAGE_SIZE_OPTIONS;

	readonly COMPLETED = InformerStatus.COMPLETED;
	readonly PENDING = InformerStatus.PENDING;

	dateRangeForm = new UntypedFormGroup({
		start: new FormControl<Date>(new Date(), Validators.required),
		end: new FormControl<Date>(new Date(), Validators.required),
	});
	dateRangeMax: Date = new Date();
	dateRangeMin: Date

	isFilterExpanded: boolean = false;
	statuses$: Observable<MasterDataDto[]>;

	constructor(
		private readonly featureFlagService: FeatureFlagService,
		private readonly modalityService: ModalityService,
		private readonly worklistService: WorklistService,
		private readonly personMasterData: PersonMasterDataService,
		private readonly router: Router,
		private readonly contextService: ContextService,
		private readonly formBuilder: UntypedFormBuilder,
		private readonly worklistFacadeService: WorklistFacadeService,
	) { }

	ngOnInit(): void {
		this.routePrefix = `institucion/${this.contextService.institutionId}/imagenes/lista-trabajos`;
		this.personMasterData.getIdentificationTypes().subscribe(types => this.identificationTypes = types);
		this.modalities$ = this.modalityService.getAll();
		this.featureFlagService.isActive(AppFeature.HABILITAR_DATOS_AUTOPERCIBIDOS).subscribe(isOn => {
			this.nameSelfDeterminationFF = isOn
		});
		this.filterForm = this.formBuilder.group({
			modalities: [],
			patientName: new FormControl<string>(null),
			patientDocument: new FormControl<string>(null, Validators.pattern(NUMBER_PATTERN)),
			institution: new FormControl<string>(null),
			status: new FormControl<number>(REPORT_STATES_ID.PENDING),
		});
		this.worklistFacadeService.reports$.subscribe(reports => {
			this.worklists = this.mapToWorklist(reports);
			this.workListsFiltered = this.worklists;
			this.pageSlice = this.worklists.slice(0, PAGE_MIN_SIZE);
			this.setFilters();
		})
		this.setWorkList();
		this.setDateRanges();
		this.setStatuses();
		this.onChanges();
	}

	selectModality(modalitySelected: MatSelectChange) {
		this.modalityId = modalitySelected.value;
		this.setWorkList();
	}

	goToDetails(appointmentId: number) {
		this.router.navigate([`${this.routePrefix}/detalle-estudio/${appointmentId}`], );
	}

	cleanModalities(){
		this.filterForm.controls.modalities.setValue(null);
		this.modalityId = null;
		this.setWorkList();
	}

	cleanStatuses() {
		this.filterForm.controls.status.setValue(null);
	}

	setWorkList() {
		this.worklistFacadeService.changeInformerFilters(
			this.modalityId,
			toApiFormat(new Date(this.dateRangeForm.value.start)),
			toApiFormat(new Date(this.dateRangeForm.value.end)))
	}

	toggleFilter(value: boolean) {
		this.isFilterExpanded = value;
	}

	setStatuses() {
		this.statuses$ = this.worklistService.getWorklistStatus()
		.pipe(
			map(data => data.filter(item => item.id === REPORT_STATES_ID.PENDING || item.id === REPORT_STATES_ID.COMPLETED))
		);
	}

	onChanges(): void {
		combineLatest([
			this.filterForm.get('patientName').valueChanges.pipe(startWith(null)),
			this.filterForm.get('patientDocument').valueChanges.pipe(startWith(null)),
			this.filterForm.get('status').valueChanges.pipe(startWith(null)),
			this.filterForm.get('institution').valueChanges.pipe(startWith(null)),
		])
		.pipe(skip(1))
		.subscribe(_ => this.setFilters())
	}

	private getValueControl(key: string) {
		return this.filterForm.get(key).value;
	}

	private setFilters() {
		const filteredWorklist: Observable<Worklist[]> = combineLatest([
			this.getValueControl('patientName')
				? this.filteredByString(this.getValueControl('patientName'), PATIENT_INFORMATION, PATIENT_NAME)
			  	: of(this.worklists),

			this.getValueControl('status')
			  	? this.filteredByStatus()
			  	: of(this.worklists),

			this.getValueControl('patientDocument')
				? this.filteredByString(this.getValueControl('patientDocument'), PATIENT_INFORMATION, IDENTIFICATION)
				: of(this.worklists),

			this.getValueControl('institution')
				? this.filteredByString(this.getValueControl('institution'), INSTITUTION_NAME)
				: of(this.worklists),

		  ]).pipe(
				map(([nameFiltered, statusFiltered, documentFiltered, institutionFiltered]) =>
					nameFiltered.filter((item) => statusFiltered.includes(item) && documentFiltered.includes(item) && institutionFiltered.includes(item))
				)
		  	);
		filteredWorklist.subscribe((result: Worklist[]) => {
			this.workListsFiltered = result;
			this.pageSlice = result.slice(0, PAGE_MIN_SIZE)
		});
	}

	private filteredByString(filterBy: string, firstProperty: string, secondProperty?: string) {
		return of(this.worklists).pipe(
			map((data) => data.filter((item) => this.compareByString(item, filterBy, firstProperty, secondProperty)))
		);
	}

	private compareByString(item: Worklist, filterBy: string, firstProperty: string, secondProperty?: string) {
		if (secondProperty)
			return item[firstProperty][secondProperty].trim().toLocaleLowerCase().includes(filterBy.toLocaleLowerCase())
		else
			return item[firstProperty].trim().toLocaleLowerCase().includes(filterBy.toLocaleLowerCase())
	}

	private filteredByStatus() {
		return of(this.worklists).pipe(
			map((data) => data.filter((item) => this.compareByStatus(item)))
		);
	}

	private compareByStatus(item: Worklist) {
		return item.state.id === this.filterForm.get('status').value;
	}

	private setDateRanges() {
		this.dateRangeMax.setDate(this.dateRangeMax.getDate());
	}

	private mapToWorklist(worklist: WorklistDto[]): Worklist[] {
		return worklist.map(w => {
			return {
				patientInformation: {
					fullName: this.capitalizeName(w.patientFullName),
					identification: `${this.getIdentificationType(w.patientIdentificationTypeId)} ${w.patientIdentificationNumber ? w.patientIdentificationNumber : 'Sin información'} - ID ${w.patientId}`,
				},
				state: mapToState(w.statusId),
				date: dateTimeDtotoLocalDate(w.actionTime),
				appointmentId: w.appointmentId,
				institutionName: w.completionInstitution.name,
			}
		})
	}

	private capitalizeName(name: string): string {
		let capitalizedName = '';
		name.split(" ").map(name => capitalizedName += this.capitalizeWords(name) + " ")
		return capitalizedName;
	}

	private capitalizeWords(sentence: string) {
        return sentence ? sentence
          .toLowerCase()
          .split(' ')
          .map((word) => word.charAt(0).toUpperCase() + word.slice(1))
          .join(' ') : "";
    }

	private getIdentificationType(id: number): string {
		return this.identificationTypes.find(identificationType => identificationType.id === id).description
	}

	onPageChange($event: any) {
		const page = $event;
		const startPage = page.pageIndex * page.pageSize;
		this.pageSlice = this.workListsFiltered.slice(startPage, $event.pageSize + startPage);
	}
}
