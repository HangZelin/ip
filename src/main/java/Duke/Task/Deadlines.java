/**
 * @author Hang Zelin
 *
 * Deadlines class that extends Task class. It is one of the types in 3 tasks.
 * Will contain a time in the form of "/by"
 */
package duke.task;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class Deadlines extends Task {

    private boolean done;
    private String task;
    private LocalDateTime time;
    private String taskType = "D";

    public Deadlines(boolean done, String task, LocalDateTime time) {
        this.done = done;
        this.task = task;
        this.time = time;
    }

    @Override
    public String getTaskInfo() {
        String done_str = "";
        if (!this.done) {
            done_str = " ";
        } else {
            done_str = "X";
        }

        return "[" + taskType + "]" + "[" + done_str + "] " + task + " (by: " + parsedTime() + ")";
    }

    @Override
    public void markDone() {
        this.done = true;
    }

    @Override
    public String parsedTime() {
        String parsedTime = "";
        if (this.time != null) {
            parsedTime = this.time.format(DateTimeFormatter.ofPattern("MMM dd yyyy HH:mm", Locale.ENGLISH));
        } else {
            parsedTime = "I don't know the time.";
        }

        return parsedTime;
    }

    @Override
    public String getTime() {
        if (this.time == null) {
            return "I don't know the time";
        }
        return this.time.getDayOfMonth() + "/" + this.time.getMonthValue() + "/" + this.time.getYear() + " " +
                ((this.time.getHour() < 10) ? "0" + this.time.getHour()
                        : this.time.getHour())
                + ((this.time.getMinute() < 10) ? "0" + this.time.getMinute()
                : this.time.getMinute());
    }


    @Override
    public String getSaveDataInfo() {
        return this.taskType + " | " + (this.done ? 1 : 0) + " | " + task + " | " + getTime();
    }
}
