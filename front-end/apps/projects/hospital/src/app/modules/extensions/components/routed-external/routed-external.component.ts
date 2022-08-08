import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { SlotedInfo, WCExtensionsService } from '@extensions/services/wc-extensions.service';
import { map } from 'rxjs/operators';
import { WCParams } from '../ui-external-component/ui-external-component.component';

@Component({
	selector: 'app-routed-external',
	templateUrl: './routed-external.component.html',
	styleUrls: ['./routed-external.component.scss']
})
export class RoutedExternalComponent implements OnInit {

	constructor(
		private readonly activatedRoute: ActivatedRoute,
		private readonly wCExtensionsService: WCExtensionsService
	) { }

	extension: WCParams;

	ngOnInit(): void {

		this.activatedRoute.paramMap.subscribe(
			params => {

				this.wCExtensionsService.homeMenuExtension$
					.pipe(
						map(filter)
					)
					.subscribe(info => {
						this.extension = {
							componentName: info.componentName,
							url: info.url
						}
					});


				function filter(array: SlotedInfo[]): SlotedInfo {
					return array.find(e => params.get('wcId') === e.componentName)
				}
			}

		)
	}


}
