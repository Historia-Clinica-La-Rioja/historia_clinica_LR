import React, { useState } from 'react';
import { useDispatch } from 'react-redux';

import { 
    useNotify, 
    fetchStart, 
    fetchEnd, 
    useRecordContext,
} from 'react-admin';

import Button from '@material-ui/core/Button';
import { sgxDownload } from '../api/fetch';

const fromSupplier = (record, ...suppliers) => suppliers.map(
    s => typeof s === 'function'? s(record) : s
);

const DownloadButton = ({
        url,
        filename,
        ...props
    }) => {
        const [urlValue, filenameValue] = fromSupplier(useRecordContext(props), url, filename);
        const [loading, setLoading] = useState(false);
        const notify = useNotify();
        const dispatch = useDispatch();

        const handleClick = () => {
            setLoading(true);
            dispatch(fetchStart()); // start the global loading indicator 

            sgxDownload(urlValue, filenameValue)
                .catch(() => {
                    notify('files.cant_download', 'warning')
                })
                .finally(() => {
                    setLoading(false);
                    dispatch(fetchEnd()); // stop the global loading indicator
                });
        };

        return (
            <Button onClick={handleClick} color="primary" size="small" disabled={!url || loading}>
                Descargar
            </Button>
        );
    };

export default DownloadButton;