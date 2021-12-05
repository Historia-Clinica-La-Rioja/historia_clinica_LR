
const safeParseJson = (text: string): any => {
    try {
        return text ? JSON.parse(text) : undefined;
    } catch(e) {
        console.warn('Please check JSON response', e);
    }
    return undefined;
};

export { safeParseJson };
