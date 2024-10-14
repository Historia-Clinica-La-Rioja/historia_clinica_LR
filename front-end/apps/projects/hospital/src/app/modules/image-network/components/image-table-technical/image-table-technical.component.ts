import { Component, OnInit } from '@angular/core';
import { PatientSummary } from '../../../hsi-components/patient-summary/patient-summary.component';
import { Observable, map, tap } from 'rxjs';
import { Color } from '@presentation/colored-label/colored-label.component';
// import { ImageQueueService } from '@api-rest/services/image-queue.service';
import { PatientNameService } from '@core/services/patient-name.service';
import { mapToListItemImageQueue } from '../../constants/image.queue.mapper';
import { ImageQueueManagementStateService } from '../../services/image-queue-management-state.service';

@Component({
	selector: 'app-image-table-technical',
	templateUrl: './image-table-technical.component.html',
	styleUrls: ['./image-table-technical.component.scss']
})
export class ImageTableTechnicalComponent implements OnInit {

	imageList$: Observable<ItemImageQueue[]>
	readonly PAGE_SIZE = 10;
	readonly FIRST_PAGE = 0;
	elementsAmount = 0
	pageIndex = 0



	constructor(
		private readonly patientNameService: PatientNameService,
		private readonly imageQueueManagementStateService: ImageQueueManagementStateService,
	) { }

	ngOnInit(): void {
		this.imageList$ = this.getImageList()
	}

	getImageList(): Observable<ItemImageQueue[]> {
		return 	this.imageQueueManagementStateService.getImageQueueList()
		.pipe(
		tap(pageImageQueue => {this.elementsAmount = pageImageQueue?.totalElementsAmount}),
		map(pageImageQueue => mapToListItemImageQueue(pageImageQueue?.content, this.patientNameService)))
	}

}

export interface ItemImageQueue {
	person: PatientSummary
	studies: string,
	status: {
		description: string,
		color: Color,
		icon?: String
	},
	serviceRequestId: number,
	date: Date,
	appointmentDate: Date,
	uid: string,
	idMove: number
}

export interface PageEvent {
	length: number
	pageIndex: number
	pageSize: number
	previousPageIndex: number
}