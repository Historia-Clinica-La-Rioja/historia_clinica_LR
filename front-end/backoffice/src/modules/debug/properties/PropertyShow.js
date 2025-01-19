import React from 'react';
import {
    Show,
    SimpleShowLayout,
    TextField,
} from 'react-admin';

import {
    SgxDateField,
} from '../../components';

const PropertyShow = (props) => {
    return (
        <Show {...props}>
            <SimpleShowLayout>
                <TextField source="property" />
                <TextField source="value" />
                <TextField source="origin" />
                <TextField source="description" />
                <TextField source="nodeId"/>
                <SgxDateField source="updatedOn" showTime />
            </SimpleShowLayout>
        </Show>
    )
};

export default PropertyShow;
