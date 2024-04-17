import React from 'react';
import {
    BooleanInput,
    Edit,
    SimpleForm,
    TextInput
} from 'react-admin';
import { CustomToolbar } from '../../components';


const UnitOfMeasureEdit = props => (
    <Edit {...props}>
        <SimpleForm toolbar={<CustomToolbar isEdit={false}/>}>
            <TextInput source="code" disabled={true}/>
            <BooleanInput source="enabled"/>
        </SimpleForm>
    </Edit>
);

export default UnitOfMeasureEdit;
