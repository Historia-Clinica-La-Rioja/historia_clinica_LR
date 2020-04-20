import React from 'react';
import {
    Show,
    SimpleShowLayout,
    ReferenceField,
    TextField,
    TopToolbar,
    EditButton,
} from 'react-admin';


const BedShowActions = ({ data }) => {
    return (!data || !data.id) ? <TopToolbar></TopToolbar> :
    (
    <TopToolbar>
    <EditButton basePath="/beds" record={{ id: data.id }} />
    </TopToolbar>
)
};
const BedShow = props => (
    <Show actions={<BedShowActions />} {...props}>
<SimpleShowLayout>
<TextField source="id" />
    <TextField source="bedNumber" />
    <ReferenceField source="roomId" reference="rooms">
    </ReferenceField>
    </SimpleShowLayout>
    </Show>
);

export default BedShow;
