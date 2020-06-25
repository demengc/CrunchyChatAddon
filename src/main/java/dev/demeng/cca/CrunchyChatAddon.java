package dev.demeng.cca;

import com.demeng7215.demlib.DemLib;
import com.demeng7215.demlib.api.Registerer;
import com.demeng7215.demlib.api.files.CustomConfig;
import com.demeng7215.demlib.api.messages.MessageUtils;
import dev.demeng.cca.commands.TrendingCmd;
import dev.demeng.cca.listeners.HashtagListener;
import dev.demeng.cca.listeners.PingListener;
import dev.demeng.cca.managers.HashtagManager;
import lombok.Getter;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;
import java.util.Calendar;

public final class CrunchyChatAddon extends JavaPlugin {

  @Getter private static CrunchyChatAddon instance;

  @Getter private HashtagManager hashtagManager;

  @Getter private CustomConfig settingsFile;

  @Getter private CustomConfig dataFile;

  @Override
  public void onEnable() {

    instance = this;

    DemLib.setPlugin(this);
    MessageUtils.setPrefix("&8[&6CrunchyChatAddon&8] &r");

    try {
      settingsFile = new CustomConfig("settings.yml");
      dataFile = new CustomConfig("data.yml");
    } catch (Exception e) {
      e.printStackTrace();
    }

    MessageUtils.setPrefix(getSettings().getString("prefix"));

    this.hashtagManager = new HashtagManager(this);

    Registerer.registerCommand(new TrendingCmd(this));

    Registerer.registerListeners(new PingListener(this));
    Registerer.registerListeners(new HashtagListener(this));

    if (isFirstDayOfMonth()) {
      MessageUtils.consoleWithoutPrefix("&cResetting trending tags...");
      getData().set("hashtags", null);
      getData().createSection("hashtags");

      try {
        dataFile.saveConfig();
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }

  @Override
  public void onDisable() {}

  public FileConfiguration getSettings() {
    return settingsFile.getConfig();
  }

  public FileConfiguration getData() {
    return dataFile.getConfig();
  }

  private boolean isFirstDayOfMonth() {
    int dayOfMonth = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
    return dayOfMonth == 1;
  }
}
