import React from 'react';
import {
    List,
    Datagrid,
    TextField,
} from 'react-admin';

const SectorList = props => (
    <List {...props} >
        <Datagrid rowClick="show">
            <TextField source="description" />
            <TextField source="descriptionProfessionRef" />
            <TextField source="sctidCode" />
        </Datagrid>
    </List>
);

export default SectorList;

