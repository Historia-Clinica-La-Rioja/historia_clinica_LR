import React from 'react';
import {
    Show,
    SimpleShowLayout,
    TextField,
    UrlField,
} from 'react-admin';

const WcDefinitionPathShow = (props) => {
    return (
        <Show {...props}>
            <SimpleShowLayout>
                <TextField source="name" />
                <UrlField source="path" target="_blank"/>
            </SimpleShowLayout>
        </Show>
    )
};

export default WcDefinitionPathShow;
