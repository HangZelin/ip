/**
 * @@author Hang Zelin
 * Main Programme to execute the Duke Project
 * Duke will allow users to add three types of tasks: "todo" "deadline" "event".
 * Duke also allow users to list all tasks, mark a task to be done if it is finished, and
 * delete the task if the task is finished.
 * You can also search a specific task by its date, keyword.
 */
package duke.main;

import duke.excpetions.DukeException;
import duke.saveloadmanager.Storage;
import duke.task.TaskList;
import duke.uimanager.Ui;

import java.io.IOException;
import java.util.ArrayList;

public class Duke {

    private final String FILEPATH = "data/tasks.txt";
    private Storage storage;
    private TaskList tasks;
    private Ui ui;

    /**
     * initialize Ui, storage and load TaskLists from specific filePath for Duke
     *
     */
    public Duke() {
        ui = new Ui();
        storage = new Storage(FILEPATH);
        try {
            tasks = new TaskList(storage.load());
        } catch (DukeException e) {
            ui.showLoadingError();
            tasks = new TaskList();
        }
    }

    /**
     * Chooses a specific task to execute via tasks type and add to the tasklists.
     * Every time an execution is done, the task will be stored to the local file called tasks.txt
     * via Storage.
     *
     * @param index Index of the task users input.
     * @param Command JavaVarargs Commands users input.
     */
    public String operationForDuke(int index, String... Command) {
        String text;
        String operationType = Command[0];
        String task = Command[1];
        String time = Command[2];

        switch (operationType) {
        case "bye": {
            text = ui.goodbyeMessage();
            break;
        }
        case "list":
            text = ui.printList(tasks);
            break;
        case "done": {
            try {
                tasks.detectIndex(index);
                tasks.markDone(index);
                text = ui.markDone(tasks.get(index).getTaskInfo());
            } catch (DukeException e) {
                text = e.getMessage();
            }
            break;
        }
        case "delete": {
            try {
                tasks.detectIndex(index);
                text = ui.delete(tasks.get(index).getTaskInfo(), tasks.size() - 1);
                tasks.delete(index);
            } catch (DukeException e) {
                text = e.getMessage();
            }
            break;
        }
        case "tell": {
            text = ui.getSpecificDateEvent() + tasks.getSpecificDateEvent(time);
            break;
        }
        case "find": {
            text = ui.findTasks() + tasks.findTasks(task);
            break;
        }
        default: {
            try {
                tasks.add(operationType, task, time);
                text =ui.add(tasks.get(tasks.size() - 1).getTaskInfo(), tasks.size());
            } catch (DukeException e) {
                text = e.getMessage();
            }
            break;
        }
        }

        assert text != "" : "OOPS, Duke stops thinking!";
        return text;
    }

    /**
     * Updates a save data every time a round of execution is done.
     */
    public void updateSaveData() {
        try {
            storage.saveListDataToFile(tasks);
        } catch (IOException e) {
            ui.showSavingError();
        }
    }

    /**
     * Runs the programme of Duke. It will firstly say Hello to users. Then it will repeatedly accept input from
     * users and filter out key commands, then call OperationForDuke to execute a task by commands.
     * The process will not stop until users enter "goodbye".
     * Noted: Every time an execution is done, the savedata will be updated.
     *
     * @param input Input user take in.
     * @return Response Duke gives.
     */
    public String getResponse(String input) {
        ArrayList<String> messages;
        String operationType;
        String task;
        String time;
        String dukeResponse;
        int index;

        try {
            messages = ui.getInputForARound(input);
        } catch (DukeException e) {
            return e.getMessage();
        }

        assert messages.size() == 4 : "Error in Parser, should produce 4 key value for duke to execute!!";

        operationType = messages.get(0);
        task = messages.get(1);
        time = messages.get(2);
        index = Integer.parseInt(messages.get(3));

        dukeResponse = operationForDuke(index, operationType, task, time);

        updateSaveData(); //Update the SaveData every time a round of operation is done.

        return dukeResponse;
    }
}
