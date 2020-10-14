import * as React from 'react';
import {FunctionComponent} from 'react';
import PropTypes from 'prop-types';
import {Field, Form} from 'react-final-form';
import CardActions from '@material-ui/core/CardActions';
import Button from '@material-ui/core/Button';
import TextField from '@material-ui/core/TextField';
import CircularProgress from '@material-ui/core/CircularProgress';
import {makeStyles, Theme} from '@material-ui/core/styles';
import {useTranslate, useLogin, useNotify, useSafeSetState} from 'ra-core';
import ReCAPTCHA from 'react-google-recaptcha';
import {Loading} from 'react-admin';
import apiRest from '../providers/utils/sgxApiRest';
import {useState, useEffect} from 'react';


interface
Props
{
	redirectTo ? : string;
}

interface
FormData
{
	username: string;
	password: string;
}

const useStyles = makeStyles(
		(theme: Theme) => ({
			form: {
				padding: '0 1em 1em 1em',
			},
			input: {
				marginTop: '1em',
			},
			button: {
				width: '100%',
			},
			icon: {
				marginRight: theme.spacing(1),
			},
		}),
		{name: 'RaLoginForm'}
);

const Input = ({
	               meta: {touched, error}, // eslint-disable-line react/prop-types
	               input: inputProps, // eslint-disable-line react/prop-types
	               ...props
               }) => (
		<TextField
				error={!!(touched && error)}
				helperText={touched && error}
				{...inputProps}
				{...props}
				fullWidth
		/>
);

/**
 * renderiza el ReCaptcha.
 */
const MyReCAPTCHA = props => {
	const [loading, setLoading] = useState(true);
	const [error, setError] = useState(false);
	const [recaptchaEnabled, setRecaptchaEnabled] = useState(false);
	const {onChange} = props;
	const [recaptchaSiteKey, setRecaptchaSiteKey] = useState(undefined);

	useEffect(() => {
		apiRest.getRecaptchaPublicConfig()
				.then(response => {
					const isRecaptchaEnabled = response.enabled;
					setRecaptchaEnabled(isRecaptchaEnabled);
					setRecaptchaSiteKey(response.siteKey);
					if (!isRecaptchaEnabled) {
						onChange('notoken');
					}
				})
				.catch(e => setError(e))
				.finally(() => setLoading(false));

	}, [onChange]);

	if (loading) {
		return <Loading/>;
	}
	if (error) {
		return <p>{JSON.stringify(error)}</p>;
	}
	if (recaptchaEnabled && recaptchaSiteKey) {
		return <ReCAPTCHA sitekey={recaptchaSiteKey} {...props}/>;
	}
	return '';
};

const LoginForm: FunctionComponent<Props> = props => {
	const {redirectTo} = props;
	const [loading, setLoading] = useSafeSetState(false);
	const [raToken, setRaToken] = useSafeSetState(null);
	const login = useLogin();
	const translate = useTranslate();
	const notify = useNotify();
	const classes = useStyles(props);

	const validate = (values: FormData) => {
		const errors = {username: undefined, password: undefined};
		if (!values.username) {
			errors.username = translate('ra.validation.required');
		}
		if (!values.password) {
			errors.password = translate('ra.validation.required');
		}
		return errors;
	};

	const submit = values => {
		setLoading(true);
		//Agrego el token a los datos de login (usr y pass)
		const loginData = {'raToken': raToken, ...values};
		login(loginData, redirectTo)
				.then(() => {
					setLoading(false);
				})
				.catch(error => {
					setLoading(false);
					window.grecaptcha.reset();
					notify(
							typeof error === 'string'
									? error
									: typeof error === 'undefined' || !error.message
									? 'ra.auth.sign_in_error'
									: error.message,
							'warning'
					);
				});
	};

	//Se llama cuando el usr pasó el captcha
	//TODO: También estamos seteando un token falso cuando no activamos recaptcha
	const onRecaptchaChange = (x) => {
		setRaToken(x);
	};

	return (

			<Form
					onSubmit={submit}
					validate={validate}
					render={({handleSubmit}) => (

							<form onSubmit={handleSubmit} noValidate>
								<div className={classes.form}>
									<div className={classes.input}>
										<Field
												autoFocus
												id="username"
												name="username"
												component={Input}
												label={translate('ra.auth.username')}
												disabled={loading}
										/>
									</div>
									<div className={classes.input}>
										<Field
												id="password"
												name="password"
												component={Input}
												label={translate('ra.auth.password')}
												type="password"
												disabled={loading}
												autoComplete="current-password"
										/>
									</div>
								</div>

								<MyReCAPTCHA onChange={onRecaptchaChange}/>

								<CardActions>
									<Button
											variant="contained"
											type="submit"
											color="primary"
											disabled={loading || !raToken}
											className={classes.button}
									>
										{loading && (
												<CircularProgress
														className={classes.icon}
														size={18}
														thickness={2}
												/>
										)}
										{translate('ra.auth.sign_in')}
									</Button>
								</CardActions>

							</form>

					)}

			/>

	);

};

LoginForm.propTypes = {
	redirectTo: PropTypes.string,
};

export default LoginForm;
