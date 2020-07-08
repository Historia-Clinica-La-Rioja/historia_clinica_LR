import React from 'react';
import {
    TextInput,
    AutocompleteInput,
    ReferenceInput,
    Edit,
    SimpleForm,
    required,
    DateInput,
    TextField,
    ReferenceField,
    ReferenceManyField,
    Datagrid,
    EditButton,
} from 'react-admin';
import CreateRelatedButton from '../components/CreateRelatedButton';
import SectionTitle from '../components/SectionTitle';

const RoomEdit = props => (
    <Edit {...props}>
        <SimpleForm redirect="show">
            <TextInput source="roomNumber" validate={[required()]} />
            <TextInput source="description" validate={[required()]} />
            <TextInput source="type" validate={[required()]} />
            <DateInput source="dischargeDate" />
            <ReferenceInput
                source="clinicalSpecialtySectorId"
                reference="clinicalspecialtysectors"
                sort={{ field: 'description', order: 'ASC' }}
                filterToQuery={searchText => ({description: searchText})}                
            >
                <AutocompleteInput optionText="description" optionValue="id"/>
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
                    <ReferenceField source="bedCategoryId" reference="bedcategories" >
                        <TextField source="description" />
                    </ReferenceField>
                    <EditButton />
                </Datagrid>
            </ReferenceManyField>

        </SimpleForm>
    </Edit>
);

export default RoomEdit;
