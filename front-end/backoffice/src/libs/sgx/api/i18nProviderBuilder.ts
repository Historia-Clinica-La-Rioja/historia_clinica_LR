import polyglotI18nProvider from 'ra-i18n-polyglot';

import raSpanishMessages from '../lang/es';
import raEnglishMessages from 'ra-language-english';


import { SupportedLanguages } from './model';


const i18nProvider = (messages: SupportedLanguages) => {

    const allMessages: SupportedLanguages = {
        ...messages,
        en: {...raEnglishMessages, ...messages['en']},
        es: {...raSpanishMessages, ...messages['es']},
    };

    return polyglotI18nProvider(
        (locale: string) => allMessages[locale] ? allMessages[locale] : allMessages.es,
        'es'
    );
}

export default i18nProvider;