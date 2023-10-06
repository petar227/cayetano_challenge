import java.util.*;

public class CardGame {
    private int balance;
    private List<String> deck;
    private List<String> playerHand;

    public CardGame() {
        initializeDeck();
        shuffleDeck();
        balance = -1;
        playerHand = new ArrayList<>();
    }

    private void initializeDeck() {
        deck = new ArrayList<>();
        String[] ranks = {"2", "3", "4", "5", "6", "7", "8", "9", "10", "Jack", "Queen", "King", "Ace"};
        String[] suits = {"Spades", "Hearts", "Diamonds", "Clubs"};

        for (String rank : ranks) {
            for (String suit : suits) {
                deck.add(rank + " of " + suit);
            }
        }

        Collections.shuffle(deck);
    }

    private void shuffleDeck() {
        Collections.shuffle(deck);
    }

    public String startGame(int initialBalance) {

        if (initialBalance <= 0) {
            return "Starting balance must be greater than 0.";
        }

        initializeDeck();
        shuffleDeck();

        balance = initialBalance;
        playerHand.clear();
        String drawnCard = drawCard();
        playerHand.add(drawnCard);

        return "New game started. Initial card: " + drawnCard;
    }

    public String shuffleGame() {
        if (balance < 0) {
            return "Game has not been started yet. Deposit a balance to start the game.";
        }

        initializeDeck();
        shuffleDeck();
        playerHand.clear();

        String drawnCard = drawCard();
        playerHand.add(drawnCard);

        return "Deck shuffled. Drawn card: " + drawnCard;
    }

    public String placeBet(int betAmount, String betType) {
        if (balance < 0) {
            return "Game not started yet.";
        }

        if (balance < betAmount) {
            return "Insufficient balance to make the bet.";
        }

        if (!betType.equals("higher") && !betType.equals("lower")){
            return "Incorrect bet type. Use higher or lower to make a bet.";
        }

        if (betAmount <= 0){
            return "Incorrect bet amount. The bet amount should be greater than 0.";
        }

        String previousCard = playerHand.get(playerHand.size() - 1);
        String drawnCard = drawCard();
        playerHand.add(drawnCard);

        int comparison = compareCards(previousCard, drawnCard);

        if (comparison == 0) {
            return "Tie! Balance: " + balance + ", drawn card: " + drawnCard;
        } else if ((comparison > 0 && "higher".equals(betType)) || (comparison < 0 && "lower".equals(betType))) {
            balance += betAmount;
            return "You win! Balance: " + balance + ", drawn card: " + drawnCard;
        } else {
            balance -= betAmount;
            return "You lose! Balance: " + balance + ", drawn card: " + drawnCard;
        }
    }

    private String drawCard() {
        if (deck.isEmpty()) {
            return "The deck is empty. Shuffle it to proceed with the game.";
        }
        return deck.remove(0);
    }

    private int compareCards(String card1, String card2) {
        String[] ranks = {"2", "3", "4", "5", "6", "7", "8", "9", "10", "Jack", "Queen", "King", "Ace"};

        String rank1 = card1.split(" ")[0];
        String rank2 = card2.split(" ")[0];

        int index1 = Arrays.asList(ranks).indexOf(rank1);
        int index2 = Arrays.asList(ranks).indexOf(rank2);

        return Integer.compare(index2, index1);
    }


}
