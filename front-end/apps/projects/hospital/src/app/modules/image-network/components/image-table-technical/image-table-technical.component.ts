import { Component, OnInit } from '@angular/core';
import { PatientSummary } from '../../../hsi-components/patient-summary/patient-summary.component';
import { Size } from '@presentation/components/item-summary/item-summary.component';
import { BehaviorSubject, Observable, map, tap } from 'rxjs';
import { Color } from '@presentation/colored-label/colored-label.component';
import { ButtonType } from '@presentation/components/button/button.component';
import { ImageQueueService } from '@api-rest/services/image-queue.service';
import { PatientNameService } from '@core/services/patient-name.service';
import { QUEUE_ERROR, mapToItemImageQueue } from '../../constants/image.queue.mapper';

@Component({
	selector: 'app-image-table-technical',
	templateUrl: './image-table-technical.component.html',
	styleUrls: ['./image-table-technical.component.scss']
})
export class ImageTableTechnicalComponent implements OnInit {

	imageList$: Observable<ItemImageQueue[]>
	size: Size = Size.SMALL
	ERROR_IMAGE = QUEUE_ERROR
	readonly PAGE_SIZE = 10;
	readonly FIRST_PAGE = 0;
	readonly ButtonType = ButtonType;
	elementsAmount = 0
	pageIndex = 0
	SIN_INFORMACION = 'Sin información'
	private pageIndexState: BehaviorSubject<PageEvent> = new BehaviorSubject<PageEvent>({
		length: null,
		pageIndex: this.FIRST_PAGE,
		pageSize: null,
		previousPageIndex: null
	})


	constructor(
		private imageQueueService: ImageQueueService,
		private patientNameService: PatientNameService,
	) { }

	ngOnInit(): void {
		this.imageList$ = this.getImageListPage(this.FIRST_PAGE)
	}


	getImageListPage(pageIndex: number): Observable<ItemImageQueue[]> {
		return 	this.imageQueueService.getImageQueueList(this.PAGE_SIZE, pageIndex)
		.pipe(
		tap(pageImageQueue => {this.elementsAmount = pageImageQueue.totalElementsAmount}),
		map(pageImageQueue => mapToItemImageQueue(pageImageQueue.content, this.patientNameService)))
	}

	tryAgain(image: ItemImageQueue): void {
		this.imageQueueService.updateImageQueueRetry(image.idMove).subscribe(
			success => {
				if (success)
					{
						this.imageList$ = this.getImageListPage(this.pageIndexState.value.pageIndex)
						this.pageIndex = this.pageIndexState.value.pageIndex
					}
			}
		)
	}

	onPageChanges(event: PageEvent): void {
		this.pageIndex = event.pageIndex
		this.imageList$ = this.getImageListPage(event.pageIndex)
		this.pageIndexState.next(event)
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
	idMove: number
}

export interface PageEvent {
	length: number
	pageIndex: number
	pageSize: number
	previousPageIndex: number
}