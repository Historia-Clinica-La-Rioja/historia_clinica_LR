import { Injectable } from '@angular/core';
import { Map, Overlay, View } from 'ol';
import Feature from 'ol/Feature';
import Point from 'ol/geom/Point';
import TileLayer from 'ol/layer/Tile';
import { OSM } from 'ol/source';
import VectorLayer from 'ol/layer/Vector';
import VectorSource from 'ol/source/Vector';
import { GlobalCoordinatesDto } from '@api-rest/api-model';
import { fromLonLat } from 'ol/proj';

@Injectable({
  	providedIn: 'root'
})
export class GisLayersService {

	vectorLayer = new VectorLayer({
		source: new VectorSource(),
	});
	overlay: Overlay;
	map: Map;
	locationPoint: Feature;
	
	setMap = () => {
		this.map = new Map({
			target: 'map',
			layers: [
				new TileLayer({
					source: new OSM()
				}),
			],
			view: new View({
				center: [0, 0],
				zoom: 16,
				minZoom: 12
			})
		});
		this.map.addLayer(this.vectorLayer);
	}

	createPoint = (coords: number[]): Feature => {
		return new Feature({
			geometry: new Point(coords)
		});
	}

	addPoint = (position: number[]) => {
		this.locationPoint = this.createPoint(position);
		this.vectorLayer?.getSource().addFeature(this.locationPoint);
	}

	removeLocationPoint = () => {
		this.vectorLayer?.getSource().removeFeature(this.locationPoint);
	}

	centerView = (position: number[]) => {
		this.map.getView().setCenter(position);
	}

	fromLonLat = (value: GlobalCoordinatesDto) => {
		return fromLonLat([value.longitude, value.latitude]);
	}
}
