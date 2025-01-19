import { Component, OnInit } from '@angular/core';
import { NotificationTemplateRenderDto } from '@api-rest/api-model';
import { NotificationTemplateService } from '@api-rest/services/notification-template.service';

@Component({
	selector: 'app-notification-templates',
	templateUrl: './notification-templates.component.html',
	styleUrls: ['./notification-templates.component.scss']
})
export class NotificationTemplatesComponent implements OnInit {

	templates: NotificationTemplateRenderDto[];

	constructor(
		private notificationTemplateService: NotificationTemplateService,
	) { }

	ngOnInit(): void {
		this.notificationTemplateService.list().subscribe(
			templates => {
				this.templates = templates;
			}
		);
	}
}
