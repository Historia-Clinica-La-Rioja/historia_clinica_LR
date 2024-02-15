import { moduleMetadata, type Meta, type StoryObj } from '@storybook/angular';
import { FactorDeRiesgoCurrentPreviousComponent } from './factor-de-riesgo-current-previous.component';

import { PresentationModule } from '@presentation/presentation.module';

const meta: Meta<FactorDeRiesgoCurrentPreviousComponent> = {
	title: 'HSI/FactorDeRiesgoCurrentPreviousComponent',
	component: FactorDeRiesgoCurrentPreviousComponent,
	tags: ['autodocs'],
	decorators: [
		moduleMetadata({
			imports: [PresentationModule, ]
		}),
	],
};

export default meta;
type Story = StoryObj<FactorDeRiesgoCurrentPreviousComponent>;

export const Custom: Story = {
	args: {
		riskFactor: {
			description: 'Titulo',
			currentValue: {
				value: 2,
				effectiveTime: new Date()
			},
			previousValue: {
				value: 1,
				effectiveTime: new Date()
			}
		},
	},
}
