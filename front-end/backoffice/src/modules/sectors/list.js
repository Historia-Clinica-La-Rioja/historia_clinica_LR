import React from 'react';
import {
    List,
    Datagrid,
    ReferenceField,
    TextField,
} from 'react-admin';

const SectorList = props => (
    <List {...props} >
        <Datagrid rowClick="show">
            <TextField source="description" />
            <ReferenceField source="institutionId" reference="institutions">
                <TextField source="name" />
            </ReferenceField>
        </Datagrid>
    </List>
);

export default SectorList;

