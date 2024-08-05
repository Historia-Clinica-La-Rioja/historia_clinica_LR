import { Map, View } from 'ol';
import Feature from 'ol/Feature';
import Point from 'ol/geom/Point';
import {Tile as TileLayer, Vector as VectorLayer} from 'ol/layer.js';
import { Cluster, Vector as VectorSource, XYZ} from 'ol/source';
import { GetPatientCoordinatesByAddedInstitutionFilterDto, GlobalCoordinatesDto, SanitaryRegionPatientMapCoordinatesDto } from '@api-rest/api-model';
import Style from 'ol/style/Style';
import {Draw, Modify, Snap} from 'ol/interaction.js';
import { EGeometry } from '../constants/geometry.utils';
import { Injectable } from '@angular/core';
import Polygon from 'ol/geom/Polygon';
import { Coordinate } from 'ol/coordinate';
import Control from 'ol/control/Control';
import { BehaviorSubject, map } from 'rxjs';
import GeoJSON from 'ol/format/GeoJSON.js';
import { OpenlayersService } from './openlayers.service';
import { fromLonLat, toLonLat } from 'ol/proj';
import { GisService } from '@api-rest/services/gis.service';
import { dateToDateDto } from '@api-rest/mapper/date-dto.mapper';
import { DateRange } from '@presentation/components/date-range-picker/date-range-picker.component';

const LOCATION_POINT_PATH = '../../../../assets/icons/location_on.svg';
const PERSON_POINT_PATH = '../../../../assets/icons/person.svg';
const IGN_MAP = 'https://wms.ign.gob.ar/geoserver/gwc/service/tms/1.0.0/capabaseargenmap@EPSG%3A3857@png/{z}/{x}/{-y}.png';
const EPSG3857 = 'EPSG:3857';
const EPSG4326 = 'EPSG:4326';
const LOCATION_POINT_ID = 'locationPoint';

