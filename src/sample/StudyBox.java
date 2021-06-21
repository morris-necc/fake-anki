package sample;

import javafx.stage.*;
import javafx.scene.*;
import javafx.scene.layout.*;
import javafx.scene.control.*;
import javafx.geometry.*;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Paths;

public class StudyBox {
    private static Stage window;
    private static Label question, answer, counter;
    private static int index = 0;
    private static boolean empty;

    public static void display(String title, Deck deck){
        index = 0;
        window = new Stage();

        empty = deck.cards.isEmpty();

        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle(title);
        window.setMinWidth(500);
        window.setMinHeight(500);

        //Question
        question = new Label();
        if(!empty){
            question.setText(deck.cards.get(0).getQuestion());
        } else {
            question.setVisible(false); //set to true when a card is added
        }
        question.setMinHeight(220);
        question.setMinWidth(480);
        question.setAlignment(Pos.TOP_LEFT);
        question.setStyle("-fx-text-fill: white; -fx-font-size: 12;-fx-border-color: white");

        //Middle part
        HBox centre = new HBox();
        counter = new Label();
        counter.setStyle("-fx-text-fill: white;");
        Button toggleVisibilityBtn = new Button("Show/hide answer");
        toggleVisibilityBtn.setOnAction(e -> {
            answer.setVisible(!answer.isVisible());
        });
        if(!empty){
            counter.setText((index + 1) +"/"+ deck.cardCount);
        } else {
            counter.setText("Deck is currently empty! Press the button below to add a card");
            toggleVisibilityBtn.setVisible(false); //set to true when new card is added
        }
        centre.getChildren().addAll(counter, toggleVisibilityBtn);
        centre.setAlignment(Pos.CENTER);

        //Answer
        answer = new Label();
        if(!empty){
            answer.setText(deck.cards.get(0).getAnswer());
        }
        answer.setMinHeight(220);
        answer.setMinWidth(480);
        answer.setAlignment(Pos.TOP_LEFT);
        answer.setStyle("-fx-text-fill: white; -fx-font-size: 12;-fx-border-color: white");
        answer.setVisible(false);

        //Bottom area
        HBox footer = new HBox();
        Button nextCardBtn = new Button("Next card");
        nextCardBtn.setOnAction(e -> nextCard(deck));

        Button prevCardBtn = new Button("Previous card");
        prevCardBtn.setOnAction(e -> prevCard(deck));

        Button addCard = new Button("Add Card");

        Button editCard = new Button("Edit Card");
        editCard.setOnAction(e->{
            Card currentCard = AddCard.display(deck.cards.get(index).getQuestion(), deck.cards.get(index).getAnswer(), "Edit Card");
            deck.cards.set(index, currentCard);
            updateCard(index, deck);
        });

        Button removeCard = new Button("Remove Card");
        removeCard.setOnAction(e -> {
            deck.removeCard(index);
            if(deck.cardCount == 0){
                empty = true;
                counter.setText("Deck is currently empty! Press the button below to add a card");
                question.setVisible(false);
                answer.setVisible(false);
                toggleVisibilityBtn.setVisible(false);
                nextCardBtn.setVisible(false);
                prevCardBtn.setVisible(false);
                editCard.setVisible(false);
                removeCard.setVisible(false);
            }
            prevCard(deck);
        });

        addCard.setOnAction(e -> {
            Card newCard = AddCard.display("Enter question here", "Enter answer here", "Add Card");
            if(!newCard.getQuestion().equals("Enter question here")){
                deck.addCard(newCard);

                if(empty){
                    empty = false;
                    question.setVisible(true);
                    toggleVisibilityBtn.setVisible(true);
                    nextCardBtn.setVisible(true);
                    prevCardBtn.setVisible(true);
                    editCard.setVisible(true);
                    removeCard.setVisible(true);
                }
            }
            updateCard(index, deck);
        });

        //if empty
        if (empty) {
            nextCardBtn.setVisible(false);
            prevCardBtn.setVisible(false);
            editCard.setVisible(false);
            removeCard.setVisible(false);
        }

        footer.getChildren().addAll(prevCardBtn, editCard, addCard, removeCard, nextCardBtn);
        footer.setAlignment(Pos.CENTER);

        //Entire layout
        VBox layout = new VBox(10);
        layout.getChildren().addAll(question, centre, answer, footer);
        layout.setAlignment(Pos.CENTER);
        layout.setStyle("-fx-background-color: #2F2F31;");

        Scene scene = new Scene(layout);
        window.setOnCloseRequest(e ->{
            saveDeck(deck);
            window.close();
        });
        window.setScene(scene);
        window.showAndWait();
    }

    public static void nextCard(Deck deck){
        //Moves to next card
        if(index < deck.cardCount - 1){
            updateCard(++index,deck);
        }
    }

    public static void prevCard(Deck deck){
        //Moves to previous card
        if(index > 0) {
            updateCard(--index, deck);
        }
    }

    public static void updateCard(int index, Deck deck){
        if(!empty){
            question.setText(deck.cards.get(index).getQuestion());
            answer.setText(deck.cards.get(index).getAnswer());
            answer.setVisible(false);
            counter.setText((index + 1) +"/"+ deck.cardCount);
        }
    }

    public static void saveDeck(Deck deck){
        try{
            String deckDir = Paths.get(System.getProperty("user.dir"), "Decks", deck.getName()+".txt").toString();
            FileWriter fileWriter = new FileWriter(deckDir);
            for(int i = 0; i < deck.cardCount; i++){
                Card tempCard = deck.cards.get(i);
                fileWriter.write(tempCard.getQuestion()+ "====separator====" +tempCard.getAnswer()+"\n");
            }
            fileWriter.close();

        } catch(IOException e) {
            System.out.println("An error has occured.");
            e.printStackTrace();
        }
    }
}
