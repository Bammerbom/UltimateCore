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
package bammerbom.ultimatecore.sponge.modules.jail.commands;

import bammerbom.ultimatecore.sponge.UltimateCore;
import bammerbom.ultimatecore.sponge.api.command.Command;
import bammerbom.ultimatecore.sponge.api.data.GlobalData;
import bammerbom.ultimatecore.sponge.api.module.Module;
import bammerbom.ultimatecore.sponge.api.module.Modules;
import bammerbom.ultimatecore.sponge.api.permission.Permission;
import bammerbom.ultimatecore.sponge.api.teleport.Teleportation;
import bammerbom.ultimatecore.sponge.modules.jail.api.Jail;
import bammerbom.ultimatecore.sponge.modules.jail.api.JailKeys;
import bammerbom.ultimatecore.sponge.modules.jail.api.JailPermissions;
import bammerbom.ultimatecore.sponge.utils.Messages;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.entity.living.player.Player;

import java.util.Arrays;
import java.util.List;

public class JailtpCommand implements Command {
    @Override
    public Module getModule() {
        return Modules.JAIL.get();
    }

    @Override
    public String getIdentifier() {
        return "jailtp";
    }

    @Override
    public Permission getPermission() {
        return JailPermissions.UC_JAIL_JAILTP_BASE;
    }

    @Override
    public List<Permission> getPermissions() {
        return Arrays.asList(JailPermissions.UC_JAIL_DELJAIL_BASE);
    }

    @Override
    public List<String> getAliases() {
        return Arrays.asList("jailtp", "jailteleport", "tpjail", "teleportjail");
    }

    @Override
    public CommandResult run(CommandSource sender, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(Messages.getFormatted(sender, "core.noplayer", "%source%", sender.getName()));
            return CommandResult.empty();
        }
        if (!sender.hasPermission(JailPermissions.UC_JAIL_JAILTP_BASE.get())) {
            sender.sendMessage(Messages.getFormatted(sender, "core.nopermissions"));
            return CommandResult.empty();
        }
        Player p = (Player) sender;
        if (args.length == 0) {
            sender.sendMessage(getUsage());
            return CommandResult.empty();
        }
        String name = args[0].toLowerCase();
        //Remove jail
        List<Jail> jails = GlobalData.get(JailKeys.JAILS).get();
        Jail jail = jails.stream().filter(jail1 -> jail1.getName().equalsIgnoreCase(name)).findAny().orElse(null);
        if (jail == null) {
            sender.sendMessage(Messages.getFormatted(sender, "jail.notfound", "%jail%", args[0]));
            return CommandResult.empty();
        }

        Teleportation tp = UltimateCore.get().getTeleportService().createTeleportation(p, Arrays.asList(p), jail.getLocation(), tel -> {
        }, (tel, reason) -> {
        }, false, false);
        tp.start();

        sender.sendMessage(Messages.getFormatted(sender, "jail.command.jailtp.success", "%jail%", jail.getName()));
        return CommandResult.success();
    }

    @Override
    public List<String> onTabComplete(CommandSource sender, String[] args, String curs, Integer curn) {
        return null;
    }
}
