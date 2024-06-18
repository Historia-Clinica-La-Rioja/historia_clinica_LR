import { Map, View } from 'ol';
import Feature from 'ol/Feature';
import Point from 'ol/geom/Point';
import {Tile as TileLayer, Vector as VectorLayer} from 'ol/layer.js';
import { OSM, Vector as VectorSource} from 'ol/source';
import { GlobalCoordinatesDto } from '@api-rest/api-model';
import { fromLonLat } from 'ol/proj';
import Style, { StyleLike } from 'ol/style/Style';
import Icon from 'ol/style/Icon';
import Modify from 'ol/interaction/Snap.js';
import Draw from 'ol/interaction/Draw.js';
import Snap from 'ol/interaction/Snap.js';
import { EGeometry } from '../constants/geometry';
import { FlatStyleLike } from 'ol/style/flat';
import { Injectable } from '@angular/core';
import Polygon from 'ol/geom/Polygon';
import { Coordinate } from 'ol/coordinate';
import Control from 'ol/control/Control';
import { BehaviorSubject } from 'rxjs';

const LOCATION_POINT = '../../../../assets/icons/gis_location_point.svg';

@Injectable({
	providedIn: 'root',
})
export class GisLayersService {

	source = new VectorSource();
	vector = new VectorLayer({
		source: this.source,
	});
	map: Map;
	locationPoint: Feature;
	draw = new Draw({
		source: this.source,
		type: EGeometry.POLYGON,
	});
	snap = new Snap({source: this.source});
	drawnPolygon: Feature;
	isPolygonCompleted = false;
	polygonCoordinates: Coordinate[][] = [];
	control: Control;
	showUndo$ = new BehaviorSubject<boolean>(false);
	showRemoveAndCreate$ = new BehaviorSubject<boolean>(false);

	setUp = () => {
		this.setMap();
		this.detectWhenDrawStart();
		this.detectWhenDrawFinish();
	}
	
	setMap = () => {
		this.map = new Map({
			target: 'map',
			layers: [
				new TileLayer({
					source: new OSM(),
				}),
				this.vector
			],
			view: new View({
				center: [0, 0],
				zoom: 16,
				minZoom: 12,
			})
		});
		this.map.addInteraction(new Modify({source: this.source}));
	}

	addPoint = (position: number[]) => {
		this.locationPoint = this.createLocationPoint(position);
		this.vector?.getSource().addFeature(this.locationPoint);
	}

	removeLocationPoint = () => {
		this.vector?.getSource().removeFeature(this.locationPoint);
	}

	centerView = (position: number[]) => {
		this.map.getView().setCenter(position);
	}

	fromLonLat = (value: GlobalCoordinatesDto) => {
		return fromLonLat([value.longitude, value.latitude]);
	}

	createVectorLayer = (vectorSource: VectorSource, style?: StyleLike | FlatStyleLike): VectorLayer<VectorSource<Feature>> => {
		return new VectorLayer({
			source: vectorSource,
			style
		});
	}

	addPolygonInteractionAndControl = () => {
		this.addPolygonInteraction();
		this.addControl('undo');
	}

	removeDrawnPolygon = () => {
		this.removePolygonInteraction();
		this.source.removeFeature(this.drawnPolygon);
		this.drawnPolygon = null;
		this.isPolygonCompleted = false;
	}

	removeLastPoint = () => {
		this.draw.removeLastPoint();
	}

	removeAndCreate = () => {
		this.removeDrawnPolygon();
		this.addPolygonInteraction();
		this.setActions(false, false);
	}

	addControl = (id: string) => {
		const locationOnRef = document.getElementById(id);
		this.control = new Control({element: locationOnRef});
		this.map?.addControl(this.control);
	}

	setActions = (undo: boolean, removeAndCreate: boolean) => {
		this.showUndo$.next(undo);
		this.showRemoveAndCreate$.next(removeAndCreate);
	}

	private addPolygonInteraction = () => {
		this.map?.addInteraction(this.draw);
		this.map?.addInteraction(this.snap);
	}

	private createLocationPoint = (coords: number[]): Feature => {
		const feature = new Feature({
			geometry: new Point(coords)
		});
		const markerStyle = new Style({
			image: new Icon({
				anchor: [0.5, 1],
				src: LOCATION_POINT,
				scale: 0.5
			})
		});
		feature.setStyle(markerStyle);
		return feature;
	}
	
	private removePolygonInteraction = () => {
		this.map?.removeInteraction(this.draw);
		this.map?.removeInteraction(this.snap);
	}

	private detectWhenDrawFinish = () => {
		this.draw.on('drawend', (event) => {
			this.isPolygonCompleted = true;
			this.removePolygonInteraction();
			this.drawnPolygon = event.feature;
			const geometry: Polygon = this.drawnPolygon.getGeometry() as Polygon;
			this.polygonCoordinates = geometry.getCoordinates();
			this.setActions(false, true)
			this.addControl('removeAndCreate');
		});
	}

	private detectWhenDrawStart = () => {
		this.draw.on('drawstart', (_) => this.setActions(true, false));
	}
}