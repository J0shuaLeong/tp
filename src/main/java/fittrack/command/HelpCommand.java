package fittrack.command;

import fittrack.parser.CommandParser;

import static fittrack.parser.CommandParser.ALL_COMMAND_WORDS;

public class HelpCommand extends Command {

    public static final String COMMAND_WORD = "help";
    private static final String DESCRIPTION =
            String.format("`%s` shows help message of the command.", COMMAND_WORD);
    private static final String KNOWN_COMMANDS = "Existing commands:\n" + ALL_COMMAND_WORDS;
    private static final String USAGE =
            String.format("Type `%s` or `%s <COMMAND>` to view help.", COMMAND_WORD, COMMAND_WORD);
    private static final String HELP = DESCRIPTION + "\n" + KNOWN_COMMANDS + "\n" + USAGE;

    private static final String MESSAGE_INVALID_COMMAND = "`%s` is an invalid command.\n" + USAGE;

    private String helpMessage;

    @Override
    public CommandResult execute() {
        return new CommandResult(helpMessage);
    }

    @Override
    public void setArguments(String args, CommandParser parser) {
        String word = parser.getFirstWord(args);
        if (word.isEmpty()) {
            helpMessage = getHelp();
            return;
        }

        Command blankCommand = parser.getBlankCommand(word);
        if (blankCommand instanceof InvalidCommand) {
            helpMessage = String.format(MESSAGE_INVALID_COMMAND, word);
            return;
        }

        helpMessage = blankCommand.getHelp();
    }

    @Override
    protected String getHelp() {
        return HELP;
    }
}
