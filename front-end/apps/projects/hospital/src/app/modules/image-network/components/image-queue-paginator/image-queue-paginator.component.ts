import { ChangeDetectionStrategy, Component, Input, OnInit } from '@angular/core';
import { ImageQueueManagementStateService, PageState } from '../../services/image-queue-management-state.service';

@Component({
	selector: 'app-image-queue-paginator',
	templateUrl: './image-queue-paginator.component.html',
	styleUrls: ['./image-queue-paginator.component.scss'],
	changeDetection: ChangeDetectionStrategy.OnPush
})
export class ImageQueuePaginatorComponent implements OnInit {


	@Input() amount: number

	currentPagination: PageState
	pageIndex = 0

	constructor(
		private readonly imageQueueManagementStateService: ImageQueueManagementStateService,
	) { }

	ngOnInit(): void {
		this.currentPagination = this.imageQueueManagementStateService.getPaginationState()
	}


	onPageChanges(event: PageEvent): void {
		this.pageIndex = event.pageIndex
		this.imageQueueManagementStateService.getNextPageImageList(event.pageSize, event.pageIndex)
	}

}

export interface PageEvent {
	length: number
	pageIndex: number
	pageSize: number
	previousPageIndex: number
}