import React from 'react';
import {
    SelectInput,
    Edit,
    ReferenceInput,
    required,
    SimpleForm,
    TextInput,
    maxLength,
    useGetOne,
    BooleanInput,
    AutocompleteInput
} from 'react-admin';

import CustomToolbar from '../../components/CustomToolbar';


const EquipmentEdit = props => {

    const renderModality = (choice) => choice ? `${choice.acronym} (${choice.description})` : '';
    const equipment = useGetOne('equipment', props.id );
    let filterValue = equipment?.data?.sectorId;
    return (
        <Edit {...props}>
            <SimpleForm redirect="show" toolbar={<CustomToolbar  />}>
                <TextInput source="name" validate={[required(), maxLength(40)]} />
                <TextInput source="aeTitle" validate={[required(), maxLength(15)]} />
                <ReferenceInput source="sectorId" reference="sectors"disabled={true}>
                    <SelectInput optionText="description" optionValue="id" />
                </ReferenceInput>
                <ReferenceInput  source="pacServerId"  reference="pacserversimagelvl"
                    filter ={{ sectorId :filterValue}}
                >
                    <SelectInput optionText="name" optionValue="id" />
                </ReferenceInput>
                <ReferenceInput source="orchestratorId" reference="orchestrator"
                    filter ={{ sectorId :filterValue}}
                >
                    <SelectInput optionText="name" optionValue="id" />
                </ReferenceInput>
                <ReferenceInput source="modalityId" reference="modality"
                    validate={[required()]}
                >
                    <AutocompleteInput optionText={renderModality} optionValue="id"/>
                </ReferenceInput>
                <BooleanInput label="resources.equipment.fields.createId" source="createId" />
            </SimpleForm>

        </Edit>
    )
};

export default EquipmentEdit;