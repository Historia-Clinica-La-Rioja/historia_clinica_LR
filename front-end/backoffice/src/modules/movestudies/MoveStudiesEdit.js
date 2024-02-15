import React from 'react';
import {
    SelectInput,
    Edit,
    ReferenceInput,
    SimpleForm,
    TextInput,
    NumberInput,
    required,
    regex
} from 'react-admin';
import CustomToolbar from '../components/CustomToolbar';

const validatePriority =regex(/^[01]$/,'resources.movestudies.errorPriority');
const MoveStudiesEdit = props => {

    return (
        <Edit {...props}>
            <SimpleForm redirect="show" toolbar={<CustomToolbar  />}>
                <ReferenceInput link={false} source="institutionId" reference="institutions" disabled>
                    <SelectInput optionText="name" optionValue="id" />
                </ReferenceInput>
                <TextInput source="imageId"/>
                <TextInput source="sizeImage" disabled />
                <TextInput source="result"/>
                <SelectInput source="status" choices={[
                    { id: 'PENDING', name: 'resources.movestudies.pending' },
                    { id: 'FINISHED', name: 'resources.movestudies.finished' },
                    { id: 'MOVING', name: 'resources.movestudies.moving' }
                ]} />
                <ReferenceInput  source="pacServerId"  reference="pacservers">
                    <SelectInput optionText="name" optionValue="id" />
                </ReferenceInput>
                <ReferenceInput source="orchestratorId" reference="orchestrator" disabled>
                    <SelectInput optionText="name" optionValue="id" />
                </ReferenceInput>
                <NumberInput source="attempsNumber" disabled/>
                <TextInput source="priorityMax" validate={[required(),validatePriority]} />

            </SimpleForm>

        </Edit>
    )
};

export default MoveStudiesEdit;