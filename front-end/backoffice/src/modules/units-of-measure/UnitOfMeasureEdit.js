import React from 'react';
import {
    BooleanInput,
    Edit,
    SimpleForm,
    TextInput
} from 'react-admin';


const UnitOfMeasureEdit = props => (
    <Edit {...props}>
        <SimpleForm>
            <TextInput source="code" disabled={true}/>
            <BooleanInput source="enabled"/>
        </SimpleForm>
    </Edit>
);

export default UnitOfMeasureEdit;
