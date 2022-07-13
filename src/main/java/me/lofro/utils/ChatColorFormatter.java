package me.lofro.utils;

import me.lofro.cerdomania.Cerdomania;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import org.bukkit.ChatColor;

public class ChatColorFormatter {

    public static final String name = ChatColorFormatter.stringToString("&c&lCerdomania");

    public static final String prefix = ChatColorFormatter.stringToString(name + " &r>> ");

    /**
     * Function that translates the given String into another String with ChatColor format.
     * @param text to translate.
     * @return String containing the ChatColor.COLOR_CHAR color code character replaced by '&'.
     */
    public static String stringToString(String text) {
        return ChatColor.translateAlternateColorCodes('&', text);
    }

    /**
     * Function that translates the given String into a Component with ChatColor format.
     * @param text to translate.
     * @return Component containing the ChatColor.COLOR_CHAR color code character replaced by '&'.
     */
    public static String componentToString(TextComponent text) {
        return ChatColor.translateAlternateColorCodes('&', text.content());
    }

    /**
     * Function that translates the given Component into a String with ChatColor format.
     * @param text to translate.
     * @return String containing the ChatColor.COLOR_CHAR color code character replaced by '&'.
     */
    public static Component stringToComponent(String text) {
        return Component.text(ChatColor.translateAlternateColorCodes('&', text));
    }

    /**
     * Function that translates the given Component into another Component with ChatColor format.
     * @param text to translate.
     * @return Component containing the ChatColor.COLOR_CHAR color code character replaced by '&'.
     */
    public static Component componentToComponent(TextComponent text) {
        return Component.text(ChatColor.translateAlternateColorCodes('&', text.content()));
    }

    /**
     * Function that translates the given String into another String with ChatColor format and the plugin prefix and the plugin prefix.
     * @param text to translate.
     * @return String containing the ChatColor.COLOR_CHAR color code character replaced by '&' with the plugin prefix.
     */
    public static String stringToStringWithPrefix(String text) {
        return ChatColor.translateAlternateColorCodes('&', prefix + text);
    }

    /**
     * Function that translates the given String into a Component with ChatColor format and the plugin prefix.
     * @param text to translate.
     * @return Component containing the ChatColor.COLOR_CHAR color code character replaced by '&' with the plugin prefix.
     */
    public static String componentToStringWithPrefix(TextComponent text) {
        return ChatColor.translateAlternateColorCodes('&', prefix + text.content());
    }

    /**
     * Function that translates the given Component into a String with ChatColor format and the plugin prefix.
     * @param text to translate.
     * @return String containing the ChatColor.COLOR_CHAR color code character replaced by '&' with the plugin prefix.
     */
    public static Component stringToComponentWithPrefix(String text) {
        return Component.text(ChatColor.translateAlternateColorCodes('&', prefix + text));
    }

    /**
     * Function that translates the given Component into another Component with ChatColor format and the plugin prefix.
     * @param text to translate.
     * @return Component containing the ChatColor.COLOR_CHAR color code character replaced by '&' with the plugin prefix.
     */
    public static Component componentToComponentWithPrefix(TextComponent text) {
        return Component.text(ChatColor.translateAlternateColorCodes('&', prefix + text.content()));
    }

}
