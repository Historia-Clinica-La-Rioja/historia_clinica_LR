import React from 'react';
import {
    TextInput,
    ReferenceInput,
    AutocompleteInput,
    Edit,
    SimpleForm,
    required,
    ReferenceManyField,
    Datagrid,
    DeleteButton,
    TextField,
    ReferenceField,
} from 'react-admin';
import CreateRelatedButton from '../components/CreateRelatedButton';
import SectionTitle from '../components/SectionTitle';

const SectorEdit = props => (
    <Edit {...props}>
        <SimpleForm redirect="show" >
            <TextInput source="description" validate={[required()]} />
            <ReferenceInput
                source="institutionId"
                reference="institutions"
                sort={{ field: 'name', order: 'ASC' }}
                filterToQuery={searchText => ({name: searchText})}                
            >
                <AutocompleteInput optionText="name" optionValue="id"/>
            </ReferenceInput>
            <SectionTitle label="resources.sectors.fields.clinicalspecialtysectors"/>
            <CreateRelatedButton
                reference="clinicalspecialtysectors"
                refFieldName="sectorId"
                label="resources.clinicalspecialtysectors.createRelated"
            />
            {/*TODO: Aislar esto en un componente. También se usa en show.js*/}
            <ReferenceManyField
                addLabel={false}
                reference="clinicalspecialtysectors"
                target="sectorId"
                sort={{ field: 'description', order: 'DESC' }}
            >
                <Datagrid rowClick="show">
                    <TextField source="description" />
                    <ReferenceField source="clinicalSpecialtyId" reference="clinicalspecialties">
                        <TextField source="name" />
                    </ReferenceField>
                    <DeleteButton />
                </Datagrid>
            </ReferenceManyField>
        </SimpleForm>
    </Edit>
);

export default SectorEdit;
