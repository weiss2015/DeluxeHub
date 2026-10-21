package net.zithium.deluxehub.utility;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import net.kyori.adventure.text.serializer.legacy.LegacyFormat;

import java.util.regex.Pattern;

/**
 * Converts legacy (&a, &#rrggbb, &x&r&r...) and MiniMessage formatting into a
 * legacy section-sign string. Uses the server's own Adventure classes.
 */
public final class ColorUtil {

    private static final LegacyComponentSerializer LEGACY_HEX_SERIALIZER = LegacyComponentSerializer.builder()
            .hexColors()
            .useUnusualXRepeatedCharacterHexFormat()
            .build();

    private static final char[] LEGACY_COLOR_CODES = "0123456789abcdefABCDEFklmnorKLMNOR".toCharArray();

    private static final Pattern LEGACY_RGB_PATTERN = Pattern.compile(
            "[§&]x[§&]([0-9a-fA-F])[§&]([0-9a-fA-F])[§&]([0-9a-fA-F])[§&]([0-9a-fA-F])[§&]([0-9a-fA-F])[§&]([0-9a-fA-F])");
    private static final Pattern LEGACY_PLUGIN_RGB_PATTERN = Pattern.compile(
            "[§&]#([0-9a-fA-F])([0-9a-fA-F])([0-9a-fA-F])([0-9a-fA-F])([0-9a-fA-F])([0-9a-fA-F])");
    private static final String RGB_REPLACEMENT = "<#$1$2$3$4$5$6>";

    private ColorUtil() {
    }

    public static String color(String input) {
        if (input == null) {
            return null;
        }
        Component component = MiniMessage.miniMessage().deserialize(replaceLegacyWithTags(input.replace('§', '&')));
        return LEGACY_HEX_SERIALIZER.serialize(component);
    }

    public static Component componentColor(String input) {
        return MiniMessage.miniMessage().deserialize(replaceLegacyWithTags(input.replace('§', '&')));
    }

    public static String replaceLegacyWithTags(String input) {
        String result = LEGACY_RGB_PATTERN.matcher(input).replaceAll(RGB_REPLACEMENT);
        result = LEGACY_PLUGIN_RGB_PATTERN.matcher(result).replaceAll(RGB_REPLACEMENT);

        for (char code : LEGACY_COLOR_CODES) {
            LegacyFormat format = LegacyComponentSerializer.parseChar(code);
            if (format == null) {
                continue;
            }
            String legacy = "&" + code;
            if (format.color() != null) {
                result = result.replace(legacy, "<" + format.color().asHexString() + ">");
            }
            if (format.decoration() != null) {
                result = result.replace(legacy, "<" + format.decoration().toString() + ">");
            }
            if (format.reset()) {
                result = result.replace(legacy, "<reset>");
            }
        }
        return result;
    }
}
