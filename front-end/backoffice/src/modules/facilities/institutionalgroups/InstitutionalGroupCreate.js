import React from 'react';
import {
    Create,
    required,
    SimpleForm,
    TextInput,
    ReferenceInput,
    SelectInput,
    maxLength
} from 'react-admin';
import CustomToolbar from '../../components/CustomToolbar';

const InstitutionalGroupCreate = props => (
    <Create {...props}>
        <SimpleForm redirect="show"  toolbar={<CustomToolbar/>}>
            <ReferenceInput
                reference="institutionalgrouptypes"
                source="typeId">
                <SelectInput optionText="value" optionValue="id" validate={[required()]}/>
            </ReferenceInput>
            <TextInput source="name" validate={[required(), maxLength(100)]}/>
        </SimpleForm>
    </Create>
);

export default InstitutionalGroupCreate;