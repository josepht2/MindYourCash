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

import nu.mad.mindyourcash.models.Purchase;
import nu.mad.mindyourcash.models.User;

// resource: https://developer.android.com/guide/topics/ui/dialogs
public class AddPurchaseDialogFragment extends DialogFragment {

    private User user;
    private String account;

    /**
     * Constructor for RegisterDialogFragment.
     *
     * @author Joseph Triolo
     */
    AddPurchaseDialogFragment(User user, String account) {
        super();
        this.user = user;
        this.account = account;
    }

    /**
     * onCreateDialog for LinkCollectorDialogFragment.
     * Gets layout, adds buttons with listeners.
     * Dialog requires a name and a url to add a link. If either are empty, the appropriate
     * EditText will give an error and the dialog won't dismiss.
     *
     * @param savedInstanceState the context associated with this dialog
     * @author Joseph Triolo
     */
    @Override
    public Dialog onCreateDialog(final Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();

        builder.setView(R.layout.addpurchase_dialog)
                .setPositiveButton(R.string.addpurchase, null);

        // resource: https://stackoverflow.com/questions/2620444/how-to-prevent-a-dialog-from-closing-when-a-button-is-clicked

        final AlertDialog alertDialog = builder.create();

        alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {

            @Override
            public void onShow(DialogInterface dialogInterface) {
                Button button = (alertDialog).getButton(AlertDialog.BUTTON_POSITIVE);

                button.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View view) {
                        EditText purchaseNameEditText = alertDialog
                                .findViewById(R.id.addpurchase_dialog_purchasename);
                        String newPurchaseName = purchaseNameEditText.getText().toString();
                        boolean isEmptyAccountName = TextUtils.isEmpty(newPurchaseName);

                        EditText costEditText = alertDialog
                                .findViewById(R.id.addpurchase_dialog_cost);
                        String newCost = costEditText.getText().toString();
                        boolean isEmptyCost = TextUtils.isEmpty(newCost);
                        boolean isValidCost = false;
                        double newCostDouble = 0;

                        if (isEmptyAccountName) {
                            purchaseNameEditText.setError("Please enter an account name.");
                        }

                        if (isEmptyCost) {
                            costEditText.setError("Please enter a cost.");
                        } else {
                            Object newCostObject = null;
                            try {
                                newCostObject = Double.parseDouble(newCost);
                            } catch (Exception e) {
                                costEditText.setError("Please enter a cost as a positive number value in the format xx.xx (e.g. 100.00).");
                            }

                            if (newCostObject != null) {
                                newCostDouble = (double) newCostObject;
                                if (newCostDouble <= 0) {
                                    costEditText.setError("Please enter a cost as a positive number value in the format xx.xx (e.g. 100.00).");
                                } else {
                                    int decimalCount = 0;
                                    for (char c : newCost.toCharArray()) {
                                        if (c == '.') {
                                            ++decimalCount;
                                        }
                                    }
                                    if (decimalCount > 1) {
                                        costEditText.setError("Please enter a cost as a positive number value in the format xx.xx (e.g. 100.00).");
                                    } else if (decimalCount == 1 && newCost.indexOf('.') != (newCost.length() - 3)) {
                                        costEditText.setError("Please enter a cost as a positive number value in the format xx.xx (e.g. 100.00).");
                                    } else if (decimalCount == 0) {
                                        newCost += ".00";
                                        newCostDouble = Double.parseDouble(newCost);
                                        isValidCost = true;
                                    } else {
                                        isValidCost = true;
                                    }
                                }
                            }
                        }

                        if (!isEmptyAccountName && isValidCost) {
                            DatabaseReference databaseReference = FirebaseDatabase.getInstance()
                                    .getReference();

                            // resource: https://firebase.google.com/docs/database/android/read-and-write
                            databaseReference.child("users").child(user.username)
                                    .child("accounts").child(account)
                                    .child("purchases").child(newPurchaseName)
                                    .setValue(new Purchase(newPurchaseName, newCostDouble));

                            // resource: https://stackoverflow.com/questions/20702333/refresh-fragment-at-reload
                            alertDialog.dismiss();
                            Snackbar.make(getActivity().findViewById(R.id.purchases_coordinatorlayout),
                                    "Successfully added purchase.", Snackbar.LENGTH_LONG).show();
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
