package net.pladema.establishment.domain.bed.exceptions;

import lombok.Getter;

@Getter
public enum BedRelocationEnumException {
    OCUPPIED_BED,
    ENTRY_DATE_AFTER_RELOCATION_DATE,
}