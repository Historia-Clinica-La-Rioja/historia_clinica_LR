import React from 'react';
import {
    AutocompleteInput,
    Datagrid,
    DateInput,
    Edit,
    EditButton,
    ReferenceInput,
    ReferenceManyField,
    required,
    SimpleForm,
    TextField,
    TextInput,
} from 'react-admin';
import CreateRelatedButton from '../components/CreateRelatedButton';
import SectionTitle from '../components/SectionTitle';
import CustomToolbar from "../components/CustomToolbar";

const RoomEdit = props => (
    <Edit {...props}>
        <SimpleForm redirect="show" toolbar={<CustomToolbar isEdit={true}/>}>
            <TextInput source="roomNumber" validate={[required()]} />
            <TextInput source="description" validate={[required()]} />
            <TextInput source="type" validate={[required()]} />
            <DateInput source="dischargeDate" />

            <ReferenceInput
                source="sectorId"
                reference="sectors"
                sort={{ field: 'description', order: 'ASC' }}
                filterToQuery={searchText => ({description: searchText})}
            >
                <AutocompleteInput optionText="description" optionValue="id" options={{ disabled: true }} />
            </ReferenceInput>

            <SectionTitle label="resources.rooms.fields.beds"/>
            <CreateRelatedButton
                reference="beds"
                refFieldName="roomId"
                label="resources.beds.createRelated"
            />
            {/*TODO: Aislar esto en un componente. También se usa en show.js*/}
            <ReferenceManyField
                addLabel={false}
                reference="beds"
                target="roomId"
                sort={{ field: 'bedNumber', order: 'DESC' }}
            >
                <Datagrid rowClick="show">
                    <TextField source="bedNumber" />
                    <EditButton />
                </Datagrid>
            </ReferenceManyField>

        </SimpleForm>
    </Edit>
);

export default RoomEdit;
