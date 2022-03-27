export const PATTERN_NUMBER_WITH_DECIMALS = /^\d+(\.\d+)?$/;
export const PATTERN_INTEGER_NUMBER = /^\d+$/;
export const PATTERN_NUMBER_WITH_MAX_2_DECIMAL_DIGITS = /^\d+(\.\d{1,2})?$/;

export const isNumberOrDot = (value: string): boolean => {
    const pattern = /^[\d\.]{1}$/;
    return pattern.test(value);
}

export const hasMaxTwoDecimalDigits = (numberValue: string): boolean => {
    return PATTERN_NUMBER_WITH_MAX_2_DECIMAL_DIGITS.test(numberValue);
}