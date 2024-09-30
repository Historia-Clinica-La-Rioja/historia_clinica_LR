import { Component } from '@angular/core';
import { PageEvent } from '@angular/material/paginator';
import { AppFeature, StudyOrderWorkListDto } from '@api-rest/api-model';
import { ServiceRequestWorkListControllerService } from '@api-rest/services/service-request-work-list-controller.service';
import { FeatureFlagService } from '@core/services/feature-flag.service';
import { Observable, switchMap } from 'rxjs';


const PAGE_SIZE_OPTIONS = [5, 10, 25];

const LABORATORIO = "108252007";
const DIAGNOSTICO_POR_IMAGEN = "108252007";

@Component({
	selector: 'app-work-order-list',
	templateUrl: './work-order-list.component.html',
	styleUrls: ['./work-order-list.component.scss']
})
export class WorkOrderListComponent {
	pageSizeOptions = PAGE_SIZE_OPTIONS;
	pageSize: Observable<number>;
	ordersCurrentPage = [];
	allOrders = [];
	constructor(
		private serviceRequestWorkListControllerService: ServiceRequestWorkListControllerService,
		private featureFlagService: FeatureFlagService,
	) {
		this.featureFlagService.isActive(AppFeature.HABILITAR_DESARROLLO_RED_IMAGENES)
			.pipe(
				switchMap(ffIsOn => {
					const categories = ffIsOn ? [LABORATORIO] : [LABORATORIO, DIAGNOSTICO_POR_IMAGEN];
					return this.serviceRequestWorkListControllerService.getList(categories);
				})
			)
			.subscribe((allOrders: StudyOrderWorkListDto[]) => {
				this.allOrders = allOrders;
				this.loadFirstPage();
			});
	}

	onPageChange($event: PageEvent) {
		const startPage = $event.pageIndex * $event.pageSize;
		this.ordersCurrentPage = this.allOrders.slice(startPage, $event.pageSize + startPage);
	}

	private loadFirstPage() {
		this.ordersCurrentPage = this.allOrders.slice(0, PAGE_SIZE_OPTIONS[0]);
	}
}
