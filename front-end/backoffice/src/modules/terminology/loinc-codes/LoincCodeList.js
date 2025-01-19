import React from 'react';
import {
    List,
    Datagrid,
    TextField,
    ReferenceField,
    EditButton,
    FunctionField,
    TextInput,
    Filter
} from 'react-admin';

const LoincCodeFilter = (props) => (
    <Filter {...props}>
        <TextInput source="code"/>
        <TextInput source="description"/>
        <TextInput source="customDisplayName"/>
    </Filter>
);

const LoincCodeList = props => (
    <List 
        {...props}
        exporter={false}
        bulkActionButtons={false}
        sort={{ field: 'code', order: 'ASC' }}
        filters={<LoincCodeFilter/>}
    >
        <Datagrid rowClick="show">
            <TextField source="code"/>
            <TextField source="description"/>
            <ReferenceField source="statusId" reference="loinc-statuses" link={false} sortable={false}>
                <TextField source="description" sortable={false}/>
            </ReferenceField>
            <ReferenceField source="systemId" reference="loinc-systems" link={false} sortable={false}>
                <FunctionField render={(system) => system.descriptionVariant || system.description} />
            </ReferenceField>
            <TextField source="displayName"/>
            <TextField source="customDisplayName"/>
            <EditButton/>
        </Datagrid>
    </List>
);

export default LoincCodeList;