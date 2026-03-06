package nic.hp.ccmgnrega.common;

import java.util.Comparator;

public class IntegerStringComparator implements Comparator<String> {
    @Override
    public int compare(String str1, String str2) {
        int intValue1 = Integer.parseInt(str1);
        int intValue2 = Integer.parseInt(str2);

        return Integer.compare(intValue1, intValue2);
    }
}
