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
package bammerbom.ultimatecore.sponge.impl.user;

import bammerbom.ultimatecore.sponge.api.user.UCUser;
import bammerbom.ultimatecore.sponge.api.user.UserService;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.entity.living.player.User;
import org.spongepowered.api.profile.GameProfile;
import org.spongepowered.api.service.user.UserStorageService;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

public class UCUserService implements UserService {

    /**
     * Retrieve an {@link UCUser} by the user's uuid.
     *
     * @param uuid The uuid of the user to get
     * @return The user, or Optional.empty() if not found
     */
    @Override
    public Optional<UCUser> getUser(UUID uuid) {
        Optional<User> user = Sponge.getServiceManager().provideUnchecked(UserStorageService.class).get(uuid);
        if (!user.isPresent()) {
            return Optional.empty();
        }
        UCUser uuser = new UCUser(user.get());
        return Optional.of(uuser);
    }

    /**
     * Retrieve an {@link UCUser} by the user's gameprofile.
     *
     * @param profile The gameprofile of the user to get
     * @return The UCUser, or Optional.empty() if not found
     */
    @Override
    public Optional<UCUser> getUser(GameProfile profile) {
        return getUser(profile.getUniqueId());
    }

    /**
     * Retrieve an {@link UCUser} by the user's User instance.
     *
     * @param user The User instance of the user to get
     * @return The UCUser, or Optional.empty() if not found
     */
    @Override
    public UCUser getUser(User user) {
        UCUser ucuser = new UCUser(user);
        return ucuser;
    }

    /**
     * Retrieve a list of all online {@link UCUser}s
     */
    @Override
    public List<UCUser> getOnlinePlayers() {
        return Sponge.getServer().getOnlinePlayers().stream().map(this::getUser).collect(Collectors.toList());
    }
}
