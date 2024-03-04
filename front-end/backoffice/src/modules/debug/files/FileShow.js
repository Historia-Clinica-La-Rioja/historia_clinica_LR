import React from 'react';
import {
    Show,
    SimpleShowLayout,
    TextField,
    usePermissions, NumberField,
} from 'react-admin';
import {
    SgxDateField,
} from '../../components';
import DownloadButton from '../../../libs/sgx/components/DownloadButton';
import FileFixButton from './FileFixButton';

const filenameSupplier = (record) => record.name;
const urlSupplier = (record) => `backoffice/files/${record.id}/downloadFile`;

const FileShow = (props) => {
    const { permissions } = usePermissions();
    return (
        <Show {...props}>
            <SimpleShowLayout>
                <NumberField source="id" />
                <TextField source="name" />
                <TextField source="relativePath" />
                <TextField source="originalPath" />
                <TextField source="source" />
                <TextField source="uuidfile" />
                <TextField source="contentType" />
                <NumberField source="size" />
                <TextField source="generatedBy" />
                <SgxDateField source="creationable.createdOn" showTime/>
                { permissions && permissions.isOn("HABILITAR_DESCARGA_DOCUMENTOS_PDF") && <DownloadButton filename={filenameSupplier} url={urlSupplier}/> }
                <FileFixButton/>
            </SimpleShowLayout>
        </Show>
    )
};

export default FileShow;
