import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { CoreModule } from '@core/core.module';

import { InstitutionExtensionComponent, SystemExtensionComponent } from './routes/extension/extension.component';
import { PageLayoutComponent } from './components/page-layout/page-layout.component';
import { TabsComponent } from './components/tabs/tabs.component';
import { UiComponentComponent } from './components/ui-component/ui-component.component';
import { UiComponentListComponent } from './components/ui-component-list/ui-component-list.component';

@NgModule({
	imports: [
		CommonModule,
		CoreModule,
	],
	declarations: [
		InstitutionExtensionComponent,
		PageLayoutComponent,
		SystemExtensionComponent,
		TabsComponent,
		UiComponentComponent,
		UiComponentListComponent,
	],
	exports: [
		PageLayoutComponent,
	]
})
export class ExtensionsModule { }
