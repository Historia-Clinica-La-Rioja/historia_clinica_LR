// import { 
//     // detects the user’s browser locale
//     resolveBrowserLocale,
// } from 'react-admin';
import polyglotI18nProvider from 'ra-i18n-polyglot';

// translations
import englishMessages from './i18n/en';
import spanishMessages from './i18n/es';

const messages = {
    en: englishMessages,
    es: spanishMessages,
}

const i18nProvider = polyglotI18nProvider(locale => 
    messages[locale] ? messages[locale] : messages.es,
    'es'
);

export default i18nProvider;
