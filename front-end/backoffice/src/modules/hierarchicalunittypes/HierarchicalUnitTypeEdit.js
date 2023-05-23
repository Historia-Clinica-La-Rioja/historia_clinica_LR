import React from 'react';
import {
    Edit,
    FormDataConsumer,
    required,
    SimpleForm,
    TextInput
} from 'react-admin';
import CustomToolbar from '../components/CustomToolbar';

const HierarchicalUnitTypeEdit = props => (
    <Edit {...props}>
        <SimpleForm redirect="show" toolbar={<CustomToolbar isEdit={true}/>}>
            <FormDataConsumer>
                <TextInput source="description" validate={[required()]} disabled={false}/>
            </FormDataConsumer>
        </SimpleForm>
    </Edit>
);

export default HierarchicalUnitTypeEdit;
