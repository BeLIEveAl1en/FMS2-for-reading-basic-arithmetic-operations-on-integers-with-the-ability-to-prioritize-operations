package com.artem.math.validator;

import javax.crypto.CipherSpi;

public class StateMachine {

    private char[] str = new char[0];
    private int position = 0;
    private int counter = 0;

    private final State state = new State(0);

    public ValidationResult validate(String inputStr){
        str = inputStr.toCharArray();
        ValidationResult result;
        for (position = 0; position < str.length; position++){
            if (!validationSymbol())
                return ValidationResult.unexpectedSymbol(str[position], position);
        }
        if (state.getState() == 3 && counter == 0){
            result = ValidationResult.valid();
        }
        else {
            result = ValidationResult.unexpectedEOF();
        }
        return result;
    }

    // 21 /_700 + 485
    public boolean validationSymbol() {

        char symbol = str[position];
        switch (state.getState()){
            case 0 :
                if (symbol == '-' || Character.isDigit(symbol)){
                    state.setState(1);
                }
                else if(symbol == '('){
                    counter++;
                    state.setState(0);
                }
                else {
                    return false;
                }
                return true;

            case 1 :
                if (isOperator(symbol)){
                    state.setState(2);
                }
                else if(!Character.isDigit(symbol) && !Character.isWhitespace(symbol)){
                    return false;
                }
                return true;

            case 2 :
                if(symbol == '-' || Character.isDigit(symbol)){
                    state.setState(3);
                }
                else if(symbol == '('){
                    counter++;
                    state.setState(0);
                }
                else if (!Character.isWhitespace(symbol)){
                    return false;
                }
                return true;

            case 3 :
                if(symbol == ')'){
                    counter--;
                    state.setState(3);
                }
                else if(Character.isWhitespace(symbol)) {
                    state.setState(1);
                }
                else if(!Character.isDigit(symbol) && !isOperator(symbol)){
                    return false;
                }
                return true;
        }
        return false;
    }

    public boolean isOperator(char symbol){
        switch (symbol){
            case '+' :
            case '/' :
            case '*' :
            case '-' :
                return true;

            default :
                return false;
        }
    }
}
