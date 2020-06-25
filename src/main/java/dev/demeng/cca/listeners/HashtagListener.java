package dev.demeng.cca.listeners;

import com.demeng7215.demlib.api.messages.MessageUtils;
import dev.demeng.cca.CrunchyChatAddon;
import org.apache.commons.lang.StringUtils;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class HashtagListener implements Listener {

  private final CrunchyChatAddon i;

  public HashtagListener(CrunchyChatAddon i) {
    this.i = i;
  }

  @EventHandler(priority = EventPriority.HIGHEST)
  public void onPlayerChat(AsyncPlayerChatEvent e) {

    if (!i.getSettings().getBoolean("hashtags.enabled")) {
      return;
    }

    String finalMessage;
    final String msg = e.getMessage();

    if (e.isCancelled() || msg.startsWith("/")) {
      return;
    }

    final List<String> words = Arrays.asList(msg.split(" "));

    final List<String> tags = new ArrayList<>(words);
    tags.removeIf(s -> s.equals("#") || !s.startsWith("#") || s.replace("#", "").isEmpty());

    if (tags.isEmpty()) {
      return;
    }

    final List<String> filteredTags = new ArrayList<>(tags);
    for (int i = 0; i < tags.size(); i++) {
      if (tags.get(i).startsWith("##")) {
        filteredTags.set(i, "##" + tags.get(i).toLowerCase());
      } else {
        filteredTags.set(i, "#" + tags.get(i).replace("#", "").toLowerCase());
      }
    }

    finalMessage = msg;

    final List<String> coloredTags = new ArrayList<>();

    for (String s : filteredTags) {
      if (s.startsWith("##")) {
        coloredTags.add("#" + s.replace("#", ""));
      } else {
        coloredTags.add(
            MessageUtils.colorize(
                i.getSettings().getString("hashtags.highlight-color") + s + "&r"));
        i.getHashtagManager().addHashtag(s);
      }
    }

    finalMessage =
        StringUtils.replaceEach(
            finalMessage, tags.toArray(new String[0]), coloredTags.toArray(new String[0]));

    e.setMessage(finalMessage);
  }
}
