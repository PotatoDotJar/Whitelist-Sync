package com.potatosaucevfx.mod.commands;

import com.potatosaucevfx.mod.utils.Log;
import com.potatosaucevfx.mod.utils.WhitelistRead;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class CommandWhitelist implements ICommand {

    private final List aliases;

    public CommandWhitelist() {
        aliases = new ArrayList();
        aliases.add("wl");
    }

    @Override
    public String getCommandName() {
        return "whitelist";
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "wl";
    }

    @Override
    public List<String> getCommandAliases() {
        return aliases;
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
        World world = sender.getEntityWorld();
        if(world.isRemote) {
            System.out.println("Mod does not process on client-side!");
        }
        else {
            if(args.length > 0) {
                Log.logln(String.valueOf(args.length));
                if (args[0].equalsIgnoreCase("list")) {
                    WhitelistRead.getWhitelistUsers().forEach(user -> sender.addChatMessage(new TextComponentString(user.toString())));
                    Log.logln("List ran by " + sender.getName());
                }
            }
        }
    }

    @Override
    public boolean checkPermission(MinecraftServer server, ICommandSender sender) {
        return true;
    }

    @Override
    public List<String> getTabCompletionOptions(MinecraftServer server, ICommandSender sender, String[] args, @Nullable BlockPos pos) {
        return null;
    }

    @Override
    public boolean isUsernameIndex(String[] args, int index) {
        return false;
    }

    @Override
    public int compareTo(ICommand o) {
        return 0;
    }
}
