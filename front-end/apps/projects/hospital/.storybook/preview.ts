import { provideAnimations } from "@angular/platform-browser/animations";
import { CoreMaterialModule } from "@core/core.material.module";
import { AppMaterialModule } from "@material/app.material.module";
import { TranslateModule } from "@ngx-translate/core";
import { applicationConfig, moduleMetadata, type Preview } from "@storybook/angular";
import { StorybookTranslateModule } from "../src/app/storybook-translate-module";
import { FlexLayoutModule } from "@angular/flex-layout";


export const decorators = [
	moduleMetadata({
		imports: [
			AppMaterialModule,
			CoreMaterialModule,
			FlexLayoutModule,
			StorybookTranslateModule,
			TranslateModule,
		],
	}),
	applicationConfig({
		providers: provideAnimations()
	})
];

const preview: Preview = {
	parameters: {
		actions: { argTypesRegex: "^on[A-Z].*" },
		controls: {
			matchers: {
				color: /(background|color)$/i,
				date: /Date$/,
			},
		},
	},
	decorators: decorators
};

export default preview;
