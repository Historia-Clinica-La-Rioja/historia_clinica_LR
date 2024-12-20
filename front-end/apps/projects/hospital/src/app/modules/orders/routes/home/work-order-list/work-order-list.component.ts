import { Component, OnInit } from '@angular/core';
import { PageEvent } from '@angular/material/paginator';
import { PageDto, StudyOrderWorkListDto, StudyOrderWorkListFilterDto } from '@api-rest/api-model';
import { ServiceRequestWorkListControllerService } from '@api-rest/services/service-request-work-list-controller.service';
import { PatientType } from '@historia-clinica/constants/summaries';
import { BehaviorSubject, Observable, } from 'rxjs';
import { FilterServiceService } from '../../../services/filter-service.service';

const PAGE_SIZE_OPTIONS = [5, 10, 25];
const PAGE_INIT = 0;
const PAGE_MIN_SIZE = 5;

const LABORATORIO = "108252007";
const EMERGENCY_CARE_TEMPORARY = PatientType.EMERGENCY_CARE_TEMPORARY;

@Component({
	selector: 'app-work-order-list',
	templateUrl: './work-order-list.component.html',
	styleUrls: ['./work-order-list.component.scss'],
})

export class WorkOrderListComponent implements OnInit {
	EMERGENCY_CARE_TEMPORARY = EMERGENCY_CARE_TEMPORARY;
	pageSizeOptions = PAGE_SIZE_OPTIONS;
	pageSize: Observable<number>;
	allOrders = [];
	totalElementsAmount = 0;
	groupedStudies: StudyOrderWorkListDto[] = [];
	pageIndex = PAGE_INIT;
	pageSizeCurrent = PAGE_MIN_SIZE;
	private references = new BehaviorSubject<PageDto<StudyOrderWorkListDto[]>>(null);
	private studyOrderWorkListFilterDto: StudyOrderWorkListFilterDto;
	readonly allOrders$ = this.references.asObservable();
	constructor(
		private serviceRequestWorkListControllerService: ServiceRequestWorkListControllerService,
		private readonly filterServiceService: FilterServiceService,
	) {	}

	ngOnInit() {
		this.filterServiceService.filters$.subscribe((filter: StudyOrderWorkListFilterDto) => {
			this.studyOrderWorkListFilterDto = filter;
			this.getOrders(PAGE_INIT, PAGE_MIN_SIZE, this.studyOrderWorkListFilterDto);
		});
	}

	onPageChange($event: PageEvent) {
		this.pageIndex = $event.pageIndex;
		this.pageSizeCurrent = $event.pageSize;
		this.getOrders($event.pageIndex, $event.pageSize, this.studyOrderWorkListFilterDto);
	}

	private getOrders(pageNumber: number, pageSize: number, studyOrderWorkListFilterDto: StudyOrderWorkListFilterDto ) {
		this.serviceRequestWorkListControllerService.getList([LABORATORIO], pageNumber, pageSize, studyOrderWorkListFilterDto)
			.subscribe((allOrders: PageDto<StudyOrderWorkListDto[]>) => {
				this.references.next(allOrders);
				this.totalElementsAmount = allOrders.totalElementsAmount;
			});
	}
}
