package duke.command;

import duke.exceptions.DukeException;
import duke.task.TaskList;
import duke.uimanager.TextUi;

/**
 * @@author Hang Zelin
 *
 * DeleteCommand will handle the situation when a user wants to delete a task.
 */
public class DeleteCommand extends Command{
    private final TaskList taskList;
    private final TextUi textUi;
    private final int index;

    /**
     * Constructor for DeleteCommand class.
     *
     * @param taskList TaskList in Duke.
     * @param textUi Duke's UI.
     * @param index Index for the specific task.
     */
    public DeleteCommand(TaskList taskList, TextUi textUi, int index){
        this.taskList = taskList;
        this.textUi = textUi;
        this.index = index;
    }

    /**
     * Return Duke's response when user wants to delete a task.
     *
     * @return Duke's response.
     */
    @Override
    public String returnResponse() {
        return delete(index);
    }

    private String delete(int index) {
        String text;
        try {
            taskList.detectIndex(index);
            text = textUi.deleteUi(taskList.get(index).getTaskStatus(), taskList.size() - 1);
            taskList.delete(index);
        } catch (DukeException e) {
            text = e.getErrorMessage();
        }
        return text;
    }
}
