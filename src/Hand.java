import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class Hand {
    private ArrayList<Card> cards;
    private int total;
    private int totalAce;

    public Hand() {
        cards = new ArrayList<>();
    }

    public Hand(Card card1, Card card2) {
        cards.add(card1);
        cards.add(card2);

        int temp1 = card1.getOrderedRank(card1.getRank());
        int temp2 = card2.getOrderedRank(card2.getRank());

        if(temp1 == 11) {
            total += 10;
            totalAce += 10;
        } else if (temp1 == 12) {
            total += 11;
            totalAce += 1;
        } else {
            total += temp1;
            totalAce += temp1;
        }

        if(temp2 == 11) {
            total += 10;
            totalAce += 10;
        } else if (temp2 == 12) {
            total += 11;
            totalAce += 1;
        } else {
            total += temp2;
            totalAce += temp2;
        }
    }

    public List<String> getHand() {
        List<Card> temp = cards;
        List<String> returns = new ArrayList<>();
        for(Card i : temp) {
            returns.add(i.toString());
        }
        return returns;
    }

    public void takeCard(Card card) {
        cards.add(card);

        int temp = card.getOrderedRank(card.getRank());

        if(temp == 11) {
            total += 10;
            totalAce += 10;
        } else if (temp == 12) {
            total += 11;
            totalAce += 1;
        } else {
            total += temp;
            totalAce += temp;
        }
    }

    public int getTotal() {
        if(total > 21) {
            return totalAce;
        } else {
            return total;
        }
    }
}
