import { Component, Input, OnInit } from '@angular/core';
import { SpaceItem, SpaceType } from '../emergency-care-attention-place-sector/emergency-care-attention-place-sector.component';
import { AvailableButtonType, AvailableButtonWidth } from '@presentation/components/button-availability/button-availability.component';
import { EmergencyCareAttentionPlaceAvailabilityButtonSelectionService } from '../../services/emergency-care-attention-place-availability-button-selection.service';
import { SpaceState, SpaceTypeDetails } from '../emergency-care-attention-place-details/emergency-care-attention-place-details.component';

@Component({
	selector: 'app-emergency-care-attention-place-space',
	templateUrl: './emergency-care-attention-place-space.component.html',
	styleUrls: ['./emergency-care-attention-place-space.component.scss']
})
export class EmergencyCareAttentionPlaceSpaceComponent implements OnInit{

	readonly AVAILABLE_BUTTON_BY_SPACE_STATE = {
		[SpaceState.AVAILABLE]: AvailableButtonType.AVAILABLE,
		[SpaceState.NOT_AVAILABLE]: AvailableButtonType.NOT_AVAILABLE,
		[SpaceState.BLOCKED]: AvailableButtonType.BLOCKED,
	}
	itemsInfo: itemAvailabilityButton[];

	@Input() space: SpaceItem;

	constructor(private availabilityButtonSelectionService: EmergencyCareAttentionPlaceAvailabilityButtonSelectionService) {}

	ngOnInit() {
		this.mapToAvailabilityButton();
		this.availabilityButtonSelectionService.selectedButton$.subscribe(selectedSpace => {
			this.updateButtonSelection(selectedSpace);
		});
	}

	mapToAvailabilityButton() {
		this.itemsInfo = this.space.items.map((space: SpaceTypeDetails) => ({
			id: space.id,
			spaceType: this.space.type,
			description: space.description,
			type: this.getAvailabilityButton(space.state),
			size: AvailableButtonWidth.LARGE,
			isSelected: false
		}));
	}

	selectItem(itemSelected: itemAvailabilityButton) {
		this.availabilityButtonSelectionService.clearSelection();
		this.availabilityButtonSelectionService.selectButton({
			id: itemSelected.id,
			spaceType: itemSelected.spaceType
		});
	}

	updateButtonSelection(selectedSpace: SelectedSpace) {
		this.diselectAll();
		if (selectedSpace) {
			const selectedItem = this.itemsInfo.find(item => item.id === selectedSpace.id && item.spaceType === selectedSpace.spaceType);
			if (selectedItem) {
				selectedItem.isSelected = true;
			}
		}
	}

	diselectAll() {
		this.itemsInfo.forEach(item => item.isSelected = false);
	}

	private getAvailabilityButton(state: SpaceState): AvailableButtonType {
		return this.AVAILABLE_BUTTON_BY_SPACE_STATE[state];
	}
}

interface itemAvailabilityButton{
	id: number,
	spaceType: SpaceType,
	description: string,
	type?: AvailableButtonType,
	size?: AvailableButtonWidth,
	isSelected?: boolean
}

export interface SelectedSpace{
	id: number,
	spaceType: SpaceType
}
