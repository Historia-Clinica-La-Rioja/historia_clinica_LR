import { Meta, StoryObj, moduleMetadata } from "@storybook/angular";
import { REGISTER_EDITOR_CASES, RegisterEditorInfoComponent } from "./register-editor-info.component";
import { ReactiveFormsModule } from "@angular/forms";

const meta: Meta<RegisterEditorInfoComponent> = {
	title: 'Presentation/RegisterEditorInfoComponent',
	component: RegisterEditorInfoComponent,
	tags: ['autodocs'],
	decorators: [
		moduleMetadata({
			imports: [ReactiveFormsModule],
		}),
	],
};

export default meta;


type Story = StoryObj<RegisterEditorInfoComponent>;


export const NameDate: Story = {
	args: {
		registerEditorCase: REGISTER_EDITOR_CASES.DATE,
		registerEditor: {
			createdBy: 'Jose Perez',
			date: new Date()
		}
	}
}

export const NameDateHours: Story = {
	args: {
		registerEditorCase: REGISTER_EDITOR_CASES.DATE_HOUR,
		registerEditor: {
			createdBy: 'Jose Perez',
			date: new Date()

		}
	}
}

export const NameInstitutionDate: Story = {
	args: {
		registerEditorCase: REGISTER_EDITOR_CASES.DATE,
		registerEditor: {
			createdBy: 'Jose Perez',
			institution: 'Hospital Ramon Santamarina',
			date: new Date()
		}
	}
}

export const NameInstitutionDateHours: Story = {
	args: {
		registerEditorCase: REGISTER_EDITOR_CASES.DATE_HOUR,
		registerEditor: {
			createdBy: 'Jose Perez',
			institution: 'Hospital Ramon Santamarina',
			date: new Date()
		}
	}
}
