package nic.hp.ccmgnrega.common;

import android.widget.ImageView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MediaStateManager {

    private static Map<ImageView, Boolean> state;

    public static void initializeState(List<ImageView> keys) {
        state = new HashMap<>();
        for (ImageView key : keys) {
            state.put(key, false);
        }
    }

    public static boolean isMediaPlaying(ImageView key) {
        if (state != null && state.containsKey(key)) {
            return state.get(key);
        }
        return false;
    }

    public static void setStateToStopped(ImageView key) {
        if (state != null && state.containsKey(key)) {
            state.put(key, false);
        }
    }

    public static void setStateToPlaying(ImageView key) {
        if (state != null && state.containsKey(key)) {
            setAllStatesToStopped();
            state.put(key,true);
        }
    }

    public static void setAllStatesToStopped() {
        if (state != null) {
            for (Map.Entry<ImageView, Boolean> entry : state.entrySet()) {
                state.put(entry.getKey(), false);
            }
        }
    }
}
