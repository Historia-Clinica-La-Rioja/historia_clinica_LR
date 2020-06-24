import React from 'react';
import {
    List,
    Datagrid,
    ReferenceField,
    TextField,
    Filter,
    ReferenceInput,
    SelectInput
} from 'react-admin';

const SectorFilter = props =>(
    <Filter {...props}>
        <ReferenceInput source="institutionId" reference="institutions" alwaysOn allowEmpty={false}>
            <SelectInput optionText="name" />
        </ReferenceInput>
    </Filter>
);


const SectorList = props => (
    <List {...props} hasCreate={false} filters={<SectorFilter />} >
        <Datagrid rowClick="show">
            <TextField source="description" />
            <ReferenceField source="institutionId" reference="institutions">
                <TextField source="name" />
            </ReferenceField>
        </Datagrid>
    </List>
);

export default SectorList;

