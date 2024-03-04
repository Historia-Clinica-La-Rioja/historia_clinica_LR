import React from 'react';
import {
    Create,
    TextInput,
    SimpleForm,
    required
} from 'react-admin';
import CustomToolbar from '../../components/CustomToolbar';

const HierarchicalUnitTypeCreate = props => (
    <Create {...props}>
        <SimpleForm redirect="show" toolbar={<CustomToolbar />}>
            <TextInput source="description" validate={[required()]} />
        </SimpleForm>
    </Create>
);

export default HierarchicalUnitTypeCreate;
