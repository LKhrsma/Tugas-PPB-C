package com.example.calculator2;

import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.button.MaterialButton;

import org.mozilla.javascript.Context;
import org.mozilla.javascript.Scriptable;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    TextView solutionTv, resultTv;
    MaterialButton buttonC,buttonBrackOpen,buttonBrackClose;
    MaterialButton buttonDivide, buttonMultiply, buttonPlus, buttonMinus, buttonEquals;
    MaterialButton button1,button2,button3,button4,button5,button6,button7,button8,button9,button0;
    MaterialButton buttonAC, buttonDot;


    void assignId(MaterialButton btn, int id){
        btn = findViewById(id);
        btn.setOnClickListener(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        // String view
        solutionTv = findViewById(R.id.solution_tv);
        resultTv = findViewById(R.id.result_tv);

        // Top Button
        assignId(buttonC, R.id.button_c);
        assignId(buttonBrackOpen, R.id.button_open_bracket);
        assignId(buttonBrackClose, R.id.button_close_bracket);
        // Operators
        assignId(buttonDivide, R.id.button_divide);
        assignId(buttonMultiply, R.id.button_multiply);
        assignId(buttonPlus, R.id.button_plus);
        assignId(buttonMinus, R.id.button_minus);
        assignId(buttonEquals, R.id.button_equals);
        // Numbers
        assignId(button0, R.id.button_0);
        assignId(button1, R.id.button_1);
        assignId(button2, R.id.button_2);
        assignId(button3, R.id.button_3);
        assignId(button4, R.id.button_4);
        assignId(button5, R.id.button_5);
        assignId(button6, R.id.button_6);
        assignId(button7, R.id.button_7);
        assignId(button8, R.id.button_8);
        assignId(button9, R.id.button_9);
        // Bottoms
        assignId(buttonAC, R.id.button_ac);
        assignId(buttonDot, R.id.button_dot);



    }

    @Override
    public void onClick(View v) {
        MaterialButton button = (MaterialButton) v;
        String buttonText = button.getText().toString();
        String dataToCalculate = solutionTv.getText().toString();

        if(buttonText.equals("AC")){
            solutionTv.setText("");
            resultTv.setText("0");
            return;
        }
        if(buttonText.equals("=")){
            solutionTv.setText(resultTv.getText());
            return;
        }
        if(buttonText.equals("C")){
            if(dataToCalculate.length() == 1){
                solutionTv.setText("");
                return;
            }if(dataToCalculate.isEmpty()){
                solutionTv.setText("");
                return;
            }
            dataToCalculate = dataToCalculate.substring(0, dataToCalculate.length()-1);

        }else{
            dataToCalculate = dataToCalculate+buttonText;
        }

        if(dataToCalculate.length()>16){
            solutionTv.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 20);
        }else{
            solutionTv.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 40);
        }


        solutionTv.setText(dataToCalculate);

        String finalResult = getResult(dataToCalculate);

        if(!finalResult.equals("Error")){
            if(finalResult.length()>10){
                resultTv.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 28);
            }else{
                resultTv.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 64);
            }
            resultTv.setText(finalResult);
        }
    }

    String getResult(String data){
        try{
            Context context = Context.enter();
            context.setOptimizationLevel(-1);
            Scriptable scriptable = context.initStandardObjects();
            String finalResult =  context.evaluateString(scriptable, data, "Javascript", 1,null).toString();
            if(finalResult.endsWith(".0")){
                finalResult = finalResult.substring(0, finalResult.length()-2);
            }
            return finalResult;

        }catch (Exception e){
            return "Error";
        }
    }
}