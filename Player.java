import java.util.Scanner;
import java.util.Random;

public class Player {
    public boolean bust = false;
    public boolean stands = false;
    public String name;
    public Deck gameDeck;
    protected Deck hand;
    protected Deck board;
    protected Deck playerDeck;
    protected int score;
    public int roundWon;
    public Scanner sc;

    public Player(Deck gameDeck, Deck hand, Deck playerDeck, Deck board) {
        sc = new Scanner(System.in);
        this.gameDeck = gameDeck;
        this.playerDeck = playerDeck;
        this.hand = hand;
        this.board = board;
        this.score = 0;
        this.roundWon = 0;
    }

    public Card draw() {
        if (gameDeck.getDeck().length == 0)
            System.out.println("The game deck is now empty.");;
        Card temp = gameDeck.getLastCard();
        if (temp != null) {
            board.addCard(temp);
            gameDeck.removeCard(temp);
            return temp;
        }
        return null;
    }

    public void setPlayerName(String name) {
        this.name = name;
    }

    public Card[] getPlayerHand() {
        return hand.getDeck();
    }

    public Card[] getPlayerDeck() {
        return playerDeck.getDeck();
    }

    public Card[] getPlayerBoard() {
        return board.getDeck();
    }

    public void setPlayerName() {
        try {
            System.out.println("Please enter your name.");
            while (true) {
                name = sc.nextLine();
                if (name != null) {
                    break;
                } else {
                    System.out.println("Please enter a valid username.");
                    continue;
                }
            }
        } catch (java.util.NoSuchElementException e) {
            System.out.println("No input provided. Using default name 'Player'");
            name = "Player";
        }
    }

    public void askPlayerMove() {
        hand.printDeck();
        board.printBoard(this);
        board.calculateScore(this);
        System.out.println("YOUR TOTAL SCORE: " + score);
        System.out.println("----------------------");
        System.out.println("Choose a move (0-2):");
        System.out.println("0-DRAW");
        System.out.println("1-PLAY");
        System.out.println("2-STAND");
        boolean flag = true;
        while (flag) {
            try {
                int choice = Integer.parseInt(sc.next());
                if (choice >= 0 && choice <= 2) {
                    System.out.println("----------------------");
                    switch (choice) {
                        case 0:
                            playDraw();
                            flag = false;
                            break;
                        case 1:
                            if (hand == null) {
                                playerDeck.setHand(hand);
                            }
                            stands = false;
                            System.out.println("YOU PLAYED: " + playerPlay().printCard());
                            board.calculateScore(this);
                            System.out.println("YOUR TOTAL SCORE: " + score);
                            if (score > 20) {
                                bust = true;
                                score = 0;
                            }
                            flag = false;
                            break;
                        case 2:
                            stands = true;
                            flag = false;
                            break;
                    }
                }
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid integer.");
                continue;
            }
        }
    }

    public void playDraw() {
        stands = false;
        System.out.println("YOU DRAW :" + draw().printCard());
        board.calculateScore(this);
        board.printBoard(this);
        System.out.println("YOUR TOTAL SCORE: " + score);
        System.out.println("----------------------");
        System.out.println("Choose a move (0-1)");
        System.out.println("0-PLAY");
        System.out.println("1-END TURN");
        boolean drawLoop = true;
        while (drawLoop) {
            try {
                int move = Integer.parseInt(sc.next());
                if (hand.getDeck().length == 0) {
                    playerDeck.setHand(hand);
                }
                switch (move) {
                    case 0:
                        if (hand == null) {
                            playerDeck.setHand(hand);
                        }
                        System.out.println("YOU PLAYED: " + playerPlay().printCard());
                        board.calculateScore(this);
                        System.out.println("YOUR TOTAL SCORE: " + score);
                        if (score > 20) {
                            bust = true;
                            score = 0;
                        }
                        drawLoop = false;
                        break;
                    case 1:
                        drawLoop = false; // Break out of the draw loop
                        break;
                }
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number.");
            }
        }
    }

