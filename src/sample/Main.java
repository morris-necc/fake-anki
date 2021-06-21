package sample;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.*;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import java.io.File;
import java.io.FileNotFoundException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;


public class Main extends Application implements EventHandler<ActionEvent>{

    Stage window;
    Label pageNumber;
    Button previous, next, addDeck, removeDeck;
    int page = 1, total_pages;
    ArrayList<Label> deckView = new ArrayList<>();
    ArrayList<Deck> decks = new ArrayList<>();

    @Override
    public void start(Stage primaryStage) throws Exception {
        //set the stage
        window = primaryStage;
        window.setTitle("Fake Anki");

        //read number of decks
        readFiles();

        //Grid layout
        GridPane grid = new GridPane();
        grid.setPadding(new Insets(10, 10, 10, 10));
        grid.setVgap(10);
        grid.setHgap(25);

        //Headings
        Label deckLabel = new Label("Decks");
        deckLabel.setStyle("-fx-font-weight: bold; -fx-text-fill: white; -fx-font-size: 14;");
        deckLabel.setOnMouseClicked((mouseEvent) -> {
            System.out.println("test");
        });
        GridPane.setConstraints(deckLabel, 4, 3);

        Label cards = new Label("Cards");
        cards.setStyle("-fx-font-weight: bold; -fx-text-fill: white; -fx-font-size: 14;");
        GridPane.setConstraints(cards, 8, 3);

        //Decks display
        total_pages = 1 + Math.floorDiv(decks.size(), 10);
        int size = (decks.size() <= 10) ? decks.size(): 10;
        // initialize deck and cards
        for(int i = 0; i < 10; i++){
            deckView.add(new Label("placeholder"));
            GridPane.setConstraints(deckView.get(i),4,4+i);
            deckView.get(i).setVisible(false);
            deckView.get(i).setStyle("-fx-text-fill: white; -fx-font-size: 14;");

            //set onclick event
            int finalI = i;
            deckView.get(i).setOnMouseClicked(e -> {
                StudyBox.display(decks.get(finalI).getName(), decks.get(finalI));
                try {
                    readFiles();
                    refreshPage();
                } catch (FileNotFoundException fileNotFoundException) {
                    fileNotFoundException.printStackTrace();
                }
            });

            grid.getChildren().add(deckView.get(i));
        }
        for(int i = 10; i < 20; i++){
            deckView.add(new Label("placeholder"));
            GridPane.setConstraints(deckView.get(i), 8, 4+(i-10));
            deckView.get(i).setVisible(false);
            deckView.get(i).setStyle("-fx-text-fill: white; -fx-font-size: 14;");
            grid.getChildren().add(deckView.get(i));
        }

        //show deck and cards
        for(int j = 0; j < size; j++){
            deckView.get(j).setText(decks.get(j).getName());
            deckView.get(j).setVisible(true);
        }
        for(int j = 10; j < 10+size; j++){
            deckView.get(j).setText(Integer.toString(decks.get(j-10).getCardCount()));
            deckView.get(j).setVisible(true);
        }

        //prev/next page
        previous = new Button("<-");
        next = new Button("->");

        //button actions
        previous.setOnAction(this);
        next.setOnAction(this);

        //page labels
        pageNumber = new Label(String.format("%d of %d pages", page, total_pages));
        pageNumber.setStyle("-fx-text-fill: white; -fx-font-size: 12;");
        GridPane.setConstraints(previous, 4, 15);
        GridPane.setConstraints(next, 8, 15);
        GridPane.setConstraints(pageNumber, 6, 15);

        //Bottom
        addDeck = new Button("Add Deck");
        addDeck.setOnAction(this);
        GridPane.setConstraints(addDeck, 5, 17);
        removeDeck = new Button("Remove Deck");
        removeDeck.setOnAction(this);
        GridPane.setConstraints(removeDeck, 7, 17);

        //Put all those elements in so that they're actually visible
        grid.getChildren().addAll(deckLabel, cards, previous, next, pageNumber, addDeck, removeDeck);
        grid.setStyle("-fx-background-color: #2F2F31;");
        Scene deckList = new Scene(grid, 630, 500);
        window.setScene(deckList);
        window.show();
    }

    public void refreshPage(){
        for(int i = (page-1)*10; i < page*10; i++){
            //refresh cards
            int tempI = i + 10;
            if(i >= decks.size()){
                deckView.get(i - (page-1)*10).setVisible(false);
                deckView.get(tempI - (page-1)*10).setVisible(false);
            } else {
                deckView.get(i - (page-1)*10).setText(decks.get(i).getName());
                deckView.get(i - (page-1)*10).setVisible(true);
                deckView.get(tempI - (page-1)*10).setText(Integer.toString(decks.get(i).getCardCount()));
                deckView.get(tempI - (page-1)*10).setVisible(true);
            }
        }

        pageNumber.setText(String.format("%d of %d pages", page, total_pages));
    }

    @Override
    public void handle(ActionEvent event){
        if(event.getSource() == previous){
            if(page > 1){
                page--;
                refreshPage();
            }
        }
        if(event.getSource() == next){
            if(page < total_pages){
                page++;
                refreshPage();
            }
        }
        if(event.getSource() == addDeck){
            addDeck();
            refreshPage();
        }
        if(event.getSource() == removeDeck){
            removeDeck();
            refreshPage();
        }
    }

    public void readFiles() throws FileNotFoundException {
        decks.clear();
        File deckDir = Paths.get(System.getProperty("user.dir"), "Decks").toFile();
        ArrayList<File> deckFiles = new ArrayList<>(Arrays.asList(deckDir.listFiles()));
        String separator = "====separator====";
        for(File file: deckFiles){
            Scanner reader = new Scanner(file);
            Deck tempDeck = new Deck();
            while(reader.hasNextLine()){ //scan and split into card
                String[] data = reader.nextLine().split(separator, 2);
                Card card = new Card(data[0], data[1]);
                tempDeck.addCard(card);
            }
            tempDeck.setName(file.getName().split(".txt")[0]); //easily breakable but ok
            tempDeck.setCardCount(tempDeck.getCards().size());
            decks.add(tempDeck);
        }
    }

    public void addDeck(){
        Deck tempDeck = AddDeck.display("Enter the name of the new deck");
        if(!tempDeck.getName().equals("")){
            decks.add(tempDeck);
        }
    }

    public void removeDeck(){
        Deck removedDeck =  AddDeck.display("Enter the name of the deck you want to delete");
        if(!removedDeck.getName().equals("")){
            //remove the deck with the same name from decks
            for(int i = 0; i < decks.size(); i++){
                if(decks.get(i).getName().equals(removedDeck.getName())){
                    decks.remove(i);
                }
            }
            //delete the txt file with the same name
            String deckDir = Paths.get(System.getProperty("user.dir"), "Decks", removedDeck.getName()+".txt").toString();
            File file = new File(deckDir);
            file.delete();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
