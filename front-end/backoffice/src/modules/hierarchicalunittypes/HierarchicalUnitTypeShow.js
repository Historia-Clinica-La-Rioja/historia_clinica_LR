import React from 'react';
import {
    Show,
    TextField,
    SimpleShowLayout,
    NumberField
} from 'react-admin';

const DEFAULT_TYPE_IDS = [1, 2, 3, 4, 5, 6, 7, 8];

const HierarchicalUnitTypeShow = props => {
    const isntDefaultTypeId = !Array.from(DEFAULT_TYPE_IDS).includes(Number(props?.id));
    return (
         <Show {...props} hasEdit={isntDefaultTypeId}>
            <SimpleShowLayout>
                <NumberField source="id"/>
                <TextField source="description"/>
            </SimpleShowLayout>
        </Show>
)
};
export default HierarchicalUnitTypeShow;
