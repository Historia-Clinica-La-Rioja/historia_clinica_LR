import React from 'react';
import {
    Show,
    SimpleShowLayout,
    TextField,
    ReferenceField,
} from 'react-admin';
import SgxDateField from '../../../dateComponents/sgxDateField';

const ReportQueueShow = (props) => {
    return (
        <Show {...props}>
            <SimpleShowLayout>
                <SgxDateField source="createdOn" showTime />
                <SgxDateField source="generatedOn" showTime />
                <TextField source="generatedError" sortable={false}/>
                <ReferenceField source="fileId"  link="show"  reference="files" sortable={false}>
                    <TextField source="name" />
                </ReferenceField>

            </SimpleShowLayout>
        </Show>
    )
};

export default ReportQueueShow;
