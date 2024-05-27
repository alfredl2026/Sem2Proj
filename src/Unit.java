import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.Random;

public class Unit {
    private static String str;
    private int expReq; //how much it takes to level up
    private String name;
    private int atk;
    private int hp;
    private int tier;
    private int exp; //current exp
    private int price;
    private int level;
    private Perk perk; //change to Perk perk
    private String ability;
    private String trait;
    public Unit(String name){
        this.name = name;
        this.price = 3;
        this.perk = new Perk("None");
        this.level = 1;
        this.exp = 0;
        this.expReq = 2;
        unitInitializer(name);
    }
    public void unitInitializer(String word){
        try {
            BufferedReader br = new BufferedReader(new FileReader("Unitdex"));
            while((str = br.readLine()) != null){
                String[] array = str.split(";");
                for (String string2 : array){
                    if(string2.matches(word)){
                        //System.out.println(Arrays.toString(array));
                        this.tier = Integer.parseInt(array[1]);
                        this.atk = Integer.parseInt(array[2]);
                        this.hp = Integer.parseInt(array[3]);
                        this.ability = array[4];
                    }
                }
            }   //System.out.println("Created " +  this);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public int getHp(){
        return hp;
    }
    public int getAtk(){
        return atk;
    }
    public int getExp(){
        return exp;
    }
    public int getTier(){
        return tier;
    }
    public String getName(){
        return name;
    }
    public int getPrice(){
        return price;
    }
    public int getLevel(){
        return level;
    }
    public void setExp(int exp){
        this.exp = exp;
        levelUpCheck();
    }
    public void setAtk(int atk){
        this.atk = atk;
    }
    public void setHp(int hp){
        this.hp = hp;
    }
    public void setPerk(Perk perk){
        this.perk = perk;
    }
    public void updateAbility(){
        try {
            BufferedReader br = new BufferedReader(new FileReader("Unitdex"));
            while((str = br.readLine()) != null){
                String[] array = str.split(";");
                for (String string2 : array){
                    if(string2.matches(this.name)){
                        if(this.level==1) this.ability = array[4];
                        if(this.level==2) this.ability = array[5];
                        if(this.level==3) this.ability = array[6];
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void levelUpCheck(){
        if(this.exp>=this.expReq && level!=3){
            while(this.exp>=this.expReq && level!=3){
                exp-=expReq;
                expReq = expReq+1;
                level++;
                updateAbility();
                System.out.println();
                System.out.println("The " + this.name + " levelled up to level " + this.level + "!");
                main.waitEnter();
            }
        }
    }
    public void merge(){
        setExp(this.exp+1);
        setAtk(this.atk+1);
        setHp(this.hp+1);
    }
    public void normalAttack() {
        Unit enemyUnit = new Unit(main.enemyTeamDisplayBattle[0]);
        String enemyName = main.enemyTeamBattle[0].getName();
        main.enemyTeamBattle[0].setHp(main.enemyTeamBattle[0].getHp() - this.atk);
        System.out.println("Your " + this.name + " attacked the enemy " + main.enemyTeamDisplayBattle[0] + " for " + this.atk + " damage!");
        main.waitEnter();
        main.teamBattle[4].setHp(main.teamBattle[4].getHp() - main.enemyTeamBattle[0].getAtk());
        System.out.println("The enemy " + enemyName + " attacked your " + main.teamDisplayBattle[4] + " for " + main.enemyTeamBattle[0].getAtk() + " damage!");
        main.waitEnter();
        if (main.teamBattle[4].getHp() <= 0) {
            main.teamBattle[4] = null;
            main.teamDisplayBattle[4] = null;
            System.out.println("Your " + this.name + " fainted!");
            main.waitEnter();
            faint("Player", 4);
        }
        if (main.enemyTeamBattle[0].getHp() <= 0) {
            main.enemyTeamBattle[0] = null;
            main.enemyTeamDisplayBattle[0] = null;
            System.out.println("The enemy's " + enemyName + " fainted!");
            main.waitEnter();
            enemyUnit.faint("Enemy", 0);
        }
    }
    public void specialAttack(String team, int unitIndex, int dmg){
        Unit enemyUnit = new Unit(main.enemyTeamDisplayBattle[0]);
        if(team.equals("Enemy")){
            main.enemyTeamBattle[unitIndex].setHp(main.enemyTeamBattle[unitIndex].getHp()-dmg);
            System.out.println("Your " + this.name + " attacked the enemy " + main.enemyTeamDisplayBattle[unitIndex] + " for " + dmg + " damage!");
            main.waitEnter();
        }
        if(team.equals("Player")){
            main.teamBattle[unitIndex].setHp(main.teamBattle[unitIndex].getHp()-dmg);
            System.out.println("The enemy " + this.name + " attacked your " + main.teamDisplayBattle[unitIndex] + " for " + dmg + " damage!");
            main.waitEnter();
        }
        if(main.enemyTeamBattle[unitIndex].getHp()<=0){
            main.enemyTeamBattle[unitIndex]=null;
            main.enemyTeamDisplayBattle[unitIndex]=null;
            System.out.println("The enemy's " + this.name + " fainted!");
            main.waitEnter();
            enemyUnit.faint("Enemy", 0);
        }
        if(main.teamBattle[unitIndex].getHp()<=0){
            main.teamBattle[unitIndex]=null;
            main.teamDisplayBattle[unitIndex]=null;
            System.out.println("Your " + this.name + " fainted!");
            main.waitEnter();
            faint("Player", 4);
        }
    }
    //Below are passive effects
    public void faint(String team, int index){
        int buffAmount = this.level;
        Random random = new Random();
        if(this.name.equals("Ant")){
            int randIndex = random.nextInt(5);
            if(team.equals("Player")){
                if(!main.checkTeamEmptyBattle("Player")){
                    while(main.teamBattle[randIndex]==null){
                        randIndex = random.nextInt(5);
                    }
                    main.teamBattle[randIndex].setAtk(main.teamBattle[randIndex].getAtk()+buffAmount);
                    main.teamBattle[randIndex].setHp(main.teamBattle[randIndex].getHp()+buffAmount);
                    System.out.println("Your Ant gave your " +  main.teamBattle[randIndex].getName() + "+" + buffAmount + "/+" + buffAmount + "!");
                    main.waitEnter();
                }
            }
            else{
                if(main.checkTeamEmptyBattle("Enemy")){
                    while(main.enemyTeamBattle[randIndex]==null){
                        randIndex = random.nextInt(5);
                    }
                    main.enemyTeamBattle[randIndex].setAtk(main.enemyTeamBattle[randIndex].getAtk()+buffAmount);
                    main.enemyTeamBattle[randIndex].setHp(main.enemyTeamBattle[randIndex].getHp()+buffAmount);
                    System.out.println("The enemy's Ant gave the enemy's " +  main.enemyTeamBattle[randIndex].getName() + "+" + buffAmount + "/+" + buffAmount + "!");
                    main.waitEnter();
                }
            }
        }
        else if(this.name.equals("Cricket")){
            if(team.equals("Player")){
                main.teamBattle[4] = new Unit("Zombie Cricket");
                main.teamBattle[4].setAtk(buffAmount);
                main.teamBattle[4].setHp(buffAmount);
                main.teamDisplayBattle[4] = "Zombie Cricket";
                System.out.println(this.ability);
                main.waitEnter();
            }
            else{
                main.enemyTeamBattle[0] = new Unit("Zombie Cricket");
                main.enemyTeamBattle[0].setAtk(buffAmount);
                main.enemyTeamBattle[0].setHp(buffAmount);
                main.enemyTeamDisplayBattle[0] = "Zombie Cricket";
                System.out.println(this.ability);
                main.waitEnter();
            }
        }
    }
    public void sell(int index){
        int buffAmount = this.level;
        Random random = new Random();
        if(this.name.equals("Beaver")){
            if(!main.checkTeamEmpty()){
                for(int i = 0; i < buffAmount; i++){
                    int randIndex = random.nextInt(5);
                    while(main.team[randIndex]==null){
                        randIndex = random.nextInt(5);
                    }
                    main.team[randIndex].setAtk(main.team[randIndex].getAtk()+1);
                    System.out.println("Your Beaver gave your " +  main.team[randIndex].getName() + "+1 ATK!");
                    main.waitEnter();
                }
            }
        }
        else if(this.name.equals("Duck")){
            if(!main.checkShopUnitsEmpty()) {
                for (int i = 0; i < main.shopUnits.length; i++) {
                    if(main.shopUnits[i]!=null) main.shopUnits[i].setHp(main.shopUnits[i].getHp()+buffAmount);
                }
                System.out.println("Your " + this.name + " gave all shop pets +" + buffAmount + " health!");
                main.waitEnter();
            }
        }
    }
    public String toString(){
        //return this.name;
        return(name + " - Price: " + price + " | Tier: " + tier + " | ATK: " + atk + " | HP: " + hp + "\n" + "Level " + level + " ability: " + ability + "\nPerk: " + perk + "\nEXP: " + exp + "/" + expReq);
    }
}
