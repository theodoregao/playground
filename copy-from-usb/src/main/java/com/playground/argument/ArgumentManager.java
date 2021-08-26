package com.playground.argument;

public interface ArgumentManager {
    <T> T getArgumentValue(Argument argument);
    void print();
}
