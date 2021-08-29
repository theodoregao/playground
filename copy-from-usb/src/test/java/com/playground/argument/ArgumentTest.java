package com.playground.argument;

import static com.playground.argument.TestArguments.*;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ArgumentTest {

    @Test
    void testEqualsMethodWithNullInputReturnsFalse() {
        assertFalse(HELP.equals(null));
    }

}