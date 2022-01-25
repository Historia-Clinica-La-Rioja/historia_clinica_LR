import React from 'react';

import { useTranslate } from 'ra-core';

import { makeStyles } from '@material-ui/core/styles';
import Card from '@material-ui/core/Card';
import CardActions from '@material-ui/core/CardActions';
import CardContent from '@material-ui/core/CardContent';
import Button from '@material-ui/core/Button';
import Typography from '@material-ui/core/Typography';


const useStyles = makeStyles({
    root: {
        minWidth: 275,
    },
    button: {
        width: '100%',
    },
    title: {
        fontSize: 14,
    },
});

interface LoginRedirectProps {
    loginUrl: string;
}

const LoginRedirect = ({ loginUrl = '' }: LoginRedirectProps) => {
    const classes = useStyles();
    const translate = useTranslate();
    return (
        <Card className={classes.root} variant="outlined">
            <CardContent>
                <Typography className={classes.title} color="textSecondary" gutterBottom>
                    {translate('bo.login.redirect.message')}
                </Typography>
            </CardContent>
            <CardActions>
                <a href={loginUrl} className={classes.button}>
                    <Button
                        variant="contained"
                        type="submit"
                        color="primary"
                        className={classes.button}
                    >
                        {translate('ra.auth.sign_in')}
                    </Button>
                </a>
            </CardActions>
        </Card>
    );
}

export default LoginRedirect;