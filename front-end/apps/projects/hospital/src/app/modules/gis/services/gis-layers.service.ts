import { Map, View } from 'ol';
import Feature from 'ol/Feature';
import Point from 'ol/geom/Point';
import {Tile as TileLayer, Vector as VectorLayer} from 'ol/layer.js';
import { Vector as VectorSource, XYZ} from 'ol/source';
import { GlobalCoordinatesDto } from '@api-rest/api-model';
import Style from 'ol/style/Style';
import Icon from 'ol/style/Icon';
import {Draw, Modify, Snap} from 'ol/interaction.js';
import { EGeometry } from '../constants/geometry.utils';
import { Injectable } from '@angular/core';
import Polygon from 'ol/geom/Polygon';
import { Coordinate } from 'ol/coordinate';
import Control from 'ol/control/Control';
import { BehaviorSubject } from 'rxjs';
import GeoJSON from 'ol/format/GeoJSON.js';
import { OpenlayersService } from './openlayers.service';

const LOCATION_POINT = '../../../../assets/icons/gis_location_point.svg';
const IGN_MAP = 'https://wms.ign.gob.ar/geoserver/gwc/service/tms/1.0.0/capabaseargenmap@EPSG%3A3857@png/{z}/{x}/{-y}.png';

@Injectable({
	providedIn: 'root',
})
export class GisLayersService {

	XYZ = new XYZ({
		url: IGN_MAP,
	});
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
	modify = new Modify({source: this.source});
	drawnPolygon: Feature;
	isPolygonCompleted = false;
	polygonCoordinates: Coordinate[][] = [];
	undoControl: Control;
	removeAndCreateControl: Control;
	showUndo$ = new BehaviorSubject<boolean>(false);
	showRemoveAndCreate$ = new BehaviorSubject<boolean>(false);
	areaLayer;

	constructor(private readonly openLayersService: OpenlayersService) {}

	setUp = () => {
		this.setMap();
		this.detectWhenDrawStart();
		this.detectWhenDrawFinish();
		this.detectWhenModifyFinish();
	}
	
	setMap = () => {
		this.clearMap();
		this.map = new Map({
			target: 'map',
			layers: [
				new TileLayer({
					source: this.XYZ
				}),
				this.vector
			],
			view: new View({
				center: [0, 0],
				zoom: 16,
				minZoom: 12,
			})
		});
	}

	addPoint = (position: Coordinate) => {
		this.locationPoint = this.createLocationPoint(position);
		this.openLayersService.addFeature(this.vector, this.locationPoint);
	}

	removeLocationPoint = () => {
		this.openLayersService.removeFeature(this.vector, this.locationPoint);
	}

	addPolygonInteraction = () => {
		this.map?.addInteraction(this.modify);
		if (!this.areaLayer) {
			this.map?.addInteraction(this.draw);
			this.map?.addInteraction(this.snap);
		} else {
			this.isPolygonCompleted = true;
			this.toggleActions(false, true);
		}
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

	removeAndAddInteraction = () => {
		this.removeDrawnPolygon();
		this.removeAreaLayer();
		this.addPolygonInteraction();
		this.toggleActions(false, false);
	}

	addControls = (undo: Control, removeAndCreate: Control) => {
		this.undoControl = undo;
		this.removeAndCreateControl = removeAndCreate;
		this.map?.addControl(this.undoControl);
		this.map?.addControl(this.removeAndCreateControl);
	}

	removeControls = () => {
		this.map?.removeControl(this.undoControl);
		this.map?.removeControl(this.removeAndCreateControl);
	}

	toggleActions = (undo: boolean, removeAndCreate: boolean) => {
		this.showUndo$.next(undo);
		this.showRemoveAndCreate$.next(removeAndCreate);
	}

	setPolygon = (area: GlobalCoordinatesDto[]) => {
		if (!area.length) return;

		const polygon = this.createPolygon(area);
		const source = new VectorSource({
			features: new GeoJSON().readFeatures(polygon),
		});
		this.areaLayer = new VectorLayer();
		this.areaLayer.setSource(source);
		this.map.addLayer(this.areaLayer);
		this.isPolygonCompleted = true;
		this.polygonCoordinates = polygon.geometry.coordinates;
		this.modify = new Modify({source: source});
		this.detectWhenModifyFinish();
	}

	removeAreaLayer = () => {
		this.areaLayer?.getSource().clear();
		this.map?.removeLayer(this.areaLayer);
		this.areaLayer = null;
	}

	getMap = (): Map => {
		return this.map;
	}

	removeModifyInteraction = () => {
		this.map?.removeInteraction(this.modify);
	}

	private clearMap = () => {
		this.removeDrawnPolygon();
		this.map?.getLayers().getArray().forEach(layer => this.map.removeLayer(layer));
	}

	private createPolygon = (area: GlobalCoordinatesDto[]) => {
		const coordinates = area.map((area: GlobalCoordinatesDto) => [area.longitude, area.latitude]);
		const polygon = this.openLayersService.createPolygon();
		polygon.geometry.coordinates = [coordinates];
		return polygon;
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
			this.toggleActions(false, true);
			this.addModifyInteraction();
			this.detectWhenModifyFinish();
		});
	}

	private detectWhenModifyFinish = () => {
		this.modify.on('modifyend', (event) => {
			this.drawnPolygon = event.features.item(0);
			const geometry: Polygon = this.drawnPolygon.getGeometry() as Polygon;
			this.polygonCoordinates = geometry.getCoordinates();
		})
	}

	private detectWhenDrawStart = () => {
		this.draw.on('drawstart', (_) => this.toggleActions(true, false));
	}

	private addModifyInteraction = () => {
		this.modify = new Modify({source: this.source});
		this.map?.addInteraction(this.modify);
	}
}