import { Injectable } from '@angular/core';
import { GlobalCoordinatesDto } from '@api-rest/api-model';
import { Feature, Map } from 'ol';
import { Coordinate } from 'ol/coordinate';
import VectorLayer from 'ol/layer/Vector';
import { fromLonLat } from 'ol/proj';
import VectorSource from 'ol/source/Vector';
import { EGeometry } from '../constants/geometry.utils';

@Injectable({
  	providedIn: 'root'
})
export class OpenlayersService {

	centerView = (map: Map, position: Coordinate) => {
		map.getView().setCenter(position);
	}

	fromLonLat = (value: GlobalCoordinatesDto): Coordinate => {
		return fromLonLat([value.longitude, value.latitude]);
	}

	removeFeature = (vector: VectorLayer<VectorSource>, feature: Feature) => {
		vector.getSource().removeFeature(feature);
	}

	addFeature = (vector: VectorLayer<VectorSource>, feature: Feature) => {
		vector.getSource().addFeature(feature);
	}

	createPolygon = () => {
		return {
			'type': 'Feature',
			'geometry': {
				'type': EGeometry.POLYGON,
				'coordinates': []
			},
		}
	}
}
