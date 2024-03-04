import React from 'react';
import {
    Edit,
    SimpleForm,
    TextInput,
    maxLength,
    required,
} from 'react-admin';
import CustomToolbar from '../../components/CustomToolbar';

const ProcedureTemplateEdit = props => (
    <Edit {...props}>
        <SimpleForm toolbar={<CustomToolbar/>}>
            <TextInput source="description" validate={[maxLength(50), required()]}/>
        </SimpleForm>
    </Edit>
);

export default ProcedureTemplateEdit;