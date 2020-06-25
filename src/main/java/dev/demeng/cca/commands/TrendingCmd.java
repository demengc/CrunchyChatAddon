package dev.demeng.cca.commands;

import com.demeng7215.demlib.api.CustomCommand;
import com.demeng7215.demlib.api.messages.MessageUtils;
import dev.demeng.cca.CrunchyChatAddon;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class TrendingCmd extends CustomCommand {

  private CrunchyChatAddon i;

  public TrendingCmd(CrunchyChatAddon i) {
    super("trending");

    this.i = i;

    setDescription("Displays trending topics on the server.");
  }

  @Override
  protected void run(CommandSender sender, String[] args) {

    final Map<String, Integer> top = i.getHashtagManager().getTopHashtags();

    final List<String> tags = new ArrayList<>(top.keySet());
    final List<Integer> counts = new ArrayList<>(top.values());

    while (tags.size() < 5) {
      tags.add("Undetermined");
      counts.add(-1);
    }

    for (String msg : i.getSettings().getStringList("hashtags.trending.message")) {
      MessageUtils.tellWithoutPrefix(
          sender,
          msg.replace("%first%", format(tags.get(0), counts.get(0)))
              .replace("%second%", format(tags.get(1), counts.get(1)))
              .replace("%third%", format(tags.get(2), counts.get(2)))
              .replace("%fourth%", format(tags.get(3), counts.get(3)))
              .replace("%fifth%", format(tags.get(4), counts.get(4))));
    }
  }

  private String format(String tag, int count) {

    if (tag.equals("Undetermined")) {
      return "Undetermined";
    }

    return i.getSettings()
        .getString("hashtags.trending.hashtag-format")
        .replace("%hashtag%", tag)
        .replace("%count%", count + "");
  }
}
