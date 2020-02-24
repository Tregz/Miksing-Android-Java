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
import com.tregz.miksing.databinding.FragmentUserBinding;
import com.tregz.miksing.home.HomeActivity;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

public class UserFragment extends BaseFragment {
    public final static String TAG = UserFragment.class.getSimpleName();

    private FragmentUserBinding binding;

    @Nullable
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState
    ) {
        binding = FragmentUserBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    v.setSelected(!v.isSelected());
                    binding.editor.setVisibility(v.isSelected() ? VISIBLE : GONE);
            }
        });
        binding.myLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserMap map = map();
                if (map != null) map.fromLastLocation();
            }
        });
        update();
        //final EditText home = view.findViewById(R.id.user_home);
        binding.saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (binding.userHome.getText() != null) {
                    String location = binding.userHome.getText().toString();
                    UserMap map = map();
                    if (map != null) map.fromLocationName(location);
                    // TODO save
                }
            }
        });
    }

    public void update() {
        if (getContext() != null) {
            binding.userName.setText(PrefShared.getInstance(getContext()).getUsername());
            String email = PrefShared.getInstance(getContext()).getEmail();
            if ((email == null || email.isEmpty()) && getContext() != null)
                email = getContext().getString(R.string.nav_drawer_sub);
            binding.subtitle.setText(email);
        }
    }

    private UserMap map() {
        return getActivity() != null ? ((HomeActivity) getActivity()).areaFragment() : null;
    }
}
