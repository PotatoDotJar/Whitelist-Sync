package com.potatosaucevfx.mod.commands;

import com.mojang.authlib.GameProfile;
import com.potatosaucevfx.mod.utils.Log;
import com.potatosaucevfx.mod.utils.WhitelistRead;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collections;
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


    // TODO: ADD DATABASE UPDATES

    /*
    NOTES:
    isWhitelisted(Gameprofile) checks if the profile is whitelisted.


     */

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
                    WhitelistRead.getWhitelistNames().forEach(user -> sender.addChatMessage(new TextComponentString(user.toString())));
                    Log.logln("List ran by " + sender.getName());
                }
                else if (args[0].equalsIgnoreCase("add")) {
                    if(args.length > 1) {
                        server.getPlayerList().addWhitelistedPlayer(server.getPlayerProfileCache().getGameProfileForUsername(args[1]));
                        sender.addChatMessage(new TextComponentString(args[1] + " added to the whitelist."));
                    }
                    else {
                        sender.addChatMessage(new TextComponentString("You must specify a name to add to the whitelist!"));
                    }
                }
                else if (args[0].equalsIgnoreCase("remove")) {
                    if(args.length > 1) {
                        GameProfile gameprofile = server.getPlayerList().getWhitelistedPlayers().getByName(args[1]);
                        if(gameprofile != null) {
                            server.getPlayerList().removePlayerFromWhitelist(gameprofile);
                            sender.addChatMessage(new TextComponentString(args[1] + " removed from the whitelist."));

                        }
                        else {
                            sender.addChatMessage(new TextComponentString("You must specify a valid name to remove from the whitelist!"));
                        }
                    }
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
        if (args.length == 1) {
            return CommandBase.getListOfStringsMatchingLastWord(args, new String[]{"list", "add", "remove"});
        } else {
            if (args.length == 2) {
                if ("remove".equals(args[0])) {
                    return CommandBase.getListOfStringsMatchingLastWord(args, server.getPlayerList().getWhitelistedPlayerNames());
                }

                if ("add".equals(args[0])) {
                    return CommandBase.getListOfStringsMatchingLastWord(args, server.getPlayerProfileCache().getUsernames());
                }
            }
            return Collections.emptyList();
        }
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
