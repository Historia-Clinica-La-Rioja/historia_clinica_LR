import { Component, OnInit } from '@angular/core';
import { EmergencyCareAttentionPlaceService } from '../../services/emergency-care-attention-place.service';
import { EmergencyCareAttentionPlaceDto } from '@api-rest/api-model';
import { EmergencyCareAttentionPlaceAvailabilityButtonSelectionService } from '../../services/emergency-care-attention-place-availability-button-selection.service';

@Component({
  selector: 'app-emergency-care-attention-places',
  templateUrl: './emergency-care-attention-places.component.html',
  styleUrls: ['./emergency-care-attention-places.component.scss']
})
export class EmergencyCareAttentionPlacesComponent implements OnInit {

    loading = true;

    hasSectors: boolean;
    sectors: EmergencyCareAttentionPlaceDto[];
    selectedSpaceId: number;

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
		this.buttonSelectionService.selectedButtonId$.subscribe(buttonId => {
            this.selectedSpaceId = buttonId;
        });
	}
}
