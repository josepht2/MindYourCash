package nu.mad.mindyourcash;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.fragment.app.DialogFragment;

import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Set;

import nu.mad.mindyourcash.models.User;

// resource: https://developer.android.com/guide/topics/ui/dialogs
public class AddAccountDialogFragment extends DialogFragment {

    private User user;
    private Set<String> accountNamesSet;

    AddAccountDialogFragment(User user, Set<String> accountNamesSet) {
        super();
        this.user = user;
        this.accountNamesSet = accountNamesSet;
    }

    @Override
    public Dialog onCreateDialog(final Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();

        builder.setView(R.layout.addaccount_dialog)
                .setPositiveButton(R.string.addaccount, null)
                .setNegativeButton("Cancel", null);

        // resource: https://stackoverflow.com/questions/2620444/how-to-prevent-a-dialog-from-closing-when-a-button-is-clicked

        final AlertDialog alertDialog = builder.create();

        alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {

            @Override
            public void onShow(DialogInterface dialogInterface) {
                Button button = (alertDialog).getButton(AlertDialog.BUTTON_POSITIVE);

                button.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View view) {
                        EditText accountNameEditText = alertDialog
                                .findViewById(R.id.addaccount_dialog_accountname);
                        String newAccountName = accountNameEditText.getText().toString();
                        boolean isEmptyAccountName = TextUtils.isEmpty(newAccountName);

                        if (isEmptyAccountName) {
                            accountNameEditText.setError("Please enter an account name.");
                        } else if (accountNamesSet.contains(newAccountName
                                .replaceAll(" ", "")
                                .replaceAll("\n", "")
                                .replaceAll("\t", "")
                                .toLowerCase())) {
                            accountNameEditText.setError("You are already using this account name.");
                        } else {
                            DatabaseReference databaseReference = FirebaseDatabase.getInstance()
                                    .getReference();

                            // resource: https://firebase.google.com/docs/database/android/read-and-write
                            databaseReference.child("users").child(user.username)
                                    .child("accounts").child(newAccountName)
                                    .child("accountName").setValue(newAccountName);

                            // resource: https://stackoverflow.com/questions/20702333/refresh-fragment-at-reload
                            alertDialog.dismiss();
                            Snackbar.make(getActivity().findViewById(R.id.accounts_coordinatorlayout),
                                    "Successfully added account.", Snackbar.LENGTH_LONG).show();
                        }
                    }
                });
            }
        });

        // resource: https://stackoverflow.com/questions/13401632/android-app-crashed-on-screen-rotation-with-dialog-open
        setRetainInstance(true);
        return alertDialog;
    }
}
