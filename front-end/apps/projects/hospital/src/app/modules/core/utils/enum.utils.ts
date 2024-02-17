export const isValueInStringEnum = <E extends string>(strEnum: Record<string, E>) : (value: string) => boolean => {
    const enumValues =  Object.values(strEnum) as string[];
    return (value: string): value is E => enumValues.includes(value)
}