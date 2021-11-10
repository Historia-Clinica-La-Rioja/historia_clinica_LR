import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { CoreModule } from '@core/core.module';
import { ApiRestModule } from '@api-rest/api-rest.module';
import { PresentationModule } from '@presentation/presentation.module';

import { SystemExtensionComponent, InstitutionExtensionComponent } from './routes/extension/extension.component';

@NgModule({
	imports: [
		CommonModule,
		CoreModule,
		PresentationModule,
	],
	declarations: [
		SystemExtensionComponent,
		InstitutionExtensionComponent,
	],
})
export class ExtensionsModule { }
