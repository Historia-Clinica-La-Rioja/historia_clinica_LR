import { Component, Input, OnInit } from '@angular/core';
import { GisLayersService } from '../../services/gis-layers.service';
import { GlobalCoordinatesDto } from '@api-rest/api-model';

@Component({
	selector: 'app-map',
	templateUrl: './map.component.html',
	styleUrls: ['./map.component.scss']
})
export class MapComponent implements OnInit {
	@Input() set setMap(coordinates: GlobalCoordinatesDto) {
		this.coordinates = coordinates;
	}	

	constructor(private readonly gisLayersService: GisLayersService) {}
	
	ngOnInit(): void {
		this.markPoint();
	}
	
	coordinates: GlobalCoordinatesDto;

	markPoint = () => {
		const position: number[] = this.gisLayersService.fromLonLat(this.coordinates);
		this.gisLayersService.setUp();
		this.gisLayersService.centerView(position);
		this.gisLayersService.removeLocationPoint();
		this.gisLayersService.addPoint(position);
	}

}
