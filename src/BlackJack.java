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
    private boolean checkb;
    private boolean stand;
    private boolean endgame;
    private String tempS;
    private boolean insurance;
    private int sideBet;

    public BlackJack() {
        this.whoseTurn = "P";
        this.in = new Scanner(System.in);
        this.check = -1;
        chips = 25;
        checkb = false;
        stand = false;
        endgame = false;
        this.player = new Hand();
        this.dealer = new Hand();
        insurance = false;
    }

    public void play() {
        do {
            insurance = false;
            tempS = "P";
            if (chips == 0) {
                System.out.print("You are all out of chips! Better luck next time!");
                closeGame();
                tempS = "Q";
            } else {
                do {
                    whoseTurn = "P";
                    checkb = false;
                    stand = false;
                    endgame = false;
                    System.out.print("\nCurrent chips: " + chips + "\nWager: ");
                    String temp = in.nextLine();
                    if (!temp.isEmpty()) {
                        try {
                            check = Integer.parseInt(temp);
                            if (check < 1 || check > 25) {
                                System.out.println("Wager must be between 1 and 25 chips.");
                            } else if (check > chips) {
                                System.out.println("Wager must be between 1 and " + chips);
                            } else {
                                wager = check;
                                checkb = true;
                            }
                        } catch (NumberFormatException e) {
                            System.out.println("Please enter a valid number.");
                        }
                    } else {
                        System.out.println("Enter the amount of chips you would like to wager to start!");
                    }
                } while (checkb == false);
                checkb = false;
                shuffleAndDeal();

                if (String.valueOf(dealer.getHand().get(1).charAt(0)).equals("A")) {
                    String temp;
                    do {
                        System.out.println("\nDEALER hand: [], " + dealer.getHand().subList( 1, dealer.getHand().size()).toString().replaceAll("\\[", "").replaceAll("\\]",""));
                        System.out.println("\nPLAYER hand: [], " + player.getHand().subList( 1, player.getHand().size()).toString().replaceAll("\\[", "").replaceAll("\\]",""));
                        System.out.print("\nWould you like to purchase insurance? (Y)es or (N)o: ");
                        temp = in.nextLine();
                    } while (!temp.equals("Y") && !temp.equals("N"));
                    if (temp.equals("Y")) {
                        if (wager / 2 > chips) {
                            System.out.println("You do not have enough chips to buy insurance.");
                        } else if (temp.equals("N")) {
                            sideBet = wager/2;
                            insurance = true;
                            System.out.println("Your insurance has been noted.");
                        }
                    }
                    if (dealer.getTotal() == 21 && player.getTotal() != 21) {
                        System.out.println("\nDEALER hand: " + dealer.getHand().toString().replaceAll("\\[", "").replaceAll("\\]",""));
                        System.out.println("\nPLAYER hand: " + player.getHand().toString().replaceAll("\\[", "").replaceAll("\\]",""));
                        System.out.print("\nThe dealer had blackjack! ");

                        if (insurance == true) {
                            System.out.println("Good thing you invested in insurance. You got your chips back!");
                        } else {
                            System.out.println("You did not invest in insurance.");
                            end();
                            do {
                                System.out.print("(P)lay again or (Q)uit: ");
                                tempS = in.nextLine();
                            } while (!tempS.equals("P") && !tempS.equals("Q"));
                        }
                    } else if (dealer.getTotal() == 21 && player.getTotal() == 21) {
                        System.out.print("\nBoth you and the dealer have blackjacks! ");
                        if (insurance == true) {
                            System.out.println("You got your insurance chips and wager back.");
                        } else {
                            System.out.println("You got your wager back.");
                        }
                        end();
                        do {
                            System.out.print("(P)lay again or (Q)uit: ");
                            tempS = in.nextLine();
                        } while (!tempS.equals("P") && !tempS.equals("Q"));
                    } else {
                        System.out.print("\nThe dealer did not have a blackjack. ");

                        if (insurance == true) {
                            System.out.println("You lost your insurance chips.");
                            chips -= sideBet;
                        } else {
                            System.out.println("You did not invest in insurance. The game proceeds as normal.");
                        }
                    }
                } else if (player.getTotal() == 21) {
                    end();
                    do {
                        System.out.print("(P)lay again or (Q)uit: ");
                        tempS = in.nextLine();
                    } while (!tempS.equals("P") && !tempS.equals("Q"));
                }
                if (dealer.getTotal() != 21 && player.getTotal() != 21) {
                    while (endgame == false) {
                        if (dealer.getTotal() < 21 && player.getTotal() < 21) {
                            if (whoseTurn.equals("P")) {
                                whoseTurn = takeTurn(false);
                            } else if (whoseTurn.equals("D")) {
                                whoseTurn = takeTurn(true);
                            }
                        }
                        if (player.getTotal() >= 21 || dealer.getTotal() >= 21 || stand == true) {
                            if ((player.getTotal() >= 21 || dealer.getTotal() >= 21) && stand == false) {
                                end();
                                do {
                                    System.out.print("(P)lay again or (Q)uit: ");
                                    tempS = in.nextLine();
                                } while (!tempS.equals("P") && !tempS.equals("Q"));

                                endgame = true;
                            } else if (dealer.getTotal() >= 17 && stand == true) {
                                end();
                                do {
                                    System.out.print("(P)lay again or (Q)uit: ");
                                    tempS = in.nextLine();
                                } while (!tempS.equals("P") && !tempS.equals("Q"));

                                endgame = true;

                            } else {
                                whoseTurn = "D";
                            }
                        }
                    }
                }
                this.player = new Hand();
                this.dealer = new Hand();
            }
        } while (tempS.equals("P"));
        closeGame();
    }

    private String takeTurn(boolean cpu) {

        if(!cpu) {
            String temp;

            do {
                showHand(true);
                showHand(false);
                System.out.print("(D)raw or (S)tand: ");
                temp = in.nextLine();
            }while (!temp.equals("D") && !temp.equals("S"));

            if (temp.equals("D")) {
                player.takeCard(deck.remove(0));
            } else if (temp.equals("S")) {
                stand = true;
            }
            return "D";
        } else {
            if (dealer.getTotal() < 17) {
                dealer.takeCard(deck.remove(0));
            }
            return "P";
        }
    }

    private void end() {
        if (player.getTotal() == dealer.getTotal()) {
            System.out.println("\nYou tied! No reward! These are the ending hands:\n");
            System.out.println("\nDEALER hand: " + dealer.getHand().toString().replaceAll("\\[", "").replaceAll("\\]",""));
            System.out.println("\nPLAYER hand: " + player.getHand().toString().replaceAll("\\[", "").replaceAll("\\]",""));
        } else if (player.getTotal() == 21) {
            System.out.println("\nBlackJack! Your payoff is 3:2! These are the ending hands:\n");
            System.out.println("\nDEALER hand: " + dealer.getHand().toString().replaceAll("\\[", "").replaceAll("\\]",""));
            System.out.println("\nPLAYER hand: " + player.getHand().toString().replaceAll("\\[", "").replaceAll("\\]",""));
            chips += (wager * 1.5);
        }  else if(dealer.getTotal() == 21) {
            System.out.println("\nYou lose! You lost your wager! These are the ending hands:\n");
            System.out.println("\nDEALER hand: " + dealer.getHand().toString().replaceAll("\\[", "").replaceAll("\\]",""));
            System.out.println("\nPLAYER hand: " + player.getHand().toString().replaceAll("\\[", "").replaceAll("\\]",""));
            chips -= wager;
        } else if(player.getTotal() > 21) {
            System.out.println("\nYou lose! You lost your wager! These are the ending hands:\n");
            System.out.println("\nDEALER hand: " + dealer.getHand().toString().replaceAll("\\[", "").replaceAll("\\]",""));
            System.out.println("\nPLAYER hand: " + player.getHand().toString().replaceAll("\\[", "").replaceAll("\\]",""));
            chips -= wager;
        } else if (dealer.getTotal() > 21) {
            System.out.println("\nYou win! Your payoff is 1:1! These are the ending hands:\n");
            System.out.println("\nDEALER hand: " + dealer.getHand().toString().replaceAll("\\[", "").replaceAll("\\]",""));
            System.out.println("\nPLAYER hand: " + player.getHand().toString().replaceAll("\\[", "").replaceAll("\\]",""));
            chips += wager;
        } else if (player.getTotal() < dealer.getTotal()) {
            System.out.println("\nYou lose! You lost your wager! These are the ending hands:\n");
            System.out.println("\nDEALER hand: " + dealer.getHand().toString().replaceAll("\\[", "").replaceAll("\\]",""));
            System.out.println("\nPLAYER hand: " + player.getHand().toString().replaceAll("\\[", "").replaceAll("\\]",""));
            chips -= wager;
        } else if (player.getTotal() > dealer.getTotal()) {
            System.out.println("\nYou win! Your payoff is 1:1! These are the ending hands:\n");
            System.out.println("\nDEALER hand: " + dealer.getHand().toString().replaceAll("\\[", "").replaceAll("\\]",""));
            System.out.println("\nPLAYER hand: " + player.getHand().toString().replaceAll("\\[", "").replaceAll("\\]",""));
            chips += wager;
        }
    }

    /*private void restart() {
        String temp;

        do {
            System.out.print("(P)lay again or (Q)uit: ");
            temp = in.nextLine();
        } while (!temp.equals("P") && !temp.equals("Q"));

        if (temp.equals("P")) {
            play();
        } else if (temp.equals("Q")) {
            endgame = true;
        }
    }*/

    public void closeGame() {
        in.close();
    }

    private void showHand(boolean cpu) {
        if (!cpu) {
            System.out.println("\nPLAYER hand: " + player.getHand().toString().replaceAll("\\[", "").replaceAll("\\]",""));
        } else {
            if(dealer.getHand().size() < 3) {
                System.out.println("\nDEALER hand: [], " + dealer.getHand().get(1));
            } else {
                System.out.println("\nDEALER hand: [], " + dealer.getHand().subList( 1, dealer.getHand().size()).toString().replaceAll("\\[", "").replaceAll("\\]",""));
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
