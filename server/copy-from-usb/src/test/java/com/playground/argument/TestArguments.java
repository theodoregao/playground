package com.playground.argument;

import java.util.List;

public class TestArguments {

    static final Argument HELP = new Argument.Builder()
            .setName("help")
            .setDescription("dummy description")
            .setKeywords(List.of("-h", "--help"))
            .setValueCount(0)
            .build();

    static final Argument EXTENSION = new Argument.Builder()
            .setName("extension")
            .setDescription("dummy description")
            .setKeywords(List.of("-e", "--ext"))
            .setValueCount(1)
            .build();

    static final Argument OUT = new Argument.Builder()
            .setName("out")
            .setDescription("dummy description")
            .setKeywords(List.of("-o", "--out"))
            .setValueCount(2)
            .build();
}
