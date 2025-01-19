import { Injectable } from '@angular/core';
import { GlobalCoordinatesDto } from '@api-rest/api-model';
import { Feature, Map } from 'ol';
import { Coordinate } from 'ol/coordinate';
import VectorLayer from 'ol/layer/Vector';
import { fromLonLat, transform } from 'ol/proj';
import VectorSource from 'ol/source/Vector';
import { EGeometry } from '../constants/geometry.utils';
import { Icon } from 'ol/style';
import {
	Fill,
	Stroke,
	Text,
  } from 'ol/style.js';

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

	transformCoordinatesTo = (coords: Coordinate, fromEPSGType: string, toEPSGType: string): Coordinate => {
		return transform(coords, fromEPSGType, toEPSGType);
	}

	setId = (id: string, feature: Feature) => {
		feature.setId(id);
	}

	createIcon = (src: string): Icon => {
		return new Icon({
			anchor: [0.5, 1],
			src,
			scale: 0.5
		})
	}

	createText = (text: string): Text => {
		return new Text({
			text,
			fill: new Fill({
				color: '#fff',
			}),
			stroke: new Stroke({
				color: '#000',
				width: 2,                  
			}),
		})
	}

	removeAllFeatures = (source: VectorSource) => {
		source.clear();
	}
}
