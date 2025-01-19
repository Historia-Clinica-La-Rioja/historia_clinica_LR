import React, {useState} from 'react';
import { useDispatch } from "react-redux";
import {
    useRecordContext,
    Button,
    fetchStart,
    useNotify,
    useRefresh,
    fetchEnd
} from 'react-admin';
import { sgxFetchApiWithToken } from '../../../libs/sgx/api/fetch';
import { STATUS_ACTIONS } from './ProcedureTemplateStatus';

const UpdateStatusButton = (props) => {
    const record = useRecordContext(props);
    const dispatch = useDispatch();
    const refresh = useRefresh();
    const notify = useNotify();
    const [loading, setLoading] = useState(false);
    const handleClick = (e) => {
        e.stopPropagation();
        setLoading(true);
        dispatch(fetchStart());
        sgxFetchApiWithToken(`backoffice/proceduretemplates/${record.id}/update-status`, { method: 'PUT' })
            .then((response) => {
                refresh();
            })
            .catch((e) => {
                if (!e.body.text)
                    notify('Error al cambiar el estado', { type: 'warning' })
                else
                    notify(e.body.text, { type: 'warning' })
            })
            .finally(() => {
                setLoading(false);
                dispatch(fetchEnd());
            });
    };
    return (
        <>
        <Button
            onClick={handleClick}
            color="primary"
            size="small"
            disabled={loading}
            label={STATUS_ACTIONS[record.statusId].nextStateActionName}
        />
        </>
    );
}

export default UpdateStatusButton;