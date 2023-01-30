package ca.cmpt213.a4.client.control;

import ca.cmpt213.a4.client.model.Consumable;
import ca.cmpt213.a4.client.model.ConsumablesFactory;

import java.io.*;
import java.time.LocalDate;
import java.util.*;

import com.google.gson.*;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

/**
 * A class that allows to read and store an ArrayList of Consumables as an object
 * from the WebAppServer and communicates with the WebAppServer with Curl commands
 *
 * @author HiFen Kong
 */
public class ConsumablesList {
    private final int YEAR_BEGIN_INDEX = 0;
    private final int YEAR_END_INDEX = 4;
    private final int MONTH_BEGIN_INDEX = 5;
    private final int MONTH_END_INDEX = 7;
    private final int DAY_BEGIN_INDEX = 8;
    private final int DAY_END_INDEX = 10;

    private final String FOOD_TYPE = "FOOD";
    private final String DRINK_TYPE = "DRINK";
    private List<Consumable> list;

    /**
     * Default Constructor for the ConsumablesList instantiates the list field
     * with an empty Consumables ArrayList
     */
    public ConsumablesList() {
        list = new ArrayList<>();
    }

    /**
     * Converts a JsonObject to a Consumable Object
     * Does so by extracting the fields of the JsonObject and using a ConsumablesFactory
     * to instantiate Consumable Objects
     *
     * @param consumableJsonObject to jsonObject that is to be converted into a consumable object
     * @return the consumable object that was instantiated through the JsonObject fields
     */
    private Consumable jsonObjectToConsumable(JsonObject consumableJsonObject) {
        // Extracts the fields of the consumableJsonObject
        Long id = consumableJsonObject.get("id").getAsLong();
        String type = consumableJsonObject.get("type").getAsString();
        String name = consumableJsonObject.get("name").getAsString();
        String notes = consumableJsonObject.get("notes").getAsString();
        double price = consumableJsonObject.get("price").getAsDouble();
        double measure = consumableJsonObject.get("measure").getAsDouble();

        // Extracts the LocalDate field as a string and converts it back into a LocalDate
        String expiryDateString = consumableJsonObject.get("expiryDate").getAsString();
        int year = Integer.parseInt(expiryDateString.substring(YEAR_BEGIN_INDEX, YEAR_END_INDEX));
        int month = Integer.parseInt(expiryDateString.substring(MONTH_BEGIN_INDEX, MONTH_END_INDEX));
        int day = Integer.parseInt(expiryDateString.substring(DAY_BEGIN_INDEX, DAY_END_INDEX));

        LocalDate expiryDate = LocalDate.of(year, month, day);

        Consumable item;
        ConsumablesFactory consumableFactory = new ConsumablesFactory();

        // Uses ConsumableFactory to get instances of DrinkItems and FoodItems using read in fields
        if (type.equalsIgnoreCase(DRINK_TYPE)) {
            item = consumableFactory.getInstance(DRINK_TYPE, id, name, notes,
                    price, expiryDate, measure);
        } else {
            item = consumableFactory.getInstance(FOOD_TYPE, id, name, notes,
                    price, expiryDate, measure);
        }

        return item;
    }

