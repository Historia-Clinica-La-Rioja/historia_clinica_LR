import React from 'react';
import {
    DateField,
    Show,
    SimpleShowLayout,
    TextField,
    ReferenceField,
} from 'react-admin';

const ReportQueueShow = (props) => {
    return (
        <Show {...props}>
            <SimpleShowLayout>
                <DateField source="createdOn" showTime />
                <DateField source="generatedOn" showTime />
                <TextField source="generatedError" sortable={false}/>
                <ReferenceField source="fileId"  link="show"  reference="files" sortable={false}>
                    <TextField source="name" />
                </ReferenceField>

            </SimpleShowLayout>
        </Show>
    )
};

export default ReportQueueShow;
