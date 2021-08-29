package com.playground.argument;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ArgumentManagerImpl implements ArgumentManager{
    private List<Argument> acceptableArguments;
    private Map<Argument, List<String>> argumentValues = new HashMap<>();

    @Override
    public List<String> getArgumentValue(Argument argument) {
        return argumentValues.get(argument);
    }

    @Override
    public String getArgumentDescription() {
        final StringBuilder stringBuilder = new StringBuilder();
        for (Argument argument: acceptableArguments) {
            stringBuilder.append("\t")
                    .append(argument.getName())
                    .append(":\t\t")
                    .append(argument.getDescription())
                    .append("\n");
        }
        return stringBuilder.toString();
    }

    @Override
    public String toString() {
        final StringBuilder stringBuilder = new StringBuilder();
        for (Argument argument: argumentValues.keySet()) {
            stringBuilder.append(argument.getName())
                    .append(": ")
                    .append(argumentValues.get(argument))
                    .append("\n");
        }
        return stringBuilder.toString();
    }

    ArgumentManagerImpl(List<Argument> acceptableArguments) {
        this.acceptableArguments = acceptableArguments;
    }

    void setArgument(Argument argument, List<String> argumentValues) {
        if (!acceptableArguments.contains(argument)) {
            throw new IllegalArgumentException("Argument name " + argument.getName() + " not acceptable");
        }
        this.argumentValues.put(argument, argumentValues);
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

    public static ArgumentManager createArgumentManager(List<Argument> acceptableArguments, String[] args) {
        final ArgumentManagerImpl argumentManager = new ArgumentManagerImpl(acceptableArguments);
        final List<String> argsList = Arrays.asList(args);
        for (int i = 0; i < argsList.size(); i++) {
            final String argumentKeyword = args[i];
            if (!argumentManager.isAcceptArgument(argumentKeyword)) {
                throw new IllegalArgumentException("Argument " + argumentKeyword + " is not accepted by the application");
            }

            final Argument argument = argumentManager.getArgument(argumentKeyword);
            if (argumentManager.getArgumentValue(argument) != null) {
                throw new IllegalArgumentException("Argument " + argument.getName() + " already set");
            }

            final int argumentCount = argument.getValueCount();
            if (i + argumentCount >= args.length) {
                throw new IllegalArgumentException("Argument " + argument.getName() + " is missing value");
            }

            argumentManager.setArgument(argument, argsList.subList(i + 1, i + 1 + argumentCount));
            i += argumentCount;
        }
        return argumentManager;
    }
}

