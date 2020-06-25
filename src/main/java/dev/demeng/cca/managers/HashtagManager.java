package dev.demeng.cca.managers;

import dev.demeng.cca.CrunchyChatAddon;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class HashtagManager {

  private CrunchyChatAddon i;

  public HashtagManager(CrunchyChatAddon i) {
    this.i = i;
  }

  public void addHashtag(String tag) {

    i.getData().set("hashtags." + tag, i.getData().getInt("hashtags." + tag, 0) + 1);

    try {
      i.getDataFile().saveConfig();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public Map<String, Integer> getTopHashtags() {

    final Map<String, Integer> unsorted = new HashMap<>();

    for (String key : i.getData().getConfigurationSection("hashtags").getKeys(false)) {
      unsorted.put(key, i.getData().getInt("hashtags." + key));
    }

    return unsorted.entrySet().stream()
        .sorted(Collections.reverseOrder(Map.Entry.comparingByValue()))
        .collect(
            Collectors.toMap(
                Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e2, LinkedHashMap::new));
  }
}
