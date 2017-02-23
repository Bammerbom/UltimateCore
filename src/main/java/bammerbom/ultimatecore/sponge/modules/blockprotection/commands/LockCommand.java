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
package bammerbom.ultimatecore.sponge.modules.blockprotection.commands;

import bammerbom.ultimatecore.sponge.api.command.HighPermCommand;
import bammerbom.ultimatecore.sponge.api.command.annotations.CommandInfo;
import bammerbom.ultimatecore.sponge.api.command.annotations.CommandPermissions;
import bammerbom.ultimatecore.sponge.api.command.selectiontask.selectiontasks.BlockSelectionTask;
import bammerbom.ultimatecore.sponge.api.language.utils.Messages;
import bammerbom.ultimatecore.sponge.api.permission.PermissionLevel;
import bammerbom.ultimatecore.sponge.modules.blockprotection.BlockprotectionModule;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.args.CommandElement;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

import java.util.ArrayList;
import java.util.List;

@CommandInfo(module = BlockprotectionModule.class, aliases = {"lock", "protect", "claim"})
@CommandPermissions(level = PermissionLevel.EVERYONE)
public class LockCommand implements HighPermCommand {
    @Override
    public CommandElement[] getArguments() {
        return new CommandElement[]{};
    }

    @Override
    public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
        checkIfPlayer(src);
        Player p = (Player) src;
        BlockSelectionTask task = new BlockSelectionTask();
        task.select(p, (loc) -> true, (loc) -> {
            List<Location<World>> locs = new ArrayList<>();
            locs.add(loc);
            //TODO better Key, this also works with redstone etc
            Messages.log(loc.getPosition());
            Messages.log(loc.getKeys());
            Messages.log("--");
            loc.getBlock().getTraitMap().forEach((blockTrait, o) -> Messages.log(blockTrait + " - " + o));

            //Messages.log("--");
            //locs.forEach(l -> Messages.log(l.getPosition()));
        });
        return CommandResult.success();
    }
}