    /**
     * A class for sending commands to the WebAppServer and updating the list of Consumables
     *
     * @param command the curl command to be sent to the WebAppServer
     */
    private void commandTheServer(String command) {
        Process process;
        try {
            process = Runtime.getRuntime().exec(command);

            // reads the input stream and creates a string from it
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            StringBuilder stringBuilder = new StringBuilder();
            reader.lines().forEach(line -> {
                stringBuilder.append(line);
            });

            // Creates the Json String and gets the JSONArray substring
            String json = stringBuilder.toString();
            json = json.substring(json.indexOf('['), json.indexOf(']') + 1);

            //Creates the jsonElement from the json string
            JsonElement listElement = JsonParser.parseString(json);

            // converts the JsonElement into a ArrayList and stores and sets the class List to that
            jsonElementToList(listElement);

            // closes the reader and the process
            reader.close();
            process.destroy();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Converts a jsonElement to A jsonArray and then converts all the JsonObjects to Consumables
     * and store them in the list
     *
     * @param inputElement the JsonElement all the object data is to be extracted from
     */
    private void jsonElementToList(JsonElement inputElement) {
        // if the fileElement is null then don't try to access it
        if (!inputElement.isJsonNull()) {
            list.clear();

            JsonArray JsonArrayOfConsumables = inputElement.getAsJsonArray();

            for (JsonElement consumableElement : JsonArrayOfConsumables) {
                // converts every JsonElement from the JsonArrayOfConsumables into
                // a JsonObject
                JsonObject consumableJsonObject = consumableElement.getAsJsonObject();

                // Converts a jsonObject to a Consumable Object
                Consumable consumable = jsonObjectToConsumable(consumableJsonObject);

                // Stores the jsonObject into an ArrayList of Consumables
                list.add(consumable);
            }
        }
    }

    /**
     * Asks the WebAppServer to save the current database to a JsonFile
     */
    public void exit() {
        String command = "curl -i -H \"Content-Type: application/json\" -X GET localhost:8080/exit";
        try {
            Runtime.getRuntime().exec(command);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Ask the WebAppServer to add a Consumable Item
     * and to return an updated ArrayList of consumables
     *
     * @param item the Consumable item that is to be added to the WebAppServer
     */
    public void addToServer(Consumable item) {
        // custom-built Gson object
        Gson myGson = new GsonBuilder().registerTypeAdapter(LocalDate.class,
                new TypeAdapter<LocalDate>() {
                    @Override
                    public void write(JsonWriter jsonWriter,
                                      LocalDate localDate) throws IOException {
                        jsonWriter.value(localDate.toString());
                    }

                    @Override
                    public LocalDate read(JsonReader jsonReader) throws IOException {
                        return LocalDate.parse(jsonReader.nextString());
                    }
                }).create();

        JsonElement consumableJsonElement = myGson.toJsonTree(item);
        String jsonString = myGson.toJson(consumableJsonElement);

        jsonString = jsonString.replace("\"", "\\\"");

        String command = "curl -i -H \"Content-Type: application/json\" -X POST -d \"" + jsonString +
                "\" localhost:8080/addItem";

        commandTheServer(command);
    }

    /**
     * Asks the WebAppServer to remove a Consumable item from the database
     * Does so by using the Consumable item's id
     * Then gets an updated ArrayList of consumables
     *
     * @param consumableItem the Consumable item to be removed from the list
     */
    public ConsumablesList remove(Consumable consumableItem) {
        long itemId = consumableItem.getId();
        String command = "curl -i -H \"Content-Type: application/json\" -X POST localhost:8080/removeItem/";
        command = command.concat(Long.toString(itemId));
        commandTheServer(command);

        return this;
    }

    /**
     * Returns the consumable object given at the passed in index of the list
     *
     * @param index the index of the consumable object in the list
     * @return the consumable object in the given index of the list
     */
    public Consumable get(int index) {
        return list.get(index);
    }

    /**
     * Gets the size of the underlying ArrayList of the ConsumableList
     *
     * @return the size of the ConsumablesList object
     */
    public int getSize() {
        return list.size();
    }

    /**
     * Return true if the underlying ArrayList of the ConsumablesList is empty
     *
     * @return a boolean: true if the ConsumablesList is empty and false otherwise
     */
    public boolean isEmpty() {
        return list.isEmpty();
    }

    /**
     * Asks the WebAppServer to return a ArrayList of all consumables
     *
     * @return a new ConsumablesList from the WebAppServer with only the expired consumables
     */
    public ConsumablesList getAllItems() {
        String command = "curl -i -H \"Content-Type: application/json\" -X GET localhost:8080/listAll";
        commandTheServer(command);
        return this;
    }

    /**
     * Asks the WebAppServer to return a ArrayList of all consumables that have expired
     *
     * @return a new ConsumablesList from the WebAppServer with only the expired consumables
     */
    public ConsumablesList getExpiredItems() {
        String command = "curl -i -H \"Content-Type: application/json\" -X GET localhost:8080/listExpired";
        commandTheServer(command);
        return this;
    }

    /**
     * Asks the WebAppServer to return a ArrayList of all consumables not expired
     *
     * @return a new ConsumablesList from the WebAppServer with only the non-expired consumables
     */
    public ConsumablesList getNonExpiredItems() {
        String command = "curl -i -H \"Content-Type: application/json\" -X GET localhost:8080/listNonExpired";
        commandTheServer(command);
        return this;
    }

    /**
     * Asks the WebAppServer to return a ArrayList of all consumables expiring in a week
     *
     * @return a new ConsumablesList from the WebAppServer with only the consumables expiring in a week
     */
    public ConsumablesList getItemsExpiringInAWeek() {
        String command = "curl -i -H \"Content-Type: application/json\" -X GET localhost:8080/listExpiringIn7Days ";
        commandTheServer(command);
        return this;
    }
}

