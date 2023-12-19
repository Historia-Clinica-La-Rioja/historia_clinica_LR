import React from 'react';
import {
    Create,
    maxLength,
    required,
    SimpleForm,
    TextInput 
} from 'react-admin';
import CustomToolbar from '../components/CustomToolbar';

const ProcedureTemplateCreate = props => (
    <Create {...props}>
        <SimpleForm toolbar={<CustomToolbar/>} redirect="list">
            <TextInput source="description" validate={[maxLength(50), required()]}/>
        </SimpleForm>
    </Create>
);

export default ProcedureTemplateCreate;
