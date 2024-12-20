import React from 'react';
import {
    Show,
    SimpleShowLayout,
    TextField,
    ReferenceField,
    usePermissions,
} from 'react-admin';
import {
    SgxDateField,
} from '../../components';

import DownloadButton from '../../../libs/sgx/components/DownloadButton';
import FileRebuildButton from './DocumentFileRebuildButton';

const filenameSupplier = (record) => record.filename;
const urlSupplier = (record) => `backoffice/documentfiles/${record.id}/downloadFile`;

const DocumentFileShow = (props) => {
    const { permissions } = usePermissions();
    return (
        <Show {...props}>
            <SimpleShowLayout>
                <TextField source="filename" />
                <ReferenceField source="typeId" reference="documenttypes" link={false}>
                    <TextField source="description" />
                </ReferenceField>
                <ReferenceField source="sourceTypeId" reference="sourcetypes" link={false}>
                    <TextField source="description" />
                </ReferenceField>
                <SgxDateField source="creationable.createdOn" showTime/>
                { permissions && permissions.isOn("HABILITAR_DESCARGA_DOCUMENTOS_PDF") && <DownloadButton filename={filenameSupplier} url={urlSupplier}/> }
                <FileRebuildButton/>
            </SimpleShowLayout>
        </Show>
    )
};

export default DocumentFileShow;
