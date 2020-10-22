import React from 'react';
import {
    TextInput,
    ReferenceInput,
    SelectInput,
    Edit,
    SimpleForm,
    required
} from 'react-admin';
import SaveCancelToolbar from "../../modules/components/save-cancel-toolbar";

const ProfessionalSpecialtyEdit = props => (
    <Edit {...props}>
        <SimpleForm redirect="show" toolbar={<SaveCancelToolbar />}>
            <TextInput source="description" validate={[required()]} />
            <TextInput source="descriptionProfessionRef" validate={[required()]} />
            <TextInput source="sctidCode" validate={[required()]} />
            <ReferenceInput
                source="educationTypeId"
                reference="educationtypes"
                sort={{ field: 'description', order: 'ASC' }}
            >
                <SelectInput optionText="description" optionValue="id"/>
            </ReferenceInput>
        </SimpleForm>
    </Edit>
);

export default ProfessionalSpecialtyEdit;
