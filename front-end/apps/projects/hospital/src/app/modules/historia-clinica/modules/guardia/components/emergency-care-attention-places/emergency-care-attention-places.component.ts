import { Component, OnInit } from '@angular/core';
import { EmergencyCareAttentionPlaceService } from '../../services/emergency-care-attention-place.service';
import { EmergencyCareAttentionPlaceDto } from '@api-rest/api-model';
import { EmergencyCareAttentionPlaceAvailabilityButtonSelectionService } from '../../services/emergency-care-attention-place-availability-button-selection.service';
import { SelectedSpace } from '../emergency-care-attention-place-space/emergency-care-attention-place-space.component';

@Component({
  selector: 'app-emergency-care-attention-places',
  templateUrl: './emergency-care-attention-places.component.html',
  styleUrls: ['./emergency-care-attention-places.component.scss']
})
export class EmergencyCareAttentionPlacesComponent implements OnInit {

    loading = true;

    hasSectors: boolean;
    sectors: EmergencyCareAttentionPlaceDto[];
    selectedSpace: SelectedSpace;

    constructor(
		private emergencyCareAttentionPlaceService: EmergencyCareAttentionPlaceService,
		private buttonSelectionService: EmergencyCareAttentionPlaceAvailabilityButtonSelectionService
	) { }

    ngOnInit() {
		this.loadAttentionPlaces();
        this.listenToButtonSelection();
    }

	private loadAttentionPlaces(){
		this.emergencyCareAttentionPlaceService.getAttentionPlaces().subscribe(sectors => {
			this.loading = false;
		  	this.sectors = sectors;
			this.hasSectors = this.sectors.length > 0;
		});
	}

	private listenToButtonSelection(){
		this.buttonSelectionService.selectedButton$.subscribe(space => {
			if (space) {
				this.selectedSpace = space;
			}
        });
	}
}
