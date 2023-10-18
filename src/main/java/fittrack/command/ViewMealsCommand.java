package fittrack.command;

import fittrack.parser.CommandParser;

public class ViewMealsCommand extends Command {
    public static final String COMMAND_WORD = "viewmeals";
    private static final String DESCRIPTION =
            String.format("`%s` shows the list of all meals.", COMMAND_WORD);
    private static final String USAGE =
            String.format("Type `%s` to view the list of meals.", COMMAND_WORD);
    public static final String HELP = DESCRIPTION + "\n" + USAGE;

    @Override
    public CommandResult execute() {
        String feedback = "These are the meals you have consumed: \n" + mealList.toString();
        return new CommandResult(feedback);
    }

    @Override
    public void setArguments(String args, CommandParser parser) {
    }

    @Override
    protected String getHelp() {
        return HELP;
    }
}