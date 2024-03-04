import {
    List,
    Datagrid,
    TextField
} from 'react-admin';
import React from 'react';

const ImageLvlPacList = props => {
    return (
        <List {...props} bulkActionButtons={false}>
            <Datagrid rowClick="show">
                <TextField source="name" />
                <TextField source="aetitle" />
                <TextField source="domain" />
                <TextField source="port" />
            </Datagrid>
        </List>
    );
};

export default ImageLvlPacList;