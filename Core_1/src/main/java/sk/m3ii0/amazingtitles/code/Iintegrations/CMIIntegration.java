package sk.m3ii0.amazingtitles.code.Iintegrations;

import com.Zrips.CMI.events.*;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import sk.m3ii0.amazingtitles.code.internal.Booter;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CMIIntegration implements Integration, Listener {

    private static File integrationFile;
    private static FileConfiguration integrationConfiguration;

    @Override
    public void reload() {
        integrationFile = new File(getDataFolder(), "CMI-Integration.yml");
        if (!integrationFile.exists()) {
            try {
                integrationFile.createNewFile();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        integrationConfiguration = YamlConfiguration.loadConfiguration(integrationFile);
        Bukkit.getPluginManager().registerEvents(this, Booter.getInstance());
    }

    @EventHandler
    public void onPortalUseEvent(CMIPortalUseEvent e) {

        String portalName = e.getPortal().getName();

        Map<String, String> placeholders = new HashMap<String, String>() {{
            put("{Player}", e.getPlayer().getName());
            put("{PortalName}", portalName);
        }};

        String path = "Portals." + portalName + ".";

        List<String> conditions = integrationConfiguration.getStringList(path + "Conditions");
        List<String> executions = integrationConfiguration.getStringList(path + "Executions");

        if (StringUtils.conditions(conditions, e.getPlayer())) StringUtils.execute(executions, placeholders);

    }

    @EventHandler
    public void onTeleportRequest(CMIPlayerTeleportRequestEvent e) {

        Map<String, String> placeholders = new HashMap<String, String>() {{
            put("{OfferingPlayer}", e.getWhoOffers().getName());
            put("{AcceptingPlayer}", e.getWhoAccepts().getName());
        }};

        String path = "TeleportRequest.";
        List<String> executions = integrationConfiguration.getStringList(path + "Executions");
        StringUtils.execute(executions, placeholders);

    }

    @EventHandler
    public void onTeleport(CMIPlayerTeleportEvent e) {

        Map<String, String> placeholders = new HashMap<String, String>() {{
            put("{Player}", e.getPlayer().getName());
            put("{World}", e.getTo().getWorld().getName());
            put("{X}", e.getTo().getBlockX() + "");
            put("{Y}", e.getTo().getBlockY() + "");
            put("{Z}", e.getTo().getBlockZ() + "");
            put("{TeleportType}", e.getType().name());
        }};

        String path = "Teleport.";

        List<String> conditions = integrationConfiguration.getStringList(path + "Conditions");
        List<String> executions = integrationConfiguration.getStringList(path + "Executions");

        if (StringUtils.conditions(conditions, e.getPlayer())) StringUtils.execute(executions, placeholders);

    }

    @EventHandler
    public void onAfkEnter(CMIAfkEnterEvent e) {

        Map<String, String> placeholders = new HashMap<String, String>() {{
            put("{Player}", e.getPlayer().getName());
        }};

        String path = "AfkEnter.";

        List<String> conditions = integrationConfiguration.getStringList(path + "Conditions");
        List<String> executions = integrationConfiguration.getStringList(path + "Executions");

        if (StringUtils.conditions(conditions, e.getPlayer())) StringUtils.execute(executions, placeholders);

    }

    @EventHandler
    public void onAfkLeave(CMIAfkLeaveEvent e) {

        Map<String, String> placeholders = new HashMap<String, String>() {{
            put("{Player}", e.getPlayer().getName());
        }};

        String path = "AfkLeave.";

        List<String> conditions = integrationConfiguration.getStringList(path + "Conditions");
        List<String> executions = integrationConfiguration.getStringList(path + "Executions");

        if (StringUtils.conditions(conditions, e.getPlayer())) StringUtils.execute(executions, placeholders);

    }

    @EventHandler
    public void onBalanceChange(CMIUserBalanceChangeEvent e) {

        Map<String, String> placeholders = new HashMap<String, String>() {{
            put("{From}", e.getFrom() + "");
            put("{To}", e.getTo() + "");
            put("{Player}", e.getUser().getPlayer().getName());
        }};

        String path = "BalanceChange.";

        List<String> conditions = integrationConfiguration.getStringList(path + "Conditions");
        List<String> executions = integrationConfiguration.getStringList(path + "Executions");

        if (StringUtils.conditions(conditions, e.getUser().getPlayer())) StringUtils.execute(executions, placeholders);

    }



}
