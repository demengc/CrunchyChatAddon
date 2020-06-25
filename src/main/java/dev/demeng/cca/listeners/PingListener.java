package dev.demeng.cca.listeners;

import com.demeng7215.demlib.api.messages.MessageUtils;
import dev.demeng.cca.CrunchyChatAddon;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class PingListener implements Listener {

  private final CrunchyChatAddon i;

  public PingListener(CrunchyChatAddon i) {
    this.i = i;
  }

  @EventHandler(priority = EventPriority.HIGHEST)
  public void onPlayerChat(AsyncPlayerChatEvent e) {

    if (!i.getSettings().getBoolean("mentions.enabled")) {
      return;
    }

    String finalMessage;
    final String msg = e.getMessage();

    if (e.isCancelled() || msg.startsWith("/")) {
      return;
    }

    final List<Player> mentioned = new ArrayList<>();

    for(Player p : Bukkit.getOnlinePlayers()) {
      for(String word : msg.split(" ")) {
        if(word.toLowerCase().contains("@" + p.getName().toLowerCase())) {
          mentioned.add(p);
        }
      }
    }

    if (mentioned.isEmpty()) {
      return;
    }

    finalMessage = msg;

    final List<String> rawMentions = new ArrayList<>();

    for (Player mention : mentioned) {
      rawMentions.add("@" + mention.getName());
      rawMentions.add("@" + mention.getName().toLowerCase());
    }

    final List<String> coloredMentions = new ArrayList<>();

    for (String s : rawMentions) {
      coloredMentions.add(
          MessageUtils.colorize(
              i.getSettings().getString("mentions.highlight-color")
                  + Bukkit.getPlayerExact(s.replace("@", "")).getName()
                  + "&r"));
    }

    finalMessage =
        StringUtils.replaceEach(
            finalMessage,
            rawMentions.toArray(new String[0]),
            coloredMentions.toArray(new String[0]));

    Bukkit.getScheduler()
        .runTask(
            i,
            () -> {
              for (Player p : mentioned) {
                p.playSound(
                    p.getLocation(),
                    Sound.valueOf(i.getSettings().getString("mentions.sound")),
                    100F,
                    100F);
              }
            });

    e.setMessage(finalMessage);
  }
}
