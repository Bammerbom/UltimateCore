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
import org.spongepowered.api.CatalogTypes;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.data.type.HandTypes;
import org.spongepowered.api.effect.potion.PotionEffectType;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.item.ItemTypes;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.text.Text;

import java.util.Arrays;
import java.util.List;

public class CmdPotion implements UltimateCommand {

    @Override
    public String getName() {
        return "potion";
    }

    @Override
    public String getPermission() {
        return "uc.potion";
    }

    @Override
    public String getUsage() {
        return "/<command> <Effect> [Duration] [Amplifier] [Splash]";
    }

    @Override
    public Text getDescription() {
        return Text.of("Spawn in or modify a potion.");
    }

    @Override
    public List<String> getAliases() {
        return Arrays.asList();
    }

    @Override
    public CommandResult run(final CommandSource cs, String label, String[] args) {
        if (!r.isPlayer(cs)) {
            return CommandResult.empty();
        }
        if (!r.perm(cs, "uc.potion", true)) {
            return CommandResult.empty();
        }
        Player p = (Player) cs;
        if (!r.checkArgs(args, 0)) {
            r.sendMes(cs, "potionUsage");
            StringBuilder sb = new StringBuilder();
            for (PotionEffectType type : Sponge.getRegistry().getAllOf(CatalogTypes.POTION_EFFECT_TYPE)) {
                if (type == null || type.getName() == null) {
                    continue;
                }
                if (!(sb.length() == 0)) {
                    sb.append(", ");
                }
                sb.append(type.getName().toLowerCase().replaceAll("_", ""));
            }
            r.sendMes(cs, "potionUsage2", "%Types", sb.toString());
            return CommandResult.empty();
        }

        Boolean spawnin = p.getItemInHand(HandTypes.MAIN_HAND).orElse(ItemStack.builder().itemType(ItemTypes.NONE).build().supports(Keys.POTION_EFFECTS);
        ItemStack stack = spawnin ? p.getItemInHand(HandTypes.MAIN_HAND).orElse(ItemStack.builder().itemType(ItemTypes.NONE).build()) : ItemStack.builder().itemType(ItemTypes.POTION).build();
        PotionMeta meta = (PotionMeta) stack.getItemMeta();
        if (args[0].equalsIgnoreCase("clear")) {
            if (spawnin) {
                r.sendMes(cs, "potionClearSpawnin");
                return CommandResult.empty();
            }
            stack.setDurability(Short.parseShort("0"));
            meta.clearCustomEffects();
            stack.setItemMeta(meta);
            r.sendMes(cs, "potionClear");
            return CommandResult.empty();

        }
        PotionEffectType ef = EffectDatabase.getByName(args[0]);
        if (ef == null) {
            r.sendMes(cs, "potionEffectNotFound", "%Effect", args[0]);
            return CommandResult.empty();
        }
        Integer dur = 120;
        Integer lev = 1;
        if (r.checkArgs(args, 1)) {
            if (!r.isInt(args[1]) && !args[1].equalsIgnoreCase("splash")) {
                r.sendMes(cs, "numberFormat", "%Number", args[1]);
                return CommandResult.empty();
            } else if (r.isInt(args[1])) {
                dur = Integer.parseInt(args[1]);
            }
        }
        if (r.checkArgs(args, 2)) {
            if (!r.isInt(args[2]) && !args[1].equalsIgnoreCase("splash")) {
                r.sendMes(cs, "numberFormat", "%Number", args[2]);
                return CommandResult.empty();
            } else if (r.isInt(args[2])) {
                lev = Integer.parseInt(args[2]);
            }
        }
        lev = r.normalize(lev, 0, 999999);
        dur = r.normalize(dur, 0, 999999);
        if (lev == 0 || dur == 0) {
            if (!spawnin) {
                meta.removeCustomEffect(ef);
                if (StringUtil.nullOrEmpty(meta.getCustomEffects())) {
                    stack.setDurability(Short.parseShort("0"));
                }
                stack.setItemMeta(meta);
                r.sendMes(cs, "potionRemove");
                return CommandResult.empty();
            } else {
                lev = r.normalize(lev, 1, 999999);
                dur = r.normalize(dur, 1, 999999);
            }
        }
        PotionEffect effect = new PotionEffect(ef, dur * 20, lev - 1);
        meta.addCustomEffect(effect, true);
        meta.setMainEffect(ef);
        stack.setItemMeta(meta);
        stack.setDurability(Short.parseShort("1"));
        Potion potion = Potion.fromItemStack(stack);
        for (String str : args) {
            if (str.equalsIgnoreCase("splash")) {
                potion.setSplash(true);
            }
            if (str.equalsIgnoreCase("lingering")) {
                //TODO lingering
            }
        }
        potion.apply(stack);
        Potion potion2 = new Potion(PotionType.getByEffect(ef));
        potion2.setSplash(potion.isSplash());
        stack.setDurability(potion2.toDamageValue());
        if (PotionType.getByEffect(ef) == null) {
            if (!potion.isSplash()) {
                stack.setDurability(Short.parseShort("8232"));
            } else {
                stack.setDurability(Short.parseShort("16424"));
            }
        }
        String splash = potion.isSplash() ? r.mes("potionSplash") : r.mes("potionPotion");
        if (spawnin) {
            p.getInventory().addItem(stack);
            r.sendMes(cs, "potionGive", "%Potion", splash, "%Effect", ef.getName().toLowerCase());
        } else {
            r.sendMes(cs, "potionAdd", "%Potion", splash, "%Effect", ef.getName().toLowerCase());
        }
        return CommandResult.success();
    }

    @Override
    public List<String> onTabComplete(CommandSource cs, String alias, String[] args, String curs, Integer curn) {
        if (curn == 0) {
            List<String> l = new ArrayList<>();
            for (PotionEffectType t : PotionEffectType.values()) {
                if (t == null || t.getName() == null) {
                    continue;
                }
                l.add(t.getName().toLowerCase());
            }
            return l;
        }
        if (curn == 3) {
            return Arrays.asList("Splash");
        }
        return new ArrayList<>();
    }
}
