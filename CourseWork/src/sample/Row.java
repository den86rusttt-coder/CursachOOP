package sample;

import javafx.geometry.Pos;
import javafx.scene.control.TextField;

public class Row {


    private TextField textField1;
    private TextField textField2;
    private TextField textField3;
    private TextField textField4;
    private TextField textField5;
    private TextField textField6;
    private TextField textField7;

    Row(int quantity_field) {
        textField1 = new TextField();
        textField1.setAlignment(Pos.CENTER);
        switch (quantity_field) {
            case 2:
                textField2 = new TextField();
                break;
            case 3:
                textField2 = new TextField();
                textField3 = new TextField();
                break;
            case 4:
                textField2 = new TextField();
                textField3 = new TextField();
                textField4 = new TextField();
                break;
            case 5:
                textField2 = new TextField();
                textField3 = new TextField();
                textField4 = new TextField();
                textField5 = new TextField();
                break;
            case 6:
                textField2 = new TextField();
                textField3 = new TextField();
                textField4 = new TextField();
                textField5 = new TextField();
                textField6 = new TextField();
                break;
            case 7:
                textField2 = new TextField();
                textField3 = new TextField();
                textField4 = new TextField();
                textField5 = new TextField();
                textField6 = new TextField();
                textField7 = new TextField();
                break;
            default:
                System.out.println("error!");
        }

    }

    public TextField getTextField1() {
        return textField1;
    }

    public void setTextField1(TextField textField1) {
        this.textField1 = textField1;
    }

    public TextField getTextField2() {
        return textField2;
    }


    public void setTextField2(TextField textField2) {
        this.textField2 = textField2;
    }

    public TextField getTextField3() {
        return textField3;
    }

    public void setTextField3(TextField textField3) {
        this.textField3 = textField3;
    }

    public TextField getTextField4() {
        return textField4;
    }

    public void setTextField4(TextField textField4) {
        this.textField4 = textField4;
    }

    public TextField getTextField5() {
        return textField5;
    }

    public void setTextField5(TextField textField5) {
        this.textField5 = textField5;
    }

    public TextField getTextField6() {
        return textField6;
    }

    public void setTextField6(TextField textField6) {
        this.textField6 = textField6;
    }

    public TextField getTextField7() {
        return textField7;
    }

    public void setTextField7(TextField textField7) {
        this.textField7 = textField7;
    }
}