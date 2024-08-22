import { Component, OnInit } from '@angular/core';
import { EmergencyCareAttentionPlaceService } from '../../services/emergency-care-attention-place.service';
import { EmergencyCareAttentionPlaceDto } from '@api-rest/api-model';

@Component({
  selector: 'app-emergency-care-attention-places',
  templateUrl: './emergency-care-attention-places.component.html',
  styleUrls: ['./emergency-care-attention-places.component.scss']
})
export class EmergencyCareAttentionPlacesComponent implements OnInit {

    loading = true;

    hasAttentionPlaces: boolean;
    attentionPlaces: EmergencyCareAttentionPlaceDto[];
    selectedAttentionPlace: EmergencyCareAttentionPlaceDto;

    constructor(
		private emergencyCareAttentionPlaceService: EmergencyCareAttentionPlaceService
	) { }

    ngOnInit() {
		this.emergencyCareAttentionPlaceService.getAttentionPlaces().subscribe(places => {
			this.loading = false;
		  	this.attentionPlaces = places;
			this.hasAttentionPlaces = this.attentionPlaces.length > 0;
		});
    }
}
