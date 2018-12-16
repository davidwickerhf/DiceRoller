package io.github.davidwickerhf.diceroller;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

public class HomeFragment extends Fragment {


    //todo Views
    private Button mRollButton;
    private TextView mNumberView;
    private TextView selectedItemTextView;
    private TextView mRollBtnText;
    private ImageView mDice1;
    private ImageView mDice2;
    private ImageView mDice3;
    private ImageView mDice4;
    private ImageView mDice5;
    public ImageView mDice6;

    private Button mAddSetting;
    private TextView mAddSettingBtnText;
    //todo LINK THESE VIEWS, SET THE VISIBILITY WEHN NEEDED!!!

    //todo Classes and Components
    private MainActivity mMainActivity;
    private SettingsViewModel settingsViewModel;
    private SettingAdapter adapter;

    //todo Variables
    private final int[] diceArray = new int[7];
    private List<Integer> addedItems = new ArrayList<>();

    private int mMaximumGenerated;
    private ArrayList<String> items;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View fragmentView;


        mMaximumGenerated = getArguments().getInt(AddSettingActivity.EXTRA_MAX_NUMBER);
        //todo Choose Layout (Number of Dices)
        if (mMaximumGenerated <= 12)
            fragmentView = inflater.inflate(R.layout.fragment_home_2dices, container, false);
        else if (mMaximumGenerated <= 30)
            fragmentView = inflater.inflate(R.layout.fragment_home, container, false);
        else
            fragmentView = inflater.inflate(R.layout.fragment_home_6dices, container, false);


        if (getArguments().getStringArrayList(AddSettingActivity.EXTRA_ITEMS_LIST) != null) {
            items = getArguments().getStringArrayList(AddSettingActivity.EXTRA_ITEMS_LIST);
        }


        mMainActivity = new MainActivity();


        //todo ViewModel
        settingsViewModel = ViewModelProviders.of(this).get(SettingsViewModel.class);
        settingsViewModel.getAllSettings().observe(this, new Observer<List<Setting>>() {
            @Override
            public void onChanged(@Nullable List<Setting> settings) {
                adapter.setSettings(settings);
            }
        });
        //todo SettingsAdapter
        adapter = new SettingAdapter();
        //todo Initialize Buttons:
        mRollButton = fragmentView.findViewById(R.id.home_roll_button);
        //todo Initialize Button Text
        mRollBtnText = fragmentView.findViewById(R.id.home_roll_button_text);
        //todo Selected Number Text View
        mNumberView = fragmentView.findViewById(R.id.selected_number_text_view);
        //todo Selected Item Text View
        selectedItemTextView = fragmentView.findViewById(R.id.selected_item_text_view);
        //todo Dices - Initialize 6 Dices
        mDice1 = fragmentView.findViewById(R.id.dice1);
        mDice2 = fragmentView.findViewById(R.id.dice2);
        mDice3 = fragmentView.findViewById(R.id.dice3);
        mDice4 = fragmentView.findViewById(R.id.dice4);
        mDice5 = fragmentView.findViewById(R.id.dice5);
        mDice6 = fragmentView.findViewById(R.id.dice6);

        //todo Dice Resources Array
        diceArray[0] = R.drawable.dice0;
        diceArray[1] = R.drawable.dice1;
        diceArray[2] = R.drawable.dice2;
        diceArray[3] = R.drawable.dice3;
        diceArray[4] = R.drawable.dice4;
        diceArray[5] = R.drawable.dice5;
        diceArray[6] = R.drawable.dice6;

        Toast.makeText(getActivity(), "Maximum Number is: " + mMaximumGenerated, Toast.LENGTH_SHORT).show();
        Log.d("HomeFragment", "Max Num is: " + mMaximumGenerated);


        //todo Select Random Number and change Dice resources:  -  -  -  -  -  -  -  -  -  -  -  -  -  -  -  -  -  -  -  -  -  -  -  -  -  -  -  -  -  -  -  -  -  -  -
        mRollButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("HomeFragment", "Roll Button Clicked");

                Random randomNumberGenerator = new Random();
                int mKeyNumber = randomNumberGenerator.nextInt(mMaximumGenerated) + 1;
                Log.d("HomeFragment", "Chosen number: " + mKeyNumber);
                Log.d("HomeFragment", "List size in HomeFragment = " + items.size());
                Log.d("HomeFragment", "Item List arrived in Home is:" + items.toString());

