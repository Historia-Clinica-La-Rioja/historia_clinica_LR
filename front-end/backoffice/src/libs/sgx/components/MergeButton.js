import React, { useState, Fragment} from 'react';
import { useDispatch } from 'react-redux';

import {
    useNotify,
    fetchStart,
    fetchEnd,
    useRecordContext,
    useRedirect,
    Confirm,
} from 'react-admin';

import Button from '@material-ui/core/Button';
import { sgxFetchApiWithToken } from '../api/fetch';

const MergeButton = ({ baseMedicalCoverage,
                         ...props
                     }) => {
    const [loading, setLoading] = useState(false);
    const [open, setOpen] = useState(false);
    const notify = useNotify();
    const redirect = useRedirect();
    const record = useRecordContext(props);
    const dispatch = useDispatch();
    const handleClick = () => setOpen(true);
    const handleDialogClose = () => setOpen(false);
    const handleConfirm = () => {
        merge();
        setOpen(false);
    };

    const merge = () => {
        setLoading(true);
        dispatch(fetchStart()); // start the global loading indicator

        sgxFetchApiWithToken(`backoffice/medicalcoveragesmerge/${record.id}/baseMedicalCoverage/${baseMedicalCoverage}`,{ method: 'PUT' })
            .then(() => {
                notify('mergeMedicalCoverage.merge_success', { type: 'info' })
                redirect('/medicalcoverages');
            })
            .catch(() => {
                notify('mergeMedicalCoverage.cant_merge', 'warning')
            })
            .finally(() => {
                setLoading(false);
                dispatch(fetchEnd()); // stop the global loading indicator
            });
    };

    return (
        <Fragment>
        <Button onClick={handleClick} color="primary" size="small" disabled={loading}>
            Unificar
        </Button>
            <Confirm
                isOpen={open}
                loading={loading}
                title='mergeMedicalCoverage.dialog_title'
                content={`¿Está seguro que desea unificar la cobertura médica ${record.name}?
                Si confirma, la misma será eliminada`}
                onConfirm={handleConfirm}
                onClose={handleDialogClose}/>
        </Fragment>

    );
};

export default MergeButton;