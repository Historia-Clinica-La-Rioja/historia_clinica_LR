import { Injectable } from '@angular/core';
import { ImageQueueFilteringCriteriaDto, ImageQueueListDto, PageDto } from '@api-rest/api-model';
import { ImageQueueService } from '@api-rest/services/image-queue.service';
import { BehaviorSubject, Observable, switchMap, take } from 'rxjs';
import { ItemImageQueue } from '../components/image-table-technical/image-table-technical.component';

@Injectable({
	providedIn: 'root'
})
export class ImageQueueManagementStateService {

	private pageSubject: BehaviorSubject<PageDto<ImageQueueListDto>> = new BehaviorSubject({content:[],totalElementsAmount: 0})
	private page$: Observable<PageDto<ImageQueueListDto>> = this.pageSubject.asObservable()
	private filterState: BehaviorSubject<ImageQueueFilteringCriteriaDto> = new BehaviorSubject<ImageQueueFilteringCriteriaDto>(null)
	private paginationState: PageState = {pageSize: 10, pageNumber: 0} as PageState


	constructor(
		private imageQueueService: ImageQueueService,
	) { }


	getImageQueueList(): Observable<PageDto<ImageQueueListDto>> {
		return this.page$
	}

	getPaginationState(): PageState {
		return this.paginationState
	}

	setListImageByFilters(filters:ImageQueueFilteringCriteriaDto) {
		this.filterState.next(filters)
		this.emit()
	}

	getNextPageImageList(pageSize: number, pageIndex: number) {
		this.paginationState = {pageSize: pageSize, pageNumber: pageIndex}
		this.emit()
	}

	private emit(): void {
		this.filterState.pipe(
			take(1),
			switchMap( filterState => this.imageQueueService.getImageQueueList(this.paginationState.pageSize, this.paginationState.pageNumber, filterState))
		).subscribe( imageListFiltered =>
			this.pageSubject.next(imageListFiltered)
		)
	}

	tryAgainItemImage(image: ItemImageQueue): void {
		this.imageQueueService.updateImageQueueRetry(image.idMove)
			.subscribe(success => {
				if (success) {
					this.emit()
				}
			}
			)
	}
}

export interface PageState {
	pageSize: number,
	pageNumber: number
}