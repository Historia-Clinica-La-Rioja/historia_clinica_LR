import { Injectable } from '@angular/core';
import { SpaceType } from '../components/emergency-care-attention-place-sector/emergency-care-attention-place-sector.component';
import { Observable } from 'rxjs';
import { BlockAttentionPlaceService } from '@api-rest/services/block-attention-place.service';
import { BlockAttentionPlaceCommandDto } from '@api-rest/api-model';

@Injectable()
export class BlockUnblockAttentionPlaceService {

	readonly PERSIST_BLOCK = {
		[SpaceType.DoctorsOffices]: (id, blockReason) => this.blockAttentionPlaceService.blockDoctorsOffice(id, blockReason),
		[SpaceType.ShockRooms]: (id, blockReason) => this.blockAttentionPlaceService.blockShockroom(id, blockReason),
		[SpaceType.Beds]: (id, blockReason) => this.blockAttentionPlaceService.blockBed(id, blockReason),
	}

	readonly PERSIST_UNBLOCK = {
		[SpaceType.DoctorsOffices]: (id) => this.blockAttentionPlaceService.unblockDoctorsOffice(id),
		[SpaceType.ShockRooms]: (id) => this.blockAttentionPlaceService.unblockShockroom(id),
		[SpaceType.Beds]: (id) => this.blockAttentionPlaceService.unblockBed(id),
	}

	constructor(
		private readonly blockAttentionPlaceService: BlockAttentionPlaceService,
	) { }

	blockAttentionPlace(attentionPlaceId: number, attentionPlaceType: SpaceType, blockReason: BlockAttentionPlaceCommandDto): Observable<void> {
		return this.PERSIST_BLOCK[attentionPlaceType](attentionPlaceId, blockReason);
	}

	unblockAttentionPlace(attentionPlaceId: number, attentionPlaceType: SpaceType): Observable<void> {
		return this.PERSIST_UNBLOCK[attentionPlaceType](attentionPlaceId);
	}
}
