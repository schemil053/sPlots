package de.emilschlampp.plots.utils;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class PlayerQuitClearList {
    private List<UUID> data = new ArrayList<>();

    public PlayerQuitClearList() {

    }

    public PlayerQuitClearList(boolean register) {
        if(register) {
            register();
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PlayerQuitClearList that = (PlayerQuitClearList) o;
        return Objects.equals(data, that.data);
    }

    public void remove(Player player) {
        while (data.remove(player.getUniqueId()));
    }

    public void add(Player player) {
        data.add(player.getUniqueId());
    }

    public boolean contains(Player player) {
        return data.contains(player.getUniqueId());
    }

    private boolean registered = false;

    public void register() {
        if(registered) {
            return;
        }
        registered = true;
        EListen.lists.add(this);
    }

    public void unregister() {
        if(!registered) {
            return;
        }
        registered = false;
        while(EListen.lists.remove(this));
    }

    @Override
    public int hashCode() {
        return Objects.hash(data);
    }

    public static class EListen implements Listener{
        private static List<PlayerQuitClearList> lists = new ArrayList<>();

        @EventHandler
        public void onQuit(PlayerQuitEvent event) {
            for(PlayerQuitClearList list : lists) {
                list.remove(event.getPlayer());
            }
        }
    }
}