export enum PatientTypeFilter {
	REGISTERED = 'registered',
	OUTPATIENT_CLINIC = 'outpatient_clinic',
}

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
	modifySource = new VectorSource();
	modifyVector = new VectorLayer({
		source: this.modifySource,
	});
	locationPoint: Feature;
	draw = new Draw({
		source: this.modifySource,
		type: EGeometry.POLYGON,
	});
	snap = new Snap({source: this.modifySource});
	modify: Modify;
	drawnPolygon: Feature;
	isPolygonCompleted = false;
	polygonCoordinates: Coordinate[][] = [];
	undoControl: Control;
	removeAndCreateControl: Control;
	showUndo$ = new BehaviorSubject<boolean>(false);
	showRemoveAndCreate$ = new BehaviorSubject<boolean>(false);
	areaLayer;
	clickListener = null;
	locationPointListener = null;
	locationCoordinates: Coordinate;
	showDetails$ = new BehaviorSubject<boolean>(false);

	patientSource = new VectorSource();
	patientVector = new VectorLayer({
		source: this.patientSource
	});
	clusterSource = new Cluster({
		distance: 50,
		minDistance: 30,
		source: this.patientSource,
	});
	corners: GetPatientCoordinatesByAddedInstitutionFilterDto;
	currentMapFilter;
	dateRangeFilter: DateRange;
	mapMoveendListener;

	constructor(private readonly openLayersService: OpenlayersService,
				private readonly gisService: GisService) {}

	setUp = () => {
		this.setMap();
		this.detectWhenDrawStart();
		this.detectWhenDrawFinish();
		this.detectWhenModifyFinish();
		this.setModifySource(this.modifySource);
	}
	
	setMap = () => {
		this.clearMap();
		const clusters = this.setClusters();
		this.map = new Map({
			target: 'map',
			layers: [
				new TileLayer({
					source: this.XYZ
				}),
				this.vector,
				this.modifyVector,
				clusters
			],
			view: new View({
				center: [0, 0],
				zoom: 16,
				minZoom: 12,
			})
		});
		this.detectWhenMapMoves();
	}
	
	addPoint = (position: Coordinate) => {
		this.setLocationCoordinates(position);
		this.locationPoint = this.createLocationPoint(this.locationCoordinates);
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
		this.modifySource.removeFeature(this.drawnPolygon);
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
		this.setModifySource(source);
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

	handleLocationClic = () => {
		this.removeModifyInteraction();
		this.removeLocationClic();
		this.clickListener = (event) => {
			this.setLocationCoordinates(event.coordinate);
            this.vector.getSource().removeFeature(this.locationPoint);
            this.addPoint(this.locationCoordinates);
        };
		this.map?.on('click', this.clickListener);
	}	

	removeLocationClic = () => {
		if (this.clickListener) {
			this.map?.un('click', this.clickListener);
			this.clickListener = null;
		}
	}

	coordinateToGlobalCoordinateDto = (): GlobalCoordinatesDto => {
		const transformedCoordinates = this.openLayersService.transformCoordinatesTo(this.locationCoordinates, EPSG3857, EPSG4326);
		return {
			latitude: transformedCoordinates[1],
			longitude: transformedCoordinates[0]
		}
	}

	setLocationCoordinates = (coords: Coordinate) => {
		this.locationCoordinates = coords;
	}

	removeLocationPointListener = () => {
		if (this.locationPointListener) {
			this.map?.un('click', this.locationPointListener);
			this.locationPointListener = null;
		}
	}

	detectIfLocationPointClickled = () => {
		this.removeLocationPointListener();
		this.locationPointListener = this.setLocationPointListener();
		this.map?.on('click', this.locationPointListener);
	}

	removePatientFeatures = () => {
		this.openLayersService.removeAllFeatures(this.patientSource);
	}

	setPatientPoints = (coords: Coordinate) => {
		this.patientSource.addFeature(
			new Feature({
				geometry: new Point(fromLonLat(coords))
			})
		);
	}

	filterByInstitution = () => {
		this.currentMapFilter = PatientTypeFilter.REGISTERED;
		this.setMapCorners();
		const corners: string = this.toStringifyByInstitution(this.corners);
		this.gisService.getPatientCoordinatesByInstitution(corners)
		.pipe(map((coords: SanitaryRegionPatientMapCoordinatesDto[]) => {
			this.removePatientFeatures();
			coords.map((coord: SanitaryRegionPatientMapCoordinatesDto) => this.setPatientPoints([coord.longitude, coord.latitude]));
		})).subscribe()
	}

	filterByOutpatientClinic = (dateRange: DateRange) => {
		this.currentMapFilter = PatientTypeFilter.OUTPATIENT_CLINIC;
		this.setMapCorners();
		this.dateRangeFilter = dateRange;
		const corners: string = this.toStringifyByOutpatientClinic(this.corners);
		this.gisService.getPatientCoordinatesByOutpatientClinic(corners)
		.pipe(map((coords: SanitaryRegionPatientMapCoordinatesDto[]) => {
			this.removePatientFeatures();
			coords.map((coord: SanitaryRegionPatientMapCoordinatesDto) => this.setPatientPoints([coord.longitude, coord.latitude]));
		})).subscribe()
	}

	detectWhenMapMoves = () => {
		this.setMapMoveendListener();
		this.map.on('moveend', this.mapMoveendListener);
	}

	private setMapMoveendListener = () => {
		this.mapMoveendListener = (_) => {
			if (!this.currentMapFilter) return;
			this.setMapCorners();
			(this.currentMapFilter === PatientTypeFilter.REGISTERED) ? this.filterByInstitution() : this.filterByOutpatientClinic(this.dateRangeFilter);
		}
	}

	private toStringifyByInstitution = (corners: GetPatientCoordinatesByAddedInstitutionFilterDto): string => {
		return JSON.stringify(
			{
				mapLowerCorner: corners.mapLowerCorner,
				mapUpperCorner: corners.mapUpperCorner
			}
		)
	}

	private toStringifyByOutpatientClinic = (corners: GetPatientCoordinatesByAddedInstitutionFilterDto): string => {
		return JSON.stringify(
			{
				mapLowerCorner: corners.mapLowerCorner,
				mapUpperCorner: corners.mapUpperCorner,
				fromDate: dateToDateDto(this.dateRangeFilter.start),
				toDate: dateToDateDto(this.dateRangeFilter.end)
			}
		)
	}
	
	private setMapCorners = () => {
		const extent = this.map.getView().calculateExtent(this.map.getSize());
		const mapLowerCorner = toLonLat([extent[0], extent[1]]);
		const mapUpperCorner = toLonLat([extent[2], extent[3]]);
		this.corners = {
			mapLowerCorner: ({
				longitude: mapLowerCorner[0],
				latitude: mapLowerCorner[1],
			}),
			mapUpperCorner: ({
				longitude: mapUpperCorner[0],
				latitude: mapUpperCorner[1],
			})
		}
	}

	private setClusters = (): VectorLayer<VectorSource> => {
		const styleCache = {};
		return new VectorLayer({
			source: this.clusterSource,
			style: (feature) => {
				const size = feature.get('features').length;
				let style = styleCache[size];
				if (!style) {
					style = this.setPatientClusterStyle(size);
					styleCache[size] = style;
				}
				return style;
			},
		});
	}

	private setPatientClusterStyle = (size: number): Style => {
		return new Style({
			image: this.openLayersService.createIcon(PERSON_POINT_PATH),
			text: this.openLayersService.createText(size > 1 ? size.toString() : '')
		});
	}

	private setLocationPointListener = () => {
		return (e) => {
			this.map.forEachFeatureAtPixel(e.pixel, (feature) => {
				const name = feature.get('name');
				if (name === LOCATION_POINT_ID) {
					this.removePatientFeatures();
					if (this.mapMoveendListener) {
						this.map.un('moveend', this.mapMoveendListener);
						this.mapMoveendListener = null;
					}
					this.currentMapFilter = null;
					this.showDetails$.next(true);
				}
			});
		}
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
			image: this.openLayersService.createIcon(LOCATION_POINT_PATH),
		});
		feature.setStyle(markerStyle);
		feature.set('name', LOCATION_POINT_ID)
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
			this.setModifySource(this.modifySource);
			this.map?.addInteraction(this.modify);
			this.detectWhenModifyFinish();
		});
	}

	private detectWhenModifyFinish = () => {
		this.modify?.on('modifyend', (event) => {
			this.drawnPolygon = event.features.item(0);
			const geometry: Polygon = this.drawnPolygon.getGeometry() as Polygon;
			this.polygonCoordinates = geometry.getCoordinates();
		})
	}

	private detectWhenDrawStart = () => {
		this.draw.on('drawstart', (_) => this.toggleActions(true, false));
	}

	private setModifySource = (source: VectorSource) => {
		this.modify = new Modify({
			source: source
		});
	}
}