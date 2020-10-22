import React from 'react';
import { SaveButton, Toolbar, ListButton } from 'react-admin';

const SaveCancelToolbar = props => (
    <Toolbar {...props} >
        <SaveButton />
        <ListButton basePath="basepath" />
    </Toolbar>
);

export default SaveCancelToolbar;