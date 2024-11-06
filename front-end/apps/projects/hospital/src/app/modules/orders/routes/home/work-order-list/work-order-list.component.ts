import { Component } from '@angular/core';
import { PageEvent } from '@angular/material/paginator';
import { AppFeature, PageDto, StudyOrderWorkListDto } from '@api-rest/api-model';
import { ServiceRequestWorkListControllerService } from '@api-rest/services/service-request-work-list-controller.service';
import { FeatureFlagService } from '@core/services/feature-flag.service';
import { BehaviorSubject, Observable, switchMap } from 'rxjs';


const PAGE_SIZE_OPTIONS = [5, 10, 25];
const PAGE_INIT = 0;
const PAGE_MIN_SIZE = 5;

const LABORATORIO = "108252007";
const DIAGNOSTICO_POR_IMAGEN = "363679005";

@Component({
	selector: 'app-work-order-list',
	templateUrl: './work-order-list.component.html',
	styleUrls: ['./work-order-list.component.scss']
})

export class WorkOrderListComponent {
	pageSizeOptions = PAGE_SIZE_OPTIONS;
	pageSize: Observable<number>;
	allOrders = [];
	totalElementsAmount = 0;
	groupedStudies: StudyOrderWorkListDto[] = [];
	private categories = [];
	private references = new BehaviorSubject<PageDto<StudyOrderWorkListDto[]>>(null);
	readonly allOrders$ = this.references.asObservable();
	constructor(
		private serviceRequestWorkListControllerService: ServiceRequestWorkListControllerService,
		private featureFlagService: FeatureFlagService,
	) {
		this.firstGetOrders(PAGE_INIT, PAGE_MIN_SIZE)
	}

	onPageChange($event: PageEvent) {
		this.getOrders($event.pageIndex, $event.pageSize);
	}

	private firstGetOrders(pageNumber: number, pageSize: number) {
		this.featureFlagService.isActive(AppFeature.HABILITAR_DESARROLLO_RED_IMAGENES)
			.pipe(
				switchMap((ffIsOn: boolean) => {
					this.categories = ffIsOn ? [LABORATORIO] : [LABORATORIO, DIAGNOSTICO_POR_IMAGEN];
					return this.serviceRequestWorkListControllerService.getList(this.categories, pageNumber, pageSize);
				})
			)
			.subscribe((allOrders: PageDto<StudyOrderWorkListDto[]>) => {
				this.references.next(allOrders);
				this.totalElementsAmount = allOrders.totalElementsAmount;
			});
	}

	private getOrders(pageNumber: number, pageSize: number) {
		this.serviceRequestWorkListControllerService.getList(this.categories, pageNumber, pageSize)
			.subscribe((allOrders: PageDto<StudyOrderWorkListDto[]>) => {
				this.references.next(allOrders);
				this.totalElementsAmount = allOrders.totalElementsAmount;
			});
	}
}