    public boolean checkWinner(Player human, Player computer) {
        if (human.score > 20) {
            human.bust = true;
            System.out.println("YOU BUST. COMPUTER WON THIS ROUND.");
            resetRound(human, computer);
            return true;
        }
        if (computer.bust) {
            human.roundWon++;
            System.out.println("YOU WON THIS ROUND!");
            resetRound(human, computer);
            return true;
        } else if (human.bust) {
            computer.roundWon++;
            System.out.println("COMPUTER WON THIS ROUND.");
            resetRound(human, computer);
            return true;
        } else if (human.board.getSize() == 9 && computer.board.getSize() == 9 && !human.bust && !computer.bust) {
            if (human.score > computer.score) {
                human.roundWon++;
                System.out.println("YOU WON THIS ROUND!");
            } else if (human.score < computer.score) {
                computer.roundWon++;
                System.out.println("COMPUTER WON THIS ROUND.");
            } else {
                System.out.println("IT'S A TIE!");
            }
            resetRound(human, computer);
            return true;
        } else if (human.stands && computer.stands) {
            if (computer.score <= 20 && human.score < computer.score) {
                computer.roundWon++;
                System.out.println("COMPUTER WON THIS ROUND.");
            } else if (computer.score == human.score) {
                System.out.println("IT'S A TIE!");
            } else {
                human.roundWon++;
                System.out.println("YOU WON THIS ROUND!");
            }
            resetRound(human, computer);
            return true;
        }
        return false;
    }

    public void resetRound(Player human, Player computer) {
        human.score = 0;
        computer.score = 0;
        human.board = new Deck(9);
        computer.board = new Deck(9);
    }

    public Card playerPlay() {
        if (hand.getDeck().length == 0) {
            playerDeck.setHand(hand);
        }
        int handSize = hand.getSize();
        System.out.println("Choose a card to play: (0-" + (handSize - 1) + ")");
        hand.printDeck();
        while (true) {
            try {
                int option = Integer.parseInt(sc.next());
                if (option >= 0 && option < handSize && getPlayerHand()[option] != null) {
                    Card card = getPlayerHand()[option];
                    hand.removeCard(card);
                    board.addCard(card);
                    return card;
                } else {
                    System.out.println("Invalid option. Please enter a valid index.");
                }
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("An error occurred. Please try again.");
            }
        }
    }

    // COMPUTER'S PLAYING METHODS

    public void computerMakeMove() {
        Random random = new Random();
        if (hand.getDeck().length == 0) {
            playerDeck.setHand(hand);
        }
        if (score <= 15) {
            if (random.nextBoolean()) {
                stands = false;
                System.out.println("THE COMPUTER DRAWS: " + draw().printCard());
            } else {
                stands = true;
                System.out.println("THE COMPUTER STANDS. YOUR TURN.");
            }
        } else if (score <= 19 && score > 15) {
            stands = true;
            System.out.println("THE COMPUTER STANDS. YOUR TURN.");
        } else if (score > 20) {
            if (hand.search(new Card("flipcard", "+/-")) != -1) {
                stands = false;
                playFlip();
            } else {
                System.out.println("COMPUTER BUSTS. ");
                bust = true;
                return;
            }
        } else if (hand.search(new Card("double", "x2")) != -1 && board.getLastCard().value + score <= 20) {
            stands = false;
            playDouble();
        } else {
            stands = false;
            play();
        }
        board.calculateScore(this);
        System.out.println("COMPUTER'S TOTAL SCORE: " + score);
    }

    public void playDouble() {
        Card doubleCard = new Card("double", "X2");
        hand.removeCard(doubleCard);
        board.addCard(doubleCard);
        System.out.println("----------------------");
        System.out.println("COMPUTER PLAYED DOUBLECARD.");

    }

    public void playFlip() {
        Card flipCard = new Card("flipcard", "+/-");
        hand.removeCard(flipCard);
        board.addCard(flipCard);
        System.out.println("----------------------");
        System.out.println("COMPUTER PLAYED FLIPCARD.");
    }

    public void play() {
        if (hand == null) {
            playerDeck.setHand(hand);
        }
        Card card = hand.getLastCard();
        hand.removeCard(card);
        board.addCard(card);
        System.out.println("----------------------");
        System.out.println("COMPUTER PLAYED: " + card.printCard());
    }
}
