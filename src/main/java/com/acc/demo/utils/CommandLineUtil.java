package com.acc.demo.utils;

import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;

public class CommandLineUtil {

    public static void addOptions(Options options) {
        Option add = Option.builder("a")
                .longOpt("add-user")
                .argName("id>:<firstName>:<surName")
                .numberOfArgs(3)
                .valueSeparator(':')
                .desc("Add user").build();

        Option edit = Option.builder("e")
                .longOpt("edit-user")
                .argName("id>:<firstName>:<surName")
                .numberOfArgs(3)
                .valueSeparator(':')
                .desc("Edit user").build();

        Option delete = Option.builder("d")
                .longOpt("delete-user")
                .argName("id")
                .numberOfArgs(1)
                .desc("Delete user").build();

        Option count = Option.builder("c")
                .longOpt("count-users")
                .desc("Count number of users").build();

        Option list = Option.builder("l")
                .longOpt("list-user")
                .desc("List all users").build();

        Option addFromXml = Option.builder("xa")
                .longOpt("xml-add-user")
                .argName("xml")
                .numberOfArgs(1)
                .desc("Add user from XML").build();

        Option help = Option.builder("h")
                .longOpt("help")
                .desc("Help").build();

        Option exit = Option.builder("exit")
                .longOpt("exit")
                .desc("Exit from application").build();

        options.addOption(add);
        options.addOption(addFromXml);
        options.addOption(edit);
        options.addOption(delete);
        options.addOption(count);
        options.addOption(list);
        options.addOption(help);
        options.addOption(exit);
    }
}
