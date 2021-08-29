package com.playground.argument;

import java.util.List;

public interface ArgumentManager {
    List<String> getArgumentValue(Argument argument);
    String getArgumentDescription();
}
