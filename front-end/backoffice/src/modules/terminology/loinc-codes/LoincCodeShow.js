import React from 'react';
import {
    Show,
    SimpleShowLayout,
    TextField,
    ReferenceField
} from 'react-admin';

const LoincCodeShow = props => (
    <Show {...props}>
        <SimpleShowLayout>
            <TextField source="code"/>
            <TextField source="description"/>
            <ReferenceField source="statusId" reference="loinc-statuses" link={false}>
                <TextField source="description" />
            </ReferenceField>
            <ReferenceField source="systemId" reference="loinc-systems" link={false}>
                <TextField source="description" />
            </ReferenceField>
            <TextField source="displayName"/>
            <TextField source="customDisplayName"/>
        </SimpleShowLayout>
    </Show>
);

export default LoincCodeShow;