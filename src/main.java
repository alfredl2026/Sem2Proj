import java.io.*;
import java.util.Arrays;
import java.util.Objects;
import java.util.Random;
import java.util.Scanner;
import java.io.BufferedReader;
import java.io.BufferedWriter;

public class main {
    public static int lives = 5;
    public static int wins = 0;
    public static String str;
    public static int turn;
    public static int fighting = 0;
    public static int gold = 12;
    public static int shopTier = 1;
    public static int[] frozenItems = new int[7];
    public static Unit[] shopUnits = new Unit[5];
    public static Perk[] shopPerks = new Perk[5];
    public static String[] shopDisplayUnits = new String[5];
    public static String[] shopDisplayPerks = new String[2];
    public static String[] teamDisplay = new String[5];
    public static String[] enemyTeamDisplay = new String[5];
    public static String[] teamDisplayBattle = new String[5];
    public static String[] enemyTeamDisplayBattle = new String[5];
    public static Unit[] teamBattle = new Unit[5];
    public static Unit[] enemyTeamBattle = new Unit[5];
    public static Unit[] enemyTeam = new Unit[5];
    public static Unit[] team = new Unit[5];
    public static int[][] yourStatsDisplay = new int[5][2];
    public static int[][] enemyStatsDisplay = new int[5][2];
    public static String[] tempDisplay = new String[5];
    public static Unit[] tempTeam = new Unit[5];

