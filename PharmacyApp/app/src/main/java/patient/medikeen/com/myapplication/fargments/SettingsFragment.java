package patient.medikeen.com.myapplication.fargments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import patient.medikeen.com.myapplication.R;

/**
 * Created by Varun on 2/21/2016.
 */
public class SettingsFragment extends Fragment {

    TextView changePassword;
    LinearLayout changePasswordHolder;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_settings, container, false);

        changePassword = (TextView) rootView.findViewById(R.id.change_password);
        changePasswordHolder = (LinearLayout) rootView.findViewById(R.id.change_password_holder);

        changePassword.setOnClickListener(new View.OnClickListener() {
                                              @Override
                                              public void onClick(View v) {
                                                  if (changePasswordHolder.getVisibility() == View.VISIBLE) {
                                                      changePasswordHolder.setVisibility(View.GONE);
                                                  } else {
                                                      changePasswordHolder.setVisibility(View.VISIBLE);
                                                  }
                                              }
                                          }
        );

        return rootView;
    }
}
