package ca.cmpt213.a4.client.view;

import com.github.lgooddatepicker.components.DatePicker;
import ca.cmpt213.a4.client.model.Consumable;
import ca.cmpt213.a4.client.model.ConsumablesFactory;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;

/**
 * A singleton class that extends the JDialog Class
 * Creates a GUI that allows for the user to add items of different types to the database
 * It is a modal JDialog that closes on creation of item, cancellation, or closing of the window
 *
 * @author HiFen Kong
 */
public class AddItemDialog extends JDialog {

    // class attributes
    public static AddItemDialog uniqueInstance = null;
    private Consumable item;
    private String itemType = "FOOD";
    private String measureType = "Weight:";

    // goodDatePicker
    private DatePicker datepicker;

    // JSwing components
    private JLabel titleLabel;
    private JLabel typeLabel;
    private JLabel nameLabel;
    private JLabel notesLabel;
    private JLabel priceLabel;
    private JLabel measureLabel;
    private JLabel expiryDateLabel;

    private JComboBox typeComboBox;
    private JTextField nameTextField;
    private JTextField notesTextField;
    private JTextField priceTextField;
    private JTextField measureTextField;

    private JButton createButton;
    private JButton cancelButton;

    // JSwing containers
    private JPanel addItemPanel;
    private JPanel addFieldsPanel;
    private JPanel fieldNamesPanel;
    private JPanel fieldsEntriesPanel;
    private JPanel buttonsPanel;

    // Constants
    private final int TEXT_FIELDS_COLUMNS = 20;
    private final int RIGID_SPACE_HEIGHT_NAMES = 18;
    private final int RIGID_SPACE_HEIGHT_FIELDS = 13;
    private final int RIGID_SPACE_WIDTH = 10;
    private final int DIALOG_DIMENSIONS = 400;
    private final String[] ITEM_TYPES = {"Food", "Drink"};
    private final int FOOD_IDX = 0;
    private final int DRINK_IDX = 1;
    private final String FOOD_TYPE = "FOOD";
    private final String DRINK_TYPE = "DRINK";
    private final String FOOD_MEASURE = "Weight:";
    private final String DRINK_MEASURE = "Volume:";

    /**
     * Constructor for the class
     * Assigns an owner from to the object with a default name: "Add Item" and modality set to true
     * Sets the modalityType to the document modal type and adds a panel with JSwing components
     *
     * @param frame
     */
    protected AddItemDialog(JFrame frame) {
        super(frame, "Add Item", true);
        this.setModalityType(ModalityType.DOCUMENT_MODAL);
        setAddItemPanel();
        super.add(addItemPanel);
        super.setPreferredSize(new Dimension(DIALOG_DIMENSIONS, DIALOG_DIMENSIONS));
    }

    /**
     * Returns a new instance of this class to the calling program
     * if there is already an instance of this class returns that instead
     *
     * @param frame the owner frame for the construction of the Object
     * @return an instance of the AddItemDialog class
     */
    public static AddItemDialog instance(JFrame frame) {
        if (uniqueInstance == null) {
            uniqueInstance = new AddItemDialog(frame);
        }
        return uniqueInstance;
    }

    /**
     * Sets up the main panel for this class and adds all its components to it
     * i.e. the fieldNamesPanel, the buttonsPanel, the fieldNamesPanel, and the fieldEntriesPanel
     */
    private void setAddItemPanel() {
        // sets up the main panel of the JDialog
        addItemPanel = new JPanel();
        addItemPanel.setVisible(true);
        addItemPanel.setLayout(new BoxLayout(addItemPanel, BoxLayout.Y_AXIS));

        // sets the header Label for the main panel
        titleLabel = new JLabel("Add Item");
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        addItemPanel.add(titleLabel);

        // creates a new panel with the default layout manager
        // to store the fieldNamesPanel and the fieldEntriesPanel
        addFieldsPanel = new JPanel();

        // Sets up the remaining panels and adds them as needed
        setFieldNamesPanel();
        setFieldEntriesPanel();
        setButtonsPanel();

        addFieldsPanel.add(fieldNamesPanel);
        addFieldsPanel.add(fieldsEntriesPanel);

        addItemPanel.add(addFieldsPanel);
        addItemPanel.add(buttonsPanel);
    }

    /**
     * Resets the Dialog and updates it depending on the itemType attribute
     * so that is request the proper measurement for the measure field
     * i.e. weight vs volume
     */
    private void resetDialog() {
        // removes the main panel from the JDialog
        super.remove(addItemPanel);

        // remove all components from all panels
        addItemPanel.removeAll();
        addFieldsPanel.removeAll();

        // Resets al the components
        addItemPanel.add(titleLabel);

        setFieldNamesPanel();
        setFieldEntriesPanel();
        setButtonsPanel();

        addFieldsPanel.add(fieldNamesPanel);
        addFieldsPanel.add(fieldsEntriesPanel);

        addItemPanel.add(addFieldsPanel);
        addItemPanel.add(buttonsPanel);

        addFieldsPanel.revalidate();
        addFieldsPanel.repaint();

        addItemPanel.revalidate();
        addItemPanel.repaint();

        super.add(addItemPanel);
        super.revalidate();
        super.repaint();
    }

