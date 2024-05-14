package com.example.calculadora;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import net.objecthunter.exp4j.Expression;
import net.objecthunter.exp4j.ExpressionBuilder;
import net.objecthunter.exp4j.operator.Operator;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView txtInput;
    private TextView txtOutput;
    private int openParenthesisCount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Buscar los TextViews por sus IDs
        txtInput = findViewById(R.id.Input);
        txtOutput = findViewById(R.id.Output);

        // Buscar los botones numéricos por sus IDs y establecer el OnClickListener
        findViewById(R.id.btn_0).setOnClickListener(this);
        findViewById(R.id.btn_1).setOnClickListener(this);
        findViewById(R.id.btn_2).setOnClickListener(this);
        findViewById(R.id.btn_3).setOnClickListener(this);
        findViewById(R.id.btn_4).setOnClickListener(this);
        findViewById(R.id.btn_5).setOnClickListener(this);
        findViewById(R.id.btn_6).setOnClickListener(this);
        findViewById(R.id.btn_7).setOnClickListener(this);
        findViewById(R.id.btn_8).setOnClickListener(this);
        findViewById(R.id.btn_9).setOnClickListener(this);

        // Buscar los botones de operaciones especiales por sus IDs y establecer el OnClickListener
        findViewById(R.id.btn_clear).setOnClickListener(this);
        findViewById(R.id.btn_delete).setOnClickListener(this);
        findViewById(R.id.btn_Corchete).setOnClickListener(this); // Cambiado de btn_Corchete a btn_Parenthesis
        findViewById(R.id.btn_Coma).setOnClickListener(this);

        // Buscar los botones de operaciones matemáticas por sus IDs y establecer el OnClickListener
        findViewById(R.id.btn_Suma).setOnClickListener(this);
        findViewById(R.id.btn_Resta).setOnClickListener(this);
        findViewById(R.id.btn_Multiplicacion).setOnClickListener(this);
        findViewById(R.id.btn_Division).setOnClickListener(this);
        findViewById(R.id.btn_Igual).setOnClickListener(this);

        //
        findViewById(R.id.btn_Pi).setOnClickListener(this);
        findViewById(R.id.btn_Euler).setOnClickListener(this);
        findViewById(R.id.btn_Raiz).setOnClickListener(this);
        findViewById(R.id.btn_X2).setOnClickListener(this);
        findViewById(R.id.btn_Logaritmo).setOnClickListener(this);
        findViewById(R.id.btn_Porcentaje).setOnClickListener(this);
        findViewById(R.id.btn_Factorial).setOnClickListener(this);

        findViewById(R.id.btn_Seno).setOnClickListener(this);
        findViewById(R.id.btn_Coseno).setOnClickListener(this);
        findViewById(R.id.btn_Tangente).setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        String currentText = txtInput.getText().toString();

        if (id == R.id.btn_clear) {
            txtInput.setText("");
            txtOutput.setText("");
            openParenthesisCount = 0;
        } else if (id == R.id.btn_delete) {
            if (!currentText.isEmpty()) {
                if (currentText.endsWith("(")) {
                    openParenthesisCount--;
                } else if (currentText.endsWith(")")) {
                    openParenthesisCount++;
                }
                txtInput.setText(currentText.substring(0, currentText.length() - 1));
                txtOutput.setText("");
            }
        } else if (id == R.id.btn_Corchete) {
            if (openParenthesisCount > 0 && !lastCharacterIsOperator(currentText)) {
                txtInput.setText(currentText + ")");
                openParenthesisCount--;
            } else {
                txtInput.setText(currentText + "(");
                openParenthesisCount++;
            }
        }

        else if (id == R.id.btn_Coma) {
            if (!currentText.contains(".")) {
                txtInput.setText(currentText + ".");
            }
        } else if (id == R.id.btn_Suma) {
            if (!lastCharacterIsOperator(currentText)) {
                txtInput.setText(currentText + "+");
            }
        } else if (id == R.id.btn_Resta) {
            if (!lastCharacterIsOperator(currentText)) {
                txtInput.setText(currentText + "-");
            }
        } else if (id == R.id.btn_Multiplicacion) {
            if (!lastCharacterIsOperator(currentText)) {
                txtInput.setText(currentText + "*");
            }
        } else if (id == R.id.btn_Division) {
            if (!lastCharacterIsOperator(currentText)) {
                txtInput.setText(currentText + "/");
            }
        } else if (id == R.id.btn_Igual) {

            calcularResultado();
        } else {
            // Asumiendo que todos los demás botones son numéricos
            txtInput.setText(currentText + ((TextView) v).getText().toString());
        }

        if (id == R.id.btn_Pi) {
            txtInput.setText(appendNumberOrOperator(currentText, "pi"));
        } else if (id == R.id.btn_Euler) {
            txtInput.setText(appendNumberOrOperator(currentText, "e"));
        } else if (id == R.id.btn_Raiz) {
            txtInput.setText(appendNumberOrOperator(currentText, "sqrt("));
        } else if (id == R.id.btn_X2) {
            txtInput.setText(appendNumberOrOperator(currentText, "^2"));
        } else if (id == R.id.btn_Logaritmo) {
            txtInput.setText(appendNumberOrOperator(currentText, "log("));
        } else if (id == R.id.btn_Porcentaje) {
            txtInput.setText(appendNumberOrOperator(currentText, "%"));
        } else if (id == R.id.btn_Factorial) {
            txtInput.setText(appendNumberOrOperator(currentText, "!"));
        }
    }

    private boolean lastCharacterIsOperator(String input) {
        return input.endsWith("+") || input.endsWith("-") || input.endsWith("*") ||
                input.endsWith("/") || input.endsWith("(");
    }

    private void calcularResultado() {
        String input = txtInput.getText().toString();
        // Reemplazar el símbolo de porcentaje (%) por su equivalente decimal (/100)
        input = input.replaceAll("%", "/100");

        try {
            // Añadir el operador factorial al ExpressionBuilder
            Expression expression = new ExpressionBuilder(input)
                    .operator(factorial) // Asegúrate de haber definido el operador factorial como se muestra arriba
                    .build();
            double resultado = expression.evaluate();

            // Verificar si el resultado es un número entero
            if (resultado == (long) resultado) {
                txtOutput.setText(String.format("%d", (long) resultado));
            } else {
                txtOutput.setText(String.format("%s", resultado));
            }
        } catch (Exception e) {
            txtOutput.setText("Error en la expresión");
        }
    }


    private String appendNumberOrOperator(String currentText, String numberOrOperator) {
        // Si el último carácter no es un operador o si es un paréntesis abierto, añade el número u operador
        if (!lastCharacterIsOperator(currentText) || numberOrOperator.equals("(")) {
            // Incrementa el contador de paréntesis abiertos si se añade un paréntesis abierto
            if (numberOrOperator.equals("sqrt(") || numberOrOperator.equals("log(") ||
                    numberOrOperator.equals("sin(") || numberOrOperator.equals("cos(") ||
                    numberOrOperator.equals("tan(")) {
                openParenthesisCount++;
            }
            return currentText + numberOrOperator;
        } else {
            // Si el último carácter es un operador y el usuario quiere añadir otro operador, reemplaza el último operador
            if (isOperator(numberOrOperator)) {
                return currentText.substring(0, currentText.length() - 1) + numberOrOperator;
            } else {
                return currentText;
            }
        }
    }

    // Añade los nuevos operadores a la función isOperator
    private boolean isOperator(String input) {
        return input.equals("+") || input.equals("-") || input.equals("*") ||
                input.equals("/") || input.equals("^") || input.equals("sqrt(") ||
                input.equals("log(") || input.equals("%") || input.equals("!") ||
                input.equals("sin(") || input.equals("cos(") || input.equals("tan(");
    }

    Operator factorial = new Operator("!", 1, true, Operator.PRECEDENCE_POWER + 1) {
        @Override
        public double apply(double... args) {
            final int arg = (int) args[0];
            if ((double) arg != args[0]) {
                throw new IllegalArgumentException("Operand for factorial has to be an integer");
            }
            if (arg < 0) {
                throw new IllegalArgumentException("The operand of the factorial can not be less than zero");
            }
            double result = 1;
            for (int i = 1; i <= arg; i++) {
                result *= i;
            }
            return result;
        }
    };




}


