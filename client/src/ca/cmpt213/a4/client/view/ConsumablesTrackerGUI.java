package ca.cmpt213.a4.client.view;

import ca.cmpt213.a4.client.control.ConsumablesList;
import ca.cmpt213.a4.client.model.Consumable;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * class that acts as the Graphic User Interface for the ConsumablesTracker
 *
 * @author HiFen Kong
 */
public class ConsumablesTrackerGUI implements ActionListener {
    // class attributes
    private ConsumablesList consumablesList = new ConsumablesList();

    // components of the application
    private JLabel appNameLabel;
    private JLabel noItemsLabel;
    private JButton listAllItemsButton;
    private JButton listExpButton;
    private JButton listNonExpButton;
    private JButton listExp7DaysButton;
    private JButton addItemButton;

    // containers for the application
    private JFrame applicationFrame;
    private JScrollPane itemsScrollFrame;
    private JPanel itemsPanel;
    private JPanel mainPanel;
    private JPanel buttonPanel;

    // constants
    private final String ALL_EMPTY_MSG = "No items to show";
    private final String EXP_EMPTY_MSG = "No expired items to show";
    private final String NON_EXP_EMPTY_MSG = "No non-expired items to show";
    private final String EXP_IN_WEEK_EMPTY_MSG = "No items expiring 7 days to show";
    private final String LIST_ALL_ITEMS_BUTTON_NAME = "All";
    private final String LIST_EXP_BUTTON_NAME = "Expired";
    private final String LIST_NON_EXP_BUTTON_NAME = "Not Expired";
    private final String LIST_EXP_7_DAYS_BUTTON_NAME = "Expiring in 7 Days";
    private final String ADD_ITEM_BUTTON_NAME = "Add Item";
    private final int APP_FRAME_WIDTH = 600;
    private final int APP_FRAME_HEIGHT = 500;
    private final int RIGID_AREA_DIMENSION = 20;
    private final int ITEM_SCROLL_FRAME_DIMENSION = 400;
    private final int BUTTON_PANEL_WIDTH = 1000;
    private final int BUTTON_PANEL_HEIGHT = 50;

    /**
     * Constructor for the class
     * Initialises the database and sets up the frame
     */
    public ConsumablesTrackerGUI() {
        setApplicationFrame(consumablesList.getAllItems());
    }

