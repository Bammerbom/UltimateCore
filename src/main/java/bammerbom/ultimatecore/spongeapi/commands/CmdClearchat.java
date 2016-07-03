/*
 * This file is part of UltimateCore, licensed under the MIT License (MIT).
 *
 * Copyright (c) Bammerbom
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package bammerbom.ultimatecore.spongeapi.commands;

import bammerbom.ultimatecore.spongeapi.UltimateCommand;
import bammerbom.ultimatecore.spongeapi.r;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;

import java.util.Arrays;
import java.util.List;

public class CmdClearchat implements UltimateCommand {

    @Override
    public String getName() {
        return "clearchat";
    }

    @Override
    public String getPermission() {
        return "uc.clearchat";
    }

    @Override
    public String getUsage() {
        return "/<command> [Player]";
    }

    @Override
    public Text getDescription() {
        return Text.of("Removes all messages from the chat.");
    }

    @Override
    public List<String> getAliases() {
        return Arrays.asList("cleanchat");
    }

    @Override
    public CommandResult run(final CommandSource cs, String label, String[] args) {
        if (!r.checkArgs(args, 0)) {
            if (!r.perm(cs, "uc.clearchat", true) && !r.perm(cs, "uc.clearchat.everyone", true)) {
                r.sendMes(cs, "noPermissions");
                return CommandResult.empty();
            }
            for (Player p : r.getOnlinePlayers()) {
                for (int i = 0; i < 100; i++) {
                    p.sendMessage(Text.of(""));
                }
            }
        } else {
            if (!r.searchPlayer(args[0]).isPresent()) {
                r.sendMes(cs, "playerNotFound", "%Player", args[0]);
                return CommandResult.empty();
            }
            Player pl = r.searchPlayer(args[0]).get();
            if (!r.perm(cs, "uc.clearchat", true) && !r.perm(cs, "uc.clearchat.player.others", true) && !(pl.equals(cs) && r.perm(cs, "uc.clearchat.player", true))) {
                r.sendMes(cs, "noPermissions");
                return CommandResult.empty();
            }
            for (int i = 0; i < 100; i++) {
                pl.sendMessage(Text.of(""));
            }
        }
        return CommandResult.success();
    }

    @Override
    public List<String> onTabComplete(CommandSource cs, String alias, String[] args, String curs, Integer curn) {
        return null;
    }
}
