package com.sconde.kata.domain.model;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@Getter
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public enum Score {

    LOVE("0"), FIFTEEN("15"), THIRTY("30"), FORTY("40"), ADVANTAGE("AD");

    String value;

    public Score incrementScore() {
        return switch (this) {
            case LOVE -> FIFTEEN;
            case FIFTEEN -> THIRTY;
            case THIRTY -> FORTY;
            default -> this;  // FORTY or ADVANTAGE don't increment here
        };
    }
}
