import React from 'react';
import { SaveButton, Toolbar } from 'react-admin';

const OnlySaveToolbar = props => (
    <Toolbar {...props} >
        <SaveButton />
    </Toolbar>
);

export default OnlySaveToolbar;