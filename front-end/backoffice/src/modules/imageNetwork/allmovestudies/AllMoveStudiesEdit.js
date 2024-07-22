import React from 'react';
import {
    SelectInput,
    Edit,
    ReferenceInput,
    SimpleForm,
    TextInput,
    DateInput
} from 'react-admin';
import CustomToolbar from '../../components/CustomToolbar';

const MoveStudiesEdit = props => {

    return (
        <Edit {...props}>
            <SimpleForm redirect="show" toolbar={<CustomToolbar  />}>
                <ReferenceInput link={false} source="institutionId" reference="institutions" disabled>
                    <SelectInput optionText="name" optionValue="id" />
                </ReferenceInput>
                <TextInput source="identificationNumber" disabled/>
                <TextInput source="firstName" disabled/>
                <TextInput source="lastName" disabled/>
                <DateInput  disabled source="appoinmentDate"/>
                <TextInput source="appoinmentTime" type="time" disabled/>
                <TextInput source="imageId"/>
                <TextInput source="result" disabled/>
                <SelectInput source="status" choices={[
                    { id: 'PENDING', name: 'resources.allmovestudies.pending' },
                    { id: 'FINISHED', name: 'resources.allmovestudies.finished' },
                    { id: 'MOVING', name: 'resources.allmovestudies.moving' },
                    { id: 'FAILED', name: 'resources.allmovestudies.failed' }
                ]} />


            </SimpleForm>

        </Edit>
    )
};

export default MoveStudiesEdit;