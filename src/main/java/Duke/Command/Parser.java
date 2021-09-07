/**
 * @@author Hang Zelin
 *
 * Parser will take in a full input Message and take out the operation type, task, time, index from the
 * one line command input by users.
 * It can also deal with the local saved data and return the parsed Message, which can be a task, time, done(or not).
 * It can also parse the time users input into the LocalDateTime.
 * Some invalid input Messages may cause throwing DukeException.
 */

package duke.command;

import duke.excpetions.DukeException;
import duke.task.TaskList;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;



public class Parser {
    private String message;

    /**
     * @param message Message users take in to be parsed.
     */
    public Parser(String message) {
        this.message = message;
    }


    private static boolean isValidDate(int day, int month, int year, int hour, int minute) {
        if (((year % 4 == 0 && year % 100 != 0) || (year % 400 == 0)) && month == 2) {
            if (day > 29 || day <= 0) {
                return false;
            }
        } else if (month == 2) {
            if (day > 28 || day <= 0) {
                return false;
            }
        }

        if (month <= 0 || month > 12) {
            return false;
        }

        if (month == 1 || month == 3 || month == 5 || month == 7 || month == 8 || month == 10 || month == 12) {
            if (day > 31 || day <= 0) {
                return false;
            }
        } else if (month != 2) {
            if (day > 30 || day <= 0) {
                return false;
            }
        }

        if (hour > 24 || hour < 0) {
            return false;
        }

        if (minute > 60 || minute < 0) {
            return false;
        }

        return true;
    }

    /**
     * Returns a LocalDateTime type that encapsulates the year, month, day, hour, minute of a time input.
     * The method takes in a String of time and convert into LocalDateTime type.
     * The format can only be: 1. dd/mm/yyyy hhmm
     * 2. yyyy-mm-dd
     *
     * @param time Time User takes in to be parsed.
     * @return Parsed time converted in the type of LocalDateTime
     */
    public LocalDateTime parseTime(String time) {
        LocalDateTime parsedTime;
        int day;
        int month;
        int year;
        int hour;
        int minute;

        if (time.contains("/") && time.indexOf("/", 3) != -1 && time.contains(" ") && !time.contains("-")) {
            int endIndex1 = time.indexOf("/");
            int endIndex2 = time.lastIndexOf(" ");
            day = Integer.parseInt(time.substring(0, endIndex1));

            Integer dayInteger = day;
            int endIndex3 = time.indexOf("/", dayInteger.toString().length() + 1);
            month = Integer.parseInt(time.substring(endIndex1 + 1, endIndex3));
            year = Integer.parseInt(time.substring(endIndex3 + 1, endIndex2));

            hour = Integer.parseInt(time.substring(endIndex2 + 1).substring(0, 2));
            minute = Integer.parseInt(time.substring(endIndex2 + 1).substring(2));
        } else if (time.contains("-")) {
            try {
                parsedTime = LocalDate.parse(time).atTime(0, 0);
                return parsedTime;
            } catch (DateTimeParseException e) {
                return null;
            }
        } else {
            return null;
        }

        //Some Other cases;
        if (!isValidDate(day, month, year, hour, minute)) {
            return null;
        } else {
            parsedTime = LocalDate.of(year, month, day).atTime(hour, minute);
        }


        return parsedTime;
    }

    /**
     * Returns a String which is a task info in a local save data.
     * Noted: you must specify it as local data, otherwise it can go wrong.
     *
     * @return Task retrieved from save data.
     */
    public String getSaveTask() {
        String task;
        char taskType = message.charAt(0);
        if (taskType == 'D' || taskType == 'E') {
            task = message.substring(8, message.indexOf("|", 8) - 1);
        } else {
            task = message.substring(8);
        }

        return task;
    }

    /**
     * Returns a String which is a time info in a local save data.
     * Noted: you must specify it as local data, otherwise it can go wrong.
     *
     * @return Time retrieved from save data.
     */
    public String getSaveTime() {
        String time;

        char taskType = message.charAt(0);
        if ((taskType == 'D' || taskType == 'E') && message.contains("/")) {
            time = message.substring(message.lastIndexOf("|") + 2);
        } else {
            time = "";
        }
        return time;
    }

