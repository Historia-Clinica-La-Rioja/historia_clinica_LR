import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
// deps
import { PresentationModule } from '@presentation/presentation.module';
// routing
import { InstitutionExtensionComponent, SystemExtensionComponent } from './routes/extension/extension.component';
// components
import { CardComponent } from './components/card/card.component';
import { JsonComponent } from './components/json/json.component';
import { PageLayoutComponent } from './components/page-layout/page-layout.component';
import { TabsComponent } from './components/tabs/tabs.component';
import { UiComponentComponent } from './components/ui-component/ui-component.component';
import { UiComponentListComponent } from './components/ui-component-list/ui-component-list.component';

@NgModule({
	imports: [
		CommonModule,
		// deps
		PresentationModule,
	],
	declarations: [
		// routing
		InstitutionExtensionComponent,
		SystemExtensionComponent,
		// components
		CardComponent,
		JsonComponent,
		PageLayoutComponent,
		TabsComponent,
		UiComponentComponent,
		UiComponentListComponent,
	],
	exports: [
		// components
		PageLayoutComponent,
	]
})
export class ExtensionsModule { }
