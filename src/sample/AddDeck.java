package sample;

import javafx.stage.*;
import javafx.scene.*;
import javafx.scene.layout.*;
import javafx.scene.control.*;
import javafx.geometry.*;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Paths;

public class AddDeck {

    private static String name;
    private static Deck tempDeck = new Deck();
    private static boolean confirmed = false;

    public static Deck display(String message){
        Stage window = new Stage();
        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle("Name your deck");
        window.setMinWidth(125);
        window.setMinHeight(125);

        tempDeck.setName("");

        Label namePrompt = new Label(message);
        TextField nameAnswer = new TextField("");
        Button confirmButton = new Button("Confirm");
        confirmButton.setAlignment(Pos.CENTER);
        confirmButton.setOnAction(e->{
            name = nameAnswer.getText();
            tempDeck.setName(name);
            confirmed = true;
            tempDeck.setCardCount(0);
            window.close();
        });

        VBox layout = new VBox(10);
        layout.getChildren().addAll(namePrompt, nameAnswer, confirmButton);
        Scene scene = new Scene(layout);
        window.setScene(scene);
        window.showAndWait();
        window.setOnCloseRequest(e->{
            if(confirmed) {
                saveDeck(tempDeck);
            }
        });

        return tempDeck;
    }

    public static void saveDeck(Deck deck){
        try{
            String deckDir = Paths.get(System.getProperty("user.dir"), "Decks", deck.getName()+".txt").toString();
            FileWriter fileWriter = new FileWriter(deckDir);
            fileWriter.write("");
            fileWriter.close();

        } catch(IOException e) {
            System.out.println("An error has occured.");
            e.printStackTrace();
        }
    }
}
