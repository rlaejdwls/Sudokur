package kr.co.treegames.core.fragment;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import kr.co.treegames.core.util.StringUtils;

/**
 * Created by Hwang on 2017-08-02.
 * 작성자 : 황의택
 */
public class NFragment extends Fragment {
    public static <T extends NFragment> T create(Class<T> klass) {
        T fragment = null;
        try {
            fragment = klass.newInstance();
        } catch (java.lang.InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return fragment;
    }
    public static <T extends NFragment> T create(Class<T> klass, Bundle params) {
        T fragment = null;
        try {
            fragment = klass.newInstance();
        } catch (java.lang.InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
        if (fragment != null) fragment.setArguments(params);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        String layout = "fragment_" + StringUtils.getAliasWithUnderBar(this.getClass().getSimpleName().replace("Fragment", ""));
        View view = inflater.inflate(getActivity().getResources().getIdentifier(layout, "layout", getActivity().getPackageName()), container, false);
        return onCreateView(view, inflater, container, savedInstanceState);
    }
    public View onCreateView(View view, LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return view;
    }
}