                mNumberView.setText(String.valueOf(mKeyNumber));
                if (items.size() != 0) {
                    selectedItemTextView.setText(items.get(mKeyNumber - 1));
                    Log.d("HomeFragment", "List size = " + items.size());
                    Log.d("HomeFragment", "Item String = " + items.get(mKeyNumber - 1));

                }
                if (mMaximumGenerated <= 12)
                    update2Dices(mKeyNumber, mMaximumGenerated);
                else if (mMaximumGenerated <= 30) {
                    update5Dices(mKeyNumber, mMaximumGenerated);
                } else
                    update6Dices(mKeyNumber, mMaximumGenerated);

            }
        });


        return fragmentView;
    }

    private void update2Dices(int key, int maximumGenerated) {

        //TODO Assign a value to a variable
        Log.d("Dicee", ".");
        int[] intForIndex = new int[2];

        //TODO Two inputs
        if (key % 6 == 0) {
            int a = 0;
            while (key > 0) {
                if (a > 2)
                    break;
                intForIndex[a] = (6);
                key -= (6);
                a++;
            }
        }
        //todo Two inputs
        else {
            intForIndex[0] = (key / 2);
            key -= (key / 2);
            intForIndex[1] = key;
        }

        Log.d("Dicee", "" + intForIndex[0] + intForIndex[1]);


        // TODO Randomizing picks in the array, to randomize the dice number position.
        int setter = returnDiceIndex(maximumGenerated);
        mDice1.setImageResource(diceArray[intForIndex[setter]]);
        addedItems.add(setter);
        Log.d("Dicee", "Dice 1 added resource");


        setter = returnDiceIndex(maximumGenerated);
        mDice2.setImageResource(diceArray[intForIndex[setter]]);
        addedItems.add(setter);
        Log.d("Dicee", "Dice 2 added resource");

        Log.d("Dicee", "" + addedItems);
        addedItems.clear();


    }

    private void update5Dices(int key, int maximumGenerated) {

        //TODO Assign a value to a variable
        Log.d("Dicee", ".");
        int[] intForIndex = new int[5];

        //TODO Five inputs
        if (key % 6 == 0) {
            int a = 0;
            while (key > 0) {
                if (a > 4)
                    break;
                intForIndex[a] = (6);
                key -= (6);
                a++;
            }
        }
        // Five inputs
        else if (key < 18) {

            intForIndex[0] = (key / 3);
            key -= (key / 3);
            intForIndex[1] = (key / 2);
            key -= (key / 2);
            intForIndex[2] = key;
            intForIndex[3] = 0;
            intForIndex[4] = 0;

        } else {
            int a;
            for (a = 0; a < 4; a++) {
                intForIndex[a] = (6);
            }
            key -= 18;
            intForIndex[3] = (key / 2);
            key -= (key / 2);

            intForIndex[4] = (key);
        }

        Log.d("Dicee", "" + intForIndex[0] + intForIndex[1] + intForIndex[2] + intForIndex[3] + intForIndex[4]);


        // TODO Randomizing picks in the array, to randomize the dice number position.
        int setter = returnDiceIndex(maximumGenerated);
        mDice1.setImageResource(diceArray[intForIndex[setter]]);
        addedItems.add(setter);
        Log.d("Dicee", "Dice 1 added resource");


        setter = returnDiceIndex(maximumGenerated);
        mDice2.setImageResource(diceArray[intForIndex[setter]]);
        addedItems.add(setter);
        Log.d("Dicee", "Dice 2 added resource");

        setter = returnDiceIndex(maximumGenerated);
        mDice3.setImageResource(diceArray[intForIndex[setter]]);
        addedItems.add(setter);
        Log.d("Dicee", "Dice 3 added resource");

        setter = returnDiceIndex(maximumGenerated);
        mDice4.setImageResource(diceArray[intForIndex[setter]]);
        addedItems.add(setter);
        Log.d("Dicee", "Dice 4 added resource");

        setter = returnDiceIndex(maximumGenerated);
        mDice5.setImageResource(diceArray[intForIndex[setter]]);
        addedItems.add(setter);
        Log.d("Dicee", "Dice 5 added resource");

        Log.d("Dicee", "" + addedItems);
        addedItems.clear();


    }

    private void update6Dices(int key, int maximumGenerated) {

        //TODO Assign a value to a variable
        Log.d("Dicee", ".");
        int[] intForIndex = new int[6];

        //TODO Six inputs
        if (key % 6 == 0) {
            int a = 0;
            while (key > 0) {
                if (a > 5)
                    break;
                intForIndex[a] = (6);
                key -= (6);
                a++;
            }
        }
        //todo Six inputs
        else if (key < 18) {

            intForIndex[0] = (key / 3);
            key -= (key / 3);
            intForIndex[1] = (key / 2);
            key -= (key / 2);
            intForIndex[2] = key;
            intForIndex[3] = 0;
            intForIndex[4] = 0;
            intForIndex[5] = 0;

        } else if (key <= 24) {
            int a;
            for (a = 0; a < 3; a++) {
                intForIndex[a] = (6);
            }
            key -= 18;
            intForIndex[3] = 0;
            intForIndex[4] = 0;
            intForIndex[5] = (key);
        } else {
            int a;
            for (a = 0; a < 4; a++) {
                intForIndex[a] = (6);
            }
            key -= 24;
            intForIndex[4] = (key / 2);
            key -= (key / 2);
            intForIndex[5] = key;
        }


        Log.d("Dicee", "" + intForIndex[0] + intForIndex[1] + intForIndex[2] + intForIndex[3] + intForIndex[4] + intForIndex[5]);


        // TODO Randomizing picks in the array, to randomize the dice number position.
        int setter = returnDiceIndex(maximumGenerated);
        mDice1.setImageResource(diceArray[intForIndex[setter]]);
        addedItems.add(setter);
        Log.d("Dicee", "Dice 1 added resource, setter is:" + setter + " : " + diceArray[intForIndex[setter]]);

        setter = returnDiceIndex(maximumGenerated);
        mDice2.setImageResource(diceArray[intForIndex[setter]]);
        addedItems.add(setter);
        Log.d("Dicee", "Dice 2 added resource, setter is:" + setter + " : " + diceArray[intForIndex[setter]]);

        setter = returnDiceIndex(maximumGenerated);
        mDice3.setImageResource(diceArray[intForIndex[setter]]);
        addedItems.add(setter);
        Log.d("Dicee", "Dice 3 added resource, setter is:" + setter + " : " + diceArray[intForIndex[setter]]);

        setter = returnDiceIndex(maximumGenerated);
        mDice4.setImageResource(diceArray[intForIndex[setter]]);
        addedItems.add(setter);
        Log.d("Dicee", "Dice 4 added resource, setter is:" + setter + " : " + diceArray[intForIndex[setter]]);

        setter = returnDiceIndex(maximumGenerated);
        mDice5.setImageResource(diceArray[intForIndex[setter]]);
        addedItems.add(setter);
        Log.d("Dicee", "Dice 5 added resource, setter is:" + setter + " : " + diceArray[intForIndex[setter]]);

        setter = returnDiceIndex(maximumGenerated);
        mDice6.setImageResource(diceArray[intForIndex[setter]]);
        addedItems.add(setter);
        Log.d("Dicee", "Dice 6 added resource, setter is:" + setter + " : " + diceArray[intForIndex[setter]]);

        Log.d("Dicee", "" + addedItems);
        addedItems.clear();

    }


    private int returnDiceIndex(int maximumGenerated) {
        Random randomNumber = new Random();
        int randForIndex;

        if (maximumGenerated <= 12) {
            while (true) {
                randForIndex = randomNumber.nextInt(2);
                if (!addedItems.contains(randForIndex)) {
                    break;
                }
            }
            return randForIndex;
        } else if (maximumGenerated <= 30) {
            while (true) {
                randForIndex = randomNumber.nextInt(5);
                if (!addedItems.contains(randForIndex)) {
                    break;
                }
            }
            return randForIndex;
        } else {
            while (true) {
                randForIndex = randomNumber.nextInt(6);
                if (!addedItems.contains(randForIndex)) {
                    break;
                }
            }
            return randForIndex;
        }
    }


}
