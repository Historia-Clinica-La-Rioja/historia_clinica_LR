import { Component } from '@angular/core';
import { PageEvent } from '@angular/material/paginator';
import { PageDto, StudyOrderWorkListDto } from '@api-rest/api-model';
import { ServiceRequestWorkListControllerService } from '@api-rest/services/service-request-work-list-controller.service';
import { PatientType } from '@historia-clinica/constants/summaries';
import { BehaviorSubject, Observable, } from 'rxjs';


const PAGE_SIZE_OPTIONS = [5, 10, 25];
const PAGE_INIT = 0;
const PAGE_MIN_SIZE = 5;

const LABORATORIO = "108252007";
const EMERGENCY_CARE_TEMPORARY = PatientType.EMERGENCY_CARE_TEMPORARY;

@Component({
	selector: 'app-work-order-list',
	templateUrl: './work-order-list.component.html',
	styleUrls: ['./work-order-list.component.scss']
})

export class WorkOrderListComponent {
	EMERGENCY_CARE_TEMPORARY = EMERGENCY_CARE_TEMPORARY;
	pageSizeOptions = PAGE_SIZE_OPTIONS;
	pageSize: Observable<number>;
	allOrders = [];
	totalElementsAmount = 0;
	groupedStudies: StudyOrderWorkListDto[] = [];
	private references = new BehaviorSubject<PageDto<StudyOrderWorkListDto[]>>(null);
	readonly allOrders$ = this.references.asObservable();
	constructor(
		private serviceRequestWorkListControllerService: ServiceRequestWorkListControllerService,
	) {
		this.getOrders(PAGE_INIT, PAGE_MIN_SIZE)
	}

	onPageChange($event: PageEvent) {
		this.getOrders($event.pageIndex, $event.pageSize);
	}

	private getOrders(pageNumber: number, pageSize: number) {
		this.serviceRequestWorkListControllerService.getList([LABORATORIO], pageNumber, pageSize)
			.subscribe((allOrders: PageDto<StudyOrderWorkListDto[]>) => {
				this.references.next(allOrders);
				this.totalElementsAmount = allOrders.totalElementsAmount;
			});
	}
}
