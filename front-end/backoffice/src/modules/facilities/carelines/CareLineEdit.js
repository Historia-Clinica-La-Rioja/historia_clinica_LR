import React from 'react';
import {
    BooleanInput,
    Edit,
    required,
    SimpleForm,
    TextInput,
} from 'react-admin';
import CustomToolbar from '../../components/CustomToolbar';

const CareLineEdit = props => (
    <Edit {...props}>
        <SimpleForm redirect="show" toolbar={<CustomToolbar isEdit={true}/>}>
            <TextInput source="description" validate={[required()]} />

            <BooleanInput source="consultation" disabled={false} initialValue={false}/>

            <BooleanInput source="procedure" disabled={false} initialValue={false}/>

            <BooleanInput source="classified" disabled={true} initialValue={false}/>

        </SimpleForm>
    </Edit>
);

export default CareLineEdit;
