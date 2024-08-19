import React from 'react';
import {
    ReferenceField,
    Show,
    SimpleShowLayout,
    TextField
} from 'react-admin';

const ImageLvlPacShow = props => (
    <Show {...props}>
        <SimpleShowLayout>
            <TextField source="name" />
            <TextField source="aetitle" />
            <TextField source="domain" />
            <TextField source="port" />
            <ReferenceField source="sectorId" reference="sectors">
                <TextField source="description"/>
            </ReferenceField>
            <TextField source="localViewerUrl" />
        </SimpleShowLayout>
    </Show>
);

export default ImageLvlPacShow;
