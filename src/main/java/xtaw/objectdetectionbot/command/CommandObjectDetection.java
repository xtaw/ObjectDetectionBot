package xtaw.objectdetectionbot.command;

import java.util.List;

import net.mamoe.mirai.console.command.CompositeCommand;
import net.mamoe.mirai.console.command.ConsoleCommandSender;
import net.mamoe.mirai.console.command.descriptor.CommandArgumentContext;
import xtaw.objectdetectionbot.config.Config;
import xtaw.objectdetectionbot.main.MainPlugin;

public class CommandObjectDetection extends CompositeCommand {

	public static final CommandObjectDetection INSTANCE = new CommandObjectDetection();

	private CommandObjectDetection() {
		super(MainPlugin.INSTANCE, "objectdetection", new String[] { "od" }, "Commands for Object Detection.", MainPlugin.INSTANCE.getParentPermission(), CommandArgumentContext.EMPTY);
	}

	@SubCommand({ "bot" })
	@Description("Modify the bots that work.")
	public void modifyBot(ConsoleCommandSender sender, @Name("add/remove") String action, @Name("id") long id) {
		List<Long> bots = Config.INSTANCE.bots;
		if (action.equalsIgnoreCase("add") || action.equalsIgnoreCase("a")) {
			if (bots.contains(id)) {
				sender.sendMessage("Bot " + id + " already exists.");
				return;
			}
			bots.add(id);
			Config.INSTANCE.save();
			sender.sendMessage("Bot " + id + " was added successfully");
		} else if (action.equalsIgnoreCase("remove") || action.equalsIgnoreCase("r")) {
			if (!bots.contains(id)) {
				sender.sendMessage("Bot " + id + " doesn't exist.");
				return;
			}
			bots.remove(id);
			Config.INSTANCE.save();
			sender.sendMessage("Bot " + id + " was removed successfully");
		} else {
			sender.sendMessage("Invalid action.");
		}
	}

	@SubCommand({ "group" })
	@Description("Modify the groups that the bots work in.")
	public void modifyGroup(ConsoleCommandSender sender, @Name("add/remove") String action, @Name("id") long id) {
		List<Long> groups = Config.INSTANCE.groups;
		if (action.equalsIgnoreCase("add") || action.equalsIgnoreCase("a")) {
			if (groups.contains(id)) {
				sender.sendMessage("Group " + id + " already exists.");
				return;
			}
			groups.add(id);
			Config.INSTANCE.save();
			sender.sendMessage("Group " + id + " was added successfully");
		} else if (action.equalsIgnoreCase("remove") || action.equalsIgnoreCase("r")) {
			if (!groups.contains(id)) {
				sender.sendMessage("Group " + id + " doesn't exist.");
				return;
			}
			groups.remove(id);
			Config.INSTANCE.save();
			sender.sendMessage("Group " + id + " was removed successfully");
		} else {
			sender.sendMessage("Invalid action.");
		}
	}

	@SubCommand({ "format" })
	@Description("Set the message's format.")
	public void format(ConsoleCommandSender sender, @Name("format") String format) {
		Config.INSTANCE.format = format;
		Config.INSTANCE.save();
		sender.sendMessage("Done.");
	}

	@SubCommand({ "list" })
	@Description("List the bots and the groups.")
	public void list(ConsoleCommandSender sender) {
		List<Long> bots = Config.INSTANCE.bots;
		List<Long> groups = Config.INSTANCE.groups;
		sender.sendMessage("Bot list: ");
		sender.sendMessage("--------------------");
		if (bots.isEmpty()) {
			sender.sendMessage("Empty.");
		} else {
			for (int i = 0; i < bots.size(); i++) {
				sender.sendMessage("Bot #" + (i + 1) + " " + bots.get(i));
			}
		}
		sender.sendMessage("--------------------");
		sender.sendMessage("");
		sender.sendMessage("Group list: ");
		sender.sendMessage("--------------------");
		if (groups.isEmpty()) {
			sender.sendMessage("Empty.");
		} else {
			for (int i = 0; i < groups.size(); i++) {
				sender.sendMessage("Group #" + (i + 1) + " " + groups.get(i));
			}
		}
		sender.sendMessage("--------------------");
	}

	@SubCommand({ "reload" })
	@Description("Reload the config.")
	public void reload(ConsoleCommandSender sender) {
		Config.INSTANCE.load();
		sender.sendMessage("Done.");
	}

}