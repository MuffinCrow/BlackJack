import java.util.*;

public class BlackJack {

    private final String[] SUITS = { "C", "D", "H", "S" };
    private final String[] RANKS = { "A", "2", "3", "4", "5", "6", "7", "8", "9", "T", "J", "Q", "K" };

    private String whoseTurn;
    private Hand player;
    private Hand dealer;
    private List<Card> deck;
    private final Scanner in;
    private int check;
    private int chips;
    private int wager;

    public BlackJack() {
        this.whoseTurn = "P";
        this.player = new Hand();
        this.dealer = new Hand();
        this.in = new Scanner(System.in);
        this.check = -1;
        chips = 200;
    }

    public void play() {
        if(chips == 0) {
            System.out.print("You are all out of chips! Better luck next time!");
            closeGame();
        }
        System.out.print("\nCurrent chips: " + chips + "\nWager: ");
        String temp = in.nextLine();
        if (!temp.isEmpty()) {
            try {
                check = Integer.parseInt(temp);
                if (check < 1 || check > 25) {
                    System.out.println("Wager must be between 1 and 25 chips.");
                    play();
                }
                if (check > chips) {
                    System.out.println("Wager must be between 1 and " + chips);
                    play();
                }
            } catch (NumberFormatException e) {
                play();
            }
            wager = check;
        } else {
            play();
        }
        shuffleAndDeal();

        while (true) {
            if (whoseTurn == "P") {
                whoseTurn = takeTurn(false);
            } else if (whoseTurn == "D") {
                whoseTurn = takeTurn(true);
            }

            if (player.getTotal() >= 21 || dealer.getTotal() >= 21) {
                end();
            }
        }
    }

    private String takeTurn(boolean cpu) {
        showHand(true);
        showHand(false);

        if(!cpu) {
            System.out.print("(D)raw or (S)tand: ");
            String temp = in.nextLine();

            if (temp.length() > 1) {
                takeTurn(cpu);
            } else if (temp.equals("D")) {
                player.takeCard(deck.remove(0));
                if(player.getTotal() >= 21) {
                    end();
                }
            } else if (temp.equals("S")) {
                end();
            } else {
                takeTurn(cpu);
            }
            return "D";
        } else {
            if (dealer.getTotal() >= 21) {
                end();
            } else if (dealer.getTotal() < 17) {
                dealer.takeCard(deck.remove(0));
            }
            return "P";
        }
    }

    private void end() {
        showHand(true);
        showHand(false);

        if (player.getTotal() == dealer.getTotal()) {
            System.out.println("\nYou tied! No reward!");
        } else if (player.getTotal() == 21) {
            System.out.println("\nBlackJack! Your payoff is 3:2!");
            chips += (wager * 1.5);
        } else if (player.getTotal() < dealer.getTotal()) {
            System.out.println("\nYou lose! You lost your wager!");
            chips -= wager;
        } else if (player.getTotal() > dealer.getTotal()) {
            System.out.println("\nYou win! Your payoff is 1:1!");
            chips += wager;
        }
        restart();
    }

    private void restart() {
        System.out.print("(P)lay again or (Q)uit: ");
        String temp = in.nextLine();

        if (temp.length() < 1 || temp.length() > 1 || temp.equals(null)) {
            restart();
        } else if (temp.equals("P")) {
            play();
        } else if (temp.equals("Q")) {
            closeGame();
        } else {
            restart();
        }
    }

    public void closeGame() {
        in.close();
    }

    private void showHand(boolean cpu) {
        if (!cpu) {
            System.out.println("\nPLAYER hand: " + player.getHand());
        } else {
            if(dealer.getHand().size() < 3) {
                System.out.println("\nDEALER hand: " + dealer.getHand().get(0) + ", []");
            } else {
                System.out.println("\nDEALER hand: " + dealer.getHand());
            }
        }
    }

    private void shuffleAndDeal() {
        if (deck == null) {
            initializeDeck();
        }
        Collections.shuffle(deck);

        while (player.getHand().size() < 2) {
            player.takeCard(deck.remove(0));
            dealer.takeCard(deck.remove(0));
        }
    }

    private void initializeDeck() {
        deck = new ArrayList<>(52);

        for (String suit : SUITS) {
            for (String rank : RANKS) {
                deck.add(new Card(rank, suit));
            }
        }
    }

    public static void main(String[] args) {
        new BlackJack().play();
    }
}
