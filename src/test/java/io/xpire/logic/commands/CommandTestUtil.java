package io.xpire.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import io.xpire.commons.core.index.Index;
import io.xpire.logic.commands.exceptions.CommandException;
import io.xpire.model.Model;
import io.xpire.model.Xpire;
import io.xpire.model.item.ContainsKeywordsPredicate;
import io.xpire.model.item.Item;
import io.xpire.testutil.Assert;

/**
 * Contains helper methods for testing commands.
 */
public class CommandTestUtil {

    public static final String VALID_NAME_APPLE = "Apple";
    public static final String VALID_NAME_BANANA = "Banana";
    public static final String VALID_NAME_CORN = "Corn";
    public static final String VALID_NAME_DUCK = "Duck";
    public static final String VALID_NAME_EGGS = "Eggs";
    public static final String VALID_NAME_FAKECRABSTICK = "Fake Crab Stick";
    public static final String VALID_NAME_JELLY = "Jelly";
    public static final String VALID_NAME_KIWI = "Kiwi";

    public static final String VALID_EXPIRY_DATE_APPLE = "01/02/2020";
    public static final String VALID_EXPIRY_DATE_BANANA = "1/2/2020";
    public static final String VALID_EXPIRY_DATE_CORN = "1/2/2020";
    public static final String VALID_EXPIRY_DATE_DUCK = "1/2/2020";
    public static final String VALID_EXPIRY_DATE_EGGS = "1/2/2020";
    public static final String VALID_EXPIRY_DATE_FAKECRABSTICK = "15/12/2019";
    public static final String VALID_EXPIRY_DATE_JELLY = "1/2/2020";
    public static final String VALID_EXPIRY_DATE_KIWI = "1/2/2020";

    public static final String VALID_QUANTITY_APPLE = "1";
    public static final String VALID_QUANTITY_BANANA = "1";
    public static final String VALID_QUANTITY_CORN = "2";
    public static final String VALID_QUANTITY_DUCK = "1";
    public static final String VALID_QUANTITY_EGGS = "1";
    public static final String VALID_QUANTITY_FAKECRABSTICK = "1";
    public static final String VALID_QUANTITY_JELLY = "4";
    public static final String VALID_QUANTITY_KIWI = "1";

    public static final String VALID_TAG_FRUIT = "Fruit";
    public static final String VALID_TAG_DRINK = "Drink";
    public static final String VALID_TAG_FRIDGE = "Fridge";
    public static final String VALID_TAG_PROTEIN = "Protein";

    public static final String VALID_REMINDER_THRESHOLD_APPLE = "0";
    public static final String VALID_REMINDER_THRESHOLD_BANANA = "0";
    public static final String VALID_REMINDER_THRESHOLD_CORN = "0";
    public static final String VALID_REMINDER_THRESHOLD_DUCK = "0";
    public static final String VALID_REMINDER_THRESHOLD_EGGS = "7";
    public static final String VALID_REMINDER_THRESHOLD_FAKECRABSTICK = "0";
    public static final String VALID_REMINDER_THRESHOLD_JELLY = "3";
    public static final String VALID_REMINDER_THRESHOLD_KIWI = "0";

    public static final String INVALID_NAME = "@pple";
    public static final String INVALID_EXPIRY_DATE = "50/50/5000";
    public static final String INVALID_TAG = "$cold";
    public static final String INVALID_QUANTITY = "-2";
    public static final String INVALID_REMINDER_THRESHOLD = "-5";


    public static final String PREAMBLE_WHITESPACE = "\t  \r  \n";
    public static final String PREAMBLE_NON_EMPTY = "NonEmptyPreamble";

    /**
     * Executes the given {@code command}, confirms that <br>
     * - the returned {@link CommandResult} matches {@code expectedCommandResult} <br>
     * - the {@code actualModel} matches {@code expectedModel}
     */
    public static void assertCommandSuccess(Command command, Model actualModel, CommandResult expectedCommandResult,
                                            Model expectedModel) {
        try {
            CommandResult result = command.execute(actualModel);
            assertEquals(expectedCommandResult, result);
            assertEquals(expectedModel, actualModel);
        } catch (CommandException ce) {
            throw new AssertionError("Execution of command should not fail.", ce);
        }
    }

    /**
     * Convenience wrapper to {@link #assertCommandSuccess(Command, Model, CommandResult, Model)}
     * that takes a string {@code expectedMessage}.
     */
    public static void assertCommandSuccess(Command command, Model actualModel, String expectedMessage,
            Model expectedModel) {
        CommandResult expectedCommandResult = new CommandResult(expectedMessage);
        assertCommandSuccess(command, actualModel, expectedCommandResult, expectedModel);
    }

    /**
     * Executes the given {@code command}, confirms that <br>
     * - a {@code CommandException} is thrown <br>
     * - the CommandException message matches {@code expectedMessage} <br>
     * - the expiry date tracker, filtered item list and selected item in {@code actualModel} remain unchanged
     */
    public static void assertCommandFailure(Command command, Model actualModel, String expectedMessage) {
        // we are unable to defensively copy the model for comparison later, so we can
        // only do so by copying its components.
        Xpire expectedXpire = new Xpire(actualModel.getXpire());
        List<Item> expectedFilteredList = new ArrayList<>(actualModel.getFilteredItemList());

        Assert.assertThrows(CommandException.class, expectedMessage, () -> command.execute(actualModel));
        assertEquals(expectedXpire, actualModel.getXpire());
        assertEquals(expectedFilteredList, actualModel.getFilteredItemList());
    }
    /**
     * Updates {@code model}'s filtered list to show only the item at the given {@code targetIndex} in the
     * {@code model}'s expiry date tracker.
     */
    public static void showItemAtIndex(Model model, Index targetIndex) {
        assertTrue(targetIndex.getZeroBased() < model.getFilteredItemList().size());

        Item item = model.getFilteredItemList().get(targetIndex.getZeroBased());
        final String[] splitName = item.getName().toString().split("\\s+");
        model.updateFilteredItemList(new ContainsKeywordsPredicate(Arrays.asList(splitName[0])));

        assertEquals(1, model.getFilteredItemList().size());
    }

}
