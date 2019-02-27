package com.acc.demo;

import com.acc.demo.model.User;
import com.acc.demo.repository.UserRepository;
import com.acc.demo.utils.CommandLineUtil;
import org.apache.commons.cli.Options;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.util.ReflectionTestUtils;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static junit.framework.Assert.assertNotNull;
import static junit.framework.TestCase.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UserServiceApplicationTests {

    @Autowired
    private UserServiceApplication application;

    @Autowired
    private UserRepository userRepository;

    private PrintStream oldStream;
    private ByteArrayOutputStream baos;


    @Before
    public void setUp() {
        Options options = new Options();
        CommandLineUtil.addOptions(options);
        ReflectionTestUtils.setField(application, "options", options);

        // Keep current System.out
        oldStream = System.out;

        // Create a ByteArrayOutputStream so that we can get the output
        // from the call to print
        baos = new ByteArrayOutputStream();
        // Change System.out to point out to our stream
        System.setOut(new PrintStream(baos));

        // clear database
        userRepository.deleteAll();
    }

    @After
    public void tearDown() {
        // Reset the System.out
        System.setOut(oldStream);
    }

    @Test
    public void testListUsersWithNoUserInDB() {
        application.executeCommand("user -l".split("\\s+"));
        String output = new String(baos.toByteArray());

        assertEquals("No user found.", output.trim());
    }

    @Test
    public void testListUsers() {
        // add users
        application.executeCommand("user --add-user 1:John:Roche".split("\\s+"));
        application.executeCommand("user --add-user 2:PP:KK".split("\\s+"));

        application.executeCommand("user -l".split("\\s+"));
        String output = new String(baos.toByteArray());

        assertTrue(output.contains("User{id='1', firstName='John', surName='Roche'}"));
        assertTrue(output.contains("User{id='2', firstName='PP', surName='KK'}"));
    }

    @Test
    public void testAddUserShortOption() {
        application.executeCommand("user -a 1:John:Roche".split("\\s+"));
        verifyNewUser();
    }

    @Test
    public void testAddUserLongOption() {
        application.executeCommand("user --add-user 1:John:Roche".split("\\s+"));
        verifyNewUser();
    }

    @Test
    public void testAddUserAlreadyExist() {
        // add user with id 1
        application.executeCommand("user --add-user 1:John:Roche".split("\\s+"));
        verifyNewUser();

        // add user again
        application.executeCommand("user --add-user 1:John:Roche".split("\\s+"));

        String output = new String(baos.toByteArray());
        assertTrue(output.contains("User already exists"));
    }

    @Test
    public void testAddUserFromXML() {
        // add user with id 1
        application.executeCommand("user -xa <user><id>1</id><firstName>John</firstName><surName>Roche</surName></user>".split("\\s+"));
        verifyNewUser();
    }

    @Test
    public void testAddUserFromInvalidXML() {
        // add user with id 1
        application.executeCommand("user -xa <user><id1>1</id><firstName>John</firstName><surName>Roche</surName></user>".split("\\s+"));
        String output = new String(baos.toByteArray());
        assertTrue(output.contains("The element type \"id1\" must be terminated by the matching end-tag \"</id1>\""));
    }

    @Test
    public void testEditUser() {
        // add user with id 1
        application.executeCommand("user --xml-add-user <user><id>1</id><firstName>John</firstName><surName>Roche</surName></user>".split("\\s+"));

        String output = new String(baos.toByteArray());
        assertEquals("New user with id : 1 added.", output.trim());

        // edit user
        application.executeCommand("user -e 1:testUpdated:testUpdated1".split("\\s+"));

        User user = userRepository.findByIdIgnoreCase("1");
        assertNotNull(user);
        assertEquals("testUpdated", user.getFirstName());
        assertEquals("testUpdated1", user.getSurName());
    }

    @Test
    public void testDeleteUser() {
        // add user with id 1
        application.executeCommand("user --add-user 1:John:Roche".split("\\s+"));
        verifyNewUser();

        // delete user
        application.executeCommand("user -d 1".split("\\s+"));

        User user = userRepository.findByIdIgnoreCase("1");
        assertNull(user);
    }

    @Test
    public void testCountUsers() {
        // add user with id 1
        application.executeCommand("user --add-user 1:John:Roche".split("\\s+"));
        application.executeCommand("user --add-user 2:PP:KK".split("\\s+"));

        // count user
        application.executeCommand("user -c".split("\\s+"));

        String output = new String(baos.toByteArray());
        assertTrue(output.contains("Total 2 user found."));
    }

    @Test
    public void testCountNoUserInDB() {
        // count user
        application.executeCommand("user -c".split("\\s+"));

        String output = new String(baos.toByteArray());
        assertTrue(output.contains("Total 0 user found."));
    }

    @Test
    public void testWrongOption() {
        application.executeCommand("user -t".split("\\s+"));
        String output = new String(baos.toByteArray());

        assertEquals("Unrecognized option: -t. Run command 'User -h' for help.", output.trim());
    }

    @Test
    public void testWrongCommand() {
        application.executeCommand("-c".split("\\s+"));
        String output = new String(baos.toByteArray());

        assertEquals("Wrong command. Run command 'user -h' for help.", output.trim());
    }

    @Test
    public void testNoCommand() {
        application.executeCommand("".split("\\s+"));
        String output = new String(baos.toByteArray());

        assertEquals("Wrong command. Run command 'user -h' for help.", output.trim());
    }

    private void verifyNewUser() {
        String output = new String(baos.toByteArray());
        assertEquals("New user with id : 1 added.", output.trim());
        User user = userRepository.findByIdIgnoreCase("1");
        assertNotNull(user);
        assertEquals("John", user.getFirstName());
        assertEquals("Roche", user.getSurName());
    }

}
