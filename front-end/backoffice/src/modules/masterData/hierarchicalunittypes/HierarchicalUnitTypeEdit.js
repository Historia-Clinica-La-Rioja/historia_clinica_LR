import React from 'react';
import {
    Edit,
    required,
    SimpleForm,
    TextInput
} from 'react-admin';
import CustomToolbar from '../../components/CustomToolbar';

const HierarchicalUnitTypeEdit = props => (
    <Edit {...props}>
        <SimpleForm redirect="show" toolbar={<CustomToolbar isEdit={true}/>}>
            <TextInput source="description" validate={[required()]} disabled={false}/>
        </SimpleForm>
    </Edit>
);

export default HierarchicalUnitTypeEdit;