    public static void main(String[] args) throws IOException, InterruptedException {
        System.out.println("Please press Enter after every line to proceed.");
        waitEnter();
        displayMenu();
        userSelectMenu();
    }
    public static void test(){
        Unit unit = new Unit("Pigeon");
        unit.setPerk(new Perk("Apple"));
        System.out.println(unit);
        unit.setExp(10);
        System.out.println(unit);
    }
    public static void userSelectMenu() throws IOException, InterruptedException { //prompts user to select one from menu
        System.out.println("Enter a number: ");
        int input;
        Scanner scanner = new Scanner(System.in);
        input = scanner.nextInt();
        if(input == 1){
            startGame();
        }
        else if(input == 2){
            userSelectMenu();
        }
        else if(input == 3){
            System.out.println("Goodbye!");
            System.exit(0);
        }
        else{
            invalid();
            userSelectMenu();
        }
    }
    public static void waitEnter(){
        Scanner scanLine = new Scanner(System.in);
        scanLine.nextLine();
    }
    public static void invalid(){
        System.out.println("\nInvalid Input!");
    }
    public static void startGame() throws FileNotFoundException, InterruptedException {
        Arrays.fill(team, null);
        shopPhase();

    }
    public static void shopPhase() throws FileNotFoundException, InterruptedException {
        gold=12;
        turn++;
        if((turn-1)%2==0 && turn != 1) shopTier++;
        shopRefresh();
        shopFunction();
    }
    public static void shopFunction() throws FileNotFoundException, InterruptedException {
        for(int i = 0; i<=50; i++){
            System.out.println();
        }
        //System.out.println("Real Team: " + Arrays.toString(team));
        System.out.println("Wins - " + wins + " | Lives - " + lives +  " | Gold - " + gold);
        System.out.println("\nYour Team: " + Arrays.toString(teamDisplay));
        System.out.println();
        System.out.println(Arrays.toString(shopDisplayUnits) + Arrays.toString(shopDisplayPerks));
        boolean isTeamFull = false;
        int intInput;
        int count = 0;
        for (String item : teamDisplay) {
            if (item != null) {
                count++;
            }
        }
        if(count==5) isTeamFull = true;
        System.out.println("\nPlease enter if you want to do modify shop, team, roll shop or end turn (shop/team/roll/end): ");
        Scanner scanner = new Scanner(System.in);
        String input = scanner.nextLine();
        if(input.equals("shop")){
            System.out.println("\nPlease enter a command (inspect, buy, freeze, unfreeze), followed by a space then the number of that item (1-7): ");
            input = scanner.nextLine();
            Unit unitChecking = null;
            Perk perkChecking = null;
            Scanner sc = new Scanner(input).useDelimiter(" ");
            if(sc.hasNext()) {
                String command = sc.next();
                if(sc.hasNext()){
                    int itemNumber = Integer.parseInt(sc.next());
                    if(itemNumber >= 1 && itemNumber <= 7) {
                        if(itemNumber<=5){
                            if(!Objects.equals(shopDisplayUnits[itemNumber - 1], "Empty")) unitChecking = shopUnits[itemNumber - 1];
                            else {
                                System.out.println("This slot is empty!");
                                waitEnter();
                                shopFunction();
                            }
                        }
                        else {
                            if(!Objects.equals(shopDisplayPerks[itemNumber - 6], "Empty"))perkChecking = shopPerks[itemNumber - 6];
                            else  {
                                System.out.println("This slot is empty!");
                                waitEnter();
                                shopFunction();
                            }
                        }
                        if(command.equals("inspect")) {
                            if(itemNumber<=5){
                                System.out.println();
                                System.out.println(unitChecking);
                                waitEnter();
                            }
                            else{
                                System.out.println();
                                System.out.println(perkChecking);
                                waitEnter();
                            }
                        }
                        else if(command.equals("buy")){
                            if(itemNumber<=5){
                                if(gold >= unitChecking.getPrice()){
                                    if(isTeamFull) System.out.println("Your team is full!");
                                    else {
                                        System.out.println("\nWhich slot do you want to put the " + unitChecking.getName() + " into (1-5): ");
                                        intInput = scanner.nextInt();
                                        if (intInput < 1 || intInput > 5 || teamDisplay[intInput - 1] != null && !Objects.equals(teamDisplay[intInput - 1], unitChecking.getName())) {
                                            invalid();
                                            waitEnter();
                                            shopFunction();
                                        } else if (Objects.equals(teamDisplay[intInput - 1], unitChecking.getName())) {
                                            team[intInput - 1].merge();
                                            gold -= unitChecking.getPrice();
                                        } else {
                                            teamDisplay[intInput - 1] = unitChecking.getName();
                                            team[intInput - 1] = unitChecking;
                                            gold -= unitChecking.getPrice();
                                        }
                                        shopBought(itemNumber);
                                    }
                                }
                                else{
                                    System.out.println("\nNot enough gold!");
                                    waitEnter();
                                }
                            }
                        }
                        else if (command.equals("freeze")){
                            if(frozenItems[itemNumber-1]==1){
                                System.out.println("This item is already frozen!");
                                waitEnter();
                            }
                            else{
                                frozenItems[itemNumber-1]=1;
                                System.out.println("Successfully frozen!");
                                waitEnter();
                            }
                        }
                        else if (command.equals("unfreeze")){
                            if(frozenItems[itemNumber-1]==0){
                                System.out.println("This item is not frozen!");
                                waitEnter();
                            }
                            else{
                                frozenItems[itemNumber-1]=0;
                                System.out.println("Successfully unfrozen!");
                                waitEnter();
                            }
                        }
                    }
                    else {
                        System.out.println("item num not in range");
                        invalid();
                        waitEnter();
                        shopFunction();
                    }
                }
                }
            else {
                invalid();
                waitEnter();
                shopFunction();
            }
        }
        else if (input.equals("team")){
            System.out.println("\nPlease enter a command (inspect, move, merge, sell), followed by a space then the number of that unit (1-5): ");
            input = scanner.nextLine();
            Unit unitChecking = null;
            Perk perkChecking = null;
            Scanner sc = new Scanner(input).useDelimiter(" ");
            if(sc.hasNext()) {
                String command = sc.next();
                if(sc.hasNext()){
                    int itemNumber = Integer.parseInt(sc.next());
                    if (itemNumber >= 1 && itemNumber <= 5) {
                        if(teamDisplay[itemNumber - 1] != null) unitChecking = team[itemNumber-1];
                        else {
                            System.out.println("This slot is empty!");
                            waitEnter();
                            shopFunction();
                        }
                        if(command.equals("inspect")) {
                            System.out.println();
                            System.out.println(unitChecking);
                            waitEnter();
                            shopFunction();
                        }
                        else if(command.equals("move")){
                            System.out.println("\nWhich slot do you want to move the " + unitChecking.getName() + " into (1-5): ");
                            intInput = scanner.nextInt();
                            if (intInput < 1 || intInput > 5) {
                                invalid();
                                waitEnter();
                                shopFunction();
                            }
                            else {
                                if(team[intInput - 1] == null){
                                    team[intInput-1] = team[itemNumber-1];
                                    team[itemNumber-1] = null;
                                    teamDisplay[intInput-1] = team[intInput-1].getName();
                                    teamDisplay[itemNumber-1] = null;
                                }
                                else{
                                    Unit temp = team[itemNumber-1];
                                    team[itemNumber-1] = team[intInput-1];
                                    team[intInput-1] = temp;
                                    teamDisplay[intInput-1] = team[intInput-1].getName();
                                    teamDisplay[itemNumber-1] = team[itemNumber-1].getName();
                                }
                            }
                        }
                        else if (command.equals("merge")) {
                            System.out.println("\nWhich slot do you want to merge the " + unitChecking.getName() + " into (1-5): ");
                            intInput = scanner.nextInt();
                            if (intInput < 1 || intInput > 5) {
                                invalid();
                                waitEnter();
                                shopFunction();
                            }
                            if(Objects.equals(teamDisplay[itemNumber - 1], teamDisplay[intInput - 1]) && itemNumber!=intInput){
                                teamDisplay[itemNumber-1] = null;
                                team[itemNumber-1] = null;
                                team[intInput-1].merge();
                                System.out.println();
                                System.out.println("Merged!");
                                waitEnter();
                                shopFunction();
                            }
                            else{
                                invalid();
                                waitEnter();
                                shopFunction();
                            }
                        }
                        else if (command.equals("sell")) {
                            if (itemNumber < 1 || itemNumber > 5) {
                                invalid();
                                waitEnter();
                                shopFunction();
                            }
                            System.out.println();
                            System.out.println("Are you sure you want to sell your " + teamDisplay[itemNumber-1] + " for " + team[itemNumber - 1].getLevel() + "? (y/n)");
                            input = scanner.next();
                            if(input.equals("y")){
                                gold += team[itemNumber-1].getLevel();
                                System.out.println("Successfully sold your " +  teamDisplay[itemNumber-1] + " for " + team[itemNumber - 1].getLevel() + "!");
                                waitEnter();
                                team[itemNumber-1].sell(itemNumber-1);
                                teamDisplay[itemNumber-1] = null;
                                team[itemNumber-1] = null;
                                shopFunction();
                            }
                            else shopFunction();
                        }
                    }
                }
            }
            else{
                invalid();
                waitEnter();
                shopFunction();
            }
        }
        else if (input.equals("roll")){
            if(gold>=1){
                shopRefresh();
                gold = gold-1;
            }
            else{
                System.out.println("Not enough gold!");
                waitEnter();
            }
        }
        else if (input.equals("end")){
            if(gold>0){
                System.out.println("You have excess gold. Are you sure you want to end the turn? (y/n): ");
                input = scanner.nextLine();
                if(input.equals("y")) battlePhase();
                else if(input.equals("n")) shopFunction();
                else{
                    invalid();
                    waitEnter();
                    shopFunction();
                }
            }
            else{
                battlePhase();
            }
        }
        shopFunction();
    }
    public static void battlePhase() throws FileNotFoundException, InterruptedException {
        randomEnemyTeam();
        battleFunction();
    }
    public static void randomEnemyTeam() throws FileNotFoundException {
        int totalAtk = 0;
        int totalHP = 0;
        Random random = new Random();
        for(int i = 0; i < team.length; i++) {
            if (team[i] != null) {
                totalAtk += team[i].getAtk();
                totalHP += team[i].getHp();
            }
        }
        enemyTeam[0] = new Unit(randomUnitSortNull());
        for(int i = 0; i < enemyTeam.length; i++){
            int check = random.nextInt(3);
            if(shopTier==1){
                if(check!=1 && check!=2){
                    enemyTeam[i] = new Unit(randomUnitSortNull());
                    check = random.nextInt(3);
                    if(check==1){
                        Perk perk = new Perk(randomPerkSortNull());
                        enemyTeam[i].setPerk(perk);
                    }
                }
            }
            else{
                if(check!=1){
                    enemyTeam[i] = new Unit(randomUnitSortNull());
                    check = random.nextInt(3);
                    if(check==1){
                        Perk perk = new Perk(randomPerkSortNull());
                        enemyTeam[i].setPerk(perk);
                    }
                }
            }
        }
        for(int i = 0; i < enemyTeamDisplay.length; i++){
            if(enemyTeam[i]!=null) {
                enemyTeamDisplay[i] = enemyTeam[i].getName();
            }
        }
    }
    public static void statsMaker() throws FileNotFoundException {
        for (int i = 0; i < yourStatsDisplay.length; i++) {
            if (teamBattle[i] == null) {
                yourStatsDisplay[i][0] = 0;
                yourStatsDisplay[i][1] = 0;
            }
            else {
                yourStatsDisplay[i][0] = teamBattle[i].getAtk();
                yourStatsDisplay[i][1] = teamBattle[i].getHp();
            }
        }
        for(int i = 0; i < enemyStatsDisplay.length; i++){
            if (enemyTeamBattle[i] == null) {
                enemyStatsDisplay[i][0] = 0;
                enemyStatsDisplay[i][1] = 0;
            }
            else {
                enemyStatsDisplay[i][0] = enemyTeamBattle[i].getAtk();
                enemyStatsDisplay[i][1] = enemyTeamBattle[i].getHp();
            }
        }
        System.out.println(Arrays.deepToString(yourStatsDisplay) + "        " + Arrays.deepToString(enemyStatsDisplay));
    }
    public static void battleFunction() throws FileNotFoundException, InterruptedException {
        for(int i = 0; i <= 50; i++){
            System.out.println();
        }
        for(int i = 0; i < teamDisplayBattle.length; i++){
            teamDisplayBattle[i] = teamDisplay[i];
            enemyTeamDisplayBattle[i] = enemyTeamDisplay[i];
            teamBattle[i] = team[i];
            enemyTeamBattle[i] = enemyTeam[i];
        }
        System.out.println("   " + Arrays.toString(teamDisplayBattle) + "               " + Arrays.toString(enemyTeamDisplayBattle));
        statsMaker();
        Scanner scanner = new Scanner(System.in);
        Unit unitChecking = null;
        System.out.println("Please enter a command (inspect, start), followed by a space then the number of that unit (1-5): ");
        String input = scanner.nextLine();
        Scanner sc = new Scanner(input).useDelimiter(" ");
        if(sc.hasNext()) {
            String command = sc.next();
            int itemNumber = 0;
            if(sc.hasNext()) {
                itemNumber = Integer.parseInt(sc.next());
                if(command.equals("inspect")){
                    if(enemyTeamDisplay[itemNumber-1]==null){
                        System.out.println("That slot is empty!");
                        waitEnter();
                    }
                    else{
                        unitChecking = new Unit(enemyTeamDisplay[itemNumber-1]);
                        System.out.println(unitChecking);
                        waitEnter();
                    }
                }
            }
            else if(command.equals("start")){
                fighting = 1;
                while (fighting != 0) {
                    battleTurn();
                }
            }
            else{
                invalid();
                waitEnter();
                battleFunction();
            }

        }
        battleFunction();
    }
    public static void battleTurn() throws FileNotFoundException, InterruptedException {
        for(int i = 0; i <= 50; i++){
            System.out.println();
        }
        if(teamDisplayBattle[4]==null || enemyTeamDisplayBattle[0]==null){
            if(teamDisplayBattle[4]==null) {
                teamMoveForward();
            }
            if(enemyTeamDisplayBattle[0]==null) {
                enemyTeamMoveForward();
            }
        }
        else {
            teamBattle[4].normalAttack();
        }
        boolean teamEmpty = checkTeamEmptyBattle("Player");
        boolean enemyTeamEmpty = checkTeamEmptyBattle("Enemy");
        if(teamEmpty==true && enemyTeamEmpty==true){
            fighting = 0;
            System.out.println("It was a tie!");
            shopPhase();
        }
        else if(teamEmpty==true){
            fighting=0;
            System.out.println("The enemy won this battle...");
            lives--;
            System.out.println("Lives: " + lives);
            waitEnter();
            if(lives<1){
                gameOver();
            }
            shopPhase();
        }
        else if(enemyTeamEmpty==true){
            fighting=0;
            System.out.println("You won this battle!");
            wins++;
            System.out.println("Wins: " + wins);
            waitEnter();
            shopPhase();
        }
        System.out.println("   " + Arrays.toString(teamDisplayBattle) + "               " + Arrays.toString(enemyTeamDisplayBattle));
        statsMaker();
        System.out.println("Press Enter to continue.");
        waitEnter();
    }
    public static boolean checkTeamEmptyBattle(String team){
        if(team.equals("Player")){
            int teamCount = 0;
            for(int i = 0; i < teamDisplayBattle.length; i++){
                if(teamDisplayBattle[i]==null) teamCount++;
            }
            if(teamCount==5) return true;
        }
        else if(team.equals("Enemy")){
            int enemyTeamCount = 0;
            for(int i = 0; i < teamDisplayBattle.length; i++){
                if(enemyTeamDisplayBattle[i]==null) enemyTeamCount++;
            }
            if(enemyTeamCount==5) return true;
        }
        return false;
    }
    public static boolean checkTeamEmpty(){
        int teamCount = 0;
        for(int i = 0; i < team.length; i++){
            if(team[i]==null) teamCount++;
        }
        if(teamCount==5) return true;
        return false;
    }
    public static boolean checkShopUnitsEmpty(){
        int teamCount = 0;
        for(int i = 0; i < shopDisplayUnits.length; i++){
            if(shopDisplayUnits[i]==null) teamCount++;
        }
        if(teamCount==5) return true;
        return false;
    }
    public static void teamMoveForward(){
        for(int i = 0; i < teamDisplayBattle.length; i++){
            tempDisplay[i] = teamDisplayBattle[i];
            tempTeam[i] = teamBattle[i];
        }
        for(int i = 0; i < teamDisplayBattle.length-1; i++){
            teamDisplayBattle[i+1] = tempDisplay[i];
            teamBattle[i+1] = tempTeam[i];
            teamDisplayBattle[0]=null;
            teamBattle[0]=null;
        }
        System.out.println("Your team moved forward!");
    }
    public static void enemyTeamMoveForward(){
        for(int i = 0; i < enemyTeamDisplayBattle.length; i++){
            tempDisplay[i] = enemyTeamDisplayBattle[i];
            tempTeam[i] = enemyTeamBattle[i];
        }
        for(int i = 4; i > 0; i--){
            enemyTeamDisplayBattle[i-1] =  tempDisplay[i];
            enemyTeamBattle[i-1] = tempTeam[i];
            enemyTeamDisplayBattle[4]= null;
            enemyTeamBattle[4] = null;
        }
        System.out.println("The enemy's team moved forward!");
    }
    public static void gameOver(){

    }
    public static void shopRefresh() throws FileNotFoundException {
        for(int i = 0; i < shopDisplayUnits.length; i++){
            if(frozenItems[i]==0) shopDisplayUnits[i]=randomUnitSortNull();
        }
        for(int i = 0; i < shopUnits.length; i++){
            shopUnits[i] = new Unit(shopDisplayUnits[i]);
        }
        for(int i = 0; i < shopDisplayPerks.length; i++){
            if(frozenItems[i+5]==0) shopDisplayPerks[i]=randomPerkSortNull();
            shopPerks[i] = new Perk(shopDisplayPerks[i]);
        }
    }
    public static void shopBought(int itemNum) throws FileNotFoundException {
       if(itemNum<=5){
           shopDisplayUnits[itemNum-1]="Empty";
           shopUnits[itemNum-1] = null;
       }
       else if(itemNum>=6){
           shopPerks[itemNum-1] = null;
       }
    }
    public static void displayMenu() { //displays main menu UI
        System.out.println("   ▀████▀     ██     ▀████▀   ▀███▀     ██     ▀███▀▀▀██▄     ██     ███▀▀██▀▀█████▀▀██▀▀███████▀   ▀███▀▀▀███▀███▀▀▀██▄");
        System.out.println("     ██      ▄██▄      ▀██     ▄█      ▄██▄      ██    ██    ▄██▄    █▀   ██   ▀█▀   ██   ▀█ ██       ██    ▀█  ██   ▀██▄");
        System.out.println("     ██     ▄█▀██▄      ██▄   ▄█      ▄█▀██▄     ██    ██   ▄█▀██▄        ██         ██      ██       ██   █    ██   ▄██");
        System.out.println("     ██    ▄█  ▀██       ██▄  █▀     ▄█  ▀██     ██▀▀▀█▄▄  ▄█  ▀██        ██         ██      ██       ██████    ███████");
        System.out.println("     ██    ████████      ▀██ █▀      ████████    ██    ▀█  ████████       ██         ██      ██     ▄ ██   █  ▄ ██  ██▄");
        System.out.println("███  ██   █▀      ██      ▄██▄      █▀      ██   ██    ▄█ █▀      ██      ██         ██      ██    ▄█ ██     ▄█ ██   ▀██▄");
        System.out.println(" █████  ▄███▄   ▄████▄     ██     ▄███▄   ▄████▄████████▄███▄   ▄████▄  ▄████▄     ▄████▄  ████████████████████████▄ ▄███▄");
        System.out.println("\n                                            Type 1 to play");
        System.out.println("                                      Type 2 to display leaderboard");
        System.out.println("                                            Type 3 to quit");
    }
    public static void displayFile(String fileName) throws FileNotFoundException { //prints out content of a file
        File file = new File(fileName);
        Scanner fileScanner = new Scanner(file);
        while (fileScanner.hasNextLine()) {
            System.out.println(fileScanner.nextLine());
        }
        fileScanner.close();
    }
    public static void addToFile(String outPutFilename, String word, int num) throws IOException {
        File file = new File(outPutFilename);
        BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(file, true));
        bufferedWriter.newLine();
        bufferedWriter.write(word + " " + num);
        bufferedWriter.close();
    }
    private static String[] fileIntoArray(String inputFilename) throws FileNotFoundException {
        File file = new File(inputFilename);
        Scanner scanner = new Scanner(file);
        int numberOfLinesInFile = countLinesInFile(inputFilename);
        String[] data = new String[numberOfLinesInFile];
        int index = 0;
        while (scanner.hasNextLine()) {
            data[index++] = scanner.nextLine();
        }
        scanner.close();
        return data;
    }
    public static String randomUnitSortNull() throws FileNotFoundException {
        String str = null;
        while(str == null){
            str = randomUnit();
        }
        return str;
    }
    public static String randomPerkSortNull() throws FileNotFoundException {
        String str = null;
        while(str == null){
            str = randomPerk();
        }
        return str;
    }
    public static String randomUnit() throws FileNotFoundException {
        String[] word = fileIntoArray("Unitdex");
        Random rand = new Random();
        int totalLines = word.length;
        int randomLineInFile;
        randomLineInFile = rand.nextInt(totalLines - 1) + 1;
        String[] array = word[randomLineInFile].split(";");
        Unit unit2 = new Unit(array[0]);
        if(unit2.getTier()<=shopTier && unit2.getTier()!=0){
            return array[0];
        }
        return null;
    }
    public static String randomUnitSameTier(Unit unit) throws FileNotFoundException {
        String[] word = fileIntoArray("Unitdex");
        Random rand = new Random();
        int totalLines = word.length;
        int randomLineInFile;
        randomLineInFile = rand.nextInt(totalLines - 1) + 1;
        String[] array = word[randomLineInFile].split(";");
        Unit unit2 = new Unit(array[0]);
        if(unit2.getTier()==unit.getTier()){
            return array[0];
        }
        return null;
    }
    public static String randomPerk() throws FileNotFoundException {
        String[] word = fileIntoArray("Perkdex");
        Random rand = new Random();
        int totalLines = word.length;
        int randomLineInFile;
        randomLineInFile = rand.nextInt(totalLines - 2) + 2;
        String[] array = word[randomLineInFile].split(";");
        Perk perk = new Perk(array[0]);
        if(perk.getTier()<=shopTier){
            return array[0];
        }
        return null;
    }
    private static int countLinesInFile(String inputFilename) throws FileNotFoundException {
        File file = new File(inputFilename);
        Scanner scanner = new Scanner(file);
        int lineCount = 0;
        while (scanner.hasNextLine()) {
            lineCount++;
            scanner.nextLine();
        }
        scanner.close();
        return lineCount;
    }
    public static void searchUnit(){ //debugger
        try {
            System.out.println("Enter data to search");
            Scanner scanner = new Scanner(System.in);
            String word = scanner.next();
            BufferedReader br = new BufferedReader(new FileReader("Unitdex"));
            while((str = br.readLine()) != null){
                String[] array = str.split("/");
                for (String string2 : array) {
                    if(string2.matches(word)){
                        System.out.println("Found record for " +word);
                    } else {
                        System.out.println("Searching...");
                    }
                }
            }   System.out.println("Done");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
