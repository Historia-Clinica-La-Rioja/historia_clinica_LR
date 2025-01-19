import React from 'react';
import {
    List,
    Datagrid,
    TextField,
    Filter,
    TextInput,
    ReferenceField,
    FunctionField,
    ReferenceInput,
    AutocompleteInput,
    NumberInput
} from 'react-admin';
import {
    SgxDateField,
} from '../../components';

const SnvsFilter = (props) => (
    <Filter {...props}>
        <NumberInput source="id"/>
        <NumberInput source="groupEventId"/>
        <NumberInput source="eventId"/>
        <NumberInput source="patientId"/>
        <TextInput source="snomedSctid"/>
        <TextInput source="status"/>
        <NumberInput source="responseCode"/>
        <NumberInput source="professionalId"/>
        <ReferenceInput source="institutionId" reference="institutions" allowEmpty={false} filterToQuery={searchText => ({name: searchText})}>
            <AutocompleteInput optionText={"name"} optionValue={"id"} resettable={true}/>
        </ReferenceInput>
        <NumberInput source="sisaRegisteredId"/>
    </Filter>
);

const SnvsList = props => (
    <List {...props} filters={<SnvsFilter />} bulkActionButtons={false}>
        <Datagrid rowClick="show">
            <TextField source="id" />
            <TextField source="groupEventId" />
            <TextField source="eventId" />
            <TextField source="manualClassificationId" />
            <TextField source="patientId" />
            <ReferenceField label="resources.snvs.patient" source="patientId" reference="patient" link={false}>
                <ReferenceField source="personId" reference="person" link={false}>
                    <FunctionField render={p => `${p.firstName} ${p.lastName}`} />
                </ReferenceField>
            </ReferenceField>
            <TextField source="snomedSctid" />
            <TextField source="snomedPt" />
            <TextField source="status" />
            <TextField source="responseCode" />
            <TextField source="professionalId" />
            <ReferenceField label="resources.snvs.professional" source="professionalId" reference="healthcareprofessionals" link={false}>
                <ReferenceField source="personId" reference="person" link={false}>
                    <FunctionField render={professional => `${professional.firstName} ${professional.lastName}`} />
                </ReferenceField>
            </ReferenceField>
            <ReferenceField source="institutionId" reference="institutions" link={false}>
                <TextField source="name" />
            </ReferenceField>
            <TextField source="sisaRegisteredId" />
            <SgxDateField source="lastUpdate" />
        </Datagrid>
    </List>
);

export default SnvsList;