    /**
     * Sets up the application frame for the GUI
     *
     * @param database of consumables that is displayed by the GUI
     */
    private void setApplicationFrame(ConsumablesList database) {
        applicationFrame = new JFrame("Consumables Tracker");
        applicationFrame.setSize(APP_FRAME_WIDTH, APP_FRAME_HEIGHT);
        applicationFrame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

        // When the window frame is closed the Class database is saved to a Gson file
        applicationFrame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                super.windowClosed(e);
                consumablesList.exit();
                applicationFrame.dispose();
            }
        });

        setMainPanel(database, ALL_EMPTY_MSG);

        applicationFrame.add(mainPanel);
        applicationFrame.setVisible(true);
    }

    /**
     * resets the application frame to update the view to match the button presses
     *
     * @param database of consumables that is displayed by the GUI
     * @param emptyMsg message that is displayed in the ScrollFrame if the database is empty
     */
    private void resetApplicationFrame(ConsumablesList database, String emptyMsg) {
        applicationFrame.remove(mainPanel);

        setMainPanel(database, emptyMsg);

        applicationFrame.add(mainPanel);

        applicationFrame.revalidate();
        applicationFrame.repaint();
    }

    /**
     * Sets up the mainPanel stored in the applicationFrame
     *
     * @param database of consumables that is displayed in the ScrollFrame
     * @param emptyMsg message that is displayed in the ScrollFrame if the database is empty
     */
    private void setMainPanel(ConsumablesList database, String emptyMsg) {
        mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));

        appNameLabel = new JLabel("My Consumables Tracker");
        appNameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        mainPanel.add(appNameLabel);

        setButtonPanel();
        mainPanel.add(buttonPanel);

        setItemsScrollFrame(database, emptyMsg);
        mainPanel.add(itemsScrollFrame);

        addItemButton = new JButton(ADD_ITEM_BUTTON_NAME);
        addItemButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        addItemButton.setFocusable(false);
        addItemButton.addActionListener(this);

        mainPanel.add(Box.createRigidArea(new Dimension(RIGID_AREA_DIMENSION, RIGID_AREA_DIMENSION)));
        mainPanel.add(addItemButton);
    }

    /**
     * Sets up and formats the ButtonPanel that contains the
     * buttons for listing the database
     */
    private void setButtonPanel() {
        buttonPanel = new JPanel();
        buttonPanel.setMaximumSize(new Dimension(BUTTON_PANEL_WIDTH, BUTTON_PANEL_HEIGHT));

        listAllItemsButton = new JButton(LIST_ALL_ITEMS_BUTTON_NAME);
        listAllItemsButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        listAllItemsButton.addActionListener(this);
        listAllItemsButton.setFocusable(false);
        buttonPanel.add(listAllItemsButton);

        listExpButton = new JButton(LIST_EXP_BUTTON_NAME);
        listExpButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        listExpButton.addActionListener(this);
        listExpButton.setFocusable(false);
        buttonPanel.add(listExpButton);

        listNonExpButton = new JButton(LIST_NON_EXP_BUTTON_NAME);
        listNonExpButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        listNonExpButton.addActionListener(this);
        listNonExpButton.setFocusable(false);
        buttonPanel.add(listNonExpButton);

        listExp7DaysButton = new JButton(LIST_EXP_7_DAYS_BUTTON_NAME);
        listExp7DaysButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        listExp7DaysButton.addActionListener(this);
        listExp7DaysButton.setFocusable(false);
        buttonPanel.add(listExp7DaysButton);
    }

    /**
     * Sets up the ScrollFrame that displays the panels containing the information
     * on the consumables in the consumables database
     *
     * @param database of consumables to be displayed as panels in the scrollFrame
     * @param emptyMsg message to be printed in case there are not consumables to display
     */
    private void setItemsScrollFrame(ConsumablesList database, String emptyMsg) {
        itemsPanel = new JPanel();
        itemsPanel.setLayout(new BoxLayout(itemsPanel, BoxLayout.Y_AXIS));

        // if the database is empty displays the emptyMsg in the scrollFrame instead
        if (database.isEmpty()) {
            noItemsLabel = new JLabel(emptyMsg);
            noItemsLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            itemsPanel.add(noItemsLabel);
        } else {
            //  converts all the consumables in the database into panels
            for (int i = 0; i < database.getSize(); i++) {
                itemsPanel.add(getItemPanel(i + 1, database.get(i)));
                itemsPanel.add(Box.createRigidArea(new Dimension(RIGID_AREA_DIMENSION, RIGID_AREA_DIMENSION)));
            }
        }

        itemsPanel.setVisible(true);
        itemsScrollFrame = new JScrollPane(itemsPanel);
        itemsScrollFrame.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        itemsScrollFrame.setMaximumSize(new Dimension(ITEM_SCROLL_FRAME_DIMENSION, ITEM_SCROLL_FRAME_DIMENSION));
    }

    /**
     * Converts a consumableItem into a panel to be added to the ScrollFrame
     * The panel contains a removeButton
     *
     * @param itemNumber of the item Represent the order in the database
     * @param item       the item which contents will be converted into a panel
     * @return a JPanel with formatted item information and a removeButton
     */
    private JPanel getItemPanel(int itemNumber, Consumable item) {
        JLabel itemInfoLabel = new JLabel();
        JPanel itemPanel = new JPanel();
        itemPanel.setLayout(new BoxLayout(itemPanel, BoxLayout.Y_AXIS));
        String itemName;

        if (item.isFoodItem()) {
            itemName = "Item #" + itemNumber + " (Food)";
        } else {
            itemName = "Item #" + itemNumber + " (Drink)";
        }

        // sets the label and changes the way new lines are implemented in the string
        itemInfoLabel.setText("<html>" + itemName +
                "<br/>" + item.toString().replaceAll("\n", "<br/> " + "<html/>"));

        // creates and implements the remove button
        JButton removeButton = new JButton("Remove");
        removeButton.setFocusable(false);
        removeButton.addActionListener(
                (e) -> {
                    resetApplicationFrame(consumablesList.remove(item), ALL_EMPTY_MSG);
                });

        itemPanel.add(itemInfoLabel);
        itemPanel.add(removeButton);
        itemPanel.setVisible(true);

        return itemPanel;
    }

    /**
     * Overloads the method so that it implements responses to different data presses
     *
     * @param e the actionEvent the actionListeners are responding to
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        // Buttons reset the applicationFrame with the appropriate database
        if (e.getActionCommand().equalsIgnoreCase(LIST_ALL_ITEMS_BUTTON_NAME)) {
            resetApplicationFrame(consumablesList.getAllItems(), ALL_EMPTY_MSG);
        } else if (e.getActionCommand().equalsIgnoreCase(LIST_EXP_BUTTON_NAME)) {
            resetApplicationFrame(consumablesList.getExpiredItems(), EXP_EMPTY_MSG);
        } else if (e.getActionCommand().equalsIgnoreCase(LIST_NON_EXP_BUTTON_NAME)) {
            resetApplicationFrame(consumablesList.getNonExpiredItems(), NON_EXP_EMPTY_MSG);
        } else if (e.getActionCommand().equalsIgnoreCase(LIST_EXP_7_DAYS_BUTTON_NAME)) {
            resetApplicationFrame(consumablesList.getItemsExpiringInAWeek(), EXP_IN_WEEK_EMPTY_MSG);
        } else if (e.getActionCommand().equalsIgnoreCase(ADD_ITEM_BUTTON_NAME)) {
            //Opends the addItemDialog menu
            AddItemDialog addItemDialog = AddItemDialog.instance(applicationFrame);
            addItemDialog.pack();
            addItemDialog.setVisible(true);

            //Reads in an item if there was one created by the addItemDialog
            Consumable item = addItemDialog.getItem();
            if (item != null) {
                consumablesList.addToServer(item);
            }

            // clears and resets the Dialog and the applicationFrame
            addItemDialog.clear();
            resetApplicationFrame(consumablesList, ALL_EMPTY_MSG);
        }
    }
}