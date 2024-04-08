import { NgModule } from "@angular/core";
import { TranslateLoader, TranslateModule, TranslateService } from "@ngx-translate/core";
import { DEFAULT_LANG } from "./app.component";
import { createTranslateLoader } from "./app.module";
import { HttpClient } from "@angular/common/http";

/**
  A utility module adding I18N support for Storybook stories
 **/
@NgModule({
	imports: [TranslateModule.forRoot({
		loader: {
			provide: TranslateLoader,
			useFactory: (createTranslateLoader),
			deps: [HttpClient]
		}
	})],
})
export class StorybookTranslateModule {
	constructor(translateService: TranslateService) {
		console.log("Configuring the translation service: ", translateService);
		console.log("Translations: ", translateService.translations);
		translateService.setDefaultLang(DEFAULT_LANG);
		translateService.use(DEFAULT_LANG);
	}
}
