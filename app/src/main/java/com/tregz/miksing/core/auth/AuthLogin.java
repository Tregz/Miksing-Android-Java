package com.tregz.miksing.core.auth;

import com.firebase.ui.auth.AuthUI;
import com.tregz.miksing.R;
import com.tregz.miksing.home.HomeView;

import java.util.ArrayList;
import java.util.List;

public class AuthLogin {

    public final static int SIGN_IN = 101;

    public AuthLogin(HomeView view) {
        AuthUI.SignInIntentBuilder ui = AuthUI.getInstance().createSignInIntentBuilder();
        List<AuthUI.IdpConfig> providers = new ArrayList<>();
        providers.add(new AuthUI.IdpConfig.EmailBuilder().build());
        providers.add(new AuthUI.IdpConfig.GoogleBuilder().build());
        // if Facebook dev account id is set in Firebase console

        // TODO restore Facebook dev account
        // providers.add(new AuthUI.IdpConfig.FacebookBuilder().build());

        // release build only: if app certificate is set in Firebase console
        providers.add(new AuthUI.IdpConfig.PhoneBuilder().build());
        ui.setIsSmartLockEnabled(false); // smart lock not well supported by FirebaseUI
        ui.setAvailableProviders(providers);
        ui.setLogo(R.mipmap.ic_launcher_logo);
        ui.setTheme(R.style.LoginTheme);
        // optional terms of use (tos) and privacy policy
        String terms = "http://www.tregz.com/miksing/aide/en/privacy.pdf"; // TODO: terms
        String policy = "http://www.tregz.com/miksing/aide/en/privacy.pdf";
        ui.setTosAndPrivacyPolicyUrls(terms, policy);
        view.startActivityForResult(ui.build(), SIGN_IN);
    }
}