    /**
     * Sets and formats the fieldNamesPanel
     * This panel contains all the Labels that allow the user to discern the difference between
     * the textFields
     */
    private void setFieldNamesPanel() {
        fieldNamesPanel = new JPanel();
        fieldNamesPanel.setLayout(new BoxLayout(fieldNamesPanel, BoxLayout.Y_AXIS));
        fieldNamesPanel.setVisible(true);
        fieldNamesPanel.add(Box.createRigidArea(new Dimension(RIGID_SPACE_WIDTH, RIGID_SPACE_HEIGHT_NAMES)));

        typeLabel = new JLabel("Type:");
        typeLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        fieldNamesPanel.add(typeLabel);
        fieldNamesPanel.add(Box.createRigidArea(new Dimension(RIGID_SPACE_WIDTH, RIGID_SPACE_HEIGHT_NAMES)));

        nameLabel = new JLabel("Name:");
        nameLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        fieldNamesPanel.add(nameLabel);
        fieldNamesPanel.add(Box.createRigidArea(new Dimension(RIGID_SPACE_WIDTH, RIGID_SPACE_HEIGHT_NAMES)));

        notesLabel = new JLabel("Notes:");
        notesLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        fieldNamesPanel.add(notesLabel);
        fieldNamesPanel.add(Box.createRigidArea(new Dimension(RIGID_SPACE_WIDTH, RIGID_SPACE_HEIGHT_NAMES)));

        priceLabel = new JLabel("Price:");
        priceLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        fieldNamesPanel.add(priceLabel);
        fieldNamesPanel.add(Box.createRigidArea(new Dimension(RIGID_SPACE_WIDTH, RIGID_SPACE_HEIGHT_NAMES)));

        measureLabel = new JLabel(measureType);
        measureLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        fieldNamesPanel.add(measureLabel);
        fieldNamesPanel.add(Box.createRigidArea(new Dimension(RIGID_SPACE_WIDTH, RIGID_SPACE_HEIGHT_NAMES)));

        expiryDateLabel = new JLabel("Expiry Date:");
        expiryDateLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        fieldNamesPanel.add(expiryDateLabel);
        fieldNamesPanel.add(Box.createRigidArea(new Dimension(RIGID_SPACE_WIDTH, RIGID_SPACE_HEIGHT_NAMES)));
    }

    /**
     * Sets and formats the fieldEntriesPanel
     * This panel contains all the JTextField components as well as the JComboBox and DateTimePicker
     * components
     */
    private void setFieldEntriesPanel() {
        fieldsEntriesPanel = new JPanel();
        fieldsEntriesPanel.setVisible(true);
        fieldsEntriesPanel.setLayout(new BoxLayout(fieldsEntriesPanel, BoxLayout.Y_AXIS));

        setTypeComboBox();
        fieldsEntriesPanel.add(typeComboBox);
        fieldsEntriesPanel.add(Box.createRigidArea(new Dimension(RIGID_SPACE_WIDTH, RIGID_SPACE_HEIGHT_FIELDS)));

        nameTextField = new JTextField(TEXT_FIELDS_COLUMNS);
        nameTextField.setAlignmentX(Component.CENTER_ALIGNMENT);
        fieldsEntriesPanel.add(nameTextField);
        fieldsEntriesPanel.add(Box.createRigidArea(new Dimension(RIGID_SPACE_WIDTH, RIGID_SPACE_HEIGHT_FIELDS)));

        notesTextField = new JTextField(TEXT_FIELDS_COLUMNS);
        notesTextField.setAlignmentX(Component.CENTER_ALIGNMENT);
        fieldsEntriesPanel.add(notesTextField);
        fieldsEntriesPanel.add(Box.createRigidArea(new Dimension(RIGID_SPACE_WIDTH, RIGID_SPACE_HEIGHT_FIELDS)));

        priceTextField = new JTextField(TEXT_FIELDS_COLUMNS);
        priceTextField.setAlignmentX(Component.CENTER_ALIGNMENT);
        fieldsEntriesPanel.add(priceTextField);
        fieldsEntriesPanel.add(Box.createRigidArea(new Dimension(RIGID_SPACE_WIDTH, RIGID_SPACE_HEIGHT_FIELDS)));

        measureTextField = new JTextField(TEXT_FIELDS_COLUMNS);
        measureTextField.setAlignmentX(Component.CENTER_ALIGNMENT);
        fieldsEntriesPanel.add(measureTextField);
        fieldsEntriesPanel.add(Box.createRigidArea(new Dimension(RIGID_SPACE_WIDTH, RIGID_SPACE_HEIGHT_FIELDS)));

        datepicker = new DatePicker();
        fieldsEntriesPanel.add(datepicker);
    }

