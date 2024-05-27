import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
public class Perk {
    private String str;
    private String type;
    private String name;
    private int price;
    private int tier;
    private String ability;
    public Perk(String name){
        this.name = name;
        this.price = 3;
        perkInitializer(name);
    }
    public void perkInitializer(String word){
        try {
            BufferedReader br = new BufferedReader(new FileReader("Perkdex"));
            while((str = br.readLine()) != null){
                String[] array = str.split(";");
                for (String string2 : array){
                    if(string2.matches(word)){
                        //System.out.println(Arrays.toString(array));
                        this.type = array[1];
                        this.tier = Integer.parseInt(array[2]);
                        this.ability = array[3];
                    }
                }
            }   //System.out.println("Created " +  this);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public int getTier(){
        return this.tier;
    }
    public void activatePerk(String type, int index) throws FileNotFoundException {
        if(type.equals("Buy")){
            if(this.name.equals("Apple")){
                main.team[index].setAtk(main.team[index].getAtk()+1);
                main.team[index].setHp(main.team[index].getHp()+1);
            }
            if(this.name.equals("Water of Youth")){
                main.team[index] = new Unit(main.randomUnitSameTier(main.team[index]));
            }
        }
    }
    public String toString(){
        return(name + " - Price: " + price + " | Tier: " + tier + " | Ability: " + ability);
    }
}
