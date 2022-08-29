import React from 'react';

import Alert from '@material-ui/lab/Alert';

const HttpErrorAlert = ({
    error, //HttpError
}) => <Alert severity="error">{error.message}</Alert>;

export default HttpErrorAlert;
