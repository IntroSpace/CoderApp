package com.diamond.avenue;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.diamond.avenue.language.AvenueSystem;
import com.diamond.avenue.language.expr.InputStatement;
import com.diamond.avenue.language.expr.Statement;
import com.diamond.avenue.language.lib.Variables;

import java.util.ArrayList;

public class AvenueResult extends AppCompatActivity {
    Statement st;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_avenue_result);
        try {
            AvenueSystem.statements.execute();
        } catch (Exception e) {
            Toast.makeText(this, "ОШИБКА", Toast.LENGTH_SHORT).show();
            finish();
        }
        ((TextView) findViewById(R.id.textView)).setText(AvenueSystem.getConclusion());
        Variables.New();
    }
}
