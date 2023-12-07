import { NgModule } from "@angular/core";
import { TranslateModule, TranslateService } from "@ngx-translate/core";
import { DEFAULT_LANG } from "./app.component";

/**
  A utility module adding I18N support for Storybook stories
 **/
@NgModule({
  imports: [TranslateModule.forRoot()],
})
export class StorybookTranslateModule {
  constructor(translateService: TranslateService) {
    console.log("Configuring the translation service: ", translateService);
    console.log("Translations: ", translateService.translations);
    translateService.setDefaultLang(DEFAULT_LANG);
    translateService.use(DEFAULT_LANG);
  }
}
