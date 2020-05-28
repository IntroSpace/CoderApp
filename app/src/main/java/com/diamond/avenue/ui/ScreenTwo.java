package com.diamond.avenue.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.diamond.avenue.AvenueResult;
import com.diamond.avenue.R;
import com.diamond.avenue.language.AvenueSystem;
import com.diamond.avenue.language.Lexer;
import com.diamond.avenue.language.Parser;
import com.diamond.avenue.language.Token;
import com.diamond.avenue.language.expr.BlockStatement;

import java.util.List;

public final class ScreenTwo extends androidx.fragment.app.Fragment {
    public ScreenTwo() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.screen_first, container,
                false);

        //listener for "Run" button
        ((Button) rootView.findViewById (R.id.button)).setOnClickListener (new View.OnClickListener ( ) {
            @Override
            public void onClick ( View v ) {
                EditText text = rootView.findViewById(R.id.lol);
                final String input = text.getText ().toString ();
                try {
                    final List<Token> tokens = new Lexer(input).tokenize();
                    AvenueSystem.statements = ((BlockStatement) new Parser(tokens).parse());
                    Intent i = new Intent(ScreenTwo.this.getActivity(), AvenueResult.class);
                    startActivity (i);
                } catch (Exception e) {
                    Toast.makeText(getActivity(), "ОШИБКА", Toast.LENGTH_SHORT).show();
                }
            }
        });

        return rootView;
    }
}