    /**
     * Returns a String which is an operation type in a line of command.
     *
     * @return Operation type parsed from users' one line of command.
     * @throws DukeException Throws when the operation type does not belong to any one of the types that
     * duke can do.
     */
    public String getOperationType() throws DukeException {
        String operationType;
        if (message.contains(" ")) {
            operationType = message.substring(0, message.indexOf(" "));
        } else {
            operationType = message;
        }

        //If the task type does not belong to the three types, throw an error.
        TaskList.OperationType[] operationTypes = TaskList.OperationType.values();
        for (TaskList.OperationType o : operationTypes) {
            if (message.startsWith(o.toString())) {
                return operationType;
            }
        }
        throw new DukeException("OOPS!!! I'm sorry, but I don't know what that means :-(");

    }

    /**
     * Returns a String which is task info in a line of command.
     *
     * @return Operation type parsed from users' one line of command.
     * @throws DukeException Throws when the task info cannot be retrieved from users' one line of command.
     */
    public String getTask() throws DukeException {
        String task = "";

        if (message.startsWith("deadline") || message.startsWith("event") || message.startsWith("todo")
                || message.startsWith("find")) {
            //Get Task description and time if it has it.
            if (message.contains("/")) {
                task = message.substring(message.indexOf(" ") + 1, message.indexOf("/") - 1);
            } else {
                if (!message.contains(" ")) {
                    throw new DukeException("OOPS!!! The description of a " + message + " cannot be empty.");
                } else {
                    task = message.substring(message.indexOf(" ") + 1);
                }
            }
        }

        return task;
    }

    /**
     * Returns a String which is time info in a line of command.
     *
     * @return Time parsed from users' one line of command.
     * @throws DukeException Throws when users' the time cannot be parsed out or the parsed out time does not
     * fit the format for a specific task type.
     */
    public String getTime() throws DukeException {
        String time = "";

        //throw exceptions for deadline or events' format.
        if (message.startsWith("todo") || message.startsWith("deadline") || message.startsWith("event")) {
            if (message.contains("/")) {
                if (message.startsWith("deadline")) {
                    if (message.contains("/by")) {
                        time = message.substring(message.indexOf("/by") + 4);
                    } else {
                        throw new DukeException("OOPS!!! I'm sorry, but the format of deadline is wrong :-(");
                    }
                } else if (message.startsWith("event")) {
                    if (message.contains("/at")) {
                        time = message.substring(message.indexOf("/at") + 4);
                    } else {
                        throw new DukeException("OOPS!!! I'm sorry, but the format of event is wrong :-(");
                    }
                } else {
                    throw new DukeException("OOPS!!! I'm sorry, but the format of todo is wrong :-(");
                }
            }
        } else if (message.startsWith("tell")) {
            if (!message.contains(" ")) {
                throw new DukeException("OOPS!!! I'm sorry, but the format of tell is wrong :-(");
            } else {
                time = message.substring(message.indexOf(" ") + 1);
            }
        }

        //Time for deadlines or event cannot be empty.
        if ((message.startsWith("event") || message.startsWith("deadline")
                || message.startsWith("tell")) && time.equals("")) {
            throw new DukeException("OOPS!!! The time of a "
                    + message.substring(0, message.indexOf(" ")) + " cannot be empty.");
        }

        return time;
    }

    /**
     * return a String which is index info in a line of command.
     * Noted: It is possible that index does not exist. This method will only be applicable for "tell", "find",
     * "done" and "delete" operation type.
     *
     * @return Index parsed from users' one line of command if it contains an index.
     */
    public Integer getIndex() {
        int index = (message.contains(" ") && (message.startsWith("done") || message.startsWith("delete")))
                ? Integer.parseInt(message.substring(message.indexOf(" ") + 1)) - 1
                : -1;

        return index;
    }
}
