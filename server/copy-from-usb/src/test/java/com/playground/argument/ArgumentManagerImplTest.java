package com.playground.argument;

import static com.playground.argument.TestArguments.*;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ArgumentManagerImplTest {

    @Test
    void testValidArguments() {
        ArgumentManager argumentManager = ArgumentManagerImpl.createArgumentManager(
                List.of(HELP, EXTENSION, OUT),
                new String[] {"-h", "-e", "txt,png", "-o", "path1", "path2"}
        );
        assertNotNull(argumentManager.getArgumentValue(HELP));
        assertEquals(List.of("txt,png"), argumentManager.getArgumentValue(EXTENSION));
        assertEquals(List.of("path1", "path2"), argumentManager.getArgumentValue(OUT));
        assertEquals("\thelp:\t\tdummy description\n\textension:\t\tdummy description\n\tout:\t\tdummy description\n",
                argumentManager.getArgumentDescription());
        assertEquals("help: []\nextension: [txt,png]\nout: [path1, path2]\n", argumentManager.toString());

        argumentManager = ArgumentManagerImpl.createArgumentManager(
                List.of(HELP, EXTENSION, OUT),
                new String[] {"--help", "--ext", "txt,png", "--out", "path1", "path2"}
        );
        assertNotNull(argumentManager.getArgumentValue(HELP));
        assertEquals(List.of("txt,png"), argumentManager.getArgumentValue(EXTENSION));
        assertEquals(List.of("path1", "path2"), argumentManager.getArgumentValue(OUT));
        assertEquals("\thelp:\t\tdummy description\n\textension:\t\tdummy description\n\tout:\t\tdummy description\n",
                argumentManager.getArgumentDescription());
        assertEquals("help: []\nextension: [txt,png]\nout: [path1, path2]\n", argumentManager.toString());
    }

    @Test
    void testValidArgumentAndLookForNullArgumentReturnsNull() {
        final ArgumentManager argumentManager = ArgumentManagerImpl.createArgumentManager(
                List.of(HELP, EXTENSION, OUT),
                new String[] {"-h", "-e", "txt,png", "-o", "path1", "path2"}
        );
        assertNull(argumentManager.getArgumentValue(null));
    }

    @Test
    void testInvalidArgumentsWithExtraValueThenThrowsException() {
        Exception e = assertThrows(IllegalArgumentException.class, () -> {
            ArgumentManagerImpl.createArgumentManager(
                    List.of(HELP, EXTENSION),
                    new String[] { "-h", "help" }
            );
        });
        assertEquals("Argument help is not accepted by the application", e.getMessage());
    }

    @Test
    void testValidArgumentsWithoutSpecificArgumentThenThrowsException() {
        Exception e = assertThrows(IllegalArgumentException.class, () -> {
            ArgumentManagerImpl.createArgumentManager(
                    List.of(EXTENSION),
                    new String[] { "-e" }
            );
        });
        assertEquals("Argument extension is missing value", e.getMessage());

        e = assertThrows(IllegalArgumentException.class, () -> {
            ArgumentManagerImpl.createArgumentManager(
                    List.of(OUT),
                    new String[] { "-o", "copy/build" }
            );
        });
        assertEquals("Argument out is missing value", e.getMessage());
    }

    @Test
    void testSetInvalidArgumentThenExceptionThrowsException() {
        Exception e = assertThrows(IllegalArgumentException.class, () -> {
            ArgumentManagerImpl.createArgumentManager(
                    List.of(HELP, EXTENSION),
                    new String[] { "-e", "png,txt", "-x" }
            );
        });
        assertEquals("Argument -x is not accepted by the application", e.getMessage());
    }

    @Test
    void testValidArgumentNotReturnUnsetValue() {
        ArgumentManager argumentManager = ArgumentManagerImpl.createArgumentManager(
                List.of(HELP, EXTENSION, OUT),
                new String[] { "-h", "-e", "txt,png" }
        );
        assertNull(argumentManager.getArgumentValue(OUT));
    }

    @Test
    void testDuplicateArgumentThrowsException() {
        Exception e = assertThrows(IllegalArgumentException.class, () -> {
            ArgumentManagerImpl.createArgumentManager(
                    List.of(HELP),
                    new String[] {"-h", "--help"}
            );
        });
        assertEquals("Argument help already set", e.getMessage());

        e = assertThrows(IllegalArgumentException.class, () -> {
            ArgumentManagerImpl.createArgumentManager(
                    List.of(HELP, EXTENSION),
                    new String[] {"-h", "-e", "text", "--ext"}
            );
        });
        assertEquals("Argument extension already set", e.getMessage());
    }

    @Test
    void testSetArgumentWithUnacceptableArgumentThrowsException() {
        final ArgumentManagerImpl argumentManager = new ArgumentManagerImpl(List.of(HELP, EXTENSION));
        final Exception e = assertThrows(IllegalArgumentException.class, () -> {
            argumentManager.setArgument(OUT, List.of("path1", "path2"));
        });
        assertEquals("Argument name out not acceptable", e.getMessage());
    }
}
