import { Component, OnInit } from '@angular/core';
import { Asset } from '../asset.model';

@Component({
	selector: 'app-document-images',
	templateUrl: './document-images.component.html',
	styleUrls: ['./document-images.component.scss']
})
export class DocumentImagesComponent implements OnInit {
	images: Asset[];
	constructor() { }

	ngOnInit(): void {
		this.images = [
			{
				name: 'Encabezado de Documentos',
				path: 'pdf/hsi-header-250x72.png',
				width: 250,
				height: 72,
				extension: '.png',
			},
			{
				name: 'Pie de Documentos',
				path: 'pdf/hsi-footer-118x21.png',
				width: 118,
				height: 21,
				extension: '.png',
			},
			{
				name: 'Encabezado de Receta Digital',
				path: 'pdf/digital_recipe_header_logo.png',
				width: 1756,
				height: 506,
				extension: '.png',
			},
			{
				name: 'Pie de Receta Digital',
				path: 'pdf/digital_recipe_logo.png',
				width: 733,
				height: 341,
				extension: '.png',
			},
		]
	}

}
