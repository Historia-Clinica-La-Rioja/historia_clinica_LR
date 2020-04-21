import React from 'react';
import {
    TextInput,
    ReferenceInput,
    SelectInput,
    Create,
    SimpleForm,
    required
} from 'react-admin';

const ProfessionalSpecialtyCreate = props => (
    <Create {...props}>
        <SimpleForm redirect="show" >
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
    </Create>
);

export default ProfessionalSpecialtyCreate;
