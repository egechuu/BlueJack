import java.util.Random;
public class Deck {
    protected Card[] deck;
    protected int index; //element number


    public Deck(int capacity) {
        this.deck = new Card[capacity];
        this.index = 0;
    }

    public Card[] getDeck() {
        return this.deck;
    }

    public int getSize() {
        return index;
    }

    public void addCard(Card card) {
        if(deck.length==0 || deck.length==index) {
            deck = new Card[9];
            index=0;
        }
        if(card!=null){
            if(index==0) {
                deck[0] = card;
            }
            else if(deck[index] == null) {
                deck[index] = card;
            }
            index++;
        }
    }

    public Card getLastCard() {
        return this.deck[this.deck.length-1];
    }

    public Card getSecondLastCard() {
        if(deck[deck.length-2]!= null) {
            return this.deck[this.deck.length-2];
        }
        return null;
    }

    public void removeCard(Card card) {
        Card[] removeDeck = new Card[this.deck.length - 1];
        int removal = search(card);
        if(removal!=-1) {
            for (int i = 0; i < removal; i++) {
                removeDeck[i] = deck[i];
            }
            for (int i = removal; i < removeDeck.length; i++) {
                if (i + 1 <= index && deck[i + 1] != null) {
                    removeDeck[i] = deck[i + 1];
                }
            }
            deck = removeDeck;
            index--;
        }
    }
    
    public String toString() {
        String deckToString = "{ ";
        if(deck != null) {
            for (Card card : deck) {
                if(card!=null) {
                    deckToString += card.printCard();
                    deckToString += "  ";
                }
            }
        }
        deckToString += "}";
        return deckToString; 
    }

    public String toStringWithIndex() {
        String deckToString = "{ ";
        int i = 0;
        for (Card card : deck) {
            if(card!=null) {
                deckToString += i + ".";
                deckToString += card.printCard();
                deckToString += "  ";
                i++;
            }
        }
        deckToString += "}";
        return deckToString; 
    }

    public void printDeck() {
        String boardText;
        boardText = "--- --- --- --- --- YOUR HAND --- --- --- --- ---";
       
        String text = toStringWithIndex();
    
        int totalWidth = 100; //  the width of the screen 
        int paddingBoard = (totalWidth - boardText.length()) / 2;
        int paddingText = (totalWidth - text.length()) / 2;
    
        // Center-align the board text using printf
        System.out.printf("%" + paddingBoard + "s%s%" + paddingBoard + "s%n%n", "", boardText, "");
    
        // Center-align the cards array text using printf
        System.out.printf("%" + paddingText + "s%s%" + paddingText + "s%n%n", "", text, "");
    
        // Separator line
        System.out.printf("%" + paddingBoard + "s%s%" + paddingBoard + "s%n", "", "-------------------------------------------------", "");
    }

    public int search(Card card) {
        for (int i = 0; i < deck.length; i++) {
            if (deck[i] != null && deck[i].equals(card)) {
                return i;
            }
        }
        return -1;
    }    
    
    public void gameDeck() {
        String[] colour = {"B", "Y", "G","R"};
        boolean sign = true;
        for(int k=0; k<4; k++) {
            for(int i=0; i<10; i++) 
            addCard(new Card(colour[k], i+1, sign));
        }
    }
    
    public void shuffle() {
        Random rand = new Random(System.currentTimeMillis());
        for (int i = 0; i<40; i++) {
            int swapIndex = rand.nextInt(40 - i) + i;
            Card temp = deck[i];
            deck[i] = deck[swapIndex];
            deck[swapIndex] = temp;
        }
    }

    public void setHand(Deck hand) {
        Random r = new Random();
        for (int i = 0; i < 4; i++) {
            int cardInd = r.nextInt(deck.length);
            if(deck[cardInd] != null) {
                hand.addCard(deck[cardInd]);
                removeCard(deck[cardInd]);
            }
        }
    }

