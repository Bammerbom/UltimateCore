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
package bammerbom.ultimatecore.sponge.impl.teleport;

import bammerbom.ultimatecore.sponge.api.teleport.TeleportRequest;
import bammerbom.ultimatecore.sponge.api.teleport.TeleportService;
import org.spongepowered.api.entity.Entity;
import org.spongepowered.api.entity.Transform;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class UCTeleportService implements TeleportService {
    private ArrayList<Consumer<TeleportRequest>> handlers = new ArrayList<>();

    public TeleportRequest createTeleportRequest(List<Entity> entities, Transform target, Consumer<TeleportRequest> complete) {
        return new UCTeleportRequest(entities, target, complete);
    }

    public void addHandler(Consumer<TeleportRequest> consumer) {
        handlers.add(consumer);
    }

    public List<Consumer<TeleportRequest>> getHandlers() {
        return handlers;
    }

    public void removeHandler(Consumer<TeleportRequest> handler) {
        handlers.remove(handler);
    }
}
