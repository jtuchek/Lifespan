package com.botlink.lifespanlibrary;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.botlink.lifespan.ILifespan;
import com.botlink.lifespan.LifespanCreation;
import com.botlink.lifespan.LifespanHolder;
import com.botlink.lifespan.LifespanTermination;
import com.botlink.lifespan.LifespanToken;


public class MainActivity extends AppCompatActivity implements LifespanToken {

    private TextView tokenField;
    private TextView actionField;

    private LifespanHolder<MainPresenter> holder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        actionField = (TextView) findViewById(R.id.action);
        tokenField = (TextView) findViewById(R.id.token);

        setAction(false);

        holder = lifespanTracker().manage(savedInstanceState,
                new LifespanCreation<MainPresenter>() {
                    @Override
                    public MainPresenter provide() {
                        setAction(true);
                        return new MainPresenter();
                    }
                }, new LifespanTermination() {
                    @Override
                    public void isFinishing() {
                        holder.value().isFinishing();
                    }
                });

        setToken(token());

        findViewById(R.id.kill)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        finish();
                    }
                });

        findViewById(R.id.spawn)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivity(new Intent(MainActivity.this, MainActivity.class));
                    }
                });
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        lifespanTracker().write(outState, holder);
    }

    @Override
    public long token() {
        return holder.token();
    }

    private ILifespan lifespanTracker() {
        return ((MainApplication) getApplicationContext()).lifespanTracker();
    }

    private void setToken(long token) {
        tokenField.setText("lifespan token: " + token);
    }

    private void setAction(boolean created) {
        actionField.setText("Lifespan action: " + (created ? "created" : "restored"));
    }

}
