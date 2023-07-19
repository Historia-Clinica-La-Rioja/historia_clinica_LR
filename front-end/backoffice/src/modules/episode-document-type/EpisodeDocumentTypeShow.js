import React from 'react';
import {
    NumberField,
    Show,
    SimpleShowLayout,
    TextField,
} from 'react-admin';

const EpisodeDocumentTypeShow = props => {
    return (<Show {...props}>
        <SimpleShowLayout>
            <NumberField source="id" />
            <TextField source="description" />
            <TextField source="richTextBody" label=""/>
        </SimpleShowLayout>
    </Show>)
};

export default EpisodeDocumentTypeShow;