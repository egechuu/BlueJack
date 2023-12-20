public class Card {
    public String name;
    public String colour;
    public int value;
    public boolean sign;
    public String operator;

    public Card(String colour, int value, boolean sign) {
        this.colour = colour;
        this.value = value;
        this.sign = sign;
    }

    public Card(String name, String operator) {
        this.name = name;
        this.operator = operator;
    }

    public Card getCard() {
        return this;
    }

    public String printCard() {

        if(this.name !=null) {
            return operator;
        }
        else if(sign == false) {
            return "(" + colour + ")" + value;
        }
        return "(" + colour + ")+" + value;
    }
}
