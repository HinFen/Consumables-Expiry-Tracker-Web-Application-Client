package ca.cmpt213.a4.client;

import ca.cmpt213.a4.client.view.ConsumablesTrackerGUI;

import javax.swing.*;

// Credits
// Some Classes and Methods were reused from previous assignments done in this class

// The UserManual.txt was also reused as the client does GUI does not change

// Credit to the given assignment description for the gsonBuilder code

// Credit to Brian's video on deserializing a json array from a file:
// https://www.youtube.com/watch?v=HSuVtkdej8Q for the readFoodDataFile() method

// Credit to this stackOverFlow thread for figuring out how to add new lines to JLables
// stackoverflow.com/questions/1090098/newline-in-jlabel

// Credit to https://medium.com/@prajwalsdharan/curl-in-java-d62a5e4c0f55
// For example of using a BuffereReader for reading the inputStream and converting it into a String

/**
 * A class for the main application of the ConsumablesTracker
 * Instantiates the ConsumablesTrackerGUI in a separate thread
 *
 * @author HiFen Kong
 */
public class ClientConsumablesTracker {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new ConsumablesTrackerGUI();
            }
        });
    }
}
