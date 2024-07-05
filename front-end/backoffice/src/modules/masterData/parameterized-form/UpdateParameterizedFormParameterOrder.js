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
import ArrowUpwardIcon from '@material-ui/icons/ArrowUpward';
import ArrowDownwardIcon from '@material-ui/icons/ArrowDownward';
import { sgxFetchApiWithToken } from '../../../libs/sgx/api/fetch';

const UpdateParameterizedFormParametersOrder = (props) => {
    const record = useRecordContext(props);
    const dispatch = useDispatch();
    const refresh = useRefresh();
    const notify = useNotify();
    const [loading, setLoading] = useState(false);
    const handleClick = (direction) => () => {
        setLoading(true);
        dispatch(fetchStart());
        sgxFetchApiWithToken(`backoffice/parameterizedformparameter/${record.id}/change-order?direction=${direction}`, { method: 'PUT' })
            .then((response) => {
                refresh();
            })
            .catch((e) => {
                if (!e.body.text)
                    notify('Error al cambiar el orden', { type: 'warning' })
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
        <Button onClick={handleClick('down')} color="primary" size="small" disabled={loading}>
            <ArrowUpwardIcon />
        </Button>
        <Button onClick={handleClick('up')} color="primary" size="small" disabled={loading}>
            <ArrowDownwardIcon />
        </Button>
        </>
    );
}

export default UpdateParameterizedFormParametersOrder;
