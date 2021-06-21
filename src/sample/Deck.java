package sample;

import java.util.ArrayList;

public class Deck {
    String name;
    ArrayList<Card> cards = new ArrayList<>();
    int cardCount;

    Deck(){

    }

    public ArrayList<Card> getCards() {
        return cards;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setCards(ArrayList<Card> cards) {
        this.cards = cards;
    }

    public void addCard(Card c){
        getCards().add(c);
        this.cardCount++;
    }

    public void removeCard(int index){
        cards.remove(index);
        this.cardCount--;
    }

    public int getCardCount() {
        return cardCount;
    }

    public void setCardCount(int cardCount) {
        this.cardCount = cardCount;
    }
}