    public void deal(Deck computerDeck, Deck playerDeck) {
        for (int i = 0; i < 5; i++) {
            if(deck[i] != null) {
                computerDeck.addCard(deck[i]);
            }
            if(deck[deck.length-1-i] != null) {
                playerDeck.addCard(deck[deck.length-1-i]);
            }
        }
        
        // Remove dealt cards from the deck
        for (int i = 0; i < 5; i++) {
            if(deck[i] != null) {
                removeCard(deck[i]);
            }
            if(deck[deck.length-1-i] != null) {
                removeCard(deck[deck.length-1-i]);
            }
        }
        
    }

    public void generatePlayerCards(Deck playerDeck) {
        Random r = new Random();
        boolean sign = false;
        // 3 random signed cards 
        Card[] signed = new Card[3];
        for (int i = 0; i < 3; i++) {
            for (Card card : deck) {
                if(card!=null && card.value <=6 && card.value >=1) {
                    signed[i] = card;
                    removeCard(card);
                    break;
                }
            }
        }
        for (Card card : signed) {
            sign = r.nextBoolean();
            if(!sign) {
                card.sign = false;
                card.value *= -1;
            }
            playerDeck.addCard(card);
        }
        //2 special or normal signed cards 
        int dice = r.nextInt(1,11);
        if(dice<=8) {
            Card[] cards = new Card[2];
            for(int i=0; i<2; i++) {
                for (Card card : deck) {
                    if(card!=null && card.value <=6 && card.value >=1) {
                        cards[i] = card;
                        removeCard(card);
                        break;
                    }
                }
            }
            for (Card card : cards) {
                    sign = r.nextBoolean();
                    if(!sign) {
                        card.sign = false;
                        card.value *= -1;
                    }
                    playerDeck.addCard(card);
            }
        }else if(dice==9) {
            Card flipCard = new Card("flipcard", "+/-");
            playerDeck.addCard(flipCard);
        }else if(dice==10) {
            Card doubleCard = new Card("double", "X2");
            playerDeck.addCard(doubleCard);
        }
    }

    public void printBoard(Player player) {

        String boardText;
        //if the player is a computer 
        if(player.name.equals("Computer")) {
            boardText = "--- --- --- --- --- C O M P U T E R' S  B O A R D --- --- --- --- ---";
        } else { // if the player is a human
            boardText = "--- --- --- --- ---      Y O U R  B O A R D       --- --- --- --- ---";
        }
        
        String text = toString();
        int totalWidth = 100; //  the width of the screen 
        int paddingBoard = (totalWidth - boardText.length()) / 2;
        int paddingText = (totalWidth - text.length()) / 2;
    
        // Center-align the board text using printf
        System.out.printf("%" + paddingBoard + "s%s%" + paddingBoard + "s%n%n", "", boardText, "");
    
        // Center-align the cards array text using printf
        System.out.printf("%" + paddingText + "s%s%" + paddingText + "s%n%n", "", text, "");
    
        // Separator line
        System.out.printf("%" + paddingBoard + "s%s%" + paddingBoard + "s%n", "", "----------------------------------------------------------------------", "");
    }
    
    public void calculateScore(Player player) {
        player.score = 0;
        if(player.board == null) return;
        for (Card card : player.getPlayerBoard()) {
            if(card==null) return;
            if(card.name == null) {
                player.score += card.value;
            }else if(card.name != null) {
                if(card.name == "flipcard") {
                    Card secondLast = player.board.getSecondLastCard();
                    if(secondLast != null) {
                        player.score -= secondLast.value;
                        secondLast.value *= -1;
                        player.score += secondLast.value;
                        player.board.removeCard(card);
                    }
                }else if(card.name == "double") {
                    Card secondLast = player.board.getSecondLastCard();
                    if(secondLast != null) {
                        player.score -= secondLast.value;
                        secondLast.value *= 2;
                        player.score += secondLast.value;
                        player.board.removeCard(card);
                    }
                }
            }
        }
    }
}