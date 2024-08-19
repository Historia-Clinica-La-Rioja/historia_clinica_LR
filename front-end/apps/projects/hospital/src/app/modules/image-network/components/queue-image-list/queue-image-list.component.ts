import { ChangeDetectionStrategy, Component, Input, OnInit } from '@angular/core';
import { ItemImageQueue } from '../image-table-technical/image-table-technical.component';
import { Size } from '@presentation/components/item-summary/item-summary.component';
import { QUEUE_ERROR } from '../../constants/image.queue.mapper';
import { ButtonType } from '@presentation/components/button/button.component';
import { ImageQueueManagementStateService } from '../../services/image-queue-management-state.service';

@Component({
	selector: 'app-queue-image-list',
	templateUrl: './queue-image-list.component.html',
	styleUrls: ['./queue-image-list.component.scss'],
	changeDetection: ChangeDetectionStrategy.OnPush
})
export class QueueImageListComponent implements OnInit {

	@Input() queueList: ItemImageQueue[]
	@Input() paginationAmount: number

	readonly ButtonType = ButtonType;
	size: Size = Size.SMALL
	ERROR_IMAGE = QUEUE_ERROR
	SIN_INFORMACION = 'Sin información'

	constructor(
		private readonly imageQueueManagementStateService: ImageQueueManagementStateService,
	) { }

	ngOnInit(): void {
	}

	tryAgain(image: ItemImageQueue): void {
		this.imageQueueManagementStateService.tryAgainItemImage(image)
	}

}