package com.nightcoders.sreekanth.ztest;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.nightcoders.sreekanth.ztest.Listeners.ChangeFragmentStateListener;
import com.nightcoders.sreekanth.ztest.Listeners.LocationListener;
import com.nightcoders.sreekanth.ztest.Listeners.StartButtonClickListener;
import com.nightcoders.sreekanth.ztest.Supports.NetworkSupport;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

import pl.pawelkleczkowski.customgauge.CustomGauge;

import static com.nightcoders.sreekanth.ztest.Literals.Core.START_FRAGMENT_TAG;

public class StartFragment extends Fragment implements ChangeFragmentStateListener, LocationListener {

    private Button run;
    private CustomGauge gauge;
    private StartButtonClickListener clickListener;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_start, container, false);
        // Inflate the layout for this fragment
        run = view.findViewById(R.id.run);
        Log.d("Starter", "Run");
        run.setEnabled(true);
        gauge = view.findViewById(R.id.gauge);
        TextView network = view.findViewById(R.id.network);
        TextView provider = view.findViewById(R.id.provider);
        //location = view.findViewById(R.id.loc);
        gauge.setVisibility(View.GONE);
        String[] data = NetworkSupport.getNetworkProvider(Objects.requireNonNull(getActivity()).getApplicationContext());
        String prov = data[1];
        String net = data[0];
        provider.setText(prov);
        network.setText(net);

        Animation shake = AnimationUtils.loadAnimation(getContext(), R.anim.btn_anim);
        shake.setRepeatCount(Animation.REVERSE);
        run.setAnimation(shake);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        run.setEnabled(true);
        Objects.requireNonNull(getView()).findViewById(R.id.run).setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View view) {
                run.setEnabled(false);
                gauge.setVisibility(View.VISIBLE);
                run.setText("Starting");
                animationRun();
            }
        });
    }

    @Override
    public void startPostponedEnterTransition() {
        super.startPostponedEnterTransition();
        Log.d("Starter", "end");
    }

    @Override
    public void onResume() {
        super.onResume();
        run.setEnabled(true);
        Log.d("Starter", "Resumed");
    }

    @Override
    public void onAttach(@NotNull Context context) {
        super.onAttach(context);

        Activity activity = (Activity) context;
        clickListener = (StartButtonClickListener) activity;
        Log.d("Attach", START_FRAGMENT_TAG);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        Log.d("Starter", "Stopped");
    }


    private void animationRun() {
        new CountDownTimer(2000, 1) {
            @Override
            public void onTick(long l) {
                gauge.setValue((int) l);
            }

            @SuppressLint("SetTextI18n")
            @Override
            public void onFinish() {
                run.setEnabled(true);
                gauge.setVisibility(View.GONE);
                run.setText("Run");
                try {
                    clickListener.OnStartButtonClick();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void OnDetachMainFragment() {
        run.setEnabled(true);
        run.setText("RUN");
    }

    @Override
    public void OnAttachMainFragment() {
        run.setEnabled(false);
        Toast.makeText(getContext(), "Attach main", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void OnResultFragmentAttach() {
        run.setEnabled(false);
    }

    @Override
    public void OnResultFragmentDetach() {
        run.setEnabled(true);

    }

    @Override
    public void OnLocationChanged(String cityName) {
        //location.setText(cityName.toUpperCase());
    }
}
