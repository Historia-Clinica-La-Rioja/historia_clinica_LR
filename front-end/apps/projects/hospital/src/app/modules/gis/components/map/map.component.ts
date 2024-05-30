import { Component, Input } from '@angular/core';
import { GisLayersService } from '../../services/gis-layers.service';
import { GlobalCoordinatesDto } from '@api-rest/api-model';

@Component({
	selector: 'app-map',
	templateUrl: './map.component.html',
	styleUrls: ['./map.component.scss']
})
export class MapComponent {

	@Input() set setMap(coordinates: GlobalCoordinatesDto) {
		this.coordinates = coordinates;
		this.markPoint();
	}	

	coordinates: GlobalCoordinatesDto;

	constructor(private readonly gisLayersService: GisLayersService) { }

	markPoint = () => {
		const position: number[] = this.gisLayersService.fromLonLat(this.coordinates);
		this.gisLayersService.setMap();
		this.gisLayersService.centerView(position);
		this.gisLayersService.removeLocationPoint();
		this.gisLayersService.addPoint(position);
	}

}
