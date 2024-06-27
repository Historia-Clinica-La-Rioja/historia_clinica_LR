import { Component, Input, OnInit } from '@angular/core';
import { GisLayersService } from '../../services/gis-layers.service';
import { GlobalCoordinatesDto } from '@api-rest/api-model';
import { OpenlayersService } from '../../services/openlayers.service';
import { Coordinate } from 'ol/coordinate';

@Component({
	selector: 'app-map',
	templateUrl: './map.component.html',
	styleUrls: ['./map.component.scss']
})
export class MapComponent implements OnInit {
	@Input() set setMapCoordinates(coordinates: GlobalCoordinatesDto) {
		this.coordinates = coordinates;
	}
	@Input() set setMapArea(area: GlobalCoordinatesDto[]) {
		this.area = area;
	}	

	constructor(private readonly gisLayersService: GisLayersService,
				private readonly openLayersService: OpenlayersService
	) {}
	
	ngOnInit(): void {
		this.markPoint();
	}
	
	coordinates: GlobalCoordinatesDto;
	area: GlobalCoordinatesDto[];

	markPoint = () => {
		const position: Coordinate = this.openLayersService.fromLonLat(this.coordinates);
		this.gisLayersService.setUp();
		this.openLayersService.centerView(this.gisLayersService.map, position);
		this.gisLayersService.removeLocationPoint();
		this.gisLayersService.addPoint(position);
		this.gisLayersService.markPolygon(this.area);
	}
}