import { Component, Input, OnInit } from '@angular/core';
import { SpaceItem } from '../emergency-care-attention-place-sector/emergency-care-attention-place-sector.component';
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
		this.availabilityButtonSelectionService.selectedButtonId$.subscribe(buttonId => {
			this.updateButtonSelection(buttonId);
		});
	}

	mapToAvailabilityButton() {
		this.itemsInfo = this.space.items.map(space => ({
			id: space.id,
			description: space.description,
			type: space.available ? AvailableButtonType.AVAILABLE : AvailableButtonType.NOT_AVAILABLE,
			size: AvailableButtonWidth.LARGE,
			isSelected: false
		}));
	}

	selectItem(itemSelected: itemAvailabilityButton) {
		this.availabilityButtonSelectionService.clearSelection();
		this.availabilityButtonSelectionService.selectButton(itemSelected.id);
	}

	updateButtonSelection(buttonId: number) {
		this.diselectAll();
		if (buttonId !== null) {
			const selectedItem = this.itemsInfo.find(item => item.id === buttonId);
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
	description: string,
	type?: AvailableButtonType,
	size?: AvailableButtonWidth,
	isSelected?: boolean
}
