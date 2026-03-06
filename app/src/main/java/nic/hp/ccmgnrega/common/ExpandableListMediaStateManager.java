package nic.hp.ccmgnrega.common;


import java.util.List;

public class ExpandableListMediaStateManager {

    private static List<List<Boolean>> state;

    public static void initializeState(List<List<Boolean>> givenState) {
        state = givenState;
    }

    public static boolean isMediaPlaying(int n, int... m) {
        if (m == null || m.length == 0) {
            m = new int[1];
            m[0] = 0;
        }
        if (isValidIndex(n,m[0])) {
            return state.get(n).get(m[0]);
        }
        return false;
    }

    public static void setStateToStopped(int n, int... m) {
        if (m == null || m.length == 0) {
            m = new int[1];
            m[0] = 0;
        }
        if (isValidIndex(n,m[0])) {
            state.get(n).set(m[0],false);
        }
    }

    public static void setStateToPlaying(int n, int... m) {
        if (m == null || m.length == 0) {
            m = new int[1];
            m[0] = 0;
        }
        if (isValidIndex(n,m[0])) {
            setAllStatesToStopped();
            state.get(n).set(m[0],true);
        }
    }
    private static boolean isValidIndex(int n, int m) {
        if (state != null && n < state.size() && state.get(n) != null && m < state.get(n).size()) {
            return true;
        }
        return false;
    }

    public static void setAllStatesToStopped() {
        if (state != null) {
            for (int i = 0; i < state.size(); i++) {
                for (int j = 0; j < state.get(i).size(); j++) {
                    state.get(i).set(j,false);
                }
            }
        }
    }
}
