package com.tregz.miksing.home.user;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.tregz.miksing.R;
import com.tregz.miksing.arch.pref.PrefShared;
import com.tregz.miksing.base.BaseFragment;
import com.tregz.miksing.home.HomeActivity;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

public class UserFragment extends BaseFragment {
    public final static String TAG = UserFragment.class.getSimpleName();

    private TextView txEmail;
    private TextView txUsername;

    @Nullable
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState
    ) {
        return inflater.inflate(R.layout.fragment_user, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        final LinearLayout editor = view.findViewById(R.id.editor);
        view.findViewById(R.id.profile).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    v.setSelected(!v.isSelected());
                    editor.setVisibility(v.isSelected() ? VISIBLE : GONE);
            }
        });
        txUsername = view.findViewById(R.id.user_name);
        txEmail = view.findViewById(R.id.subtitle);
        view.findViewById(R.id.my_location).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserMap map = map();
                if (map != null) map.fromLastLocation();
            }
        });
        update();
        final EditText home = view.findViewById(R.id.user_home);
        view.findViewById(R.id.save_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String location = home.getText().toString();
                UserMap map = map();
                if (map != null) map.fromLocationName(location);
            }
        });
    }

    public void update() {
        if (getContext() != null) {
            txUsername.setText(PrefShared.getInstance(getContext()).getUsername());
            String email = PrefShared.getInstance(getContext()).getEmail();
            if ((email == null || email.isEmpty()) && getContext() != null)
                email = getContext().getString(R.string.nav_drawer_sub);
            txEmail.setText(email);
        }
    }

    private UserMap map() {
        return getActivity() != null ? ((HomeActivity) getActivity()).areaFragment() : null;
    }
}
