import React from 'react';
import {
    List,
    Datagrid,
    TextField,
    Filter,
    TextInput
} from 'react-admin';

const SectorFilter = (props) => (
    <Filter {...props}>
        <TextInput source="description" />
        <TextInput source="sctidCode" />
    </Filter>
);

const SectorList = props => (
    <List {...props} filters={<SectorFilter />}>
        <Datagrid rowClick="show">
            <TextField source="description" />
            <TextField source="descriptionProfessionRef" />
            <TextField source="sctidCode" />
        </Datagrid>
    </List>
);

export default SectorList;

