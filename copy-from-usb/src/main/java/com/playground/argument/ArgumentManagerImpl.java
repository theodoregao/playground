package com.playground.argument;

import com.playground.constants.ArgumentConfigs;

import java.util.*;

public class ArgumentManagerImpl implements ArgumentManager {
    private List<Argument> acceptableArguments;
    private Map<Argument, Object> arguments = new HashMap<>();

    @Override
    public <T> T getArgumentValue(Argument argument) {
        return (T) arguments.get(argument);
    }

    @Override
    public void print() {
        for (Argument argument: acceptableArguments) {
            System.out.println("\t" + argument.getName() + ":\t\t" + argument.getDescription());
        }
    }

    @Override
    public String toString() {
        final StringBuilder stringBuilder = new StringBuilder();
        for (Argument argument: arguments.keySet()) {
            stringBuilder.append(argument.getName() + ": " + arguments.get(argument) + "\n");
        }
        return stringBuilder.toString();
    }

    private ArgumentManagerImpl(List<Argument> acceptableArguments) {
        this.acceptableArguments = acceptableArguments;
    }

    private <T> void setArgument(Argument argument, T value) {
        if (!acceptableArguments.contains(argument)) {
            throw new IllegalArgumentException("Argument name " + argument.getName() + " not exists");
        }
        arguments.put(argument, value);
    }

    private boolean isAcceptArgument(String argumentName) {
        return getArgument(argumentName) != null;
    }

    private Argument getArgument(String keyword) {
        for (Argument argument: acceptableArguments) {
            if (argument.getKeywords().contains(keyword)) {
                return argument;
            }
        }
        return null;
    }

    public static ArgumentManager createArgumentManager(String[] args) {
        final ArgumentManagerImpl argumentManager = new ArgumentManagerImpl(
                List.of(ArgumentConfigs.HELP,
                        ArgumentConfigs.EXTENSIONS,
                        ArgumentConfigs.OUTPUT_FOLDER));
        for (int i = 0; i < args.length; i++) {
            final String argumentName = args[i];
            if (argumentManager.isAcceptArgument(argumentName)) {
                final Argument argument = argumentManager.getArgument(argumentName);
                argumentManager.setArgument(argument, true);
                for (int j = 0; j < argument.getValueCount(); j++) {
                    if (i >= args.length - 1) {
                        throw new IllegalArgumentException("Argument " + argumentName + " is missing value");
                    }
                    argumentManager.setArgument(argument, args[++i]);
                }
            } else {
                throw new IllegalArgumentException("Argument " + argumentName + " is not accepted by the application");
            }
        }
        System.out.println(argumentManager);
        return argumentManager;
    }

}
