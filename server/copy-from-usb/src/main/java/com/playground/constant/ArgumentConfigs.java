package com.playground.constant;

import com.playground.argument.Argument;

import java.util.List;

public class ArgumentConfigs {

    public static final Argument EXTENSIONS = new Argument.Builder()
            .setName("extensions")
            .setValueCount(1)
            .setDescription("The file types want to copy.")
            .setKeywords(List.of("-e", "--ext"))
            .build();

    public static final Argument OUT = new Argument.Builder()
            .setName("out")
            .setValueCount(1)
            .setDescription("The name of output folder")
            .setKeywords(List.of("-o", "--out"))
            .build();

    public static final Argument HELP = new Argument.Builder()
            .setName("help")
            .setValueCount(0)
            .setDescription("The description of instructions")
            .setKeywords(List.of("-h", "--help"))
            .build();

    public static final Argument METADATA = new Argument.Builder()
            .setName("metadata")
            .setValueCount(1)
            .setDescription("Specify the metadata file you want to persist")
            .setKeywords(List.of("-m", "--metadata"))
            .build();
}