    /**
     * Sets up the JComboBox and implements an action listener that changes the JDialog
     * to reflect the selection made in the JComboBox
     */
    private void setTypeComboBox() {
        // initialises the JComboBox
        // the starting index depends on what the current itemType is
        typeComboBox = new JComboBox(ITEM_TYPES);
        if (itemType.equalsIgnoreCase(FOOD_TYPE)) {
            typeComboBox.setSelectedIndex(FOOD_IDX);
        } else {
            typeComboBox.setSelectedIndex(DRINK_IDX);
        }

        // implements the actionListener for the button
        // Changes the itemType attribute to match the selection of the JComboBox
        // then updates the JDialog so that the view properly represent the selection
        typeComboBox.addActionListener((e) -> {
            String type = typeComboBox.getSelectedItem().toString();
            if (type.equalsIgnoreCase(FOOD_TYPE)) {
                itemType = FOOD_TYPE;
                measureType = FOOD_MEASURE;
            } else if (type.equalsIgnoreCase(DRINK_TYPE)) {
                itemType = DRINK_TYPE;
                measureType = DRINK_MEASURE;
            }
            resetDialog();
        });
    }

    /**
     * Sets the buttonsPanel and provides the actionListener
     * Contains the cancelButton that disposes of the JDialog and the createButton
     * that creates a Consumable Item
     */
    private void setButtonsPanel() {
        buttonsPanel = new JPanel();
        buttonsPanel.setVisible(true);

        // Creates the cancelButton and implements the actionListener for it
        cancelButton = new JButton("Cancel");
        cancelButton.setFocusable(false);
        cancelButton.addActionListener((e) -> this.dispose());
        buttonsPanel.add(cancelButton);

        // Creates the createButton and implements the actionListener for it
        createButton = new JButton("Create");
        createButton.setFocusable(false);
        createButton.addActionListener((e) -> {
            createItem();
        });
        buttonsPanel.add(createButton);
    }

    /**
     * Creates a Consumable Item using the user information provided by the
     * JComboBox, JTextFields, and the DatePicker components.
     * Uses the class itemType and a ConsumablesFactory to determine what type of Object is created
     * If an invalid price, measure, or if fields are missing other than the notes field then an
     * appropriate messageDialog will prompt the user to correct it
     * Once the item is created the JDialog will close
     */
    private void createItem() {
        // gets the date from the datePicker components
        LocalDate expiryDate = datepicker.getDate();

        // checks if any fields were left empty other than the notes field
        // if so then prompts the user to correct it with a MessageDialog
        boolean nameTextFieldEmpty = nameTextField.getText().isEmpty();
        boolean priceTextFieldEmpty = priceTextField.getText().isEmpty();
        boolean measureTextFieldEmpty = measureTextField.getText().isEmpty();
        if (expiryDate == null || nameTextFieldEmpty || priceTextFieldEmpty || measureTextFieldEmpty) {
            JOptionPane.showMessageDialog(this, "Some Field(s) are empty. All fields, " +
                    "but the notes, must be filled in.");
        } else {
            // Extracts the field info from the components
            String name = nameTextField.getText();
            String notes = notesTextField.getText();
            double price = Double.parseDouble(priceTextField.getText());
            double measure = Double.parseDouble(measureTextField.getText());

            // if there is an invalid price or measure prompts the user to correct it with a MessageDialog
            if (price <= 0) {
                JOptionPane.showMessageDialog(this, "The price field should be greater than 0. " +
                        "Please change.");
            } else if (measure <= 0) {
                if (itemType.equalsIgnoreCase(FOOD_TYPE)) {
                    JOptionPane.showMessageDialog(this, "The weight field should be greater than 0. " +
                            "Please change.");
                } else {
                    JOptionPane.showMessageDialog(this, "The volume field should be greater than 0. " +
                            "Please change.");
                }
            } else {

                // Constructs a foodItem or a DrinkItem based on read in Fields
                ConsumablesFactory consumableFactory = new ConsumablesFactory();

                // constructs an Item from user input and adds it to the database
                long id = 0;
                if (itemType.equalsIgnoreCase(FOOD_TYPE)) {
                    item = consumableFactory.getInstance(FOOD_TYPE, id, name, notes, price, expiryDate, measure);
                } else {
                    item = consumableFactory.getInstance(DRINK_TYPE, id, name, notes, price, expiryDate, measure);
                }

                // Closes the JDialog on successful creation of the Consumable
                this.dispose();
            }
        }
    }

    /**
     * Returns the sole consumable item associated during the current running of this Class
     *
     * @return the Consumable item attribute
     */
    public Consumable getItem() {
        return item;
    }

    /**
     * Clears the instance of the class and sets it back to null
     */
    public void clear() {
        uniqueInstance.dispose();
        item = null;
        uniqueInstance = null;
    }
}
