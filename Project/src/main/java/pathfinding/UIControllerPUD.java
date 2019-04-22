package pathfinding;

import application.UIController;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXToggleButton;
import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import entities.emailDirection;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.util.List;
import java.util.regex.Pattern;

public class UIControllerPUD extends UIController {

    public static final String ACCOUNT_SID = "AC176f9cd821ffa8dcad559ceecad9ecf1";
    public static final String AUTH_TOKEN = "ab7f1a58335f47c98bac61b471920dfe";
    @FXML
    private TextArea directions; //the actual directions
    @FXML
    private JFXButton printDirections; //the option to print a receipt
    @FXML
    private JFXButton textDirections; //the option to text the directions to a cell phone
    @FXML
    private JFXButton emailDirections; //the option to email the directions
    @FXML
    private ScrollPane directionsBox; //gives the ability to sroll with directionsS
    @FXML
    private TextField phoneNumber; //the prompt box for a phone number
    @FXML
    private TextField email; //the prompt box for an email
    @FXML
    private JFXButton okButton; //ok button to exit confirmation window
    @FXML
    private JFXComboBox floorSelect; //drop down to select which floor's directions to display

    @FXML
    public void onShow(){
        floorSelect.getItems().addAll("All", "L2", "L1", "G", "1", "2", "3", "4");
    }
    @FXML
    public void setDirections(List<List<List<String>>> message) {
        String returnText = "";

        for(int i = 0; i < message.size(); i++){
            for (int j = 0; j < message.get(i).size(); j++) {
                for (int k = 0; k < message.get(i).get(j).size(); k++) {
                    returnText += message.get(i).get(j).get(k);
                }
            }
        }
        directions.setText(returnText); //  sets the text received from the pathfinding

    }

    @FXML
    public void sendEmail(ActionEvent event) {
        if (!isValidEmail(email.getText())) {
            popupMessage("Please Enter A Valid Email", true);
        } else {
            String receivingEmail = email.getText();
            emailDirection.sendEmail(directions.getText(), receivingEmail);
            popupMessage("An email has been sent to you!", false);
        }
    }

    @FXML
    public void sendText(ActionEvent event) {
        String number = phoneNumber.getText();

        if (!number.matches("\\d{10}")) {
            popupMessage("Please Enter A Valid Phone Number", true);

        } else {
            Twilio.init(ACCOUNT_SID, AUTH_TOKEN);
            Message.creator(
                    new com.twilio.type.PhoneNumber("+1" + number),
                    new com.twilio.type.PhoneNumber("+17472290044"),
                    directions.getText())
                    .create();

            popupMessage("A text message has been sent to you!", false);
        }

    }

    @FXML
    public void setOkButton() {
        Stage stage = (Stage) okButton.getScene().getWindow();
        stage.close();
    }

    private static boolean isValidEmail(String email)
    {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\."+
                "[a-zA-Z0-9_+&*-]+)*@" +
                "(?:[a-zA-Z0-9-]+\\.)+[a-z" +
                "A-Z]{2,7}$";

        Pattern pat = Pattern.compile(emailRegex);
        if (email == null)
            return false;
        return pat.matcher(email).matches();
    }
}
