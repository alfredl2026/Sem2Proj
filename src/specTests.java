import java.io.FileNotFoundException;
import java.io.IOException;

public class specTests {
    public void spec1Test() {
        System.out.println(main.username);
    }
    public void spec2Test() throws IOException, InterruptedException {
        main.displayMenu();
        main.userSelectMenu();
    }
    public void spec3Test() throws FileNotFoundException {
        main.displayLeaderboard("leaderboard");
    }
    public void spec4Test() throws IOException, InterruptedException {
        main.shopFunction();
        main.shopFunction();
        main.shopFunction();
    }
    public void spec8Test() throws IOException, InterruptedException {
        main.lives = 5;
        main.wins = 0;
        System.out.println("The enemy won this battle...");
        main.lives--;
        System.out.println("Lives: " + main.lives);
        main.waitEnter();
        if(main.lives<1){
            main.gameOver();
        }
        System.out.println("You won this battle!");
        main.wins++;
        System.out.println("Wins: " + main.wins);
    }
    public void spec9Test() throws IOException, InterruptedException {
        main.lives=1;
        System.out.println("The enemy won this battle...");
        main.lives--;
        System.out.println("Lives: " + main.lives);
        main.waitEnter();
        if(main.lives<1){
            main.gameOver();
        }
    }
    public void spec11Test() throws IOException, InterruptedException {
        main.wins=9;
        System.out.println("You won this battle!");
        main.wins++;
        System.out.println("Wins: " + main.wins);
        main.waitEnter();
        main.tenWinCheck();
    }
}
