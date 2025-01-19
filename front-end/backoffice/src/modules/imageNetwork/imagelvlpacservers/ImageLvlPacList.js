import {
    List,
    Datagrid,
    TextField
} from 'react-admin';
import React from 'react';

const ImageLvlPacList = props => {
    return (
        <List {...props} bulkActionButtons={false} hasCreate={false}>
            <Datagrid rowClick="show">
                <TextField source="name" />
                <TextField source="aetitle" />
                <TextField source="domain" />
                <TextField source="port" />
                <TextField source="localViewerUrl" />
            </Datagrid>
        </List>
    );
};

export default ImageLvlPacList;