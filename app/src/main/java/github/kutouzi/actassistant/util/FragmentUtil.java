package github.kutouzi.actassistant.util;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import java.util.List;

public class FragmentUtil {
    public static void switchFragment(FragmentManager fragmentManager,Fragment fragment) {
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        List<Fragment> fragments = fragmentManager.getFragments();
        for (Fragment f : fragments) {
            if (f != null) {
                if (!f.equals(fragment)) {
                    fragmentTransaction.hide(f);
                }
            }
        }
        fragmentTransaction.show(fragment);
        fragmentTransaction.commit();
    }
}
