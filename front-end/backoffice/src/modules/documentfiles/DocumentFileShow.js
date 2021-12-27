import React from 'react';
import {
    Show,
    SimpleShowLayout,
    TextField,
    ReferenceField,
} from 'react-admin';
import SgxDateField from "../../dateComponents/sgxDateField";
import DownloadButton from '../../libs/sgx/components/DownloadButton';

const filenameSupplier = (record) => record.filename;
const urlSupplier = (record) => `documents/${record.id}/downloadFile`;

const DocumentFileShow = ({ permissions, ...props }) => (
    <Show {...props}>
        <SimpleShowLayout>
            <TextField source="filename" />
            <ReferenceField source="typeId" reference="documenttypes" link={false}>
                <TextField source="description" />
            </ReferenceField>
            <SgxDateField source="creationable.createdOn" showTime/>
            { permissions && permissions.isOn("HABILITAR_DESCARGA_DOCUMENTOS_PDF") && <DownloadButton filename={filenameSupplier} url={urlSupplier}/> }
        </SimpleShowLayout>
    </Show>
);

export default DocumentFileShow;
