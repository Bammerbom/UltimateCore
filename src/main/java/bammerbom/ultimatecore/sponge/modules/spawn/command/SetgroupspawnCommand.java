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
package bammerbom.ultimatecore.sponge.modules.spawn.command;

import bammerbom.ultimatecore.sponge.api.command.Arguments;
import bammerbom.ultimatecore.sponge.api.command.CommandInfo;
import bammerbom.ultimatecore.sponge.api.command.SmartCommand;
import bammerbom.ultimatecore.sponge.api.command.arguments.StringArgument;
import bammerbom.ultimatecore.sponge.api.data.GlobalData;
import bammerbom.ultimatecore.sponge.api.permission.Permission;
import bammerbom.ultimatecore.sponge.modules.spawn.SpawnModule;
import bammerbom.ultimatecore.sponge.modules.spawn.api.SpawnKeys;
import bammerbom.ultimatecore.sponge.modules.spawn.api.SpawnPermissions;
import bammerbom.ultimatecore.sponge.utils.Messages;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.args.CommandElement;
import org.spongepowered.api.entity.Transform;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.world.World;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

@CommandInfo(module = SpawnModule.class, aliases = {"setgroupspawn"})
public class SetgroupspawnCommand implements SmartCommand {

    @Override
    public Permission getPermission() {
        return SpawnPermissions.UC_SPAWN_SETGROUPSPAWN_BASE;
    }

    @Override
    public List<Permission> getPermissions() {
        return Arrays.asList(SpawnPermissions.UC_SPAWN_SETGROUPSPAWN_BASE);
    }

    @Override
    public CommandElement[] getArguments() {
        return new CommandElement[]{
                Arguments.builder(new StringArgument(Text.of("group"))).onlyOne().build()
        };
    }

    @Override
    public CommandResult execute(CommandSource sender, CommandContext args) throws CommandException {
        if (!(sender instanceof Player)) {
            sender.sendMessage(Messages.getFormatted(sender, "core.noplayer"));
            return CommandResult.empty();
        }
        if (!sender.hasPermission(SpawnPermissions.UC_SPAWN_SETGROUPSPAWN_BASE.get())) {
            sender.sendMessage(Messages.getFormatted(sender, "core.nopermissions"));
            return CommandResult.empty();
        }
        checkIfPlayer(sender);
        checkPermission(sender, SpawnPermissions.UC_SPAWN_SETGROUPSPAWN_BASE);
        String group = args.<String>getOne("group").get();
        Player p = (Player) sender;

        HashMap<String, Transform<World>> groupspawns = GlobalData.get(SpawnKeys.GROUP_SPAWNS).get();
        groupspawns.put(group, p.getTransform());
        GlobalData.offer(SpawnKeys.GROUP_SPAWNS, groupspawns);
        p.sendMessage(Messages.getFormatted(p, "spawn.command.setgroupspawn.success", "%group%", group));
        return CommandResult.success();
    }
}
