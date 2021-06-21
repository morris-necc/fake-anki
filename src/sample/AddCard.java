package sample;

import javafx.stage.*;
import javafx.scene.*;
import javafx.scene.layout.*;
import javafx.scene.control.*;
import javafx.geometry.*;

public class AddCard {

    public static Card display(String q, String a, String title){
        Card tempCard = new Card(q, a);
        Stage window = new Stage();
        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle(title);
        window.setMinWidth(250);
        window.setMinHeight(250);

        //Question TextField
        TextField question = new TextField(q);
        question.setMinHeight(110);
        question.setMinWidth(240);
        question.setAlignment(Pos.TOP_LEFT);
        question.setStyle("-fx-border-color: white");

        //Answer TextField
        TextField answer = new TextField(a);
        answer.setMinHeight(110);
        answer.setMinWidth(240);
        answer.setAlignment(Pos.TOP_LEFT);
        answer.setStyle("-fx-border-color: white");

        //Create 2 buttons
        HBox buttons = new HBox();

        Button yesButton = new Button(title);
        Button noButton = new Button("Cancel");

        yesButton.setOnAction(e -> {
            //read question and answer TextFields, put them into tempCard
            tempCard.setQuestion(question.getText());
            tempCard.setAnswer(answer.getText());
            window.close();
        });

        noButton.setOnAction(e -> {
            window.close();
        });

        buttons.getChildren().addAll(yesButton, noButton);
        buttons.setAlignment(Pos.CENTER);

        VBox layout = new VBox(10);
        layout.getChildren().addAll(question, answer, buttons);
        layout.setAlignment(Pos.CENTER);
        layout.setStyle("-fx-background-color: #2F2F31;");

        Scene scene = new Scene(layout);
        window.setScene(scene);
        window.showAndWait();

        return tempCard;
    }
}
