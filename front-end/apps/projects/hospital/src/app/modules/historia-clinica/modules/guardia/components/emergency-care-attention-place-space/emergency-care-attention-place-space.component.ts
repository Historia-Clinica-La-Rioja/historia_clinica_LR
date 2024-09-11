import { Component, Input, OnInit } from '@angular/core';
import { SpaceItem, SpaceType } from '../emergency-care-attention-place-sector/emergency-care-attention-place-sector.component';
import { AvailableButtonType, AvailableButtonWidth } from '@presentation/components/button-availability/button-availability.component';
import { EmergencyCareAttentionPlaceAvailabilityButtonSelectionService } from '../../services/emergency-care-attention-place-availability-button-selection.service';

@Component({
	selector: 'app-emergency-care-attention-place-space',
	templateUrl: './emergency-care-attention-place-space.component.html',
	styleUrls: ['./emergency-care-attention-place-space.component.scss']
})
export class EmergencyCareAttentionPlaceSpaceComponent implements OnInit{

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
		this.itemsInfo = this.space.items.map(space => ({
			id: space.id,
			spaceType: this.space.type,
			description: space.description,
			type: space.available ? AvailableButtonType.AVAILABLE : AvailableButtonType.NOT_AVAILABLE,
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
