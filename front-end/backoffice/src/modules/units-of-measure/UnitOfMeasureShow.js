import React from 'react';
import {
    Show,
    SimpleShowLayout,
    TextField,
} from 'react-admin';

const UnitOfMeasureShow = props => (
    <Show {...props}>
        <SimpleShowLayout>
            <TextField source="code"/>
            <TextField source="enabled"/>
        </SimpleShowLayout>
    </Show>
);

export default UnitOfMeasureShow;
