package duke.task;

import duke.command.Parser;
import duke.exceptions.DukeException;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Locale;

/**
 * @@author Hang Zelin
 *
 * Stores all the tasks for the Duke. Duke can refer to this tasklist to see a specific task
 * or make use of the methods in it to execute an operation.
 */
public class TaskList {

    private ArrayList<Task> tasks;

    /**
     * Constructor to store all the tasks in a Generic ArrayList.
     *
     * @param tasks A list of Task type variables.
     */
    public TaskList(ArrayList<Task> tasks) {
        this.tasks = tasks;
    }

    /**
     * Another Constructor to initialize an empty TaskList if there is no save data.
     */
    public TaskList() {
        this.tasks = new ArrayList<Task>();
    }

    /**
     * Returns all the tasks that match the time users take in.
     *
     * @param time String message that indicates time users take in to find specific event.
     * @return All the tasks that match the time users take in.
     */
    public String getSpecificDateEvent(String time) {
        Parser parser = new Parser("");
        String text = "";
        int count = 0; //count the number of the events happen on the time.

        for (int i = 0; i < tasks.size(); i++) {
            String message = tasks.get(i).getTaskStatus();
            String unparsedInfo = tasks.get(i).getTimeForSaveData();
            String timeInFormat = (parser.parseTime(time) != null)
                    ? parser.parseTime(time).format(DateTimeFormatter.ofPattern("MMM dd yyyy HH:mm", Locale.ENGLISH))
                    : "Nope";

            if ((unparsedInfo != null && (unparsedInfo.contains(time) || unparsedInfo.contains(timeInFormat)))
                    || message.contains(time) || message.contains(timeInFormat)) {
                count++;
                text += count + "." + message + "\n";
            }
        }
        if (count == 0) {
            return "Sorry. There is no tasks occurred on the time you give me!! :(\n";
        }

        return text;
    }

    /**
     * Returns all the tasks that match the key word users take in.
     *
     * @param keyword String message that indicates the keyword users want to search.
     * @return All the tasks that match the key word users take in.
     */
    public String findTasks(String keyword) {
        String text = "";
        int count = 0;

        for (int i = 0; i < tasks.size(); i++) {
            String message = tasks.get(i).getTaskStatus();
            if (message.contains(keyword)) {
                count++;
                text += count + "." + message + "\n";
            }
        }

        if (count == 0) {
            return "Sorry. There is no tasks matching the keyword you give me!! :(\n";
        }

        return text;
    }

    /**
     * Marks a specific task as done.
     *
     * @param index Integer indicates the index for the task
     */
    public void markDone(int index) {
        this.tasks.get(index).markDone();
    }

    /**
     * Deletes a specific task.
     *
     * @param index Integer indicates the index for the task
     */
    public void delete(int index) {
        this.tasks.remove(index);
    }

    /**
     * Adds a task to the TaskLists. This method will automatically decide which type of the
     * task is added to the list.
     *
     * @param taskType String message indicates the task type.
     * @param task String message indicates the task info.
     * @param time String message indicates the time info.
     * @throws DukeException Throws when a task cannot be created or added to the TaskList.
     */
    public void add(String taskType, String task, String time) throws DukeException {
        Parser parser = new Parser("");

        LocalDateTime parsedTime = parser.parseTime(time);
        OperationType[] taskTypes = OperationType.values();
        for (OperationType t : taskTypes) {
            if (t.toString().equals(taskType.toUpperCase())) {
                Task newTask = t.assignTaskType(t, task, parsedTime);
                tasks.add(newTask);
                break;
            }
        }

    }

    /**
     * Returns a specific task users refer to.
     *
     * @param index An integer indicates the index of the task.
     * @return Task users refer to
     * */
    public Task get(int index) {
        return this.tasks.get(index);
    }


    /**
     * Returns the size of the TaskList
     *
     * @return Integer indicates the size of the TaskList.
     */
    public int size() {
        return this.tasks.size();
    }

    /**
     * Detects if the index taking in is invalid or not.
     *
     * @param index Integer indicates the index of the task.
     * @throws DukeException Throws when the index is invalid.
     */
    public void detectIndex(int index) throws DukeException {
        if (index < 0 || index >= this.tasks.size()) {
            throw new DukeException("OOPS!!! I'm sorry, but the index is invalid :-(");
        }
    }

    /**
     * Enum of all types of operations that is able to execute.
     * It also contains a method AssignTask Type to find the specific type of task to create.
     */
    public enum OperationType {
        BYE, DONE, DELETE, TELL, FIND, LIST, TODO, DEADLINE, EVENT;

        /**
         * Returns a task in a specific operationType. It can be either todo, deadline or event.
         *
         * @param type
         * @param task
         * @param time
         * @return Task in a specific operationType. It can be either todo, deadline or event.
         */
        public Task assignTaskType(OperationType type, String task, LocalDateTime time) {
            switch (type) {
            case TODO:
                return new ToDos(false, task);
            case DEADLINE:
                return new Deadlines(false, task, time);
            case EVENT:
                return new Events(false, task, time);
            default:
                return null;
            }
        }
    }
}
