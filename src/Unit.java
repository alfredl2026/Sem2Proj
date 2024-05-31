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
    private int unitIndex;
    private int exp; //current exp
    private int price;
    private int level;
    private Perk perk; //change to Perk perk
    private String ability;
    private String trait;
    private boolean summoned;
    public Unit(String name){
        this.name = name;
        this.price = 3;
        this.perk = new Perk("None");
        this.level = 1;
        this.exp = 0;
        this.expReq = 2;
        this.summoned = false;
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
            }
            //System.out.println("Created " +  this);
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
    public void setSummoned(boolean summoned, String team, int index){
        this.summoned = summoned;
        if(this.summoned==true && team.equals("Player")){
            for(int i = 0; i < main.teamBattle.length; i++){
                if(main.teamBattle[i]!=null && main.teamBattle[i]!=this) main.teamBattle[i].friendSummoned(team,index);
            }
        }
        else if(this.summoned==true && team.equals("Enemy")){
            for(int i = 0; i < main.enemyTeamBattle.length; i++){
                if(main.enemyTeamBattle[i]!=null && main.enemyTeamBattle[i]!=this) main.enemyTeamBattle[i].friendSummoned(team,index);
            }
        }
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
                System.out.println("The " + this.name + " leveled up to level " + this.level + "!");
                main.waitEnter();
                for(int i = 0; i < main.team.length; i++){
                    if(main.fighting != 1){
                        if(main.team[i] != null){
                            main.team[i].levelUp();
                        }
                    }
                    else if(main.fighting == 1){
                        if(main.teamBattle[i] != null){
                            main.teamBattle[i].levelUp();
                        }
                    }
                }
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
        main.enemyTeamBattle[0].setHp(main.enemyTeamBattle[0].getHp()-this.atk);
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
        Unit enemyUnit = main.enemyTeam[0];
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
    public void checkFaint(){
        Unit enemyUnit = main.enemyTeam[0];
        for(int unitIndex = 0; unitIndex < main.enemyTeam.length; unitIndex++){
            if(main.enemyTeamBattle[unitIndex]!=null && main.enemyTeamBattle[unitIndex].getHp()<=0){
                main.enemyTeamBattle[unitIndex]=null;
                main.enemyTeamDisplayBattle[unitIndex]=null;
                System.out.println("The enemy's " + main.enemyTeamDisplayBattle[unitIndex] + " fainted!");
                main.waitEnter();
                enemyUnit.faint("Enemy", unitIndex);
            }
            if(main.teamBattle[unitIndex] !=null && main.teamBattle[unitIndex].getHp()<=0){
                main.teamBattle[unitIndex]=null;
                main.teamDisplayBattle[unitIndex]=null;
                System.out.println("Your " +  main.teamDisplayBattle[unitIndex] + " fainted!");
                main.waitEnter();
                faint("Player", unitIndex);
            }
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
                    System.out.println("Faint → Your Ant gave your " +  main.teamBattle[randIndex].getName() + " +" + buffAmount + "/+" + buffAmount + "!");
                    main.waitEnter();
                }
            }
            else{
                if(!main.checkTeamEmptyBattle("Enemy")){
                    while(main.enemyTeamBattle[randIndex]==null){
                        randIndex = random.nextInt(5);
                    }
                    main.enemyTeamBattle[randIndex].setAtk(main.enemyTeamBattle[randIndex].getAtk()+buffAmount);
                    main.enemyTeamBattle[randIndex].setHp(main.enemyTeamBattle[randIndex].getHp()+buffAmount);
                    System.out.println("Faint → The enemy's Ant gave the enemy's " +  main.enemyTeamBattle[randIndex].getName() + "+" + buffAmount + "/+" + buffAmount + "!");
                    main.waitEnter();
                }
            }
        }
        else if(this.name.equals("Cricket")){
            if(team.equals("Player")){
                main.teamBattle[index] = new Unit("Zombie Cricket");
                main.teamBattle[index].setAtk(buffAmount);
                main.teamBattle[index].setHp(buffAmount);
                main.teamDisplayBattle[index] = "Zombie Cricket";
                System.out.println(this.ability);
                main.teamBattle[index].setSummoned(true, team, index);
                main.waitEnter();
            }
            else{
                main.enemyTeamBattle[index] = new Unit("Zombie Cricket");
                main.enemyTeamBattle[index].setAtk(buffAmount);
                main.enemyTeamBattle[index].setHp(buffAmount);
                main.enemyTeamDisplayBattle[index] = "Zombie Cricket";
                System.out.println(this.ability);
                main.enemyTeamBattle[index].setSummoned(true, team, index);
                main.waitEnter();
            }
        }
    }
    public void buy(int index){
        int buffAmount = this.level;
        Random random = new Random();
        if(this.name.equals("Otter")){
            for(int i = 0; i < buffAmount; i++){
                int randIndex = random.nextInt(5);
                while(main.team[randIndex]==null){
                    randIndex = random.nextInt(5);
                }
                main.team[randIndex].setHp(main.team[randIndex].getHp()+1);
                System.out.println("Buy → Your Otter gave your " +  main.team[randIndex].getName() + " +1 HP!");
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
                    while(main.team[randIndex]==null && main.team[randIndex]!=this){
                        randIndex = random.nextInt(5);
                    }
                    main.team[randIndex].setAtk(main.team[randIndex].getAtk()+1);
                    System.out.println("Sell → Your Beaver gave your " +  main.team[randIndex].getName() + " +1 ATK!");
                    main.waitEnter();
                }
            }
        }
        else if(this.name.equals("Duck")){
            if(!main.checkShopUnitsEmpty()) {
                for (int i = 0; i < main.shopUnits.length; i++) {
                    if(main.shopUnits[i]!=null) main.shopUnits[i].setHp(main.shopUnits[i].getHp()+buffAmount);
                }
                System.out.println("Sell → Your Duck gave all shop pets +" + buffAmount + " health!");
                main.waitEnter();
            }
        }
        else if(this.name.equals("Pig")){
            main.gold+=buffAmount;
            System.out.println("Sell → Your Pig gave you +" + buffAmount + " gold!");
        }
    }
    public void levelUp(){
        int buffAmount = this.level;
        Random random = new Random();
        if(this.name.equals("Fish")){
            if(!main.checkTeamEmpty()){
                for(int i = 0; i < 2; i++){
                    int randIndex = random.nextInt(5);
                    while(main.team[randIndex]==null){
                        randIndex = random.nextInt(5);
                    }
                    main.team[randIndex].setAtk(main.team[randIndex].getAtk()+buffAmount);
                    main.team[randIndex].setHp(main.team[randIndex].getHp()+buffAmount);
                    System.out.println("Level up → Your Fish gave your " +  main.team[randIndex].getName() + " +" + buffAmount + " ATK!");
                    main.waitEnter();
                }
            }
        }
    }
    public void friendSummoned(String team, int index){
        int buffAmount = this.level;
        Random random = new Random();
        if(this.name.equals("Horse")){
            if(team.equals("Player")){
                main.teamBattle[index].setAtk(main.teamBattle[index].getAtk()+buffAmount);
                System.out.println();
                System.out.println("Friend summoned → Your Horse gave your " +  main.teamBattle[index].getName() + " +" + buffAmount + " ATK!");
                main.waitEnter();
            }
            else if(team.equals("Enemy")){
                main.enemyTeamBattle[index].setAtk(main.enemyTeamBattle[index].getAtk()+buffAmount);
                System.out.println();
                System.out.println("Friend summoned → The enemy's Horse gave their " +  main.enemyTeamBattle[index].getName() + " +" + buffAmount + " ATK!");
                main.waitEnter();
            }
        }
    }
    public void startOfBattle(String team, int index){
        int buffAmount = this.level;
        Random random = new Random();
        if(this.name.equals("Mosquito")){
            if(team.equals("Player")){
                for(int i = 0; i < buffAmount; i++){
                    int randIndex = random.nextInt(5);
                    while(main.enemyTeamBattle[randIndex]==null){
                        randIndex = random.nextInt(5);
                    }
                    main.enemyTeamBattle[randIndex].setHp(main.enemyTeamBattle[randIndex].getHp()-1);
                    System.out.println("Start of battle → Your Mosquito dealt " + 1 + " dmg to the enemy " + main.enemyTeamDisplayBattle[randIndex] + "!");
                    main.waitEnter();
                    checkFaint();
                }
            }
            if(team.equals("Enemy")){
                for(int i = 0; i < buffAmount; i++){
                    int randIndex = random.nextInt(5);
                    while(main.teamBattle[randIndex]==null){
                        randIndex = random.nextInt(5);
                    }
                    main.teamBattle[randIndex].setHp(main.teamBattle[randIndex].getHp()-1);
                    System.out.println("Start of battle → The enemy's Mosquito dealt " + 1 + " dmg to your " + main.teamDisplayBattle[randIndex] + "!");
                    main.waitEnter();
                    checkFaint();
                }
            }
        }
    }
    public String toString(){
        //return this.name;
        return(name + " - Price: " + price + " | Tier: " + tier + " | ATK: " + atk + " | HP: " + hp + "\n" + "Level " + level + " ability: " + ability + "\nPerk: " + perk + "\nEXP: " + exp + "/" + expReq);
    }
}
