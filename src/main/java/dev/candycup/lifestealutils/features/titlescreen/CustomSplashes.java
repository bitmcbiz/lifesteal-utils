package dev.candycup.lifestealutils.features.titlescreen;

import dev.candycup.lifestealutils.Config;
import dev.candycup.lifestealutils.event.EventPriority;
import dev.candycup.lifestealutils.event.events.SplashTextRequestEvent;
import dev.candycup.lifestealutils.event.listener.UIEventListener;

import java.util.ArrayList;

/**
 * provides custom splash texts for the title screen.
 */
public final class CustomSplashes implements UIEventListener {
    private static final ArrayList<String> SPLASH_TEXTS = new ArrayList<>() {{
        // TP trapper galore
        add("tpa for team");
        // Claim shield gimmick
        add("Your claim shield isn't up yet!");
        // Newbies repeatedly being confused about road/claim protection
        add("Why can't I break anything?");
        // Taking sides on the S2 drunken enchant controversy
        add("Nerf Drunken");
        add("Buff Drunken");
    }};

    @Override
    public boolean isEnabled() {
        return Config.getCustomSplashes();
    }

    @Override
    public EventPriority getPriority() {
        return EventPriority.NORMAL;
    }

    @Override
    public void onSplashTextRequest(SplashTextRequestEvent event) {
        String splash = SPLASH_TEXTS.get((int) (Math.random() * SPLASH_TEXTS.size()));
        event.setSplashText(splash);
    }
}
