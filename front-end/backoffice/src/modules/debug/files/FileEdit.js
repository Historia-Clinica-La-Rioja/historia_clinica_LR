import React from 'react';
import {
    Edit,
    SimpleForm,
    TextInput,
    TextField,
    usePermissions, NumberField, required,
} from 'react-admin';
import {
    SgxDateField,
} from '../../components';
import DownloadButton from '../../../libs/sgx/components/DownloadButton';
import FileFixButton from './FileFixButton';

const filenameSupplier = (record) => record.name;
const urlSupplier = (record) => `backoffice/files/${record.id}/downloadFile`;

const FileEdit = (props) => {
    const { permissions } = usePermissions();
    return (
        <Edit {...props}>
            <SimpleForm>
                <NumberField source="id" />
                <TextField source="name" />
                <TextInput source="relativePath" validate={[required()]}/>
                <TextField source="originalPath"/>
                <TextField source="source" />
                <TextField source="uuidfile" />
                <TextField source="contentType" />
                <NumberField source="size" />
                <TextField source="generatedBy" />
                <SgxDateField source="creationable.createdOn" showTime/>
                { permissions && permissions.isOn("HABILITAR_DESCARGA_DOCUMENTOS_PDF") && <DownloadButton filename={filenameSupplier} url={urlSupplier}/> }
                <FileFixButton/>
            </SimpleForm>
        </Edit>
    )
};

export default FileEdit;
