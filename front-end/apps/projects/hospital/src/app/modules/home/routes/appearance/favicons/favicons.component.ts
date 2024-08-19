import { Component, OnInit } from '@angular/core';
import { Asset, Favicon } from '../asset.model';
import { addAvailableIconsToList } from '../asset.facade';

@Component({
	selector: 'app-favicons',
	templateUrl: './favicons.component.html',
	styleUrls: ['./favicons.component.scss']
})
export class FaviconsComponent implements OnInit {
	favicon: Asset;
	icons: Asset[];

	constructor() { }

	ngOnInit(): void {
		this.favicon = new Favicon();
		this.icons = addAvailableIconsToList();
	}

}
