package com.playground.constants;

import com.playground.argument.Argument;

import java.util.List;

public class ArgumentConfigs {

    public static final Argument HELP =
            new Argument.Builder<>()
                    .setName("help")
                    .setDescription("This app can help you to detect USB connection and auto copy files for you")
                    .setKeywords(List.of("-h", "--help"))
                    .setValueCount(0)
                    .build();

    public static final Argument EXTENSIONS =
            new Argument.Builder<>()
                    .setName("extensions")
                    .setDescription("Use -e/--ext to set whatever file types you would like to copy")
                    .setKeywords(List.of("-e", "--ext"))
                    .setValueCount(1)
                    .build();

    public static final Argument OUTPUT_FOLDER =
            new Argument.Builder<>()
                    .setName("output folder")
                    .setDescription("Use -o/--out to set destination folder for your copied files")
                    .setKeywords(List.of("-o", "--out"))
                    .setValueCount(1)
                    .build();

}
