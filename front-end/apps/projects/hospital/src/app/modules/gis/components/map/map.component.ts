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
		if (this.gisLayersService.getMap())
			this.centerViewPoint();
	}
	@Input() set setMapArea(area: GlobalCoordinatesDto[]) {
		this.gisLayersService.removeAreaLayer();
		this.area = area;
		if (this.gisLayersService.getMap()) 
			this.gisLayersService.setPolygon(this.area);
	}	
	@Input() handleLocationPoint: boolean;

	constructor(private readonly gisLayersService: GisLayersService,
				private readonly openLayersService: OpenlayersService
	) {}
	
	ngOnInit(): void {
		this.setUpMap();
	}
	
	coordinates: GlobalCoordinatesDto;
	area: GlobalCoordinatesDto[];

	private setUpMap = () => {
		this.gisLayersService.setUp();
		this.centerViewPoint();
		this.gisLayersService.setPolygon(this.area);
		if (this.handleLocationPoint)
			this.gisLayersService.handleLocationClic()
	}

	private centerViewPoint = () => {
		const position: Coordinate = this.openLayersService.fromLonLat(this.coordinates);
		this.gisLayersService.setLocationCoordinates(position);
		this.openLayersService.centerView(this.gisLayersService.getMap(), position);
		this.gisLayersService.removeLocationPoint();
		this.gisLayersService.addPoint(position);
	}
}