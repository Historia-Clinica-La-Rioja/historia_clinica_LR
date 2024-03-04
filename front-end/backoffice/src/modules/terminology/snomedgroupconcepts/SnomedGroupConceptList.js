import React from 'react';
import {
    List,
    Datagrid,
    TextField,
    NumberField,
    TextInput,
    Filter,
    ReferenceField,
    ReferenceInput,
    AutocompleteInput,
} from 'react-admin';
import {
    SgxDateField,
} from '../../components';

const SnomedGroupConceptListFilter = props =>(
    <Filter {...props}>
        <ReferenceInput source="groupId" reference="snomedgroups"
            filterToQuery={searchText => ({ description: searchText })}
            alwaysOn>
            <AutocompleteInput optionText="description" optionValue="id" />
        </ReferenceInput>
        <TextInput source="conceptPt" alwaysOn/>
        <TextInput source="conceptSctid" />
    </Filter>
);

const SnomedGroupConceptList = props => (
    <List 
        {...props}
        exporter={false}
        bulkActionButtons={false}
        // sort={{ field: 'code', order: 'ASC' }
        filters={<SnomedGroupConceptListFilter/>}
    >
        <Datagrid rowClick="show">
            <NumberField source="id" sortable={false} />
            <NumberField source="conceptId" sortable={false} />
            <ReferenceField source="groupId" reference="snomedgroups" link={false} sortable={false}>
                <TextField source="description" />
            </ReferenceField>
            <TextField source="conceptSctid"/>
            <TextField source="conceptPt"/>
            <NumberField source="orden" />
            <SgxDateField source="lastUpdate"/>
        </Datagrid>
    </List>
);

export default SnomedGroupConceptList;