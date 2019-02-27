package com.acc.demo;

import com.acc.demo.model.User;
import com.acc.demo.service.UserService;
import com.acc.demo.utils.CommandLineUtil;
import com.acc.demo.utils.UserServiceException;
import org.apache.commons.cli.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.Banner;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.util.StringUtils;

import javax.xml.bind.DataBindingException;
import javax.xml.bind.JAXB;
import javax.xml.bind.JAXBException;
import javax.xml.bind.UnmarshalException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.util.List;

@SpringBootApplication
public class UserServiceApplication implements CommandLineRunner {

    @Autowired
    private UserService userService;

    private static Options options = new Options();

    public static void main(String[] args) throws IOException {
        CommandLineUtil.addOptions(options);
        new SpringApplicationBuilder(UserServiceApplication.class)
                .bannerMode(Banner.Mode.OFF)
                .run(args);
    }

    /**
     *  If application started in CLI mode then will read user input from console and print results.
     */
    @Override
    public void run(String[] args) throws IOException {
        if (args.length > 0 && "start-cli".equalsIgnoreCase(args[0])) {
            System.out.print("\n\nEnter command => ");
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
            String line = reader.readLine();
            while (!line.equals("exit")) {
                executeCommand(line.split("\\s+"));
                System.out.print("Enter command => ");
                line = reader.readLine();
            }
        }
    }

    // check if options is valid then execute command otherwise show message for wrong command.
    public void executeCommand(String[] args) {
        if (args == null || args.length == 0 || !"user".equalsIgnoreCase(args[0])) {
            System.out.println("Wrong command. Run command 'user -h' for help.");
            return;
        }

        CommandLineParser parser = new DefaultParser();

        try {
            CommandLine commandLine = parser.parse(options, args);

            if (commandLine.hasOption("xa")) {
                User user = JAXB.unmarshal(new StringReader(commandLine.getOptionValue("xa")), User.class);
                userService.addUser(user);
                System.out.println("New user with id : " + user.getId() + " added.");
            } else if (commandLine.hasOption("a")) {
                User user = createUser(commandLine, "a");
                userService.addUser(user);
                System.out.println("New user with id : " + user.getId() + " added.");
            } else if (commandLine.hasOption("e")) {
                User user = createUser(commandLine, "e");
                userService.updateUser(user);
                System.out.println("Details for user with id : " + user.getId() + " updated.");
            } else if (commandLine.hasOption("d")) {
                deleteUser(commandLine);
            } else if (commandLine.hasOption("c")) {
                System.out.println("Total " + userService.countUsers() + " user found.");
            } else if (commandLine.hasOption("l")) {
                listUsers();
            } else {
                showHelpOption();
            }

        } catch (ParseException e) {
            System.out.println(e.getMessage() + ". Run command 'User -h' for help.");
        } catch (UserServiceException e) {
            System.out.println(e.getMessage());
        } catch (DataBindingException e) {
            System.out.println(e.getMessage());
        }
    }


    private User createUser(CommandLine commandLine, String option) {
        String[] values = commandLine.getOptionValues(option);
        return new User(values[0], values[1], values[2]);
    }

    private void deleteUser(CommandLine commandLine) {
        String id = commandLine.getOptionValue("d");
        if(StringUtils.isEmpty(id)) {
            System.out.println("Id can nt be null or empty.");
        } else {
            userService.deleteUser(id);
            System.out.println("Details for user with id : " + id + " deleted.");
        }
    }

    private void listUsers() {
        List<User> users = userService.findAllUser();
        if (users.isEmpty()) {
            System.out.println("No user found.");
        }

        for (User user : users) {
            System.out.println(user.toString());
        }
    }

    private void showHelpOption() {
        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp("user", options);
    }

}
