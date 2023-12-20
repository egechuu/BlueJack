import java.util.Scanner;
public class Main {
    
    public static void main(String[] args) {
        
        Deck gamedeck = new Deck(40);
        gamedeck.gameDeck();
        gamedeck.shuffle();

        Deck playerHand = new Deck(4);
        Deck playerDeck = new Deck(10);
        Deck playerBoard = new Deck(9);
        Player human = new Player(gamedeck, playerHand, playerDeck, playerBoard);
        human.setPlayerName();

        Deck computerHand = new Deck(4);
        Deck computerDeck = new Deck(10);
        Deck computerBoard = new Deck(9);
        Player computer = new Player(gamedeck, computerHand,computerDeck, computerBoard);
        computer.setPlayerName("Computer");

        gamedeck.deal(computerDeck, playerDeck);
        
        gamedeck.generatePlayerCards(playerDeck);
        gamedeck.generatePlayerCards(computerDeck);

        playerDeck.setHand(playerHand);
        computerDeck.setHand(computerHand);
        
        while (human.roundWon < 3 && computer.roundWon < 3) {
            human.askPlayerMove();
        
            // Check if the human won the round
            if (human.score == 20 && !human.stands && !human.bust) {
                human.roundWon++;
                System.out.println("YOU WIN!");
                human.resetRound(human, computer);
            }
        
            computer.computerMakeMove();
            
            // Check if the computer won the round
            if (computer.score == 20 && !computer.stands && !computer.bust) {
                computer.roundWon++;
                System.out.println("COMPUTER WINS.");
                human.resetRound(human, computer);
            }
        
            human.checkWinner(human, computer);
            computerBoard.printBoard(computer);
        }
        System.out.println("THE GAME ENDS HERE.");
        if(human.roundWon>computer.roundWon)
        System.out.println("CONGRATS, YOU WIN.");
        GameHistory gameHistory = new GameHistory(new Log(human, computer));
        Scanner scanner = new Scanner(System.in);
        int option;
        System.out.println("Press 0 to see game history. 1 to exit.");
        System.out.print("Enter your choice: ");
        while(true) {
            try {
                option = scanner.nextInt();
                if(option == 0 || option == 1) {
                    switch (option) {
                        case 0:
                            gameHistory.PrintGameHistory();
                            break;
                        case 1:
                            System.out.println("Exiting the game. Goodbye!");
                            scanner.close();
                            System.exit(0);
                    }
                }
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid index.");
            }
        }
    }
}
