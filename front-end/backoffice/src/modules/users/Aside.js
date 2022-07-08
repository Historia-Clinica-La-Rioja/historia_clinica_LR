import React from 'react';
import {
    useMutation,
    useTranslate,
    useGetList,
    useNotify,
    useRefresh,
} from 'react-admin';

import PropTypes from 'prop-types';
import {
    Tooltip,
    Typography,
    Card,
    CardHeader,
    Avatar,
    IconButton,
} from '@material-ui/core';

import LaunchIcon from '@material-ui/icons/Launch';
import BlockIcon from '@material-ui/icons/Block';
import { makeStyles } from '@material-ui/core/styles';

import { parseISO, isFuture } from 'date-fns';

import LockIcon from '@material-ui/icons/Lock';
import twoFactorAuthentication from '../twofactorauthentication-reset';
import { openPasswordReset } from '../../providers/utils/webappLink';
import { sgxFetchApiWithToken } from "../../libs/sgx/api/fetch";

const useAsideStyles = makeStyles(theme => ({
    root: {
        width: 400,
        [theme.breakpoints.down('md')]: {
            display: 'none',
        },
    },
}));

const isNotExpired = ({expiryDate}) => isFuture(parseISO(expiryDate));

const Aside = ({ record, basePath }) => {
    const classes = useAsideStyles();
    return (
        <div className={classes.root}>
            {record && <PasswordResetList record={record} basePath={basePath} />}
            {record?.twoFactorAuthenticationEnabled && <TwoFactorAuthenticationList record={record} /> }
        </div>
    );
};

Aside.propTypes = {
    record: PropTypes.any,
    basePath: PropTypes.string,
};

const PasswordResetList = ({ record }) => {
    const { data: resetCodes, ids: resetCodesId } = useGetList(
        'password-reset',
        { page: 1, perPage: 100 },
        { field: 'expiryDate', order: 'DESC' },
        { userId: record && record.id, enable: true }
    );

    const [createPasswordReset, { loading }] = useMutation({
        type: 'create',
        resource: 'password-reset',
        payload: { data: { userId: record && record.id } }
    }, {
        onSuccess: ({ data }) => {
            openPasswordReset(data.token);
        }
    });

    const validResetCode = (resetCodesId || [])
        .map(resetCodeId => resetCodes[resetCodeId])
        .find(isNotExpired);

    const actionClick = (!validResetCode) ? createPasswordReset : () => openPasswordReset(validResetCode.token);

    return (
        <>
            <ResetPasswordCard actionClick={actionClick} loading={loading}></ResetPasswordCard>
        </>
    );
};

const useEventStyles = makeStyles({
    card: {
        margin: '0 0 1em 1em',
    },
    cardHeader: {
        alignItems: 'flex-start',
    },
    clamp: {
        display: '-webkit-box',
        '-webkit-line-clamp': 3,
        '-webkit-box-orient': 'vertical',
        overflow: 'hidden',
    },
});

const ResetPasswordCard = ({ actionClick, loading }) => {
    const translate = useTranslate();
    const classes = useEventStyles();
    return (
        <Card className={classes.card}>
            <CardHeader
                className={classes.cardHeader}
                avatar={
                    <Avatar
                        className={classes.avatar}
                    >
                        <LockIcon />
                    </Avatar>
                }
                action={<OpenPageButton actionClick={actionClick} disabled={loading}/>}
                title={translate('resources.users.fieldGroups.passwordResets')}
                subheader={
                    <>
                        <Typography variant="body2">Puede definir una nueva clave de acceso</Typography>
                    </>
                }
            />
        </Card>
    );
};

const OpenPageButton = ({ actionClick }) => {
    const translate = useTranslate();
    return (
        <Tooltip title={translate('resources.users.action.reset')}>
            <IconButton
                aria-label={translate('resources.users.action.reset')}
                onClick={actionClick}
            >
                <LaunchIcon />
            </IconButton>
        </Tooltip>
    );
};

const TwoFactorAuthenticationList = ({ record }) => {
    const notify = useNotify();
    const refresh = useRefresh();
    const handleResponse = () => {
        notify('resources.users.action.twoFactorAuthenticationResetSuccess');
        refresh();
    }
    const disableTwoFactorAuthenticationAction = () => {
        sgxFetchApiWithToken(
            `backoffice/users/${record.id}/reset-2fa`,
            {method: 'PUT'}
        ).then(() => handleResponse())
    };
    return (
        <>
            <ResetTwoFactorAuthenticationCard actionClick={disableTwoFactorAuthenticationAction} />
        </>
    );
};

const ResetTwoFactorAuthenticationCard = ({ actionClick }) => {
    const translate = useTranslate();
    const classes = useEventStyles();
    return (
        <Card className={classes.card}>
            <CardHeader
                className={classes.cardHeader}
                avatar={
                    <Avatar
                        className={classes.avatar}
                    >
                        <twoFactorAuthentication.icon />
                    </Avatar>
                }
                action={<ResetTwoFactorAuthenticationButton actionClick={actionClick}/>}
                title={translate('resources.users.fieldGroups.twoFactorAuthenticationReset')}
                subheader={
                    <>
                        <Typography variant="body2">Puede deshabilitar el doble factor de autenticación para el usuario</Typography>
                    </>
                }
            />
        </Card>
    );
};

const ResetTwoFactorAuthenticationButton = ({ actionClick }) => {
    const translate = useTranslate();
    return (
        <IconButton
            aria-label={translate('resources.users.action.reset')}
            onClick={actionClick}
        >
            <BlockIcon />
        </IconButton>
    );
};


export default Aside;
