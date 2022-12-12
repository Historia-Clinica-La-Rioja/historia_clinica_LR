import React from 'react';
import {
    List,
    Datagrid,
    TextField,
} from 'react-admin';

const EpisodeDocumentTypeList = props => (
    <List {...props}>
        <Datagrid rowClick="show">
            <TextField source="id" />
            <TextField source="description" />
        </Datagrid>
    </List>
);

export default EpisodeDocumentTypeList;