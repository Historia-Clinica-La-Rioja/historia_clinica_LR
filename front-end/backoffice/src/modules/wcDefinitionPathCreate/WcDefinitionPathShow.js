import React from 'react';
import {
    Show,
    SimpleShowLayout,
    TextField,
} from 'react-admin';

const WcDefinitionPathShow = (props) => {
    return (
        <Show {...props}>
            <SimpleShowLayout>
                <TextField source="name" />
                <TextField source="path" />
            </SimpleShowLayout>
        </Show>
    )
};

export default WcDefinitionPathShow;
